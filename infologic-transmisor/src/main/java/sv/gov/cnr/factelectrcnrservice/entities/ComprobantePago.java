package sv.gov.cnr.factelectrcnrservice.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "comprobante_pago_transaccion")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComprobantePago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_COMPROBANTE")
    private Long idComprobante;

    @Column(name = "ID_TRANSACCION")
    private long idTransaccion;

    @Column(name = "NUMERO_COMPROBANTE")
    private String numeroComprobante;

    @Column(name = "REQUEST_ESPECIFICO")
    private String requestEspecifico;
}
