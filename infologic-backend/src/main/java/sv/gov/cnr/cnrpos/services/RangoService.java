
package sv.gov.cnr.cnrpos.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import sv.gov.cnr.cnrpos.entities.Rango;
import sv.gov.cnr.cnrpos.entities.Sucursal;
import sv.gov.cnr.cnrpos.repositories.RangoRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RangoService {
    private final RangoRepository rangoRepository;

    public Rango saveRango(Rango rango){
        System.out.println("Contenido rango: " + rango);
        rango.setUsuario(getAuthenticatedUserName());
        return rangoRepository.save(rango);
    }
    public List<Rango> getRangosBySucursalId(Long idSucursal) {
        return rangoRepository.findByIdSucursalAndActive(idSucursal, true);
    }

    public Rango getRango(Long id){
        return rangoRepository.findById(id).orElse(null);
    }

    private String getAuthenticatedUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

}
