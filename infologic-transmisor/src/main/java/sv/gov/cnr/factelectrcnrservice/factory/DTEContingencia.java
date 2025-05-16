package sv.gov.cnr.factelectrcnrservice.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sv.gov.cnr.factelectrcnrservice.config.AppConfig;
import sv.gov.cnr.factelectrcnrservice.entities.DteTransaccion;
import sv.gov.cnr.factelectrcnrservice.entities.Transaccion;
import sv.gov.cnr.factelectrcnrservice.models.dte.contingencia.*;
import sv.gov.cnr.factelectrcnrservice.models.dto.MotivoAnulacionDTO;
import sv.gov.cnr.factelectrcnrservice.services.*;

import java.util.AbstractMap;
import java.util.ArrayList;

@Slf4j
@Data
@RequiredArgsConstructor
@Component
public class DTEContingencia extends DTE{


    private final AppConfig appConfig;
    private final CompanyService companyService;
    private final CatalogoService catalogoService;
    private final RangoService rangoService;
    private final TransaccionService transaccionService;
    private final ColaService colaService;
    private final Contingencia documentoContingencia = new Contingencia();


    @Value("${configuracion.contingencia.tipo-contingencia}")
    private Integer tipoContingencia;
    @Value("${configuracion.contingencia.motivo-contingencia}")
    private String motivoContingencia;


    private DteTransaccion generarIdentificacion(Transaccion transaccion){
        DteTransaccion dteTransaccion = new DteTransaccion();
        var identificacion = new Identificacion();
        identificacion.setAmbiente(Identificacion.Ambiente.fromValue(appConfig.getAmbiente()));
        identificacion.setCodigoGeneracion(obtenerCodigoGeneracion());
        identificacion.setFTransmision(obtenerFechaEmision());
        identificacion.setHTransmision(obtenerHoraEmision());
        dteTransaccion.setTransaccion(transaccion);
        dteTransaccion.setCodigoGeneracionContingencia(identificacion.getCodigoGeneracion());
        dteTransaccion.setVersion(identificacion.getVersion());
        dteTransaccion.setAmbiente(appConfig.getAmbiente());
        dteTransaccion.setNumeroDte(transaccion.getNumeroDTE());
        dteTransaccion.setFechaEmision(obtenerFechaEmision());
        dteTransaccion.setHoraEmision(obtenerHoraEmision());
        documentoContingencia.setIdentificacion(identificacion);
        return dteTransaccion;
    }

    private void generarEmisor(Transaccion transaccion) {
        var emisorInfo = companyService.getEmisor();
        var emisor = new Emisor();
        emisor.setNit(emisorInfo.getNit());
        emisor.setNombre(emisorInfo.getNameCompany());
        emisor.setTipoDocResponsable(Emisor.TipoDocResponsable._36); //NIT
        emisor.setNumeroDocResponsable(emisorInfo.getNit());
        emisor.setNombreResponsable(emisorInfo.getNameCompany());
        emisor.setTipoEstablecimiento(Emisor.TipoEstablecimiento._01); //Sucursal
        emisor.setTelefono(transaccion.getSucursal().getTelefono());
        emisor.setCorreo(transaccion.getSucursal().getEmail());  
        emisor.setCodEstableMH(transaccion.getSucursal().getCodigoSucursal().substring(0,4));
        emisor.setCodPuntoVenta(transaccion.getSucursal().getCodigoSucursal().substring(4));


        documentoContingencia.setEmisor(emisor);
    }

    private void generarDetalleDTE(Transaccion transaccion){
        var detallDteList = new ArrayList<DetalleDTE>();
        var dte = new DetalleDTE();
        dte.setNoItem(1);
        dte.setCodigoGeneracion(transaccion.getCodigoGeneracion());
        dte.setTipoDoc(transaccion.getTipoDTE());
        detallDteList.add(dte);
        documentoContingencia.setDetalleDTE(detallDteList);
    }

    private void generarMotivo(Transaccion transaccion){
        var jsonPrevio = dteTransaccionService.findDteTransaccionByTransaccion(transaccion);
        var motivo = new Motivo();
        AbstractMap.SimpleEntry<String, String> nuevaHoraFecha = actualizarFechaHora(jsonPrevio.getFechaEmision(), jsonPrevio.getHoraEmision(), 2);
        motivo.setFInicio(jsonPrevio.getFechaEmision());
        motivo.setFFin(nuevaHoraFecha.getKey());
        motivo.setHInicio(jsonPrevio.getHoraEmision());
        motivo.setHFin(nuevaHoraFecha.getValue());
        motivo.setTipoContingencia(Motivo.TipoContingencia.fromValue(tipoContingencia));
        motivo.setMotivoContingencia(motivoContingencia);
        documentoContingencia.setMotivo(motivo);
    }

    @Override
    public Object crearDTEJSON(Transaccion transaccion) throws JsonProcessingException {
        var dteTransaccion = generarIdentificacion(transaccion);
        generarDetalleDTE(transaccion);
        generarEmisor(transaccion);
        generarMotivo(transaccion);
        crearDteInfo(dteTransaccion);
        return documentoContingencia;
    }

    @Override
    public Object crearJsonInvalidacion(Transaccion transaccion, MotivoAnulacionDTO data) {
        return null;
    }

}
