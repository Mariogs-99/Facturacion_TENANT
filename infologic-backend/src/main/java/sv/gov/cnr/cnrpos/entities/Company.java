package sv.gov.cnr.cnrpos.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import oracle.sql.DATE;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Table(name = "company")
public class Company implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_seq")
    @SequenceGenerator(name = "company_seq", sequenceName = "COMPANY_SEQ", allocationSize = 1)
    @Column(name = "ID_COMPANY", nullable = false)
    private Long idCompany;

    @Column(name = "NAME_COMPANY", length = 150)
    private String nameCompany;

    @Column(name = "DESCRIPTION_COMPANY", length = 250)
    private String descriptionCompany;

    @Column(name = "NUM_ID", length = 50)
    private String numId;

    @Column(name = "NIT", length = 25)
    private String nit;

    @Column(name = "NRC", length = 25)
    private String nrc;

    @Column(name = "ADDRESS_COMPANY", length = 250)
    private String addressCompany;

    @Column(name = "PHONE_COMPANY", length = 100)
    private String phoneCompany;

    @Column(name = "ID_GIRO_COMPANY")
    private Long idGiroCompany;

    @Column(name = "SOCIAL_REASON_COMPANY", length = 250)
    private String socialReasonCompany;

    // Getter y Setter para el nuevo campo
    @Setter
    @Getter
    @Column(name = "ID_DEPTO_COMPANY")
    private Long idDeptoCompany;

    @Column(name = "ID_MUNI_COMPANY")
    private Long idMuniCompany;

    @Column(name = "EMAIL_COMPANY", length = 150)
    private String emailCompany;

    @Lob
    @Column(name = "IMAGE_COMPANY")
    private String imageCompany;

    @Column(name = "FOOTER_NOTE", length = 2000)
    private String footerNote;

    @Column(name = "ID_ACTIVIDAD_MH")
    private Long idActividadMH;

    @Column(name = "MH_USER", length = 100)
    private String mhUser;

    @Column(name = "MH_PASS", length = 100)
    private String mhPass;

    @Column(name = "ACTIVE")
    private Boolean active;

    @Column(name = "ID_RECINTO")
    private Long idRecinto;

    @Column(name = "ID_REGIMEN")
    private Long idRegimen;

    @Column(name = "CLAVE_PRIMARIA_CERT", length = 100)
    private String clavePrimariaCert;

    @Column(name = "CREATED_AT")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "UPDATED_AT")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @Column(name = "DELETED_AT")
    private Timestamp deletedAt;

    @Column(name = "NEW_COMPANY")
    private Byte newCompany;

    // Constructors, getters, and setters


    // Nuevos campos para almacenar el nombre del id
    @Setter
    @Transient
    private String nombreDeptoCompany;

    @Setter
    @Transient
    private String nombreMunicipioCompany;

    @Setter
    @Transient
    private String nombreGiroCompany;

    @Setter
    @Transient
    private String nombreRegimenCompany;

    @Setter
    @Transient
    private String nombreRecintoCompany;


    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
}
