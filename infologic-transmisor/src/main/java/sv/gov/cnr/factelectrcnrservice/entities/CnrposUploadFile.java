package sv.gov.cnr.factelectrcnrservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.InputStreamResource;

@Data
@Entity
@Table(name = "upload_file")
@NoArgsConstructor
@AllArgsConstructor
public class CnrposUploadFile {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "PRE_ID")
    private int preID;

    @Column(name = "USU_CREA")
    private String usuCrea;

    @Column(name = "TAN_ID")
    private int tanId;

    @Column(name = "URL_API")
    private String urlApi;

    @Column(name = "URL_CONSULTA")
    private String urlConsulta;

    @Column(name = "ESTADO")
    private String estado;

    @Transient
    private InputStreamResource file;
}
