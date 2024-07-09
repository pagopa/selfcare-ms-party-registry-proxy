package it.pagopa.selfcare.party.registry_proxy.connector.rest.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlRootElement(name = "Impresa", namespace = "http://it.registroimprese.pcad.ws")
@XmlType(propOrder = {
        "businessTaxId",
        "businessName",
        "legalNature",
        "legalNatureDescription",
        "cciaa",
        "nRea",
        "businessStatus",
        "provinciaSede",
        "comuneSede",
        "toponimoSede",
        "viaSede",
        "ncivicoSede",
        "capSede",
        "digitalAddress"
})
public class PDNDImpresa {

    @XmlElement(name = "CodiceFiscale")
    private String businessTaxId;

    @XmlElement(name = "Denominazione")
    private String businessName;

    @XmlElement(name = "NaturaGiuridica")
    private String legalNature;

    @XmlElement(name = "DescNaturaGiuridica")
    private String legalNatureDescription;

    @XmlElement(name = "Cciaa")
    private String cciaa;

    @XmlElement(name = "NRea")
    private String nRea;

    @XmlElement(name = "StatoImpresa")
    private String businessStatus;

    @XmlElement(name = "ComuneSede")
    private String city;

    @XmlElement(name = "ProvinciaSede")
    private String county;

    @XmlElement(name = "CapSede")
    private String zipCode;

    @XmlElement(name = "PEC")
    private String digitalAddress;

    @XmlElement(name = "ToponimoSede")
    private String toponimoSede;

    @XmlElement(name = "ViaSede")
    private String viaSede;

    @XmlElement(name = "NcivicoSede")
    private String ncivicoSede;

    @XmlTransient
    public String getAddress() {
        return toponimoSede + " " + viaSede + " " + ncivicoSede;
    }

}
