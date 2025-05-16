package sv.gov.cnr.cnrpos.models.security;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Cambiado para MySQL
    @Column(name = "ID_USER")
    private Long idUser;

    @Nullable
    private String firstname;

    @Nullable
    private String lastname;

    @Nullable
    @Column(unique = true)
    private String email;

    @Nullable
    private Long docType;

    @Nullable
    private String docNumber;

    private String password;

    @Nullable
    private String phone;

    private Boolean isActive = true;

    @Nullable
    private Boolean resetPassword = false;

    private Boolean testMode = false;

    @Nullable
    private Long idCompany = 1L;

    @Nullable
    private Long idBranch;

    @Nullable
    private Long idPosition;

    @Column(unique = true)
    private String carnet;

    @Nullable
    @Column(unique = true)
    private String usuario;

    @Nullable
    private String tipo;

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
            name = "users_roles",
            joinColumns = @JoinColumn(name = "ID_USER"),
            inverseJoinColumns = @JoinColumn(name = "ID_ROLE")
    )
    private Set<Rol> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public List<Long> getRoless() {
        return Optional.ofNullable(roles)
                .map(r -> r.stream().map(Rol::getIdRole).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return usuario;
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
