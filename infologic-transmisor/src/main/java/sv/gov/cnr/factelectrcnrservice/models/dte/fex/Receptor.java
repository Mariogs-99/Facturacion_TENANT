
package sv.gov.cnr.factelectrcnrservice.models.dte.fex;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import jakarta.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Receptor
 * 
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "nombre",
    "tipoDocumento",
    "numDocumento",
    "nombreComercial",
    "codPais",
    "nombrePais",
    "complemento",
    "tipoPersona",
    "descActividad",
    "telefono",
    "correo"
})
@Generated("jsonschema2pojo")
@Data
@NoArgsConstructor
public class Receptor implements Serializable
{

    /**
     * Nombre, denominación o razón social del contribuyente (Receptor)
     * (Required)
     * 
     */
    @JsonProperty("nombre")
    @JsonPropertyDescription("Nombre, denominaci\u00f3n o raz\u00f3n social del contribuyente (Receptor)")
    @Size(min = 1, max = 250)
    @NotNull
    public String nombre;
    /**
     * Tipo de documento de identificación (Receptor)
     * (Required)
     * 
     */
    @JsonProperty("tipoDocumento")
    @JsonPropertyDescription("Tipo de documento de identificaci\u00f3n (Receptor)")
    @NotNull
    public Receptor.TipoDocumento tipoDocumento;
    /**
     * Número de documento de Identificación (Receptor)
     * (Required)
     * 
     */
    @JsonProperty("numDocumento")
    @JsonPropertyDescription("N\u00famero de documento de Identificaci\u00f3n (Receptor)")
    @Size(min = 3, max = 20)
    @NotNull
    public String numDocumento;
    /**
     * Nombre, denominación o razón social del contribuyente (Receptor)
     * (Required)
     * 
     */
    @JsonProperty("nombreComercial")
    @JsonPropertyDescription("Nombre, denominaci\u00f3n o raz\u00f3n social del contribuyente (Receptor)")
    @Size(min = 1, max = 150)
    @NotNull
    public String nombreComercial;
    /**
     * Código de país (receptor)
     * (Required)
     * 
     */
    @JsonProperty("codPais")
    @JsonPropertyDescription("C\u00f3digo de pa\u00eds (receptor)")
    @NotNull
    public Receptor.CodPais codPais;
    /**
     * País destino de la exportación (receptor)
     * (Required)
     * 
     */
    @JsonProperty("nombrePais")
    @JsonPropertyDescription("Pa\u00eds destino de la exportaci\u00f3n (receptor)")
    @Size(min = 3, max = 50)
    @NotNull
    public String nombrePais;
    /**
     * Colocar las especificaciones de la direccion
     * (Required)
     * 
     */
    @JsonProperty("complemento")
    @JsonPropertyDescription("Colocar las especificaciones de la direccion")
    @Size(min = 5, max = 300)
    @NotNull
    public String complemento;
    /**
     * tipo de persona Juridica o persona natural
     * (Required)
     * 
     */
    @JsonProperty("tipoPersona")
    @JsonPropertyDescription("tipo de persona Juridica o persona natural")
    @NotNull
    public Receptor.TipoPersona tipoPersona;
    /**
     * Actividad Económica (Receptor)
     * (Required)
     * 
     */
    @JsonProperty("descActividad")
    @JsonPropertyDescription("Actividad Econ\u00f3mica (Receptor)")
    @Size(min = 5, max = 150)
    @NotNull
    public String descActividad;
    /**
     * Teléfono (Receptor)
     * (Required)
     * 
     */
    @JsonProperty("telefono")
    @JsonPropertyDescription("Tel\u00e9fono (Receptor)")
    @Size(min = 8, max = 50)
    @NotNull
    public String telefono;
    /**
     * Correo electrónico (Receptor)
     * (Required)
     * 
     */
    @JsonProperty("correo")
    @JsonPropertyDescription("Correo electr\u00f3nico (Receptor)")
    @Size(min = 3, max = 100)
    @NotNull
    public String correo;
    private final static long serialVersionUID = -5969815750308254746L;


    /**
     * Código de país (receptor)
     * 
     */
    @Generated("jsonschema2pojo")
    public enum CodPais {

