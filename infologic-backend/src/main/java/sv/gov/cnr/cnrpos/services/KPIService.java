package sv.gov.cnr.cnrpos.services;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import sv.gov.cnr.cnrpos.entities.Sucursal;
import sv.gov.cnr.cnrpos.models.dto.PuntoVentaDTO;
import sv.gov.cnr.cnrpos.models.dto.SucursalDTO;
import sv.gov.cnr.cnrpos.models.enums.EstatusCola;
import sv.gov.cnr.cnrpos.repositories.SucursalRepository;
import sv.gov.cnr.cnrpos.repositories.TransaccionRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class KPIService {

    private TransaccionRepository transaccionRepository;
    private SucursalRepository sucursalRepository;

    public long invalidadosKPI() {
        return transaccionRepository.countInvalidatedDocuments(EstatusCola.ANULADO);
    }

    public long invalidadosByVentaKPI(Long idSucursal) {
        return transaccionRepository.countInvalidatedDocumentsByVenta(EstatusCola.ANULADO,idSucursal);
    }

    public long cantidadDocumentosEmitidosKPI(){
        return  transaccionRepository.countGeneratedDocuments(EstatusCola.APROBADO,EstatusCola.APROBADO_CONTINGENCIA);
    }

    public long cantidadDocumentosEmitidosByVentaKPI(Long idSucursal){
        return  transaccionRepository.countGeneratedDocumentsByVenta(EstatusCola.APROBADO, EstatusCola.APROBADO_CONTINGENCIA, idSucursal);
    }

    public long countContigenciaDocumentos(){ return transaccionRepository.countContingenciaDocumentos(EstatusCola.MARCADO_CONTINGENCIA);}

    public long countContigenciaDocumentosByVenta(Long idSucursal){return transaccionRepository.countContingenciaDocumentosByVenta(EstatusCola.MARCADO_CONTINGENCIA,idSucursal);}

    //DOCUMENTOS RECHAZADOS
    public long countRechazadosDocumentos(){ return transaccionRepository.countRechazadosDocumentos(EstatusCola.RECHAZADO);}

    public long countRechazadosDocumentosByVenta(Long idSucursal){return transaccionRepository.countRechazadosDocumentosByVenta(EstatusCola.RECHAZADO, idSucursal);}

    //FE CCFE CANCELADAS EN EFECTIVO
    public long countGeneratedDocumentosByTipoPagoAndTipoDoc(String tipoDTE){return transaccionRepository.countGeneratedDocumentosByTipoPagoAndTipoDoc(EstatusCola.APROBADO, EstatusCola.APROBADO_CONTINGENCIA, tipoDTE);}

    public List<PuntoVentaDTO> findSucursalesWithConcat(){
        List<Sucursal> list = sucursalRepository.findAllByDeletedAtIsNull();
        Map<String, List<PuntoVentaDTO.PuntoVenta>> listMap = new LinkedHashMap<>();
        for (Sucursal sucursalEntity : list) {
            String sucursal = sucursalEntity.getNombre();
            PuntoVentaDTO.PuntoVenta puntoVenta = new PuntoVentaDTO.PuntoVenta();
            puntoVenta.setIdSucursal(sucursalEntity.getIdSucursal());
            puntoVenta.setPuntoVenta(sucursalEntity.getMisional());

            if (!listMap.containsKey(sucursal)){
                listMap.put(sucursal, new ArrayList<>());
            }

            listMap.get(sucursal).add(puntoVenta);
        }

        List<PuntoVentaDTO> puntoVentaDTOS = new ArrayList<>();
        for (Map.Entry<String, List<PuntoVentaDTO.PuntoVenta>> entry : listMap.entrySet()){
            PuntoVentaDTO puntoVentaDTO = new PuntoVentaDTO();
            puntoVentaDTO.setSucursal(entry.getKey());
            puntoVentaDTO.setPuntoVentas(entry.getValue());
            puntoVentaDTOS.add(puntoVentaDTO);
        }
        return puntoVentaDTOS;
    }
}
