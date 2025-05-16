package sv.gov.cnr.cnrpos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sv.gov.cnr.cnrpos.entities.Company;
import sv.gov.cnr.cnrpos.exceptions.ResourceNotFoundException;
import sv.gov.cnr.cnrpos.repositories.CompanyRepository;
import sv.gov.cnr.cnrpos.utils.Utils;

import java.util.List;
import java.util.Optional;


@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final RcatalogoService rcatalogoService;
    private Utils utils;

    @Autowired
    public CompanyService(CompanyRepository companyRepository, RcatalogoService rcatalogoService) {
        this.companyRepository = companyRepository;
        this.rcatalogoService = rcatalogoService;
    }

    public Company saveCompany(Company company){
        return companyRepository.save(company);

    }

    public List<Company> getAllCompanies(){
        //return companyRepository.findAll();

        List<Company> companies = companyRepository.findAll();

        // Obtener el nombre del departamento y agregarlo a cada objeto Company
        companies.forEach(company -> {

            if (company.getIdDeptoCompany() != null) {
                String nombreDepto = rcatalogoService.getRcatalogosValue(company.getIdDeptoCompany());
                company.setNombreDeptoCompany(nombreDepto != null ? nombreDepto : "");
            } else {
                company.setNombreDeptoCompany("");
            }
            if (company.getIdMuniCompany() != null) {
                String nombreMunicipio = rcatalogoService.getRcatalogosValue(company.getIdMuniCompany());
                company.setNombreMunicipioCompany(nombreMunicipio != null ? nombreMunicipio : "");
            } else {
                company.setNombreMunicipioCompany("");
            }

            if (company.getIdGiroCompany() != null) {
                String nombreGiro = rcatalogoService.getRcatalogosValue(company.getIdGiroCompany());
                company.setNombreGiroCompany(nombreGiro != null ? nombreGiro : "");
            } else {
                company.setNombreGiroCompany("");
            }

            if (company.getIdRecinto() != null) {
                String nombreRecinto = rcatalogoService.getRcatalogosValue(company.getIdRecinto());
                company.setNombreRecintoCompany(nombreRecinto != null ? nombreRecinto : "");
            } else {
                company.setNombreRecintoCompany("");
            }

            if (company.getIdRegimen() != null) {
                String nombreRegimen = rcatalogoService.getRcatalogosValue(company.getIdRegimen());
                company.setNombreRegimenCompany(nombreRegimen != null ? nombreRegimen : "");
            } else {
                company.setNombreRegimenCompany("");
            }

        });

        return companies;
    }

    public Company getCompany(Long id) {
        return companyRepository.findById(id).orElse(null);
    }


    public ResponseEntity<Object> deleteCompany(Long id) {
        Optional<Company> company = companyRepository.findById(id);
        if (company.isPresent()) {
            companyRepository.delete(company.get());
        } else {
            throw new ResourceNotFoundException("No se encontró la empresa con ID " + id);
        }
        return null;
    }






}
