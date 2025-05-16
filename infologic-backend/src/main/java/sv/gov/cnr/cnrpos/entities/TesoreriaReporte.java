package sv.gov.cnr.cnrpos.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@Table(name = "tesoreria_reportes")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TesoreriaReporte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "REPORTE_NAME")
    private String reporteName;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CSV")
    private String csv;
    @Column(name = "ES_PARA_TODOS")
    private String esParaTodos;
    @Column(name = "DEPTO_REEPORTE")
    private String deptoReporte;
    @Column(name = "ES_POR_SUCURSAL")
    private String esPorSucursal;
    @Column(name = "CLASIFICACION")
    private String clasificacion;

}
