package sv.gov.cnr.factelectrcnrservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.gov.cnr.factelectrcnrservice.entities.MasterCompany;
import sv.gov.cnr.factelectrcnrservice.repositories.MasterCompanyRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MasterCompanyService {

    private final MasterCompanyRepository repository;

    public MasterCompany getByNit(String nit) {
        return repository.findByNit(nit)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada para NIT: " + nit));
    }

    public List<MasterCompany> obtenerEmpresasActivas() {
        return repository.findAllByActiveTrue();
    }
}
