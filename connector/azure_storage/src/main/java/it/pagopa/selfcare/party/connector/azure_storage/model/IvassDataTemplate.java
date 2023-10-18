package it.pagopa.selfcare.party.connector.azure_storage.model;

import com.opencsv.bean.CsvBindByName;
import it.pagopa.selfcare.party.registry_proxy.connector.model.InsuranceCompany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "taxCode")
public class IvassDataTemplate implements InsuranceCompany {
    @CsvBindByName(column = "CODICE_IVASS")
    private String originId;
    @CsvBindByName(column = "CODICE_FISCALE")
    private String taxCode;
    @CsvBindByName(column = "DENOMINAZIONE_IMPRESA")
    private String description;
    @CsvBindByName(column = "PEC")
    private String digitalAddress;
    @CsvBindByName(column = "TIPO_LAVORO")
    private String workType;
    @CsvBindByName(column = "TIPO_ALBO")
    private String registerType;
    @CsvBindByName(column = "INDIRIZZO_SEDE_LEGALE_RAPPRESENTANZA_IN_ITALIA")
    private String address;
}