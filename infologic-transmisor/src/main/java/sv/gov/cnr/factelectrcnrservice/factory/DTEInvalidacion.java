package sv.gov.cnr.factelectrcnrservice.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.RoundingMode;

import org.springframework.stereotype.Component;
import sv.gov.cnr.factelectrcnrservice.config.AppConfig;
import sv.gov.cnr.factelectrcnrservice.entities.DteTransaccion;
import sv.gov.cnr.factelectrcnrservice.entities.Transaccion;
import sv.gov.cnr.factelectrcnrservice.models.dte.invalidacion.*;
import sv.gov.cnr.factelectrcnrservice.models.dto.MotivoAnulacionDTO;
import sv.gov.cnr.factelectrcnrservice.services.*;


@Slf4j
@Data
@RequiredArgsConstructor
@Component
public class DTEInvalidacion extends DTE{

    private final AppConfig appConfig;
    private final CompanyService companyService;
    private final CatalogoService catalogoService;
    private final RangoService rangoService;
    private final TransaccionService transaccionService;
    private final ColaService colaService;
    private Invalidacion documentoInvalidacion = new Invalidacion();

    private DteTransaccion generarIdentificacion(Transaccion transaccion){
        DteTransaccion dteTransaccion = new DteTransaccion();
        var identificacion = new Identificacion();
        identificacion.setAmbiente(Identificacion.Ambiente.fromValue(appConfig.getAmbiente()));
        identificacion.setFecAnula(obtenerFechaEmision());
        identificacion.setCodigoGeneracion(obtenerCodigoGeneracion());
        identificacion.setHorAnula(obtenerHoraEmision());
        dteTransaccion.setCodigoGeneracionAnulacion(identificacion.getCodigoGeneracion());
        dteTransaccion.setFechaAnulacion(identificacion.getFecAnula());
        dteTransaccion.setHoraAnula(identificacion.getHorAnula());
        dteTransaccion.setTransaccion(transaccion);
        dteTransaccion.setAmbiente(String.valueOf(identificacion.getAmbiente()));
        dteTransaccion.setVersion(identificacion.version);
        dteTransaccion.setNumeroDte(transaccion.getNumeroDTE());
        documentoInvalidacion.setIdentificacion(identificacion);
        return dteTransaccion;
    }

    private void generarEmisor(Transaccion transaccion){
        var emisorInfo = companyService.getEmisor();
        var emisor = new Emisor();
        emisor.setNit(emisorInfo.getNit());
        emisor.setNombre(emisorInfo.getNameCompany());
        emisor.setTipoEstablecimiento(Emisor.TipoEstablecimiento._01); //Sucursal
        emisor.setNomEstablecimiento(transaccion.getSucursal().getNombre());
        emisor.setTelefono(transaccion.getSucursal().getTelefono());
        emisor.setCorreo(transaccion.getSucursal().getEmail());
        documentoInvalidacion.setEmisor(emisor);
    }

    private void generarDocumento(Transaccion transaccion, MotivoAnulacionDTO data){
        var documento = new Documento();
        var ultimoJson = dteTransaccionService.findDteTransaccionByTransaccion(transaccion);
        documento.setTipoDte(transaccion.getTipoDTE());
        documento.setCodigoGeneracion(transaccion.getCodigoGeneracion());
        documento.setSelloRecibido(ultimoJson.getSelloRecibidoMh());
        documento.setNumeroControl(transaccion.getNumeroDTE());
        documento.setFecEmi(ultimoJson.getFechaEmision());
        documento.setMontoIva(transaccion.getTotalIva().setScale(2, RoundingMode.HALF_UP).doubleValue());
        documento.setCodigoGeneracionR(data.getCodigoGeneracionr());
        documento.setTipoDocumento(Documento.TipoDocumento.fromValue(transaccion.getCliente().getTipoDocumento()));
        documento.setNumDocumento(transaccion.getCliente().getNumeroDocumento());
        documento.setNombre(transaccion.getCliente().getNombre());
        documento.setTelefono(transaccion.getCliente().getTelefono());
        documento.setCorreo(transaccion.getCliente().getEmail());
        documentoInvalidacion.setDocumento(documento);
    }

    private void generarMotivo(MotivoAnulacionDTO dataMotivo){
        var motivo = new Motivo();
        motivo.setTipoAnulacion(dataMotivo.getTipoInvalidacion());
        motivo.setMotivoAnulacion(dataMotivo.getMotivoInvalidacion());
        motivo.setNombreResponsable(dataMotivo.getNombreResponsable());
        motivo.setTipDocResponsable(dataMotivo.getTipoDocResponsable());
        motivo.setNumDocResponsable(dataMotivo.getNumDocResponsable());
        motivo.setNombreSolicita(dataMotivo.getNombreSolicita());
        motivo.setTipDocSolicita(dataMotivo.getTipoDocSolicita());
        motivo.setNumDocSolicita(dataMotivo.getNumDocSolicita());
        documentoInvalidacion.setMotivo(motivo);
    }


    @Override
    public Object crearDTEJSON(Transaccion transaccion) throws JsonProcessingException {
        return  null;
    }

    @Override
    public Object crearJsonInvalidacion(Transaccion transaccion, MotivoAnulacionDTO data) {
        DteTransaccion dteTransaccion;
        DteTransaccion dteTransaccionAnterior = dteTransaccionService.findDteTransaccionByTransaccion(transaccion);
        dteTransaccion = generarIdentificacion(transaccion);
        generarEmisor(transaccion);
        generarDocumento(transaccion, data);
        generarMotivo(data);
        crearDteInfo(dteTransaccion);
        dteTransaccion.setCodigoGeneracion(dteTransaccionAnterior.getCodigoGeneracion());
        dteTransaccion.setSelloRecibidoMh(dteTransaccionAnterior.getSelloRecibidoMh());
        dteTransaccion.setFechaGeneracion(dteTransaccionAnterior.getFechaGeneracion());
        dteTransaccion.setTipoDTE(dteTransaccionAnterior.getTipoDTE());
        dteTransaccion.setFechaEmision(dteTransaccionAnterior.getFechaEmision());
        dteTransaccion.setHoraEmision(dteTransaccionAnterior.getHoraEmision());
        return documentoInvalidacion;
    }

}
