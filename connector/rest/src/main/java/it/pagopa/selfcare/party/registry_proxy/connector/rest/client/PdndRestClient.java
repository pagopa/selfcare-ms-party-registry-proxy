package it.pagopa.selfcare.party.registry_proxy.connector.rest.client;

import it.pagopa.selfcare.party.user_registry.generated.openapi.v1.api.AuthApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "${rest-client.pdnd.serviceCode}", url = "${rest-client.pdnd.base-url}")
public interface PdndRestClient extends AuthApi {

}