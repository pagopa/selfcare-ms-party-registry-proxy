package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import it.pagopa.selfcare.party.registry_proxy.connector.api.OpenDataConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Category;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.OpenDataRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.IPAOpenDataCategory;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.IPAOpenDataInstitution;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Slf4j
abstract class OpenDataConnectorTemplate implements OpenDataConnector {

    private final OpenDataRestClient restClient;

    public OpenDataConnectorTemplate(OpenDataRestClient restClient) {
        this.restClient = restClient;
    }


    @SneakyThrows
    @Override
    public List<? extends Institution> getInstitutions() {
        List<IPAOpenDataInstitution> institutions;
        final String csv = restClient.retrieveInstitutions();

        try (Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(csv.getBytes())))) {
            CsvToBean<IPAOpenDataInstitution> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(getInstitutionType())
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            institutions = csvToBean.parse();
        }

        //TODO: scartare i record che non soddisfano determinati requisiti?
        return institutions;
    }


    @SneakyThrows
    @Override
    public List<? extends Category> getCategories() {
        List<IPAOpenDataCategory> categories;
        final String csv = restClient.retrieveCategories();

        try (Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(csv.getBytes())))) {
            CsvToBean<IPAOpenDataCategory> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(getCategoryType())
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            categories = csvToBean.parse();
        }

        //TODO: scartare i record che non soddisfano determinati requisiti?
        return categories;
    }


    protected abstract Class<? extends Institution> getInstitutionType();

    protected abstract Class<? extends Category> getCategoryType();

}
