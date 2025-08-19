package it.pagopa.selfcare.party.registry_proxy.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dapr.client.DaprClient;
import it.pagopa.selfcare.party.connector.dapr.client.DaprSelcClient;
import it.pagopa.selfcare.party.registry_proxy.connector.model.SearchDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SearchServiceImpl implements SearchService{

  private final DaprSelcClient daprClient;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  public SearchServiceImpl(DaprSelcClient daprClient) {
    this.daprClient = daprClient;
  }

  @Override
  public void indexDocument(SearchDocument document) {
    try {
      String documentJson = objectMapper.writeValueAsString(document);
      daprClient.daprClient().invokeBinding("azure-search-binding", "create", documentJson).retry(10).block();
      log.info("Document " + document.getTitle() + " indexed via Dapr binding.");
    } catch (Exception e) {
      log.error("Error creating search document", e);
    }
  }
}
