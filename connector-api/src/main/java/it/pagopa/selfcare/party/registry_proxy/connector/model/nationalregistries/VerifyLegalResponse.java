package it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries;

import lombok.Data;

@Data
public class VerifyLegalResponse {

    private boolean verificationResult = false;
    private String verifyLegalResultCode;
    private String verifyLegalResultDetail;
}
