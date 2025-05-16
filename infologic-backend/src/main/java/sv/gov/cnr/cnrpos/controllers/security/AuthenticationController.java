package sv.gov.cnr.cnrpos.controllers.security;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.gov.cnr.cnrpos.models.security.Token;
import sv.gov.cnr.cnrpos.security.AuthenticationRequest;
import sv.gov.cnr.cnrpos.security.AuthenticationService;
import sv.gov.cnr.cnrpos.security.RegisterRequest;
import sv.gov.cnr.cnrpos.utils.Utils;

import java.util.HashMap;
import java.util.Map;

@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final Utils utils;



    @PostMapping("/authenticate")
    public ResponseEntity<Object> login(@RequestBody AuthenticationRequest request) {
        try {
            return utils.jsonResponse(200, "bienvenido", authenticationService.authenticate(request));
        } catch (Exception ex) {
            return utils.jsonResponse(401, "Usuario y/o Contraseña incorrectos", ex.getMessage());

        }
    }


    //! LOGOUT CAMBIADO PARA AUTORIZACION
    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestBody Map<String, String> body, HttpServletRequest request) {
        try {
            // Obtener el token desde el header Authorization
            String authHeader = request.getHeader("Authorization");
            String token = null;

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            } else if (body.containsKey("token")) {
                // Si no está en el header, buscarlo en el body
                token = body.get("token");
            }

            if (token == null || token.isBlank()) {
                return utils.jsonResponse(400, "Token no proporcionado o inválido", null);
            }

            authenticationService.logout(token);
            return utils.jsonResponse(200, "logout", null);

        } catch (Exception ex) {
            return utils.jsonResponse(500, "Error al cerrar sesión", ex.getMessage());
        }
    }



}
