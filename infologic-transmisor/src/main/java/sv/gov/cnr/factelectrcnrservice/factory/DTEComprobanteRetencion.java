package sv.gov.cnr.factelectrcnrservice.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sv.gov.cnr.factelectrcnrservice.config.AppConfig;
import sv.gov.cnr.factelectrcnrservice.entities.*;
import sv.gov.cnr.factelectrcnrservice.models.dte.Direccion;
import sv.gov.cnr.factelectrcnrservice.models.dte.cr.*;
import sv.gov.cnr.factelectrcnrservice.models.dto.MotivoAnulacionDTO;
import sv.gov.cnr.factelectrcnrservice.services.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Data
@RequiredArgsConstructor
@Component
public class DTEComprobanteRetencion extends DTE{

    private final AppConfig appConfig;
    private final CompanyService companyService;
    private final CatalogoService catalogoService;
    private final RangoService rangoService;
    private final TransaccionService transaccionService;
    private final ColaService colaService;
    private final ComprobanteRetencionElectronica comprobanteRetencionElectronica = new ComprobanteRetencionElectronica();

    @Value("${configuracion.contingencia.tipo-contingencia}")
    private Integer tipoContingencia;
    @Value("${configuracion.contingencia.motivo-contingencia}")
    private String motivoContingencia;

    private DteTransaccion generarIdentificacion(Transaccion transaccion, String correlativo, String codigoGeneracion, Cola cola){
        DteTransaccion dteTransaccion = new DteTransaccion();
        var identificacion = new Identificacion();
        identificacion.setAmbiente(Identificacion.Ambiente.fromValue(appConfig.getAmbiente()));
        if(transaccion.getCodigoGeneracion() == null){
            identificacion.setNumeroControl(String.format("DTE-%s-%s-%015d", identificacion.getTipoDte(), transaccion.getSucursal().getCodigoSucursal(), Integer.valueOf(correlativo)));
        }else{
            identificacion.setNumeroControl(correlativo);
        }
        identificacion.setFecEmi(obtenerFechaEmision());
        identificacion.setHorEmi(obtenerHoraEmision());
        identificacion.setCodigoGeneracion(codigoGeneracion);
        dteTransaccion.setTransaccion(transaccion);
        dteTransaccion.setCodigoGeneracion(identificacion.getCodigoGeneracion());
        dteTransaccion.setFechaGeneracion(identificacion.getFecEmi());
        dteTransaccion.setVersion(identificacion.getVersion());
        dteTransaccion.setAmbiente(appConfig.getAmbiente());
        dteTransaccion.setNumeroDte(identificacion.getNumeroControl());
        dteTransaccion.setFechaEmision(identificacion.getFecEmi());
        dteTransaccion.setHoraEmision(identificacion.getHorEmi());
        dteTransaccion.setTipoDTE(transaccion.getTipoDTE());
        comprobanteRetencionElectronica.setIdentificacion(identificacion);
        return dteTransaccion;
    }


    private void generarEmisor(Transaccion transaccion) {
        var emisorInfo = companyService.getEmisor();
        var emisor = new Emisor();
        emisor.setNit(emisorInfo.getNit());
        emisor.setNrc(emisorInfo.getNrc());
        emisor.setNombre(emisorInfo.getNameCompany());
        emisor.setCodActividad(catalogoService.findById(emisorInfo.getIdActividadMH()));
        var descActividad = catalogoService.obtenerValorMHPorIDMHYIDCatalogo(catalogoService.findById(emisorInfo.getIdActividadMH()), CatalogoService.CODIGO_DE_ACTIVIDAD_ECONOMICA).orElse("");
        emisor.setDescActividad(descActividad);
        emisor.setNombreComercial(emisorInfo.getSocialReasonCompany());
        emisor.setTipoEstablecimiento(Emisor.TipoEstablecimiento._01);// 01 = Sucursal/Agencia CAT-009 Tipo de establecimiento
        var direccion = new Direccion();
        direccion.setDepartamento(catalogoService.findById(transaccion.getSucursal().getIdDeptoBranch()));
        direccion.setMunicipio(catalogoService.findById(transaccion.getSucursal().getIdMuniBranch()));
        direccion.setComplemento(transaccion.getSucursal().getDireccion());
        emisor.setDireccion(direccion);
        emisor.setTelefono(transaccion.getSucursal().getTelefono());
        emisor.setCorreo(transaccion.getSucursal().getEmail());
        emisor.setCodigoMH(emisor.getCodigoMH());
        emisor.setCodigo(emisor.getCodigo());
        emisor.setPuntoVentaMH(emisor.getPuntoVentaMH());
        emisor.setPuntoVenta(emisor.getPuntoVenta());
        comprobanteRetencionElectronica.setEmisor(emisor);
    }

    private void generarReceptor(Cliente cliente){
        var receptor = new Receptor();
        receptor.setCorreo(cliente.getEmail());
        receptor.setNombre(cliente.getNombre());
        receptor.setNrc(cliente.getNrc());
        receptor.setTelefono(cliente.getTelefono());
        receptor.setNombreComercial(cliente.getNombreComercial());
        receptor.setNumDocumento(cliente.getNumeroDocumento());
        receptor.setTipoDocumento(Receptor.TipoDocumento.fromValue(cliente.getTipoDocumento()));
        var direccion = new Direccion();
        direccion.setDepartamento(cliente.getDepartamento());
        direccion.setMunicipio(cliente.getMunicipio());
        direccion.setComplemento(cliente.getDireccion());
        receptor.setDireccion(direccion);
        receptor.setCodActividad(cliente.getActividadEconomica());
        var descActividad = catalogoService.obtenerValorMHPorIDMHYIDCatalogo(cliente.getActividadEconomica(),CatalogoService.CODIGO_DE_ACTIVIDAD_ECONOMICA).orElse(null);
        receptor.setDescActividad(descActividad);
        comprobanteRetencionElectronica.setReceptor(receptor);
    }


