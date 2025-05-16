package sv.gov.cnr.factelectrcnrservice.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "token_mh")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TokenMh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Nuevo campo PK

    @Lob // Esto le dice a JPA que el campo puede ser grande (TEXT, BLOB)
    private String token;

    @CreationTimestamp
    @Column(name = "FECHA_GENERACION")
    private Timestamp fechaGeneracion;
}
