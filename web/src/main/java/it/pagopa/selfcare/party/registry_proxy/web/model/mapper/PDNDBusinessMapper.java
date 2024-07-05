package it.pagopa.selfcare.party.registry_proxy.web.model.mapper;

import it.pagopa.selfcare.party.registry_proxy.connector.model.InsuranceCompany;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistriespdnd.PDNDBusiness;
import it.pagopa.selfcare.party.registry_proxy.web.model.InsuranceCompanyResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.PDNDBusinessResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PDNDBusinessMapper {

    List<PDNDBusinessResponse> toResponses(List<PDNDBusiness> pdndBusiness);

}
