package sv.gov.cnr.factelectrcnrservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sv.gov.cnr.factelectrcnrservice.entities.RCatalogos;
import sv.gov.cnr.factelectrcnrservice.repositories.CatalogoRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CatalogoService {
    private final CatalogoRepository catalogoRepository;
    public static final String AMBIENTE_DESTINO = "CAT-001";
    public static final String TIPO_DE_DOCUMENTO = "CAT-002";
    public static final String MODELO_DE_FACTURACION = "CAT-003";
    public static final String TIPO_DE_TRANSMISION = "CAT-004";
    public static final String TIPO_DE_CONTINGENCIA = "CAT-005";
    public static final String RETENCION_IVA_MH = "CAT-006";
    public static final String TIPO_DE_GENERACION_DEL_DOCUMENTO = "CAT-007";
    public static final String CATÁLOGO_ELIMINADO = "CAT-008";
    public static final String TIPO_DE_ESTABLECIMIENTO = "CAT-009";
    public static final String CODIGO_TIPO_DE_SERVICIO_MEDICO = "CAT-010";
    public static final String TIPO_DE_ITEM = "CAT-011";
    public static final String DEPARTAMENTO = "CAT-012";
    public static final String MUNICIPIO = "CAT-013";
    public static final String UNIDAD_DE_MEDIDA = "CAT-014";
    public static final String TRIBUTOS = "CAT-015";
    public static final String CONDICION_DE_LA_OPERACION = "CAT-016";
    public static final String FORMA_DE_PAGO = "CAT-017";
    public static final String PLAZO = "CAT-018";
    public static final String CODIGO_DE_ACTIVIDAD_ECONOMICA = "CAT-019";
    public static final String PAIS = "CAT-020";
    public static final String OTROS_DOCUMENTOS_ASOCIADOS = "CAT-021";
    public static final String TIPO_DE_DOCUMENTO_DE_RECEPTOR = "CAT-022";
    public static final String TIPO_DE_DOCUMENTO_EN_CONTINGENCIA = "CAT-023";
    public static final String TIPO_DE_INVALIDACION = "CAT-024";
    public static final String TITULO_A_QUE_SE_REMITEN_LOS_BIENES = "CAT-025";
    public static final String TIPO_DE_DONACION = "CAT-026";
    public static final String TIPO_DE_PERSONA = "CAT-029";
    public static final String RECINTO_FISCAL = "CAT-027";
    public Optional<String> obtenerIDMHPorIdCatalogoYValor(String idCatalogo, String valor){//Para obtener el Código definido por el ministerio de hacienda en el documento catalogos, por medio del valor y del catálogo a buscar
        return catalogoRepository.findByCatPadreAndValorIgnoreCase(idCatalogo,valor).map(RCatalogos::getIdMh);
    }

    public Optional<String> obtenerValorMHPorIdCatalogoYValor(String idCatalogo, String valor){//Para obtener el Código definido por el ministerio de hacienda en el documento catalogos, por medio del valor y del catálogo a buscar
        return catalogoRepository.findByCatPadreAndIdMhIgnoreCase(idCatalogo,valor).map(RCatalogos::getValor);
    }

    public Optional<String> findByCatPadreAndIdMhAndIdMhPadreIgnoreCase(String idCatalogo, String valor, String valor2){//Para obtener el Código definido por el ministerio de hacienda en el documento catalogos, por medio del valor y del catálogo a buscar
        return catalogoRepository.findByCatPadreAndIdMhAndIdMhPadreIgnoreCase(idCatalogo,valor,valor2).map(RCatalogos::getValor);
    }
    
    public Optional<String> obtenerValorMHPorIDMHYIDCatalogo(String idMh, String catalogo){
        if(StringUtils.hasText(idMh)){
            return catalogoRepository.findByIdMhAndCatPadre(idMh,catalogo).map(RCatalogos::getValor);
        }
        return Optional.empty();
    }

    public Optional<String> obtenerIdMHPorIDMHYIDCatalogo(String idMh, String catalogo){
        if(StringUtils.hasText(idMh)){
            return catalogoRepository.findByIdMhAndCatPadre(idMh,catalogo).map(RCatalogos::getIdMh);
        }
        return Optional.empty();
    }

    public String findById(Long id){
        return catalogoRepository.findById(id).map(RCatalogos::getIdMh)
                .orElseThrow(() -> new IllegalArgumentException("No existe un registro en RCatalogos con el id: " + id ));
    }

    public String findByIdValor(Long id){
        return catalogoRepository.findById(id).map(RCatalogos::getValor)
                .orElseThrow(() -> new IllegalArgumentException("No existe un registro en RCatalogos con el id: " + id ));
    }

    public Long obtenerIdCatalogoRegistroPorIDMHyIDCatalogo(String idMh, String catalogo){
        return catalogoRepository.findByIdMhAndCatPadre(idMh, catalogo).map(RCatalogos::getIdCatalogo)
                .orElseThrow(() -> new IllegalArgumentException("No existe un registro en RCatalogos"));
    }


}
