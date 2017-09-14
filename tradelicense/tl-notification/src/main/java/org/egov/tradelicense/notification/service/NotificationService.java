package org.egov.tradelicense.notification.service;

import java.util.HashMap;
import java.util.Map;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.TradeLicenseContract;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.requests.TradeLicenseRequest;
import org.egov.tl.commons.web.response.LicenseStatusResponse;
import org.egov.tradelicense.notification.config.PropertiesManager;
import org.egov.tradelicense.notification.enums.NewLicenseStatus;
import org.egov.tradelicense.notification.util.NotificationUtil;
import org.egov.tradelicense.notification.util.TimeStampUtil;
import org.egov.tradelicense.notification.web.contract.EmailMessage;
import org.egov.tradelicense.notification.web.contract.EmailMessageContext;
import org.egov.tradelicense.notification.web.contract.SmsMessage;
import org.egov.tradelicense.notification.web.repository.StatusRepository;
import org.egov.tradelicense.notification.web.requests.EmailRequest;
import org.egov.tradelicense.notification.web.responses.SearchTenantResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Shubham
 *
 */

@Service
@Slf4j
public class NotificationService {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	KafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	NotificationUtil notificationUtil;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	StatusRepository statusRepository;

	/**
	 * This method is to send email and sms license acknowledgement
	 * 
	 * @param tradeLicenseRequest
	 */
	public void licenseNewCreationAcknowledgement(TradeLicenseRequest tradeLicenseRequest) {

		for (TradeLicenseContract tradeLicenseContract : tradeLicenseRequest.getLicenses()) {

			String applicationNumber = "";
			String ownerName = tradeLicenseContract.getOwnerName();
			String emailAddress = tradeLicenseContract.getEmailId();
			String mobileNumber = tradeLicenseContract.getMobileNumber();
			Long applicationDate = null;
			String ulbName = getULB(tradeLicenseContract.getTenantId(), tradeLicenseRequest.getRequestInfo());

			if (tradeLicenseContract.getApplication() != null) {

				applicationNumber = tradeLicenseContract.getApplication().getApplicationNumber();
				applicationDate = tradeLicenseContract.getApplication().getApplicationDate();
			}

			Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
			if (ulbName != null) {
				propertyMessage.put("ULB Name", ulbName);
			}
			propertyMessage.put("Owner", ownerName);
			propertyMessage.put("Application Number", applicationNumber);

			if (applicationDate != null) {
				String date = TimeStampUtil.getDateFromTimeStamp(applicationDate);
				propertyMessage.put("Application Date", date);
			} else {
				propertyMessage.put("Application Date", "");
			}

			String message = notificationUtil.buildSmsMessage(propertiesManager.getLicenseAcknowledgementSms(),
					propertyMessage);
			SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
			EmailMessageContext emailMessageContext = new EmailMessageContext();
			emailMessageContext.setBodyTemplateName(propertiesManager.getLicenseAcknowledgementEmailBody());
			emailMessageContext.setBodyTemplateValues(propertyMessage);
			emailMessageContext.setSubjectTemplateName(propertiesManager.getLicenseAcknowledgementEmailSubject());
			emailMessageContext.setSubjectTemplateValues(propertyMessage);

			EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
			EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);

			kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
			kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);

		}
	}

	public void processNewLicenseUpdateAcknowledgement(TradeLicenseRequest tradeLicenseRequest) {

		RequestInfo requestInfo = tradeLicenseRequest.getRequestInfo();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		for (TradeLicenseContract tradeLicenseContract : tradeLicenseRequest.getLicenses()) {

			if (tradeLicenseContract.getApplication() != null) {

				String applicationStatus = tradeLicenseContract.getApplication().getStatus();
				LicenseStatusResponse currentStatus = statusRepository.findByIds(tradeLicenseContract.getTenantId(),
						applicationStatus, requestInfoWrapper);

				if (null != currentStatus && !currentStatus.getLicenseStatuses().isEmpty()) {

					if (currentStatus.getLicenseStatuses().size() > 0) {

						String statusCode = currentStatus.getLicenseStatuses().get(0).getCode();

						if (statusCode != null
								&& statusCode.equalsIgnoreCase(NewLicenseStatus.SCRUTINY_COMPLETED.getName())) {

							licenseApplicationForwardedForInspectionAcknowledgement(tradeLicenseContract, requestInfo);

						} else if (statusCode != null
								&& statusCode.equalsIgnoreCase(NewLicenseStatus.FINAL_APPROVAL_COMPLETED.getName())) {

							licenseApplicationFinalApprovalCompletedAcknowledgement(tradeLicenseContract, requestInfo);

						} else if (statusCode != null
								&& statusCode.equalsIgnoreCase(NewLicenseStatus.LICENSE_FEE_PAID.getName())) {

							licenseFeePaidAcknowledgement(tradeLicenseContract, requestInfo);

						} else if (statusCode != null
								&& statusCode.equalsIgnoreCase(NewLicenseStatus.REJECTED.getName())) {

							licenseApplicationRejectionAcknowledgement(tradeLicenseContract, requestInfo);

						}
					}
				}
			}
		}
	}

	public void licenseApplicationFeePaidAcknowledgement(TradeLicenseContract tradeLicenseContract,
			RequestInfo requestInfo) {

		String applicationNumber = "";
		String ownerName = tradeLicenseContract.getOwnerName();
		Double amount = null;
		String emailAddress = tradeLicenseContract.getEmailId();
		String mobileNumber = tradeLicenseContract.getMobileNumber();
		Long applicationDate = null;
		String ulbName = getULB(tradeLicenseContract.getTenantId(), requestInfo);

		if (tradeLicenseContract.getApplication() != null) {

			applicationNumber = tradeLicenseContract.getApplication().getApplicationNumber();
			applicationDate = tradeLicenseContract.getApplication().getApplicationDate();
			amount = tradeLicenseContract.getApplication().getLicenseFee();
		}

		Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
		if (ulbName != null) {
			propertyMessage.put("ULB Name", ulbName);
		}
		propertyMessage.put("Owner", ownerName);
		propertyMessage.put("Application Number", applicationNumber);

		if (applicationDate != null) {
			propertyMessage.put("Application Date", applicationDate);
		} else {
			propertyMessage.put("Application Date", "");
		}

		if (amount != null) {
			propertyMessage.put("Application Fee", amount);
		} else {
			propertyMessage.put("Application Fee", "");
		}

		String message = notificationUtil.buildSmsMessage(propertiesManager.getLicenseFeeCollAcknowledgementSms(),
				propertyMessage);
		SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
		EmailMessageContext emailMessageContext = new EmailMessageContext();
		emailMessageContext.setBodyTemplateName(propertiesManager.getLicenseFeeCollAcknowledgementEmailBody());
		emailMessageContext.setBodyTemplateValues(propertyMessage);
		emailMessageContext.setSubjectTemplateName(propertiesManager.getLicenseFeeCollAcknowledgementEmailSubject());
		emailMessageContext.setSubjectTemplateValues(propertyMessage);

		EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
		EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);

		kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
		kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
	}

	public void licenseApplicationForwardedForInspectionAcknowledgement(TradeLicenseContract tradeLicenseContract,
			RequestInfo requestInfo) {

		String applicationNumber = "";
		String ownerName = tradeLicenseContract.getOwnerName();
		String emailAddress = tradeLicenseContract.getEmailId();
		String mobileNumber = tradeLicenseContract.getMobileNumber();
		Long applicationDate = null;
		String ulbName = getULB(tradeLicenseContract.getTenantId(), requestInfo);

		if (tradeLicenseContract.getApplication() != null) {

			applicationNumber = tradeLicenseContract.getApplication().getApplicationNumber();
			applicationDate = tradeLicenseContract.getApplication().getApplicationDate();
		}

		Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
		if (ulbName != null) {
			propertyMessage.put("ULB Name", ulbName);
		}
		propertyMessage.put("Owner", ownerName);
		propertyMessage.put("Application Number", applicationNumber);

		if (applicationDate != null) {
			propertyMessage.put("Application Date", applicationDate);
		} else {
			propertyMessage.put("Application Date", "");
		}

		String message = notificationUtil.buildSmsMessage(propertiesManager.getLicenseAppForwordedAcknowledgementSms(),
				propertyMessage);
		SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
		EmailMessageContext emailMessageContext = new EmailMessageContext();
		emailMessageContext.setBodyTemplateName(propertiesManager.getLicenseAppForwordedAcknowledgementEmailBody());
		emailMessageContext.setBodyTemplateValues(propertyMessage);
		emailMessageContext
				.setSubjectTemplateName(propertiesManager.getLicenseAppForwordedAcknowledgementEmailSubject());
		emailMessageContext.setSubjectTemplateValues(propertyMessage);

		EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
		EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);

		kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
		kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
	}

	public void licenseApplicationFinalApprovalCompletedAcknowledgement(TradeLicenseContract tradeLicenseContract,
			RequestInfo requestInfo) {

		String applicationNumber = "";
		String ownerName = tradeLicenseContract.getOwnerName();
		Double amount = null;
		String emailAddress = tradeLicenseContract.getEmailId();
		String mobileNumber = tradeLicenseContract.getMobileNumber();
		Long applicationDate = null;
		String ulbName = getULB(tradeLicenseContract.getTenantId(), requestInfo);

		if (tradeLicenseContract.getApplication() != null) {

			applicationNumber = tradeLicenseContract.getApplication().getApplicationNumber();
			applicationDate = tradeLicenseContract.getApplication().getApplicationDate();
			amount = tradeLicenseContract.getApplication().getLicenseFee();
		}

		Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
		if (ulbName != null) {
			propertyMessage.put("ULB Name", ulbName);
		}
		propertyMessage.put("Owner", ownerName);
		propertyMessage.put("Application Number", applicationNumber);

		if (applicationDate != null) {
			propertyMessage.put("Application Date", applicationDate);
		} else {
			propertyMessage.put("Application Date", "");
		}

		if (amount != null) {
			propertyMessage.put("Amount", amount);
		} else {
			propertyMessage.put("Amount", "");
		}

		String message = notificationUtil.buildSmsMessage(propertiesManager.getLicenseAppApprovedAcknowledgementSms(),
				propertyMessage);
		SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
		EmailMessageContext emailMessageContext = new EmailMessageContext();
		emailMessageContext.setBodyTemplateName(propertiesManager.getLicenseAppApprovedAcknowledgementEmailBody());
		emailMessageContext.setBodyTemplateValues(propertyMessage);
		emailMessageContext
				.setSubjectTemplateName(propertiesManager.getLicenseAppApprovedAcknowledgementEmailSubject());
		emailMessageContext.setSubjectTemplateValues(propertyMessage);

		EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
		EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);

		kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
		kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
	}

	public void licenseFeePaidAcknowledgement(TradeLicenseContract tradeLicenseContract, RequestInfo requestInfo) {

		String applicationNumber = "";
		String ownerName = tradeLicenseContract.getOwnerName();
		Double amount = null;
		String emailAddress = tradeLicenseContract.getEmailId();
		String mobileNumber = tradeLicenseContract.getMobileNumber();
		Long applicationDate = null;
		String ReceiptNumber = "";
		String ulbName = getULB(tradeLicenseContract.getTenantId(), requestInfo);

		if (tradeLicenseContract.getApplication() != null) {

			applicationNumber = tradeLicenseContract.getApplication().getApplicationNumber();
			applicationDate = tradeLicenseContract.getApplication().getApplicationDate();
			amount = tradeLicenseContract.getApplication().getLicenseFee();
		}

		Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
		if (ulbName != null) {
			propertyMessage.put("ULB Name", ulbName);
		}
		propertyMessage.put("Owner", ownerName);
		propertyMessage.put("Application Number", applicationNumber);

		if (applicationDate != null) {
			propertyMessage.put("Application Date", applicationDate);
		} else {
			propertyMessage.put("Application Date", "");
		}

		if (amount != null) {
			propertyMessage.put("Amount", amount);
		} else {
			propertyMessage.put("Amount", "");
		}
		propertyMessage.put("Receipt Number", ReceiptNumber);

		String message = notificationUtil
				.buildSmsMessage(propertiesManager.getLicenseFeeCollPaymentAcknowledgementSms(), propertyMessage);
		SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
		EmailMessageContext emailMessageContext = new EmailMessageContext();
		emailMessageContext.setBodyTemplateName(propertiesManager.getLicenseFeeCollPaymentAcknowledgementEmailBody());
		emailMessageContext.setBodyTemplateValues(propertyMessage);
		emailMessageContext
				.setSubjectTemplateName(propertiesManager.getLicenseFeeCollPaymentAcknowledgementEmailSubject());
		emailMessageContext.setSubjectTemplateValues(propertyMessage);

		EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
		EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);

		kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
		kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
	}

	public void licenseApplicationRejectionAcknowledgement(TradeLicenseContract tradeLicenseContract,
			RequestInfo requestInfo) {

		String applicationNumber = "";
		String ownerName = tradeLicenseContract.getOwnerName();
		String remarks = tradeLicenseContract.getRemarks();
		String emailAddress = tradeLicenseContract.getEmailId();
		String mobileNumber = tradeLicenseContract.getMobileNumber();
		String ulbName = getULB(tradeLicenseContract.getTenantId(), requestInfo);

		if (tradeLicenseContract.getApplication() != null) {

			applicationNumber = tradeLicenseContract.getApplication().getApplicationNumber();
		}

		Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
		if (ulbName != null) {
			propertyMessage.put("ULB Name", ulbName);
		}
		propertyMessage.put("Owner", ownerName);
		propertyMessage.put("Application Number", applicationNumber);
		propertyMessage.put("Reason/Remarks", remarks);

		String message = notificationUtil.buildSmsMessage(propertiesManager.getLicenseAppRejectionAcknowledgementSms(),
				propertyMessage);
		SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
		EmailMessageContext emailMessageContext = new EmailMessageContext();
		emailMessageContext.setBodyTemplateName(propertiesManager.getLicenseAppRejectionAcknowledgementEmailBody());
		emailMessageContext.setBodyTemplateValues(propertyMessage);
		emailMessageContext
				.setSubjectTemplateName(propertiesManager.getLicenseAppRejectionAcknowledgementEmailSubject());
		emailMessageContext.setSubjectTemplateValues(propertyMessage);

		EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
		EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailAddress);

		kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
		kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
	}

	private String getULB(String tenantId, RequestInfo requestInfo) {

		String hostUrl = propertiesManager.getTenantServiceHostName() + propertiesManager.getTenantServiceBasePath();
		String searchUrl = propertiesManager.getTenantServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();

		if (tenantId != null) {
			content.append("?code=" + tenantId);
		}

		url = url + content.toString();
		SearchTenantResponse tenantResponse = null;
		try {
			Map<String, Object> requestMap = new HashMap();
			requestMap.put("RequestInfo", requestInfo);
			tenantResponse = restTemplate.postForObject(url, requestInfo, SearchTenantResponse.class);

		} catch (Exception e) {
			log.debug("Error connecting to Tenant service end point " + url);
		}

		if (tenantResponse != null && tenantResponse.getTenant() != null && tenantResponse.getTenant().size() != 0
				&& tenantResponse.getTenant().get(0).getCity() != null) {
			return tenantResponse.getTenant().get(0).getCity().getName();
		} else {
			return null;
		}

	}

}
