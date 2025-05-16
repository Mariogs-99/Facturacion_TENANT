package sv.gov.cnr.factelectrcnrservice.entities;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "rcatalogos")
public class RCatalogos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CATALOGO", nullable = false)
    private Long idCatalogo;

    @Column(name = "ID_MH", length = 25)
    private String idMh;

    @Column(name = "VALOR", length = 1000, nullable = false)
    private String valor;

    @Column(name = "ALTERNO", length = 1000)
    private String alterno;

    @Column(name = "ALTERNO_A", length = 1000)
    private String alternoA;

    @Column(name = "ALTERNO_B", length = 1000)
    private String alternoB;

    @Column(name = "GRUPO", length = 100)
    private String grupo;

    @Column(name = "SUB_GRUPO", length = 1000)
    private String subGrupo;

    @Column(name = "GRUPO_PADRE", length = 100)
    private String grupoPadre;

    @Column(name = "CAT_PADRE", length = 150)
    private String catPadre;

    @Column(name = "ID_MH_PADRE", length = 150)
    private String idMhPadre;

    @Column(name = "ID_PADRE")
    private Long idPadre;

    @Column(name = "ESTADO", length = 1)
    private String estado;

    @Column(name = "DESCRIPCION", columnDefinition = "CLOB")
    private String descripcion;

    @Column(name = "NOTAS", columnDefinition = "CLOB")
    private String notas;

    @Column(name = "USU_CREA", length = 50)
    private String usuCrea;

    @Column(name = "FEC_CREA")
    private Timestamp fecCrea;

    @Column(name = "USU_MODIFICA", length = 50)
    private String usuModifica;

    @Column(name = "FEC_MODIFICA")
    private Timestamp fecModifica;

    @Column(name = "TRASH")
    private Timestamp trash;
}
