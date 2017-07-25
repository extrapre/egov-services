package org.egov.egf.master.web.repository;

import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.master.web.requests.FunctionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FunctionContractRepository {
	private RestTemplate restTemplate;
	private String hostUrl;
	public static final String SEARCH_URL = " /egf-master/functions/search?";
	@Autowired
	private ObjectMapper objectMapper;

	public FunctionContractRepository(@Value("${egf.masterhost.url}") String hostUrl, RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.hostUrl = hostUrl;
	}

	public FunctionContract findById(FunctionContract functionContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
		StringBuffer content = new StringBuffer();
		if (functionContract.getId() != null) {
			content.append("id=" + functionContract.getId());
		}

		if (functionContract.getTenantId() != null) {
			content.append("&tenantId=" + functionContract.getTenantId());
		}
		url = url + content.toString();
		FunctionResponse result = restTemplate.postForObject(url, null, FunctionResponse.class);

		if (result.getFunctions() != null && result.getFunctions().size() == 1) {
			return result.getFunctions().get(0);
		} else {
			return null;
		}

	}
}