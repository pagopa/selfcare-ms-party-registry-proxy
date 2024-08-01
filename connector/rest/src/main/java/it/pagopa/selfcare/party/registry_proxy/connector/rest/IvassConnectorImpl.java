package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IvassDataConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.InsuranceCompany;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.IVASSRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.IvassUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@PropertySource("classpath:config/ivass-rest-config.properties")
@ConditionalOnProperty(
        value = "ivass.file.connector.type",
        havingValue = "rest")
public class IvassConnectorImpl implements IvassDataConnector {
    private final IVASSRestClient ivassRestClient;
    private final List<String> registryTypesAdmitted;
    private final List<String> workTypesAdmitted;
    private final IvassUtil ivassUtil;

    public IvassConnectorImpl(
            IVASSRestClient ivassRestClient,
            @Value("#{'${ivass.registryTypes.admitted}'.split(',')}") List<String> registryTypes,
            @Value("#{'${ivass.workTypes.admitted}'.split(',')}") List<String> registryWorkTypes,
            IvassUtil ivassUtil
    ) {
        this.ivassRestClient = ivassRestClient;
        this.registryTypesAdmitted = registryTypes;
        this.workTypesAdmitted = registryWorkTypes;
        this.ivassUtil = ivassUtil;
    }

    @Override
    public List<InsuranceCompany> getInsurances() {
        byte[] zip = ivassRestClient.getInsurancesZip();
        byte[] csv = ivassUtil.extractSingleFileFromZip(zip);
        csv = ivassUtil.manageUTF8BOM(csv);
        List<InsuranceCompany> companies = ivassUtil.readCsv(csv);
        return filterCompanies(companies);
    }

    private List<InsuranceCompany> filterCompanies(List<InsuranceCompany> companies) {
        return companies
                .stream()
                .filter(company -> StringUtils.hasText(company.getDigitalAddress())
                        && workTypesAdmitted.contains(company.getWorkType())
                        && registryTypesAdmitted
                        .stream()
                        .anyMatch(StringUtils.trimAllWhitespace(company.getRegisterType()
                                .split("-")[0])::equals))
                .collect(Collectors.toList());
    }

}
