package sv.gov.cnr.factelectrcnrservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "conf_whatsapp")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CnrposConfWhatsapp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CONF", nullable = false)
    private Long idConf;

    @Column(name = "NUMERO", length = 10)
    private String numero;

    @Lob
    @Column(name = "TOKEN")
    private String token;

    @Lob
    @Column(name = "ID_NUMERO")
    private String idNumero;

    @Column(name = "VERSION_APP", length = 10)
    private String versionApp;

    @Column(name = "NOMBRE_TEMPLATE", length = 100)
    private String nombreTemplate;

    @Lob
    @Column(name = "URL_ENDPOINT")
    private String urlEndpoint;

    @Column(name = "ESTADO", length = 1)
    private String estado;

}
