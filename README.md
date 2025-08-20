# selfcare-ms-party-registry-proxy

## Description
This Spring Boot-based microservice is designed to handle several key functionalities in onboarding operations domain. It includes business logic for proxy data sources used in SelfCare, such as:

- IPA data for PA, AOO and UO
- ANAC
- IVASS 
- INFOCAMERE
- PDND_INFOCAMERE
- ADE

## Prerequisites
Before running the microservice, ensure you have installed:

- Java JDK 17 or higher
- Maven 3.6 or higher
- Connection to VPN selc-d-vnet

## Configuration
Look at app/src/main/resources/`application.yml` file to set up environment-specific settings, such as database details.

## Installation and Local Startup
To run the microservice locally, follow these steps:

1. **Clone the Repository**

```shell script
git clone https://github.com/pagopa/selfcare-ms-party-registry-proxy.git
cd selfcare-ms-party-registry-proxy
```

2. **Build the Project**

```shell script
mvn clean install
```

2. **Start the Application**

```shell script
mvn spring-boot:run -pl app
```

## Usage
After starting, the microservice will be available at `http://localhost:8080/`.

To use the API, refer to the Swagger UI documentation (if available) at `http://localhost:8080/swagger-ui.html`.

## DAPR
### Configure
Before run DAPR add:
.dapr/components/eventhub-pubsub.yaml
```shell script
apiVersion: dapr.io/v1alpha1
kind: Component
metadata:
  name: eventhub-pubsub
spec:
  type: pubsub.azure.eventhubs
  version: v1
  metadata:
    - name: connectionString
      value: "CONNECTION_STRING"
    - name: consumerGroup
      value: "CONSUMER_GROUP"
    - name: enableEntityManagement
      value: "false"
```

.dapr/config/azure-search-binding.yaml
```shell script
apiVersion: dapr.io/v1alpha1
kind: Component
metadata:
name: azure-search-binding
spec:
type: bindings.http
version: v1
metadata:
- name: url
value: "AZURE_SEARCH_URL"
- name: headers
value: |
{
"api-key": "AZURE_SEARCH_AKY_KEY",
"Content-Type": "application/json"
}
```

.dapr/config/config.yaml
```shell script
apiVersion: dapr.io/v1alpha1
kind: Configuration
metadata:
  name: dapr-config
  namespace: default
spec:
  tracing:
    samplingRate: "1"
    zipkin:
      endpointAddress: "http://localhost:9411/api/v2/spans"
  logging:
    apiLogging:
      enabled: true
  httpPipeline:
    handlers:
      - name: cors
        type: middleware.http.cors
        spec:
          allowedOrigins: ["*"]
          allowedMethods: ["GET", "POST", "PUT", "DELETE"]
          allowedHeaders: ["*"]
```

### Run

```shell script
dapr init
dapr run --app-id dapr-consumer --app-port 8080 --dapr-http-port 3500 --components-path ./.dapr/components
```

### Logs
```shell script
az containerapp logs show --name ca-dapr-consumer-dev --resource-group rg-dapr-consumer --container daprd
```

