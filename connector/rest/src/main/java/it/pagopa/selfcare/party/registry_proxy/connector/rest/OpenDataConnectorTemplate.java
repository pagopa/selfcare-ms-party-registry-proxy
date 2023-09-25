package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import it.pagopa.selfcare.party.registry_proxy.connector.api.FileStorageConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.api.OpenDataConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.*;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.OpenDataRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.OpenDataPDNDTemplate;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.List;

@Slf4j
abstract class OpenDataConnectorTemplate<I extends Institution, C extends Category, A extends AOO, U extends UO, K extends PDND> implements OpenDataConnector<I, C, A, U, K> {

    private final OpenDataRestClient restClient;
    private final FileStorageConnector fileStorageConnector;

    protected OpenDataConnectorTemplate(OpenDataRestClient restClient, FileStorageConnector fileStorageConnector) {
        this.restClient = restClient;
        this.fileStorageConnector = fileStorageConnector;
    }

    @SneakyThrows
    @Override
    public List<I> getInstitutions() {
        log.trace("getInstitutions start");
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
        log.debug("getInstitutions result = {}", institutions);
        log.trace("getInstitutions end");
        return institutions;
    }


    @SneakyThrows
    @Override
    public List<C> getCategories() {
        log.trace("getCategories start");
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
        log.debug("getCategories result = {}", categories);
        log.trace("getCategories end");
        return categories;
    }

    @Override
    public List<A> getAOOs() {
        log.trace("getAOOs start");
        List<A> aoos;
        final String csv = restClient.retrieveAOOs();

        try (Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(csv.getBytes())))) {
            CsvToBean<A> csvToBean = new CsvToBeanBuilder<A>(reader)
                    .withType(getAOOType())
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            aoos = csvToBean.parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //TODO: scartare i record che non soddisfano determinati requisiti?
        log.debug("getAOOs result = {}", aoos);
        log.trace("getAOOs end");
        return aoos;
    }

    @Override
    public List<U> getUOs() {
        log.trace("getUOs start");
        List<U> uos;
        final String csv = restClient.retrieveUOs();

        try (Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(csv.getBytes())))) {
            CsvToBean<U> csvToBean = new CsvToBeanBuilder<U>(reader)
                    .withType(getUOType())
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            uos = csvToBean.parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //TODO: scartare i record che non soddisfano determinati requisiti?
        log.debug("getUOs result = {}", uos);
        log.trace("getUOs end");
        return uos;
    }

    @Override
    public List<K> getStations(String fileName) {
        log.trace("getPDNDs start");
        List<K> pdnds;
        final ResourceResponse resourceResponse = fileStorageConnector.getFile(fileName);
        try (Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(resourceResponse.getData())))) {
            CsvToBean<K> csvToBean = new CsvToBeanBuilder<K>(reader)
                    .withType(getPDNDType())
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            pdnds = csvToBean.parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log.debug("getPDNDs result = {}", pdnds);
        log.trace("getPDNDs end");
        return pdnds;
    }


    protected abstract Class<I> getInstitutionType();

    protected abstract Class<C> getCategoryType();

    protected abstract Class<A> getAOOType();

    protected abstract Class<U> getUOType();

    protected abstract Class<K> getPDNDType();

}
