package org.egov.egf.instrument.domain.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.instrument.domain.model.InstrumentAccountCode;
import org.egov.egf.instrument.domain.model.InstrumentAccountCodeSearch;
import org.egov.egf.instrument.domain.model.InstrumentType;
import org.egov.egf.instrument.persistence.entity.InstrumentAccountCodeEntity;
import org.egov.egf.instrument.persistence.repository.InstrumentAccountCodeJdbcRepository;
import org.egov.egf.master.web.contract.ChartOfAccountContract;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InstrumentAccountCodeRepositoryTest {

	@InjectMocks
	private InstrumentAccountCodeRepository instrumentAccountCodeRepository;

	@Mock
	private InstrumentAccountCodeJdbcRepository instrumentAccountCodeJdbcRepository;

	@Test
	public void test_find_by_id() {
		InstrumentAccountCodeEntity entity = getInstrumentAccountCodeEntity();
		InstrumentAccountCode expectedResult = entity.toDomain();

		when(instrumentAccountCodeJdbcRepository.findById(any(InstrumentAccountCodeEntity.class))).thenReturn(entity);

		InstrumentAccountCode actualResult = instrumentAccountCodeRepository.findById(getInstrumentAccountCodeDomin());

		assertEquals(expectedResult.getAccountCode().getId(), actualResult.getAccountCode().getId());
		assertEquals(expectedResult.getInstrumentType().getId(), actualResult.getInstrumentType().getId());
	}

	@Test
	public void test_find_by_id_return_null() {
		InstrumentAccountCodeEntity entity = getInstrumentAccountCodeEntity();

		when(instrumentAccountCodeJdbcRepository.findById(null)).thenReturn(entity);

		InstrumentAccountCode actualResult = instrumentAccountCodeRepository.findById(getInstrumentAccountCodeDomin());

		assertEquals(null, actualResult);
	}

	@Test
	public void test_save() {

		InstrumentAccountCodeEntity entity = getInstrumentAccountCodeEntity();
		InstrumentAccountCode expectedResult = entity.toDomain();

		when(instrumentAccountCodeJdbcRepository.create(any(InstrumentAccountCodeEntity.class))).thenReturn(entity);

		InstrumentAccountCode actualResult = instrumentAccountCodeRepository.save(getInstrumentAccountCodeDomin());

		assertEquals(expectedResult.getAccountCode().getId(), actualResult.getAccountCode().getId());
		assertEquals(expectedResult.getInstrumentType().getId(), actualResult.getInstrumentType().getId());

	}

	@Test
	public void test_update() {

		InstrumentAccountCodeEntity entity = getInstrumentAccountCodeEntity();
		InstrumentAccountCode expectedResult = entity.toDomain();

		when(instrumentAccountCodeJdbcRepository.update(any(InstrumentAccountCodeEntity.class))).thenReturn(entity);

		InstrumentAccountCode actualResult = instrumentAccountCodeRepository.update(getInstrumentAccountCodeDomin());

		assertEquals(expectedResult.getAccountCode().getId(), actualResult.getAccountCode().getId());
		assertEquals(expectedResult.getInstrumentType().getId(), actualResult.getInstrumentType().getId());
	}

	@Test
	public void test_search() {

		Pagination<InstrumentAccountCode> expectedResult = new Pagination<>();
		expectedResult.setPageSize(500);
		expectedResult.setOffset(0);

		when(instrumentAccountCodeJdbcRepository.search(any(InstrumentAccountCodeSearch.class)))
				.thenReturn(expectedResult);

		Pagination<InstrumentAccountCode> actualResult = instrumentAccountCodeRepository
				.search(getInstrumentAccountCodeSearch());

		assertEquals(expectedResult, actualResult);

	}

	private InstrumentAccountCode getInstrumentAccountCodeDomin() {
		InstrumentAccountCode instrumentAccountCodeDetail = new InstrumentAccountCode();
		instrumentAccountCodeDetail.setAccountCode(ChartOfAccountContract.builder().id("accountCodeId").build());
		instrumentAccountCodeDetail.setInstrumentType(InstrumentType.builder().id("instrumentTypeId").build());
		instrumentAccountCodeDetail.setTenantId("default");
		return instrumentAccountCodeDetail;
	}

	private InstrumentAccountCodeEntity getInstrumentAccountCodeEntity() {
		InstrumentAccountCodeEntity entity = new InstrumentAccountCodeEntity();
		entity.setAccountCodeId("accountCodeId");
		entity.setInstrumentTypeId("instrumentTypeId");
		entity.setTenantId("default");
		return entity;
	}

	private InstrumentAccountCodeSearch getInstrumentAccountCodeSearch() {
		InstrumentAccountCodeSearch instrumentAccountCodeSearch = new InstrumentAccountCodeSearch();
		instrumentAccountCodeSearch.setPageSize(500);
		instrumentAccountCodeSearch.setOffset(0);
		return instrumentAccountCodeSearch;

	}

}
