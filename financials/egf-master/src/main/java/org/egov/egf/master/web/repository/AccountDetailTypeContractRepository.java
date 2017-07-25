package org.egov.egf.master.web.repository;

import org.egov.egf.master.web.contract.AccountDetailTypeContract;
import org.egov.egf.master.web.requests.AccountDetailTypeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AccountDetailTypeContractRepository {
	private RestTemplate restTemplate;
	private String hostUrl;
	public static final String SEARCH_URL = " /egf-master/accountdetailtypes/search?";
	@Autowired
	private ObjectMapper objectMapper;

	public AccountDetailTypeContractRepository(@Value("${egf.masterhost.url}") String hostUrl,
			RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.hostUrl = hostUrl;
	}

	public AccountDetailTypeContract findById(AccountDetailTypeContract accountDetailTypeContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
		StringBuffer content = new StringBuffer();
		if (accountDetailTypeContract.getId() != null) {
			content.append("id=" + accountDetailTypeContract.getId());
		}

		if (accountDetailTypeContract.getTenantId() != null) {
			content.append("&tenantId=" + accountDetailTypeContract.getTenantId());
		}
		url = url + content.toString();
		AccountDetailTypeResponse result = restTemplate.postForObject(url, null, AccountDetailTypeResponse.class);

		if (result.getAccountDetailTypes() != null && result.getAccountDetailTypes().size() == 1) {
			return result.getAccountDetailTypes().get(0);
		} else {
			return null;
		}

	}
}