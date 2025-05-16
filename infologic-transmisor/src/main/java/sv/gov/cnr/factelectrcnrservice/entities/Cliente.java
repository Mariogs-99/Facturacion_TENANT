package sv.gov.cnr.factelectrcnrservice.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sv.gov.cnr.factelectrcnrservice.models.enums.TipoDocumentoReceptor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "cliente")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CLIENTE", nullable = false)
    private Long idCliente;

    @Column(name = "NOMBRE_CLIENTE", length = 200)
    private String nombre;

    @Column(name = "NOMBRE_COMERCIAL", length = 200)
    private String nombreComercial;

    @Column(name = "TIPO_DOCUMENTO")
    private String tipoDocumento;//Ligado al CAT-022 Tipo de documento de identificación del Receptor.
    @Column(name = "NUMERO_DOCUMENTO", length = 20)
    private String numeroDocumento;
    @Column(name = "PAIS")
    private String pais;//Ligado a CAT-020	PAÍS
    @Column(name = "DEPARTAMENTO")
    private String departamento;//ligado a CAT-012	DEPARTAMENTO
    @Column(name = "MUNICIPIO")
    private String municipio;//Ligado a CAT-013	MUNICIPIO
    @Column(name = "COLONIA", length = 200)
    private String colonia;//complemento direccion
    @Column(name = "CALLE", length = 200)
    private String calle;//complemento direccion
    @Column(name = "APARTAMENTOLOCAL", length = 200)
    private String apartamentoLocal;//complemento direccion
    @Column(name = "NUMEROCASA", length = 200)
    private String numeroCasa;//complemento direccion
    @Column(name = "DIRECCION", length = 250)
    private String direccion;//complemento direccion
    @Column(name = "TELEFONO")
    private String telefono;
    @Column(name = "ACTIVIDAD_ECONOMICA")
    private String actividadEconomica;//Ligado al CAT-019 Código de Actividad Económica (Se necesita requerir al CNR agregar este valor a su catalogo, tomando el mismo valor del catalogo de hacienda
    @Column(name = "EMAIL_CUSTOMER", length = 250)
    private String email;
    @Column(name = "NIT_CUSTOMER", length = 20)
    private String nit;
    @Column(name = "NRC_CUSTOMER", length = 8)
    private String nrc;
    @Column(name="PORCENTAJE_DESCUENTO")
    private Double porcentajeDescuento;//algunos clientes tienen un descuento especial especificado en porcentaje, puede ser de hasta el 100%.
    @Column(name = "DESCRIPCION_DESCUENTO")
    private String descripcionDescuento;//para especificar que tipo de descuento es, por ser alcaldia, por ser ong, por ser gobierno, etc.

    @Column(name = "es_consumidor_final")
    private Boolean esConsumidorFinal;

    @Column(name = "es_extranjero")
    private Boolean esExtranjero;

    @Column(name = "es_gran_contribuyente")
    private Boolean esGranContribuyente;

    @Column(name = "es_gobierno")
    private Boolean esGobierno;


    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private Timestamp updatedAt;

    @Column(name = "DELETED_AT")
    private Timestamp deletedAt;

}

