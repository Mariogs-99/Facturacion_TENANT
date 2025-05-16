package sv.gov.cnr.factelectrcnrservice.models.security;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USER")
    private Long idUser;

    @Column(name = "FIRSTNAME")
    private String firstname;

    @Column(name = "LASTNAME")
    private String lastname;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "DOC_TYPE")
    private Long docType;

    @Column(name = "DOC_NUMBER")
    private String docNumber;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive;

    @Column(name = "TEST_MODE")
    private Boolean testMode;

    @Column(name = "ID_COMPANY")
    private Long idCompany;

    @Column(name = "ID_BRANCH")
    private Long idBranch;

    @Column(name = "ID_POSITION")
    private Long idPosition;

    @Column(name = "CREATED_AT")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "UPDATED_AT")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @Column(name = "DELETED_AT")
    private Timestamp deletedAt;

    @Transient
    private List<Long> rolIds;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "roles_user",
            joinColumns = @JoinColumn(name = "USER_ID_USER", referencedColumnName = "ID_USER"),
            inverseJoinColumns = @JoinColumn(name = "ROL_ID_ROLE", referencedColumnName = "ID_ROLE")
    )
    private Set<Rol> roles;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("GrantedAuthority - Obteniendo roles: " + getRoless());
        return null;
    }

    public List<Long> getRoless() {
        return roles.stream().map(Rol::getIdRole).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

