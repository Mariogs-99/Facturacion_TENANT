package sv.gov.cnr.cnrpos.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "master_company")
@Data
public class MasterCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "name_company", length = 150)
    private String nameCompany;

    @Column(name = "address_company", length = 250)
    private String addressCompany;

    @Column(name = "phone_company", length = 100)
    private String phoneCompany;

    @Column(name = "email_company", length = 150)
    private String emailCompany;

    @Column(name = "description_company", length = 250)
    private String descriptionCompany;

    @Column(name = "social_reason_company", length = 250)
    private String socialReasonCompany;

    @Column(name = "nit", length = 25)
    private String nit;

    @Column(name = "nrc", length = 25)
    private String nrc;

    @Column(name = "num_id", length = 50)
    private String numId;

    @Column(name = "mh_user", length = 100)
    private String mhUser;

    @Column(name = "mh_pass", length = 100)
    private String mhPass;

    @Column(name = "database_url", length = 255)
    private String databaseUrl;

    @Column(name = "database_username", length = 100)
    private String databaseUsername;

    @Column(name = "database_password", length = 100)
    private String databasePassword;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "new_company")
    private Integer newCompany;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
