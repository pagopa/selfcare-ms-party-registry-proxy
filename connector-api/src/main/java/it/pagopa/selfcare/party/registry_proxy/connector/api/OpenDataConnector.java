package it.pagopa.selfcare.party.registry_proxy.connector.api;

import it.pagopa.selfcare.party.registry_proxy.connector.model.AOO;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Category;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import it.pagopa.selfcare.party.registry_proxy.connector.model.UO;

import java.util.List;

public interface OpenDataConnector<I extends Institution, C extends Category, A extends AOO, U extends UO> {

    List<I> getInstitutions();

    List<C> getCategories();

    List<A> getAOOs();

    List<U> getUOs();

}