        _9320("9320"),
        _9539("9539"),
        _9565("9565"),
        _9905("9905"),
        _9999("9999"),
        _9303("9303"),
        _9306("9306"),
        _9309("9309"),
        _9310("9310"),
        _9315("9315"),
        _9317("9317"),
        _9318("9318"),
        _9319("9319"),
        _9324("9324"),
        _9327("9327"),
        _9330("9330"),
        _9333("9333"),
        _9336("9336"),
        _9339("9339"),
        _9342("9342"),
        _9345("9345"),
        _9348("9348"),
        _9349("9349"),
        _9350("9350"),
        _9354("9354"),
        _9357("9357"),
        _9360("9360"),
        _9363("9363"),
        _9366("9366"),
        _9372("9372"),
        _9374("9374"),
        _9375("9375"),
        _9377("9377"),
        _9378("9378"),
        _9381("9381"),
        _9384("9384"),
        _9387("9387"),
        _9390("9390"),
        _9393("9393"),
        _9394("9394"),
        _9396("9396"),
        _9399("9399"),
        _9402("9402"),
        _9405("9405"),
        _9408("9408"),
        _9411("9411"),
        _9414("9414"),
        _9417("9417"),
        _9420("9420"),
        _9423("9423"),
        _9426("9426"),
        _9432("9432"),
        _9435("9435"),
        _9438("9438"),
        _9440("9440"),
        _9441("9441"),
        _9444("9444"),
        _9446("9446"),
        _9447("9447"),
        _9450("9450"),
        _9453("9453"),
        _9456("9456"),
        _9459("9459"),
        _9462("9462"),
        _9465("9465"),
        _9468("9468"),
        _9471("9471"),
        _9474("9474"),
        _9477("9477"),
        _9480("9480"),
        _9481("9481"),
        _9483("9483"),
        _9486("9486"),
        _9487("9487"),
        _9495("9495"),
        _9498("9498"),
        _9501("9501"),
        _9504("9504"),
        _9507("9507"),
        _9513("9513"),
        _9516("9516"),
        _9519("9519"),
        _9522("9522"),
        _9525("9525"),
        _9526("9526"),
        _9528("9528"),
        _9531("9531"),
        _9534("9534"),
        _9537("9537"),
        _9540("9540"),
        _9543("9543"),
        _9544("9544"),
        _9546("9546"),
        _9549("9549"),
        _9552("9552"),
        _9555("9555"),
        _9558("9558"),
        _9561("9561"),
        _9564("9564"),
        _9567("9567"),
        _9570("9570"),
        _9573("9573"),
        _9576("9576"),
        _9577("9577"),
        _9582("9582"),
        _9585("9585"),
        _9591("9591"),
        _9594("9594"),
        _9597("9597"),
        _9600("9600"),
        _9601("9601"),
        _9603("9603"),
        _9606("9606"),
        _9609("9609"),
        _9611("9611"),
        _9612("9612"),
        _9615("9615"),
        _9618("9618"),
        _9621("9621"),
        _9624("9624"),
        _9627("9627"),
        _9633("9633"),
        _9636("9636"),
        _9638("9638"),
        _9639("9639"),
        _9642("9642"),
        _9645("9645"),
        _9648("9648"),
        _9651("9651"),
        _9660("9660"),
        _9663("9663"),
        _9666("9666"),
        _9669("9669"),
        _9672("9672"),
        _9675("9675"),
        _9677("9677"),
        _9678("9678"),
        _9679("9679"),
        _9680("9680"),
        _9681("9681"),
        _9682("9682"),
        _9683("9683"),
        _9684("9684"),
        _9687("9687"),
        _9690("9690"),
        _9691("9691"),
        _9693("9693"),
        _9696("9696"),
        _9699("9699"),
        _9702("9702"),
        _9705("9705"),
        _9706("9706"),
        _9707("9707"),
        _9708("9708"),
        _9714("9714"),
        _9717("9717"),
        _9720("9720"),
        _9722("9722"),
        _9723("9723"),
        _9725("9725"),
        _9726("9726"),
        _9727("9727"),
        _9729("9729"),
        _9732("9732"),
        _9735("9735"),
        _9738("9738"),
        _9739("9739"),
        _9740("9740"),
        _9741("9741"),
        _9744("9744"),
        _9747("9747"),
        _9750("9750"),
        _9756("9756"),
        _9758("9758"),
        _9759("9759"),
        _9760("9760"),
        _9850("9850"),
        _9862("9862"),
        _9863("9863"),
        _9865("9865"),
        _9886("9886"),
        _9898("9898"),
        _9899("9899"),
        _9897("9897"),
        _9887("9887"),
        _9571("9571"),
        _9300("9300"),
        _9369("9369"),
        _9439("9439"),
        _9510("9510"),
        _9579("9579"),
        _9654("9654"),
        _9711("9711"),
        _9736("9736"),
        _9737("9737"),
        _9640("9640"),
        _9641("9641"),
        _9673("9673"),
        _9472("9472"),
        _9311("9311"),
        _9733("9733"),
        _9541("9541"),
        _9746("9746"),
        _9551("9551"),
        _9451("9451"),
        _9338("9338"),
        _9353("9353"),
        _9482("9482"),
        _9494("9494"),
        _9524("9524"),
        _9304("9304"),
        _9332("9332"),
        _9454("9454"),
        _9457("9457"),
        _9489("9489"),
        _9491("9491"),
        _9492("9492"),
        _9523("9523"),
        _9530("9530"),
        _9532("9532"),
        _9535("9535"),
        _9542("9542"),
        _9547("9547"),
        _9548("9548"),
        _9574("9574"),
        _9598("9598"),
        _9602("9602"),
        _9607("9607"),
        _9608("9608"),
        _9623("9623"),
        _9652("9652"),
        _9692("9692"),
        _9709("9709"),
        _9712("9712"),
        _9716("9716"),
        _9718("9718"),
        _9719("9719"),
        _9751("9751"),
        _9452("9452"),
        _9901("9901"),
        _9902("9902"),
        _9903("9903"),
        _9664("9664"),
        _9415("9415"),
        _9904("9904"),
        _9514("9514"),
        _9906("9906"),
        _9359("9359"),
        _9493("9493"),
        _9521("9521"),
        _9533("9533"),
        _9538("9538"),
        _9689("9689"),
        _9713("9713"),
        _9449("9449"),
        _9888("9888"),
        _9490("9490"),
        _9527("9527"),
        _9529("9529"),
        _9536("9536"),
        _9545("9545"),
        _9568("9568"),
        _9610("9610"),
        _9622("9622"),
        _9643("9643"),
        _9667("9667"),
        _9676("9676"),
        _9685("9685"),
        _9686("9686"),
        _9688("9688"),
        _9715("9715"),
        _9900("9900"),
        _9371("9371"),
        _9376("9376"),
        _9907("9907");
        private final String value;
        private final static Map<String, CodPais> CONSTANTS = new HashMap<String, CodPais>();

