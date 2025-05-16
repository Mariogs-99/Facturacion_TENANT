package sv.gov.cnr.cnrpos.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import sv.gov.cnr.cnrpos.entities.Company;
import sv.gov.cnr.cnrpos.entities.Sucursal;
import sv.gov.cnr.cnrpos.models.SucursalProjection;
import sv.gov.cnr.cnrpos.repositories.CompanyRepository;
import sv.gov.cnr.cnrpos.repositories.SucursalRepository;

import java.util.List;
import java.util.Optional;



@Service
@RequiredArgsConstructor
public class SucursalService {
    private final SucursalRepository sucursalRepository;
    private final RcatalogoService rcatalogoService;


    public Optional<Sucursal> obtenerSucursal(Long id){
        return sucursalRepository.findById(id);
    }


    public List<Sucursal> getAllSucursales(){
        List<Sucursal> sucursales = sucursalRepository.findAllByDeletedAtIsNull();
        return sucursales;
    }

    public Sucursal saveSucursal(Sucursal sucursal){
        System.out.println("Contenido sucursal: " + sucursal);
        sucursal.setUsuario(getAuthenticatedUserName());

        return sucursalRepository.save(sucursal);
    }

    public Sucursal getSucursal(Long id) {
        return sucursalRepository.findById(id).orElse(null);
        }


    public List<Sucursal> getAllActiveSucursales() {
        return sucursalRepository.findAllByDeletedAtIsNull();
    }

    public List<Sucursal> getSucursalByCompanyId(Long id){
        return sucursalRepository.getSucursalByCompanyId(id);
    }

    public List<SucursalProjection> getSucursalCounts() {
        return sucursalRepository.findSucursalCounts();
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

    public List<String> modulosSucursales(){
       return sucursalRepository.modulosSucursales();
    }

}
