package sv.gov.cnr.factelectrcnrservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import sv.gov.cnr.factelectrcnrservice.models.enums.EstatusCola;

import java.sql.Timestamp;

@Data
@Builder
@Entity(name = "cola")
@NoArgsConstructor
@AllArgsConstructor
public class Cola {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_COLA", nullable = false)
    private Long idCola;

    @ManyToOne
    @JoinColumn(name = "ID_TRANSACCION")
    private Transaccion transaccion;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private EstatusCola estatusCola;

    @CreationTimestamp
    @Column(name = "FECHA_HORA")
    private Timestamp fechaIngresoCola;

    @Column(name = "NRO_INTENTO")
    private Integer nroIntento;

    @Column(name = "NRO_INTENTO_CONT")
    private Integer nroIntentoCont;

    @Column(name = "ES_CONTINGENCIA")
    private Boolean esContingencia;

    @Column(name = "NOTIFICADO_CONTIGENCIA")
    private Boolean notificadoContigencia;

    @Column(name = "FINALIZADO")
    private Boolean finalizado;

}
