package org.egov.pt.calculator.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.pt.calculator.web.models.property.BillingSlab;
import org.egov.pt.calculator.web.models.property.BillingSlabReq;
import org.egov.pt.calculator.web.models.property.BillingSlabRes;
import org.egov.pt.calculator.web.models.property.BillingSlabSearcCriteria;
import org.egov.tracer.model.CustomException;
import org.egov.pt.calculator.repository.PTCalculatorRepository;
import org.egov.pt.calculator.service.BillingSlabService;
import org.egov.pt.calculator.util.BillingSlabConstants;
import org.egov.pt.calculator.util.BillingSlabUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BillingSlabValidator {

	@Autowired
	private BillingSlabUtils billingSlabUtils;

	@Autowired
	private PTCalculatorRepository repository;

	@Autowired
	private BillingSlabService billingSlabService;

	public void validateCreate(BillingSlabReq billingSlabReq) {
		Map<String, String> errorMap = new HashMap<>();
		runCommonChecks(billingSlabReq, errorMap);
		validateDuplicateBillingSlabs(billingSlabReq, errorMap);
		//fetchAndvalidateMDMSCodes(billingSlabReq, errorMap);
		if (!CollectionUtils.isEmpty(errorMap)) {
			throw new CustomException(errorMap);
		}
	}

	public void validateUpdate(BillingSlabReq billingSlabReq) {
		Map<String, String> errorMap = new HashMap<>();
		runCommonChecks(billingSlabReq, errorMap);
		checkIfBillingSlabsExist(billingSlabReq, errorMap);
		//fetchAndvalidateMDMSCodes(billingSlabReq, errorMap);
		if (!CollectionUtils.isEmpty(errorMap)) {
			throw new CustomException(errorMap);
		}
	}
	
	public void runCommonChecks(BillingSlabReq billingSlabReq, Map<String, String> errorMap) {
		Set<String> tenantIds = billingSlabReq.getBillingSlab().parallelStream().map(BillingSlab::getTenantId)
				.collect(Collectors.toSet());
		if(tenantIds.size() < 1)
			errorMap.put("EG_PT_INVALID_INPUT", "Input must have atleast once billing slab");
		else if(tenantIds.size() > 1)
			errorMap.put("EG_PT_INVALID_INPUT", "All billing slabs must belong to same tenant");
		
		if (!CollectionUtils.isEmpty(errorMap)) {
			throw new CustomException(errorMap);
		}
	}

	public void checkIfBillingSlabsExist(BillingSlabReq billingSlabReq, Map<String, String> errorMap) {
		List<String> ids = billingSlabReq.getBillingSlab().parallelStream().map(BillingSlab::getId)
				.collect(Collectors.toList());
		BillingSlabRes billingSlabRes = billingSlabService.searchBillingSlabs(billingSlabReq.getRequestInfo(),
				BillingSlabSearcCriteria.builder().id(ids).tenantId(billingSlabReq.getBillingSlab().get(0).getTenantId()).build());
		if (CollectionUtils.isEmpty(billingSlabRes.getBillingSlab())) {
			errorMap.put("EG_PT_INVALID_IDS", "Following records are unavailable, IDs: "+ ids);
		} else {
			List<String> idsAvailableintheDB = billingSlabRes.getBillingSlab().parallelStream().map(BillingSlab::getId)
					.collect(Collectors.toList());
			if (idsAvailableintheDB.size() != ids.size()) {
				List<String> invalidIds = new ArrayList<>();
				for (String id : ids) {
					if (!idsAvailableintheDB.contains(id))
						invalidIds.add(id);
				}
				errorMap.put("EG_PT_INVALID_IDS", "Following records are unavailable, IDs: " + invalidIds);
			}
		}
		
		if (!CollectionUtils.isEmpty(errorMap))
			throw new CustomException(errorMap);
	}

	public void validateDuplicateBillingSlabs(BillingSlabReq billingSlabReq, Map<String, String> errorMap) {
		List<String> ids = billingSlabReq.getBillingSlab().parallelStream().map(BillingSlab::getId)
				.collect(Collectors.toList());
		BillingSlabRes billingSlabRes = billingSlabService.searchBillingSlabs(billingSlabReq.getRequestInfo(),
				BillingSlabSearcCriteria.builder().id(ids).build());
		if (!CollectionUtils.isEmpty(billingSlabRes.getBillingSlab())) {
			List<String> duplicateIds = billingSlabRes.getBillingSlab().parallelStream().map(BillingSlab::getId)
					.collect(Collectors.toList());

			errorMap.put("EG_PT_DUPLICATE_IDS", "Following records are duplicate, IDs: [" + duplicateIds + "]");
		}
		
		if (!CollectionUtils.isEmpty(errorMap))
			throw new CustomException(errorMap);
	}

	public void fetchAndvalidateMDMSCodes(BillingSlabReq billingSlabReq, Map<String, String> errorMap) {
		StringBuilder uri = new StringBuilder();
		MdmsCriteriaReq request = billingSlabUtils.prepareRequest(uri,
				billingSlabReq.getBillingSlab().get(0).getTenantId(), billingSlabReq.getRequestInfo());
		Object response = null;
		try {
			response = repository.fetchResult(uri, request);
			if (null == response) {
				log.info("MDMS data couldn't be fetched. Skipping code validation.....");
				return;
			}
			validateMDMSCodes(billingSlabReq, errorMap, response);
		} catch (Exception e) {
			log.error("MDMS data couldn't be fetched. Skipping code validation.....", e);
			return;
		}
	}

	public void validateMDMSCodes(BillingSlabReq billingSlabReq, Map<String, String> errorMap, Object mdmsResponse) {
		List<Object> propertyTypes = new ArrayList<>();
		List<Object> propertySubtypes = new ArrayList<>();
		List<Object> usageCategoryMajor = new ArrayList<>();
		List<Object> usageCategoryMinors = new ArrayList<>();
		List<Object> usageCategorySubMinor = new ArrayList<>();
		List<Object> ownerShipCategory = new ArrayList<>();
		List<Object> subOwnerShipCategory = new ArrayList<>();
		try {
			propertyTypes = JsonPath.read(mdmsResponse, BillingSlabConstants.MDMS_PROPERTYSUBTYPE_JSONPATH);
			propertySubtypes = JsonPath.read(mdmsResponse, BillingSlabConstants.MDMS_PROPERTYSUBTYPE_JSONPATH);
			
			usageCategoryMajor = JsonPath.read(mdmsResponse, BillingSlabConstants.MDMS_USAGEMINOR_JSONPATH);
			usageCategoryMinors = JsonPath.read(mdmsResponse, BillingSlabConstants.MDMS_USAGEMINOR_JSONPATH);
			usageCategorySubMinor = JsonPath.read(mdmsResponse, BillingSlabConstants.MDMS_USAGESUBMINOR_JSONPATH);
			
			ownerShipCategory = JsonPath.read(mdmsResponse, BillingSlabConstants.MDMS_SUBOWNERSHIP_JSONPATH);
			subOwnerShipCategory = JsonPath.read(mdmsResponse, BillingSlabConstants.MDMS_SUBOWNERSHIP_JSONPATH);
		} catch (Exception e) {
			if (CollectionUtils.isEmpty(propertySubtypes) && CollectionUtils.isEmpty(usageCategoryMinors)
					&& CollectionUtils.isEmpty(usageCategorySubMinor)
					&& CollectionUtils.isEmpty(subOwnerShipCategory)) {
				log.error("MDMS data couldn't be fetched. Skipping code validation.....", e);
				return;
			}
		}
		for (BillingSlab billingSlab : billingSlabReq.getBillingSlab()) {
			if(!StringUtils.isEmpty(billingSlab.getPropertyType())) {
				List<String> allowedPropertyTypes = JsonPath.read(propertyTypes,"$.*.code");
				if(!allowedPropertyTypes.contains(billingSlab.getPropertyType())) {
					errorMap.put("INVALID_PROPERTY_TYPE","Allowed Property Type are: "+allowedPropertyTypes);
				}else {
					if (!StringUtils.isEmpty(billingSlab.getPropertySubType())) {
						if (!CollectionUtils.isEmpty(propertySubtypes)) {
							List<String> allowedPropertySubTypes = JsonPath.read(propertySubtypes,"$.*.[?(@.propertyType=='" + billingSlab.getPropertyType() + "')].code");
							if (!allowedPropertySubTypes.contains(billingSlab.getPropertySubType())) {
								errorMap.put("INVALID_PROPERTY_SUBTYPE", "Allowed property subtype for this property type: "+ billingSlab.getPropertyType() + " are: " + allowedPropertySubTypes);
							}
						}
					}
				}
			}
			if(!StringUtils.isEmpty(billingSlab.getOwnerShipCategory())) {
				List<String> allowedOwnerShipCategories = JsonPath.read(ownerShipCategory,"$.*.code");
				if(!allowedOwnerShipCategories.contains(billingSlab.getOwnerShipCategory())) {
					errorMap.put("INVALID_OWNERSHIP_CATEGORY","Allowed Ownership categories are: "+allowedOwnerShipCategories);
				}else {
					if (!CollectionUtils.isEmpty(subOwnerShipCategory)) {
						List<String> allowedsubOwnerShipCategories = JsonPath.read(subOwnerShipCategory,"$.*.[?(@.ownerShipCategory=='" + billingSlab.getOwnerShipCategory() + "')].code");
						if (!allowedsubOwnerShipCategories.contains(billingSlab.getSubOwnerShipCategory())) {
							errorMap.put("INVALID_SUBOWNERSHIP_CATEGORY","Allowed subownership category for this ownership category: "+ billingSlab.getOwnerShipCategory() + " are: " + allowedsubOwnerShipCategories);
						}
					}
				}
			}
			if(!StringUtils.isEmpty(billingSlab.getUsageCategoryMajor())) {
				List<String> allowedUsageCategoryMajor = JsonPath.read(usageCategoryMajor,"$.*.code");
				if(!allowedUsageCategoryMajor.contains(billingSlab.getUsageCategoryMajor())) {
					errorMap.put("INVALID_USAGECATEGORY_MAJOR","Allowed Usage category major are: "+allowedUsageCategoryMajor);
				}else {
					if (!CollectionUtils.isEmpty(usageCategoryMinors)) {
						List<String> allowedUsageCategoryMinors = JsonPath.read(usageCategoryMinors, "$.*.[?(@.usageCategoryMajor=='" + billingSlab.getUsageCategoryMajor() + "')].code");
						if (!allowedUsageCategoryMinors.contains(billingSlab.getUsageCategoryMinor())) {
							errorMap.put("INVALID_USAGE_CATEGORY_MINOR","Allowed Usage category minor for this usage category major: "+ billingSlab.getUsageCategoryMajor() + " are: " + allowedUsageCategoryMinors);
						} else {
							if (!CollectionUtils.isEmpty(allowedUsageCategoryMinors)) {
								List<String> usageCategorySubMinors = JsonPath.read(allowedUsageCategoryMinors,"$.*.[?(@.usageCategoryMinor=='" + billingSlab.getUsageCategoryMinor() + "')].code");
								if (!usageCategorySubMinors.contains(billingSlab.getUsageCategorySubMinor())) {
									errorMap.put("INVALID_USAGE_CATEGORY_SUBMINOR","Allowed Usage category sub minor for this usage category minor: "+ billingSlab.getUsageCategoryMinor() + " are: " + usageCategorySubMinors);
								}
							}
						}
					}
				}
			}
		}
	}
}
