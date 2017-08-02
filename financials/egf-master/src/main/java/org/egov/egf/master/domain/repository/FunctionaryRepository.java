package org.egov.egf.master.domain.repository;

import java.util.HashMap;
import java.util.Map;

import org.egov.common.constants.Constants;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.Functionary;
import org.egov.egf.master.domain.model.FunctionarySearch;
import org.egov.egf.master.persistence.entity.FunctionaryEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.FunctionaryJdbcRepository;
import org.egov.egf.master.web.requests.FunctionaryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FunctionaryRepository {

	@Autowired
	private FunctionaryJdbcRepository functionaryJdbcRepository;
	@Autowired
	private MastersQueueRepository functionaryQueueRepository;

	public Functionary findById(Functionary functionary) {
		FunctionaryEntity entity = functionaryJdbcRepository.findById(new FunctionaryEntity().toEntity(functionary));
		return entity.toDomain();

	}

	@Transactional
	public Functionary save(Functionary functionary) {
		FunctionaryEntity entity = functionaryJdbcRepository.create(new FunctionaryEntity().toEntity(functionary));
		return entity.toDomain();
	}

	@Transactional
	public Functionary update(Functionary functionary) {
		FunctionaryEntity entity = functionaryJdbcRepository.update(new FunctionaryEntity().toEntity(functionary));
		return entity.toDomain();
	}

	public void add(FunctionaryRequest request) {
		Map<String, Object> message = new HashMap<>();

		if (request.getRequestInfo().getAction().equalsIgnoreCase(Constants.ACTION_CREATE)) {
			message.put("functionary_create", request);
		} else {
			message.put("functionary_update", request);
		}
		functionaryQueueRepository.add(message);
	}

	public Pagination<Functionary> search(FunctionarySearch domain) {

		return functionaryJdbcRepository.search(domain);

	}

}