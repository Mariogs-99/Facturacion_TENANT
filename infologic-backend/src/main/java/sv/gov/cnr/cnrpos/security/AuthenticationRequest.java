package sv.gov.cnr.cnrpos.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    private String email;
    private String password;
    private String usuario;
    private String clave;
    private String nuevaClave;
    private String claveConfirmada;
}