    private void generarCuerpoDocumento(List<CuerpoDocCR> items){
        var cuerpoDocumentoList = new ArrayList<CuerpoDocumento>();
        var contador = 1;
        for (var item: items){
            var cuerpoDocumento = new CuerpoDocumento();

// Calcular el IVA retenido
            double ivaRetenido = item.getMontoSujeto().doubleValue() * 0.01;

// Redondear a dos decimales usando BigDecimal
            BigDecimal ivaRetenidoRedondeado = item.getIvaRetenido().setScale(2, RoundingMode.HALF_UP);
            

            cuerpoDocumento.setNumItem(contador);
            cuerpoDocumento.setCodigoRetencionMH(CuerpoDocumento.CodigoRetencionMH.fromValue(item.getTipoRetencion())); //TODO: pendiente
            cuerpoDocumento.setTipoDte(cuerpoDocumento.getCodigoRetencionMH() == CuerpoDocumento.CodigoRetencionMH.C_4 ?null:CuerpoDocumento.TipoDte.fromValue(item.getTipoDte()));
            cuerpoDocumento.setTipoDoc(cuerpoDocumento.getCodigoRetencionMH() ==  CuerpoDocumento.CodigoRetencionMH.C_4?null:CuerpoDocumento.TipoDoc.fromValue(item.getTipoDoc()));
            cuerpoDocumento.setNumDocumento(cuerpoDocumento.getCodigoRetencionMH() ==  CuerpoDocumento.CodigoRetencionMH.C_4?null:item.getNumDocumento());
            cuerpoDocumento.setFechaEmision(cuerpoDocumento.getCodigoRetencionMH() ==  CuerpoDocumento.CodigoRetencionMH.C_4?null:item.getFechaEmision());
            cuerpoDocumento.setMontoSujetoGrav(item.getMontoSujeto().setScale(2, RoundingMode.HALF_UP).doubleValue()); //TODO: pendiente
            //cuerpoDocumento.setIvaRetenido(item.getIvaItem().doubleValue()); //TODO: validar si será en este campo
//            cuerpoDocumento.setIvaRetenido(ivaRetenidoRedondeado); // Calcula el IVA retenido como el 1% del monto sujeto a retención
            cuerpoDocumento.setIvaRetenido(ivaRetenidoRedondeado.setScale(2, RoundingMode.HALF_UP).doubleValue());
            cuerpoDocumento.setDescripcion(item.getDescripcion());
            cuerpoDocumentoList.add(cuerpoDocumento);
            contador++;
        }
        comprobanteRetencionElectronica.setCuerpoDocumento(cuerpoDocumentoList);

    }

    private void generarResumen(Transaccion transaccion){
        var resumen = new Resumen();
        resumen.setTotalSujetoRetencion(transaccion.getTotalGravado().setScale(2, RoundingMode.HALF_UP).doubleValue()); //TODO: validar estos atributos
        resumen.setTotalIVAretenidoLetras(cantidadALetras(transaccion.getIvaRetenido().setScale(2, RoundingMode.HALF_UP)));
        resumen.setTotalIVAretenido(transaccion.getIvaRetenido().setScale(2, RoundingMode.HALF_UP).doubleValue());
        comprobanteRetencionElectronica.setResumen(resumen);
    }


    @Override
    public Object crearDTEJSON(Transaccion transaccion) throws JsonProcessingException {
        DteTransaccion dteTransaccion;
        var infoCola = colaService.obtenerInfoCola(transaccion);
        if(transaccion.getCodigoGeneracion() == null){
            var tipoDte = catalogoService.obtenerIdCatalogoRegistroPorIDMHyIDCatalogo(transaccion.getTipoDTE(),CatalogoService.TIPO_DE_DOCUMENTO);
            Integer correlativoDte = rangoService.findRangoActivoPorDte(tipoDte.intValue(),transaccion.getSucursal().getIdSucursal());
            String codigoGeneracion = obtenerCodigoGeneracion();
            dteTransaccion = generarIdentificacion(transaccion, correlativoDte.toString(), codigoGeneracion, infoCola);
            rangoService.updateRango(tipoDte.intValue(), correlativoDte, transaccion.getSucursal().getIdSucursal());            
            transaccion.setCodigoGeneracion(codigoGeneracion);
            transaccion.setNumeroDTE("DTE-%s-%s-%015d".formatted(transaccion.getTipoDTE(), transaccion.getSucursal().getCodigoSucursal(), correlativoDte));
            transaccionService.actualizarTransaccion(transaccion);
        }else{
            dteTransaccion = generarIdentificacion(transaccion, transaccion.getNumeroDTE(), transaccion.getCodigoGeneracion(), infoCola);
        }
        generarEmisor(transaccion);
        generarReceptor(transaccion.getCliente());
        generarResumen(transaccion);
        generarCuerpoDocumento(transaccion.getCuerpoDocCR());
        crearDteInfo(dteTransaccion);
        return comprobanteRetencionElectronica;
    }

    @Override
    public Object crearJsonInvalidacion(Transaccion transaccion, MotivoAnulacionDTO data) {
        return null;
    }


}
