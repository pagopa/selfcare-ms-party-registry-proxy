package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import feign.FeignException;
import it.pagopa.selfcare.onboarding.crypto.utils.DataEncryptionUtils;
import it.pagopa.selfcare.party.registry_proxy.connector.api.PDNDInfoCamereConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.exception.ResourceNotFoundException;
import it.pagopa.selfcare.party.registry_proxy.connector.model.national_registries_pdnd.PDNDBusiness;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.PDNDInfoCamereRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.PDNDVisuraInfoCamereRawRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.PDNDVisuraInfoCamereRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.config.PDNDInfoCamereRestClientConfig;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.config.PDNDVisuraInfoCamereRestClientConfig;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.ClientCredentialsResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.PDNDImpresa;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.PDNDVisuraImpresa;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.mapper.PDNDBusinessMapper;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.service.*;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.XMLCleaner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class PDNDInfoCamereConnectorImpl implements PDNDInfoCamereConnector {

    private static final String TAX_CODE_REQUIRED_MESSAGE = "TaxCode is required";
    private static final String BEARER = "Bearer ";

    private final PDNDInfoCamereRestClient pdndInfoCamereRestClient;
    private final PDNDVisuraInfoCamereRawRestClient pdndVisuraInfoCamereRawRestClient;
    private final PDNDVisuraInfoCamereRestClient pdndVisuraInfoCamereRestClient;
    private final PDNDBusinessMapper pdndBusinessMapper;
    private final TokenProvider tokenProviderPDND;
    private final TokenProviderVisura tokenProviderVisura;
    private final StorageAsyncService storageAsyncService;
    private final PDNDInfoCamereRestClientConfig pdndInfoCamereRestClientConfig;
    private final PDNDVisuraInfoCamereRestClientConfig pdndVisuraInfoCamereRestClientConfig;
    private final PDNDVisuraServiceCacheable pdndVisuraServiceCacheable;

    public PDNDInfoCamereConnectorImpl(
            PDNDInfoCamereRestClient pdndInfoCamereRestClient,
            PDNDVisuraInfoCamereRawRestClient pdndVisuraInfoCamereRawRestClient,
            PDNDVisuraInfoCamereRestClient pdndVisuraInfoCamereRestClient,
            PDNDBusinessMapper pdndBusinessMapper,
            TokenProviderPDND tokenProviderPDND,
            TokenProviderVisura tokenProviderVisura,
            PDNDInfoCamereRestClientConfig pdndInfoCamereRestClientConfig,
            PDNDVisuraInfoCamereRestClientConfig pdndVisuraInfoCamereRestClientConfig,
            StorageAsyncService storageAsyncService, PDNDVisuraServiceCacheable pdndVisuraServiceCacheable) {
        this.pdndInfoCamereRestClient = pdndInfoCamereRestClient;
        this.pdndVisuraInfoCamereRawRestClient = pdndVisuraInfoCamereRawRestClient;
        this.pdndVisuraInfoCamereRestClient = pdndVisuraInfoCamereRestClient;
        this.pdndBusinessMapper = pdndBusinessMapper;
        this.tokenProviderPDND = tokenProviderPDND;
        this.tokenProviderVisura = tokenProviderVisura;
        this.pdndInfoCamereRestClientConfig = pdndInfoCamereRestClientConfig;
        this.pdndVisuraInfoCamereRestClientConfig = pdndVisuraInfoCamereRestClientConfig;
        this.storageAsyncService = storageAsyncService;
        this.pdndVisuraServiceCacheable = pdndVisuraServiceCacheable;
    }

    @Override
    public List<PDNDBusiness> retrieveInstitutionsPdndByDescription(String description) {
        Assert.hasText(description, "Description is required");
        ClientCredentialsResponse tokenResponse = tokenProviderPDND.getTokenPdnd(pdndInfoCamereRestClientConfig.getPdndSecretValue());
        String bearer = BEARER + tokenResponse.getAccessToken();
        List<PDNDImpresa> result = pdndInfoCamereRestClient.retrieveInstitutionsPdndByDescription(description, bearer);
        return pdndBusinessMapper.toPDNDBusinesses(result);
    }

    @Override
    public PDNDBusiness retrieveInstitutionPdndByTaxCode(String taxCode) throws JsonProcessingException {
        Assert.hasText(taxCode, TAX_CODE_REQUIRED_MESSAGE);
        String result = pdndVisuraServiceCacheable.getInfocamereImpresa(taxCode);
        ObjectMapper objectMapper = new ObjectMapper();
        PDNDImpresa pdndImpresa = objectMapper.readValue(DataEncryptionUtils.decrypt(result), new TypeReference<>() {});
        return pdndBusinessMapper.toPDNDBusiness(pdndImpresa);
    }

    @Override
    public PDNDBusiness retrieveInstitutionDetail(String taxCode) {
        Assert.hasText(taxCode, TAX_CODE_REQUIRED_MESSAGE);
        try {
            var encryptedTaxCode = DataEncryptionUtils.encrypt(taxCode);
            byte[] document = DataEncryptionUtils.decrypt(pdndVisuraServiceCacheable.getEncryptedDocument(encryptedTaxCode)).getBytes();

            storageAsyncService.saveStringToStorage(new String(document, StandardCharsets.UTF_8),
                    "visura_" + taxCode + "_" + LocalDateTime.now() + ".xml");

            PDNDVisuraImpresa result = xmlToVisuraImpresa(document);

            return pdndBusinessMapper.toPDNDBusiness(result);
        } catch (Exception e) {
            log.error("Unexpected exception occurred while retrieving institution detail", e);
            throw new IllegalArgumentException("Unexpected error while retrieving institution detail", e);
        }
    }


    @Override
    public byte[] retrieveInstitutionDocument(String taxCode) {
        Assert.hasText(taxCode, TAX_CODE_REQUIRED_MESSAGE);
        ClientCredentialsResponse tokenResponse = tokenProviderVisura.getTokenPdnd(pdndVisuraInfoCamereRestClientConfig.getPdndSecretValue());
        String bearer = BEARER + tokenResponse.getAccessToken();
        byte[] result = pdndVisuraInfoCamereRawRestClient.getRawInstitutionDetail(taxCode, bearer);
        try {
            return XMLCleaner.cleanXml(result, Arrays.asList("persone-sede", "elenco-soci"));
        } catch (Exception e) {
            throw new IllegalArgumentException("Impossible to parse document for institution with taxCode: " + taxCode);
        }
    }

    @Override
    public PDNDBusiness retrieveInstitutionFromRea(String county, String rea) {
        Assert.hasText(rea, "Rea is required");
        Assert.hasText(rea, "County is required");
        ClientCredentialsResponse tokenResponse = tokenProviderVisura.getTokenPdnd(pdndVisuraInfoCamereRestClientConfig.getPdndSecretValue());
        String bearer = BEARER + tokenResponse.getAccessToken();
        List<PDNDImpresa> institutions = pdndVisuraInfoCamereRestClient.retrieveInstitutionPdndFromRea(rea, county, bearer);
        if (Objects.isNull(institutions) || institutions.isEmpty()) {
            throw new ResourceNotFoundException("No institution found with rea: " + county + "-" + rea);
        }
        PDNDImpresa result = institutions.get(0);
        PDNDVisuraImpresa visuraImpresa = pdndVisuraInfoCamereRestClient.retrieveInstitutionDetail(result.getBusinessTaxId(), bearer);
        return pdndBusinessMapper.toPDNDBusiness(visuraImpresa);
    }

    private PDNDVisuraImpresa xmlToVisuraImpresa(byte[] xmlBytes) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.findAndRegisterModules();
        return xmlMapper.readValue(xmlBytes, PDNDVisuraImpresa.class);
    }

}
