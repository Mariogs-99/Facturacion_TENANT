package sv.gov.cnr.cnrpos.entities;
import jakarta.persistence.*;
import lombok.Data;
import sv.gov.cnr.cnrpos.models.security.User;

import java.io.Serializable;

@Data
@Entity
@Table(name = "user_company")
public class UserCompany implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumn(name = "ID_COMPANY")
    private Company company;

    @Id
    @ManyToOne
    @JoinColumn(name = "ID_USER")
    private User user;

    // Constructors, getters, and setters
}
