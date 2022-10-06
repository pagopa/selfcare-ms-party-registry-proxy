# selfcare-ms-party-registry-proxy
A proxy for all Party Registry data sources used in Self Care

## Configuration Properties

| **Application properties** |
|:--------------------------:|

| **Property** | **Enviroment Variable** | **Default** | **Required** |
|--------------|-------------------------|-------------|:------------:|
|server.port|MS_PARTY_REGISTRY_PROXY_SERVER_PORT|<a name= "default property"></a>[default_property](https://github.com/pagopa/selfcare-ms-party-registry-proxy/blob/main/app/src/main/resources/config/application.yml)| yes |
|logging.level.it.pagopa.selfcare.party.registry_proxy| MS_PARTY_REGISTRY_PROXY_LOG_LEVEL |<a name= "default property"></a>[default_property](https://github.com/pagopa/selfcare-ms-party-registry-proxy/blob/main/app/src/main/resources/config/application.yml)| yes |


| **REST client Configurations** |
|:--------------------------:|

| **Property** | **Enviroment Variable** | **Default** | **Required** |
|--------------|-------------------------|-------------|:------------:|
|rest-client.ipa-open-data.base-url|IPA_OPEN_DATA_URL|<a name= "default property"></a>[default_property](https://github.com/pagopa/selfcare-ms-party-registry-proxy/blob/main/connector/rest/src/main/resources/config/ipa-open-data-rest-client.properties)| yes |
|rest-client.ipa-open-data.retrieveInstitutions.path|IPA_OPEN_DATA_INSTITUTION_ENDPOINT|<a name= "default property"></a>[default_property](https://github.com/pagopa/selfcare-ms-party-registry-proxy/blob/main/connector/rest/src/main/resources/config/ipa-open-data-rest-client.properties)| yes |
|rest-client.ipa-open-data.retrieveCategories.path|IPA_OPEN_DATA_CATEGORY_ENDPOINT|<a name= "default property"></a>[default_property](https://github.com/pagopa/selfcare-ms-party-registry-proxy/blob/main/connector/rest/src/main/resources/config/ipa-open-data-rest-client.properties)| yes |
|feign.client.config.ipa-open-data.connectTimeout|IPA_OPEN_DATA_REST_CLIENT_CONNECT_TIMEOUT<br>REST_CLIENT_CONNECT_TIMEOUT|<a name= "default property"></a>[default_property](https://github.com/pagopa/selfcare-ms-party-registry-proxy/blob/main/connector/rest/src/main/resources/config/ipa-open-data-rest-client.properties)| yes |
|feign.client.config.ipa-open-data.readTimeout|IPA_OPEN_DATA_REST_CLIENT_READ_TIMEOUT<br>REST_CLIENT_READ_TIMEOUT|<a name= "default property"></a>[default_property](https://github.com/pagopa/selfcare-ms-party-registry-proxy/blob/main/connector/rest/src/main/resources/config/ipa-open-data-rest-client.properties)| yes |
|feign.client.config.ipa-open-data.loggerLevel|IPA_OPEN_DATA_REST_CLIENT_LOGGER_LEVEL<br>REST_CLIENT_LOGGER_LEVEL|<a name= "default property"></a>[default_property](https://github.com/pagopa/selfcare-ms-party-registry-proxy/blob/main/connector/rest/src/main/resources/config/ipa-open-data-rest-client.properties)| yes |
|open-data.mock.enabled|MOCK_OPEN_DATA_ENABLED|<a name= "default property"></a>[default_property](https://github.com/pagopa/selfcare-ms-party-registry-proxy/blob/main/connector/rest/src/main/resources/config/mock-open-data-rest-client.properties)| yes |
|rest-client.mock-open-data.base-url|MOCK_OPEN_DATA_URL|<a name= "default property"></a>[default_property](https://github.com/pagopa/selfcare-ms-party-registry-proxy/blob/main/connector/rest/src/main/resources/config/ipa-open-data-rest-client.properties)| yes |
|rest-client.mock-open-data.retrieveInstitutions.path|MOCK_OPEN_DATA_INSTITUTION_ENDPOINT|<a name= "default property"></a>[default_property](https://github.com/pagopa/selfcare-ms-party-registry-proxy/blob/main/connector/rest/src/main/resources/config/mock-open-data-rest-client.properties)| yes |
|rest-client.mock-open-data.retrieveCategories.path|MOCK_OPEN_DATA_CATEGORY_ENDPOINT|<a name= "default property"></a>[default_property](https://github.com/pagopa/selfcare-ms-party-registry-proxy/blob/main/connector/rest/src/main/resources/config/mock-open-data-rest-client.properties)| yes |
|feign.client.config.mock-open-data.connectTimeout|MOCK_OPEN_DATA_REST_CLIENT_CONNECT_TIMEOUT<br>REST_CLIENT_CONNECT_TIMEOUT|<a name= "default property"></a>[default_property](https://github.com/pagopa/selfcare-ms-party-registry-proxy/blob/main/connector/rest/src/main/resources/config/mock-open-data-rest-client.properties)| yes |
|feign.client.config.mock-open-data.readTimeout|MOCK_OPEN_DATA_REST_CLIENT_READ_TIMEOUT<br>REST_CLIENT_READ_TIMEOUT|<a name= "default property"></a>[default_property](https://github.com/pagopa/selfcare-ms-party-registry-proxy/blob/main/connector/rest/src/main/resources/config/mock-open-data-rest-client.properties)| yes |
|feign.client.config.mock-open-data.loggerLevel|MOCK_OPEN_DATA_REST_CLIENT_LOGGER_LEVEL<br>REST_CLIENT_LOGGER_LEVEL|<a name= "default property"></a>[default_property](https://github.com/pagopa/selfcare-ms-party-registry-proxy/blob/main/connector/rest/src/main/resources/config/mock-open-data-rest-client.properties)| yes |


| **Lucene Configurations** |
|:--------------------------:|

| **Property** | **Enviroment Variable** | **Default** | **Required** |
|--------------|-------------------------|-------------|:------------:|
|lucene.index.institutions.folder|LUCENE_INDEX_INSTITUTIONS_FOLDER|<a name= "default property"></a>[default_property](https://github.com/pagopa/selfcare-ms-party-registry-proxy/blob/main/connector/lucene/src/main/resources/config/lucene-config.properties)| yes |
|lucene.index.categories.folder|LUCENE_INDEX_CATEGORIES_FOLDER|<a name= "default property"></a>[default_property](https://github.com/pagopa/selfcare-ms-party-registry-proxy/blob/main/connector/lucene/src/main/resources/config/lucene-config.properties)| yes |
