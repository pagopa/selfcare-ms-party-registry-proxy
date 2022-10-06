package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import it.pagopa.selfcare.party.registry_proxy.connector.api.OpenDataConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Category;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.OpenDataRestClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Slf4j
abstract class OpenDataConnectorTemplate<I extends Institution, C extends Category> implements OpenDataConnector<I, C> {

    private final OpenDataRestClient restClient;


    protected OpenDataConnectorTemplate(OpenDataRestClient restClient) {
        this.restClient = restClient;
    }


    @SneakyThrows
    @Override
    public List<I> getInstitutions() {
        List<I> institutions;
        final String csv = restClient.retrieveInstitutions();

        try (Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(csv.getBytes())))) {
            CsvToBean<I> csvToBean = new CsvToBeanBuilder<I>(reader)
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
    public List<C> getCategories() {
        List<C> categories;
        final String csv = restClient.retrieveCategories();

        try (Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(csv.getBytes())))) {
            CsvToBean<C> csvToBean = new CsvToBeanBuilder<C>(reader)
                    .withType(getCategoryType())
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            categories = csvToBean.parse();
        }

        //TODO: scartare i record che non soddisfano determinati requisiti?
        return categories;
    }


    protected abstract Class<I> getInstitutionType();

    protected abstract Class<C> getCategoryType();

}
