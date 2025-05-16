package sv.gov.cnr.cnrpos.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.gov.cnr.cnrpos.entities.Company;
import sv.gov.cnr.cnrpos.repositories.CompanyRepository;
import sv.gov.cnr.cnrpos.services.CompanyService;
import sv.gov.cnr.cnrpos.exceptions.ResourceNotFoundException;
import sv.gov.cnr.cnrpos.services.RcatalogoService;
import sv.gov.cnr.cnrpos.utils.Utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
//@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true") // EMTORNO DEV
//@CrossOrigin(origins = "http://localhost:80", allowedHeaders = "*", allowCredentials = "true") // EMTORNO DEV
//@CrossOrigin(origins = "https://tudominio.com", allowedHeaders = "*", allowCredentials = "true") //ENTORNO PROD
@RequestMapping(path = "/api/v1/company")
public class CompanyController {

    private final CompanyService companyService;
    private final RcatalogoService rcatalogoService;
    private Utils utils;

    private final CompanyRepository companyRepository;
    public CompanyController(CompanyService companyService, RcatalogoService rcatalogoService, CompanyRepository companyRepository) {
        this.companyService = companyService;
        this.rcatalogoService = rcatalogoService;
        this.companyRepository = companyRepository;
    }

    @GetMapping(path = "/test")
    public String testEndpoint(){
        return "this is a test";
    }



    @GetMapping(path = "/list")
    public Object companyList() throws JsonProcessingException {
        return utils.jsonResponse(200, "listado de Compañias", companyService.getAllCompanies());
    }

    @GetMapping(path = "/index")
    public Object companyIndex() {
        List<Company> companies = companyService.getAllCompanies();
        // Filtrar las compañías cuyo deletedAt sea nulo o vacío
        List<Company> activeCompanies = companies.stream()
                .filter(company -> company.getDeletedAt() == null || company.getDeletedAt().toLocalDateTime().toString().isEmpty())
                .collect(Collectors.toList());

        Object municipios = rcatalogoService.getMunicipios();
        // Obtener información de Regimen y Giro
        Object regimens = rcatalogoService.getRCatalogosAsList("REGIMEN");
        Object giros = rcatalogoService.getRCatalogosAsList("ACTIVIDAD_ECONOMICA");
        Object recintos = rcatalogoService.getRCatalogosAsList("RECINTO_FISCAL");


        // información bajo el atributo "catalogos"
        Map<String, Object> catalogos = new HashMap<>();
        catalogos.put("municipios", municipios != null ? municipios : Collections.emptyList());
        catalogos.put("regimens", regimens != null ? regimens : Collections.emptyList());
        catalogos.put("giros", giros != null ? giros : Collections.emptyList());
        catalogos.put("recintos", recintos != null ? recintos : Collections.emptyList());

        // Verificar si la lista de compañías es nula y asignar un array vacío en su lugar
        Map<String, Object> data = new HashMap<>();
        data.put("companies", !activeCompanies.isEmpty()  ? activeCompanies : Collections.emptyList());
        data.put("catalogos", !catalogos.isEmpty() ? catalogos : Collections.emptyList());


        return utils.jsonResponse(200, "Index Empresas", data);

    }

    // Crear una Compañia Nueva
    @PostMapping
    public Object registerNewCompany(@RequestBody Company company){
         try
         {
            return utils.jsonResponse(200, "Empresa Agregada con éxito", companyService.saveCompany(company));

        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se pudo agregar la Empresa", ex.getMessage());

        }
    }


    @PutMapping("/{id}")
    public Object updateCompany(@PathVariable Long id, @RequestBody Company companyDetails) {

        try {
            Company company = companyService.getCompany(id);

            if (company == null) {
                return ResponseEntity.notFound().build();
            }

            // Actualizar propiedades adicionales
            company.setNumId(companyDetails.getNumId());
            company.setNit(companyDetails.getNit());
            company.setNrc(companyDetails.getNrc());
            company.setAddressCompany(companyDetails.getAddressCompany());
            company.setPhoneCompany(companyDetails.getPhoneCompany());
            company.setIdGiroCompany(companyDetails.getIdGiroCompany());
            company.setSocialReasonCompany(companyDetails.getSocialReasonCompany());
            company.setIdDeptoCompany(companyDetails.getIdDeptoCompany());
            company.setIdMuniCompany(companyDetails.getIdMuniCompany());
            company.setEmailCompany(companyDetails.getEmailCompany());
            company.setImageCompany(companyDetails.getImageCompany());
            company.setFooterNote(companyDetails.getFooterNote());
            company.setIdActividadMH(companyDetails.getIdActividadMH());
            company.setMhUser(companyDetails.getMhUser());
            company.setMhPass(companyDetails.getMhPass());
            company.setActive(companyDetails.getActive());
            company.setIdRecinto(companyDetails.getIdRecinto());
            company.setIdRegimen(companyDetails.getIdRegimen());
            company.setClavePrimariaCert(companyDetails.getClavePrimariaCert());
            company.setNewCompany(companyDetails.getNewCompany());
            company.setNameCompany(companyDetails.getNameCompany());
            company.setDescriptionCompany(companyDetails.getDescriptionCompany());

            return utils.jsonResponse(200, "Empresa Actualizada con éxito", companyService.saveCompany(company));

        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se pudo Actualizar la Empresa", ex.getMessage());

        }


    }


    @DeleteMapping("/{id}")
    public Object deleteCompany(@PathVariable Long id) {
        try {
            Company company = companyService.getCompany(id);

            if (company == null) {
                return utils.jsonResponse(200, "No se encontraron Empresas", null);
            }

            company.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
            companyService.saveCompany(company);

            return utils.jsonResponse(200, "Se elimino correctamente", null);
        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se pudo eliminar", ex.getMessage());

        }
    }



    @GetMapping("find/{id}")
    public Object findCompany(@PathVariable Long id) {
        try {
            Company company = companyService.getCompany(id);

            if (company == null) {
                return ResponseEntity.notFound().build();
            }

            Object municipios = rcatalogoService.getMunicipios();
            // Obtener información de Regimen y Giro
            Object regimens = rcatalogoService.getRCatalogosAsList("REGIMEN");
            Object giros = rcatalogoService.getRCatalogosAsList("ACTIVIDAD_ECONOMICA");
            Object recintos = rcatalogoService.getRCatalogosAsList("RECINTO_FISCAL");


            // información bajo el atributo "catalogos"
            Map<String, Object> catalogos = new HashMap<>();
            catalogos.put("municipios", municipios != null ? municipios : Collections.emptyList());
            catalogos.put("regimens", regimens != null ? regimens : Collections.emptyList());
            catalogos.put("giros", giros != null ? giros : Collections.emptyList());
            catalogos.put("recintos", recintos != null ? recintos : Collections.emptyList());

            Map<String, Object> data = new HashMap<>();
            data.put("company", company);
            data.put("catalogos", !catalogos.isEmpty() ? catalogos : Collections.emptyList());


            return utils.jsonResponse(200, "Detalle Sucursal", data);
        } catch (Exception ex) {
            return utils.jsonResponse(500, "Sucursal No existe", ex.getMessage());

        }
    }





}
