package it.pagopa.selfcare.party.registry_proxy.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dapr.client.DaprClient;
import it.pagopa.selfcare.party.registry_proxy.connector.model.SearchDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SearchServiceImpl implements SearchService{

  private final String daprClient = "";
  private final ObjectMapper objectMapper = new ObjectMapper();

//  @Autowired
//  public SearchServiceImpl(String daprClient) {
//    this.daprClient = daprClient;
//  }

  @Override
  public void indexDocument(SearchDocument document) {
    try {
//      String documentJson = objectMapper.writeValueAsString(document);
//      daprClient.invokeBinding("azure-search-binding", "create", documentJson).block();
      log.info("Document " + document.getTitle() + " indexed via Dapr binding.");
    } catch (Exception e) {
      log.error("Error creating search document", e);
    }
  }
}
