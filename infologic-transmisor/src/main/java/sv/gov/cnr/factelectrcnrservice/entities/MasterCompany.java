package sv.gov.cnr.factelectrcnrservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "master_company")
@Getter
@Setter
@NoArgsConstructor
public class MasterCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "name_company")
    private String nameCompany;

    @Column(name = "address_company")
    private String addressCompany;

    @Column(name = "phone_company")
    private String phoneCompany;

    @Column(name = "email_company")
    private String emailCompany;

    @Column(name = "description_company")
    private String descriptionCompany;

    @Column(name = "social_reason_company")
    private String socialReasonCompany;

    @Column(name = "nit")
    private String nit;

    @Column(name = "nrc")
    private String nrc;

    @Column(name = "num_id")
    private String numId;

    @Column(name = "mh_user")
    private String mhUser;

    @Column(name = "mh_pass")
    private String mhPass;

    @Column(name = "database_url")
    private String databaseUrl;

    @Column(name = "database_username")
    private String databaseUsername;

    @Column(name = "database_password")
    private String databasePassword;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "new_company")
    private Boolean newCompany;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
