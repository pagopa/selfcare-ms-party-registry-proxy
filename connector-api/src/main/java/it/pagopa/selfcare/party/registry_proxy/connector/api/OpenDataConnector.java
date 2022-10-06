package it.pagopa.selfcare.party.registry_proxy.connector.api;

import it.pagopa.selfcare.party.registry_proxy.connector.model.Category;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;

import java.util.List;

public interface OpenDataConnector<I extends Institution, C extends Category> {

    List<I> getInstitutions();

    List<C> getCategories();

}
