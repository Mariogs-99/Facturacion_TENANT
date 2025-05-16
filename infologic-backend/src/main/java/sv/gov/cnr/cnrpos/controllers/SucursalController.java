package sv.gov.cnr.cnrpos.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.gov.cnr.cnrpos.entities.Company;
import sv.gov.cnr.cnrpos.entities.Rango;
import sv.gov.cnr.cnrpos.entities.Sucursal;
import sv.gov.cnr.cnrpos.repositories.RangoRepository;
import sv.gov.cnr.cnrpos.repositories.SucursalRepository;
import sv.gov.cnr.cnrpos.services.CompanyService;
import sv.gov.cnr.cnrpos.services.RangoService;
import sv.gov.cnr.cnrpos.services.RcatalogoService;
import sv.gov.cnr.cnrpos.services.SucursalService;
import sv.gov.cnr.cnrpos.utils.Utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(path = "/api/v1/sucursal")
public class SucursalController {

    private final SucursalService sucursalService;
    private final CompanyService companyService;
    private final RcatalogoService rcatalogoService;
    private final RangoService rangoService;
    private final SucursalRepository sucursalRepository;
    private final RangoRepository rangoRepository;
    private final Utils utils;

    public SucursalController(
            SucursalService sucursalService,
            CompanyService companyService,
            RcatalogoService rcatalogoService,
            RangoService rangoService,
            SucursalRepository sucursalRepository,
            RangoRepository rangoRepository,
            Utils utils
    ) {
        this.sucursalService = sucursalService;
        this.companyService = companyService;
        this.rcatalogoService = rcatalogoService;
        this.rangoService = rangoService;
        this.sucursalRepository = sucursalRepository;
        this.rangoRepository = rangoRepository;
        this.utils = utils;
    }


    @GetMapping(path = "/test")
    public String testEndpoint(){
        return "this is a test";
    }

    @GetMapping(path = "/list")
    public Object companyList() throws JsonProcessingException {
        return utils.jsonResponse(200, "listado de Sucursales", sucursalService.getAllSucursales());
    }
    @GetMapping(path = "/index")
    public Object sucursalIndex() {
        List<Sucursal> sucursals = sucursalService.getAllSucursales();
        List<Company> activeCompanies = companyService.getAllCompanies();
        // Filtrar las compañías cuyo deletedAt sea nulo o vacío
        List<Company> companies = activeCompanies.stream()
                .filter(company -> company.getDeletedAt() == null || company.getDeletedAt().toLocalDateTime().toString().isEmpty())
                .collect(Collectors.toList());
        Object municipios = rcatalogoService.getMunicipios();
        Object tipos_establecimientos = rcatalogoService.getRCatalogosAsList("TIPO_ESTABLECIMIENTO");
        Object tipo_rango = rcatalogoService.getRCatalogosAsList("TIPO_DOCUMENTO");

        List<Map<String, Object>> sucursalesArray = new ArrayList<>();
        for (Sucursal sucursal : sucursals) {
            if (sucursal.getDeletedAt() == null || sucursal.getDeletedAt().toLocalDateTime().toString().isEmpty()) {
                Map<String, Object> sucursalMap = new HashMap<>();
                sucursalMap.put("nombre", sucursal.getNombre());
                sucursalMap.put("email", sucursal.getEmail());
                sucursalMap.put("direccion", sucursal.getDireccion());
                sucursalMap.put("telefono", sucursal.getTelefono());
                sucursalMap.put("idMuniBranch", sucursal.getIdMuniBranch());
                sucursalMap.put("idCompany", sucursal.getIdCompany());
                sucursalMap.put("nombreMunicipioBranch", sucursal.getNombreMunicipioBranch());
                sucursalMap.put("idSucursal", sucursal.getIdSucursal());
                sucursalMap.put("codigoSucursal", sucursal.getCodigoSucursal());
                sucursalMap.put("misional", sucursal.getMisional());

                sucursalMap.put("idTipoEstablecimiento", sucursal.getIdTipoEstablecimiento());

                // Obteniendo el nombre de la empresa
                String nombreEmpresa = "";
                for (Company company : companies) {
                    if (company.getIdCompany().equals(sucursal.getIdCompany())) {
                        nombreEmpresa = company.getNameCompany();
                        break;
                    }
                }
                sucursalMap.put("nombreCompany", nombreEmpresa);

                List<Map<String, Object>> rangosArray = new ArrayList<>();
                List<Rango> rangos = rangoService.getRangosBySucursalId(sucursal.getIdSucursal());
                for (Rango rango : rangos) {
                    if (rango.getDeletedAt() == null || rango.getDeletedAt().toLocalDateTime().toString().isEmpty()) {
                        Map<String, Object> rangoMap = new HashMap<>();
                        rangoMap.put("descripcion", rango.getDescripcion());
                        rangoMap.put("idRango", rango.getIdRango());
                        rangoMap.put("inicioRango", rango.getInicioRango());
                        rangoMap.put("finalRango", rango.getFinalRango());
                        rangoMap.put("actualValor", rango.getActualValor());
                        rangoMap.put("name", rango.getNombre());
                        rangosArray.add(rangoMap);
                    }
                }
                sucursalMap.put("rango", rangosArray);

                sucursalesArray.add(sucursalMap);
            }
        }

        Map<String, Object> catalogos = new HashMap<>();
        catalogos.put("municipios", municipios != null ? municipios : Collections.emptyList());
        catalogos.put("tipos_establecimientos", tipos_establecimientos != null ? tipos_establecimientos : Collections.emptyList());
        catalogos.put("tipo_rango", tipo_rango != null ? tipo_rango : Collections.emptyList());

        Map<String, Object> data = new HashMap<>();
        data.put("companies", companies != null ? companies : Collections.emptyList());
        data.put("branches", sucursalesArray);
        data.put("catalogos", catalogos);

        return utils.jsonResponse(200, "Index de Sucursales", data);
    }



