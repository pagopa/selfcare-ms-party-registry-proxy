package it.pagopa.selfcare.party.registry_proxy.connector.rest.model;

import it.pagopa.selfcare.party.registry_proxy.connector.model.InsuranceCompany;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IvassQueryResultImpl implements QueryResult<InsuranceCompany> {

    private IvassSearchServiceResponse azureResponse;

        @Override
        public List<InsuranceCompany> getItems() {
            return azureResponse.getValue();
        }

        @Override
        public long getTotalHits() {
            return azureResponse.getOdataCount() != null ? azureResponse.getOdataCount() : 0;
        }
}
