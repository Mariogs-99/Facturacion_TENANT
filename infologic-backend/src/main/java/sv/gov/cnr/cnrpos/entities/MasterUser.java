package sv.gov.cnr.cnrpos.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "master_user")
@Data
public class MasterUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "usuario", unique = true, nullable = false, length = 50)
    private String usuario;

    @Column(name = "clave", nullable = false, length = 255)
    private String clave;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private MasterCompany company;
}
