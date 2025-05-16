package sv.gov.cnr.cnrpos.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    public String firstname;
    public String lastname;
    public String email;
    public String password;
}
