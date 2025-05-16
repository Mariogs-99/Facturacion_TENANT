package sv.gov.cnr.factelectrcnrservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sv.gov.cnr.factelectrcnrservice.entities.CnrposUploadFile;
import sv.gov.cnr.factelectrcnrservice.repositories.CnrposUploadFileRepository;

import java.util.Optional;

@Service
public class CnrposUploadFileService {

    @Autowired
    private CnrposUploadFileRepository cnrposUploadFileRepository;

    public Optional<CnrposUploadFile> getActivo(){
        return cnrposUploadFileRepository.findFirstByEstadoEquals("A");
    }

}
