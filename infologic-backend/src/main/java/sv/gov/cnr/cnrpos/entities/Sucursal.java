package sv.gov.cnr.cnrpos.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@Table(name = "sucursal")
public class Sucursal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "branch_seq")
    @SequenceGenerator(name = "branch_seq", sequenceName = "BRANCH_SEQ", allocationSize = 1)
    @Column(name = "ID_SUCURSAL", nullable = false)
    private Long idSucursal;

    @Column(name = "ID_COMPANY")
    private Long idCompany;

    @Column(name = "NOMBRE_SUCURSAL", length = 150)
    private String nombre;

    @Column(name = "DIRECCION_SUCURSAL", length = 250)
    private String direccion;

    @Column(name = "TELEFONO_SUCURSAL", length = 9)
    private String telefono;

    @Column(name = "EMAIL_SUCURSAL", length = 150)
    private String email;

    @Column(name = "ID_MUNI_BRANCH")
    private Long idMuniBranch;

    @Column(name = "ID_DEPTO_BRANCH")
    private Long idDeptoBranch;

    @Column(name = "ID_TIPO_ESTABLECIMIENO")
    private Long idTipoEstablecimiento;

    @Column(name = "CODIGO_SUCURSAL", length = 8)
    private String codigoSucursal;

    @Column(name = "MISIONAL", length = 150)
    private String misional;

    @Column(name = "MODULO", length = 150)
    private String modulo;

    @Column(name = "CREATED_AT")
    @CreationTimestamp
    private java.sql.Timestamp createdAt;

    @Column(name = "UPDATED_AT")
    @UpdateTimestamp
    private java.sql.Timestamp updatedAt;

    @Column(name = "DELETED_AT")
    private Timestamp deletedAt;

    @Column(name = "USUARIO")
    private String usuario;

    // Nuevos campos para almacenar el nombre del id
    @Setter
    @Transient
    private String nombreDeptoBranch;

    @Setter
    @Transient
    private String nombreMunicipioBranch;

    @Setter
    @Transient
    private String nombreCompany;

    @ManyToOne
    @JoinColumn(name = "ID_COMPANY", referencedColumnName = "ID_COMPANY", insertable = false, updatable = false)
    private Company company;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new java.sql.Timestamp(System.currentTimeMillis());
    }

    public String getNombreCompany(){
        if (this.company != null) {
            return this.company.getNameCompany();
        } else {
            return null;
        }
    }






}