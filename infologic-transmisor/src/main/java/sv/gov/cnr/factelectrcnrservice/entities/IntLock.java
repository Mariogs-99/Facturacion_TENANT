package sv.gov.cnr.factelectrcnrservice.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sv.gov.cnr.factelectrcnrservice.models.security.IntLockId;
import java.util.Date;

@Entity
@Table(name = "int_lock")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(IntLockId.class)
public class IntLock {

    @Id
    @Column(name = "LOCK_KEY", nullable = false, length = 36)
    private String lockKey;

    @Id
    @Column(name = "REGION", nullable = false, length = 100)
    private String region;

    @Column(name = "CLIENT_ID", length = 36)
    private String clientId;

    @Column(name = "CREATED_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
}
