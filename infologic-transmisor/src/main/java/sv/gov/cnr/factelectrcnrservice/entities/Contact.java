package sv.gov.cnr.factelectrcnrservice.entities;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "contact")
public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CONTACT", nullable = false)
    private Long idContact;

    @Column(name = "NAME", length = 250)
    private String name;

    @Column(name = "PHONE", length = 100)
    private String phone;

    @Column(name = "EMAIL", length = 300)
    private String email;

    @Column(name = "ID_CUSTOMER")
    private Long idCustomer;

    @Column(name = "CREATED_AT")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "UPDATED_AT")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @Column(name = "DELETED_AT")
    private Timestamp deletedAt;

    @ManyToOne
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID_CLIENTE", insertable = false, updatable = false)
    private Cliente cliente;

    // Constructors, getters, and setters
}

