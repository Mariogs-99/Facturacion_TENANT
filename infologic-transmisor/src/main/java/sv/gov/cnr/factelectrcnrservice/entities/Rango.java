
package sv.gov.cnr.factelectrcnrservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "rango")

public class Rango implements Serializable {
    private static  final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RANGO", nullable = false)
    private  Long idRango;

    @Column(name = "NOMBRE_RANGO", length = 100)
    private String nombre;

    @Column(name = "DESCRIPCION_RANGO", length = 100)
    private String descripcion;

    @Column(name = "INICIO_RANGO")
    private Long inicioRango;

    @Column(name = "FINAL_RANGO")
    private Long finalRango;

    @Column(name = "ACTUAL_VALOR")
    private Long actualValor;

    @Column(name = "ID_TIPO_RANGO")
    private Long idTipoRango;

    @Column(name = "ID_SUCURSAL", nullable = false)
    private Long idSucursal;

    @Column(name = "ACTIVE")
    private Boolean active;

    @Column(name = "CREATED_AT")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "UPDATED_AT")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @Column(name = "DELETED_AT")
    private Timestamp deletedAt;


   @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

}
