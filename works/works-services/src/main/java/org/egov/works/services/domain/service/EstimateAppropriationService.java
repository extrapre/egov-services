package org.egov.works.services.domain.service;

import java.util.List;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.services.common.config.PropertiesManager;
import org.egov.works.services.web.contract.EstimateAppropriationRequest;
import org.egov.works.services.web.contract.RequestInfo;
import org.egov.works.services.web.model.AuditDetails;
import org.egov.works.services.web.model.EstimateAppropriation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EstimateAppropriationService {

	@Autowired
	private PropertiesManager propertiesManager;
    
    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
    
	public Boolean validateEstimateAppropriation(final EstimateAppropriation estimateAppropriation) {

		//Check Budget control type also if its not none then check for budget available
		Boolean flag = Boolean.FALSE;
		String url = "";

		final RestTemplate restTemplate = new RestTemplate();

		RequestInfo requestInfo = new RequestInfo();

		restTemplate.postForObject(url, requestInfo, Object.class);

		return flag;

	}

	public List<EstimateAppropriation> save(final EstimateAppropriationRequest estimateAppropriationRequest) {
		RequestInfo requestInfo = estimateAppropriationRequest.getRequestInfo();
		AuditDetails auditDetails = new AuditDetails();
		for(EstimateAppropriation estimateAppropriation: estimateAppropriationRequest.getEstimateAppropriations()) {
            auditDetails.setCreatedBy(requestInfo.getUserInfo().getUsername());
            estimateAppropriation.setAuditDetails(auditDetails);
		}
        kafkaTemplate.send(propertiesManager.getEstimateAppropriationsCreateTopic(), estimateAppropriationRequest);
        return estimateAppropriationRequest.getEstimateAppropriations();

		
	}

}
