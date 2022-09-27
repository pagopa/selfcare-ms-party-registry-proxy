package it.pagopa.selfcare.party.registry_proxy.connector.rest.model;

import com.opencsv.bean.CsvBindByName;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Category;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "code")
public class CategoryIPAOpenData implements Category {

    @CsvBindByName(column = "Codice_categoria")
    private String code;
    @CsvBindByName(column = "Nome_categoria")
    private String name;
    @CsvBindByName(column = "Tipologia_categoria")
    private String kind;
    private String origin;


    public Origin getOrigin() {
        return Origin.IPA;
    }

}
