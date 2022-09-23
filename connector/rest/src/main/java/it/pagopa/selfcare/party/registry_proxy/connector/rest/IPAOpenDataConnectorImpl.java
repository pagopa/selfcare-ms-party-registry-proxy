package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import it.pagopa.selfcare.party.registry_proxy.connector.api.OpenDataConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Category;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.IPAOpenDataRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.CategoryIPAOpenData;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.InstitutionIPAOpenData;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Slf4j
@Service
class IPAOpenDataConnectorImpl implements OpenDataConnector {

    private final IPAOpenDataRestClient restClient;

    @Autowired
    public IPAOpenDataConnectorImpl(IPAOpenDataRestClient restClient) {
        this.restClient = restClient;
    }


    @SneakyThrows  //TODO
    @Override
    public List<? extends Institution> getInstitutions() {
        List<InstitutionIPAOpenData> institutions;
        final String csv = restClient.retrieveInstitutions("True");

        try (Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(csv.getBytes())))) {
            CsvToBean<InstitutionIPAOpenData> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(InstitutionIPAOpenData.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            institutions = csvToBean.parse();
        }

        //TODO: scartare i record che non soddisfano determinati requisiti?
        return institutions;
    }


    @SneakyThrows  //TODO
    @Override
    public List<? extends Category> getCategories() {
        List<CategoryIPAOpenData> categories;
        final String csv = restClient.retrieveCategories("True");

        try (Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(csv.getBytes())))) {
            CsvToBean<CategoryIPAOpenData> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(CategoryIPAOpenData.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            categories = csvToBean.parse();
        }

        //TODO: scartare i record che non soddisfano determinati requisiti?
        return categories;
    }

}
