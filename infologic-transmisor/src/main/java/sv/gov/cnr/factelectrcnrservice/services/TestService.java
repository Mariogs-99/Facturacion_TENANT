package sv.gov.cnr.factelectrcnrservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sv.gov.cnr.factelectrcnrservice.models.dto.DocumentosDTO;
import sv.gov.cnr.factelectrcnrservice.repositories.TestRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestService {

    private final TestRepository testRepository;

    public List<DocumentosDTO> obtenerDocumentosNotaCredito(String nitCliente){
        return testRepository.getDocumentoNotaCredito(nitCliente);
    }

}
