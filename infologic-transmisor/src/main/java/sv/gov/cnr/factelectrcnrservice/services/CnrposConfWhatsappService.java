package sv.gov.cnr.factelectrcnrservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sv.gov.cnr.factelectrcnrservice.entities.CnrposConfWhatsapp;
import sv.gov.cnr.factelectrcnrservice.repositories.CnrposConfWhatsappRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CnrposConfWhatsappService {

    @Autowired
    private CnrposConfWhatsappRepository repository;

    public List<CnrposConfWhatsapp> findAll() {
        return repository.findAll();
    }

    public Optional<CnrposConfWhatsapp> findById(Long id) {
        return repository.findById(id);
    }

    public CnrposConfWhatsapp save(CnrposConfWhatsapp cnrposConfWhatsapp) {
        return repository.save(cnrposConfWhatsapp);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Optional<CnrposConfWhatsapp> findLastActive() {
        return repository.findFirstByEstadoOrderByIdConfDesc("A");
    }

}
