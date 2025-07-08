package it.pagopa.selfcare.party.registry_proxy.connector.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "blocchi-impresa")
public class PDNDVisuraImpresa {

  @JsonProperty("dati-identificativi")
  private DatiIdentificativiImpresa datiIdentificativiImpresa;

  @JsonProperty("info-attivita")
  private InfoAttivita infoAttivita;

}
