package sv.gov.cnr.factelectrcnrservice.models.security;

import lombok.Data;

import java.io.Serializable;

@Data
public class IntLockId implements Serializable {

    private String lockKey;
    private String region;

}
