package it.pagopa.selfcare.party.registry_proxy.connector.constant;

public enum GenericError {
    RETRIEVE_GEO_TAXONOMIES_ERROR("0052", "Error while retrieving geographic taxonomy");
    private final String code;
    private final String detail;


    GenericError(String code, String detail) {
        this.code = code;
        this.detail = detail;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return detail;
    }

}
