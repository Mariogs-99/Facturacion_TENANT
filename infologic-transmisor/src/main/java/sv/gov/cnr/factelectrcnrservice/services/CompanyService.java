package sv.gov.cnr.factelectrcnrservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sv.gov.cnr.factelectrcnrservice.entities.Company;
import sv.gov.cnr.factelectrcnrservice.repositories.CompanyRepository;

import java.util.List;


@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company saveCompany(Company company){
        return companyRepository.save(company);
    }

    public List<Company> getAllCompanies(){
        return companyRepository.findAll();
    }

    public Company getCompany(Long id) {
        return companyRepository.findById(id).orElse(null);
    }
    public Company getEmisor(){
        //En la reunión con el equipo de  desarrollo (Jonathan y nelson)
        //Se acordó, que para guardar la información del CNR se utilizaría una tabla con un solo registro con la información del CNR
        var emisor =  companyRepository.findFirstByOrderByIdCompanyAsc();//TODO: cambiar este metodo, se debe escribir la logica para obtener el codigo de empresa a través del usuario logueado -> sucursal -> company
        if(emisor.isEmpty()){
            throw new RuntimeException("No se encontró al emisor en la base de datos");
        }
        return emisor.get();
    }
}