        static {
            for (CodPais c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        CodPais(String value) {
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
        public static CodPais fromValue(String value) {
            CodPais constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Tipo de documento de identificación (Receptor)
     * 
     */
    @Generated("jsonschema2pojo")
    public enum TipoDocumento {

        _36("36"),
        _13("13"),
        _02("02"),
        _03("03"),
        _37("37");
        private final String value;
        private final static Map<String, TipoDocumento> CONSTANTS = new HashMap<String, TipoDocumento>();

        static {
            for (TipoDocumento c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        TipoDocumento(String value) {
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
        public static TipoDocumento fromValue(String value) {
            TipoDocumento constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * tipo de persona Juridica o persona natural
     * 
     */
    @Generated("jsonschema2pojo")
    public enum TipoPersona {

        _1(1),
        _2(2);
        private final Integer value;
        private final static Map<Integer, TipoPersona> CONSTANTS = new HashMap<Integer, TipoPersona>();

        static {
            for (TipoPersona c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        TipoPersona(Integer value) {
            this.value = value;
        }

        @JsonValue
        public Integer value() {
            return this.value;
        }

        @JsonCreator
        public static TipoPersona fromValue(Integer value) {
            TipoPersona constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException((value +""));
            } else {
                return constant;
            }
        }

    }

}
