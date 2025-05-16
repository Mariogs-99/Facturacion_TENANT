
package sv.gov.cnr.factelectrcnrservice.models.dte.cl;

import java.util.HashMap;
import java.util.Map;
import jakarta.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("jsonschema2pojo")
public enum Tributo {

    _20("20"),
    C_3("C3"),
    _59("59"),
    _71("71"),
    D_1("D1"),
    C_8("C8"),
    D_5("D5"),
    D_4("D4");
    private final String value;
    private final static Map<String, Tributo> CONSTANTS = new HashMap<String, Tributo>();

    static {
        for (Tributo c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    Tributo(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @JsonValue
    public String value() {
        return this.value;
    }

    @JsonCreator
    public static Tributo fromValue(String value) {
        Tributo constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
