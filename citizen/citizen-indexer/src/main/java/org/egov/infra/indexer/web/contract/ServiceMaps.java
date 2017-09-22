package org.egov.infra.indexer.web.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ServiceMaps   {
	
  @JsonProperty("serviceName")
  private String serviceName = null;

  @JsonProperty("mappings")
  private List<Mapping> mappings = null;
 
}