    // Definición de la clase BodyRequest como clase interna
    @Data
    public static class BodyRequest {
        public Sucursal branch;
        public List<Rango> ranges;

        // Constructor vacío necesario para la deserialización JSON
        public BodyRequest() {}

        // Getters y setters (opcional)


    }

    @PostMapping()
    public Object registerNewSucursal(@RequestBody BodyRequest body) {
        try {
            // Obtener los datos de Sucursal y Rango del cuerpo JSON
            Sucursal sucursal = body.getBranch();
            List<Rango> rangos = body.getRanges();

            // Guardar la Sucursal en la base de datos
            Sucursal nuevaSucursal = sucursalService.saveSucursal(sucursal);

            if (rangos != null){
                // Asignar el id de la nueva Sucursal a cada Rango y guardarlos en la base de datos
                for (Rango rango : rangos) {
                    rango.setIdSucursal(nuevaSucursal.getIdSucursal());
                    rangoService.saveRango(rango);
                }

            }


            return utils.jsonResponse(200, "Sucursal Agregada con éxito", null);
        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se pudo agregar la Sucursal", ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Object deleteSucursal(@PathVariable Long id) {
        try {
            Sucursal sucursal = sucursalService.getSucursal(id);

            if (sucursal == null) {
                return ResponseEntity.notFound().build();
            }

            sucursal.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
            sucursalService.saveSucursal(sucursal);

            return utils.jsonResponse(200, "Se elimino correctamente", null);
        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se pudo eliminar", ex.getMessage());

        }
    }


    @PutMapping("/{id}")
    public Object updateSucursal(@PathVariable Long id, @RequestBody Sucursal sucursalDetails) {

        try {
            Sucursal sucursal = sucursalService.getSucursal(id);

            if (sucursal == null) {
                return ResponseEntity.notFound().build();
            }
            sucursal.setNombre(sucursalDetails.getNombre());
            sucursal.setDireccion(sucursalDetails.getDireccion());
            sucursal.setEmail(sucursalDetails.getEmail());
            sucursal.setIdMuniBranch(sucursalDetails.getIdMuniBranch());
            sucursal.setIdCompany(sucursalDetails.getIdCompany());
            sucursal.setIdTipoEstablecimiento(sucursalDetails.getIdTipoEstablecimiento());
            sucursal.setCodigoSucursal(sucursalDetails.getCodigoSucursal());
            sucursal.setMisional(sucursalDetails.getMisional());
            sucursal.setTelefono(sucursalDetails.getTelefono());

            return utils.jsonResponse(200, "Sucursal Actualizada con éxito", sucursalService.saveSucursal(sucursal));

        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se pudo Actualizar la Sucursal", ex.getMessage());

        }


    }


}
