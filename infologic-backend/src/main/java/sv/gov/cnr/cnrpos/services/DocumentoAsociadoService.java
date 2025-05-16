package sv.gov.cnr.cnrpos.services;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import sv.gov.cnr.cnrpos.repositories.DocumentoAsociadoRepository;

@Service
@AllArgsConstructor
public class DocumentoAsociadoService {

    private final DocumentoAsociadoRepository documentoAsociadoRepository;


    
    public List<Long> getAllTransactionIds() {
        return documentoAsociadoRepository.findAllTransactionIds();
    }


    public List<Long> getAllTransactionIdsHijas() {
        return documentoAsociadoRepository.findAllTransactionIdsHijas();
    }


    

}
