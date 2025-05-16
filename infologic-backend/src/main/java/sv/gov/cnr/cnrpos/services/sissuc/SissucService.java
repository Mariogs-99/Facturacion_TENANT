package sv.gov.cnr.cnrpos.services.sissuc;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sv.gob.cnr.ClienteSisucc.APIClient;
import sv.gov.cnr.cnrpos.exceptions.ResourceNotFoundException;
import sv.gov.cnr.cnrpos.models.dto.sisucc.DtoSisucc;
import sv.gov.cnr.cnrpos.security.AuthenticationRequest;

import java.net.ConnectException;

@Service
@RequiredArgsConstructor
public class SissucService {

    @Value("${cnrapps.SISID}")
    private long sisId;

    public Boolean authenticatedSisuccBasic(AuthenticationRequest request) throws ConnectException, RuntimeException, Exception {
        try {
            APIClient api = new APIClient();
            String resp = api.login(request.getUsuario(), request.getClave(), "" + this.sisId);

            if (resp == null) {
                throw new ResourceNotFoundException("api - sissuc no retorno el resultado esperado");
            }

            DtoSisucc dtoSisucc = new Gson().fromJson(resp, DtoSisucc.class);

            if (dtoSisucc.getCodigo() != 1) {
                throw new ResourceNotFoundException(dtoSisucc.getMensaje() + " - CÓDIGO: " + dtoSisucc.getCodigo());
            }

            return true;
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException(ex.getMessage());
        } catch (ConnectException e) {
            throw new ConnectException("No se obtuvo respuesta del servidor - sissuc");
        } catch (Exception exc) {
            throw new Exception("Error desconocido: " + exc.getMessage());
        }
    }
}
