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
import sv.gov.cnr.factelectrcnrservice.models.dte.cl.*;
import sv.gov.cnr.factelectrcnrservice.models.dto.MotivoAnulacionDTO;
import sv.gov.cnr.factelectrcnrservice.services.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Slf4j
@Data
@Component
@RequiredArgsConstructor
public class DTEComprobanteLiquidacion extends DTE{

    private final AppConfig appConfig;
    private final CompanyService companyService;
    private final CatalogoService catalogoService;
    private final RangoService rangoService;
    private final TransaccionService transaccionService;
    private final ColaService colaService;
    private final ComprobanteLiquidacionElectronica comprobanteLiquidacionElectronica = new ComprobanteLiquidacionElectronica();




    private DteTransaccion generarIdentificacion(Transaccion transaccion, String correlativo, String codigoGeneracion, Cola cola){
        DteTransaccion dteTransaccion = new DteTransaccion();
        var identificacion = new Identificacion();
        identificacion.setAmbiente(Identificacion.Ambiente.fromValue(appConfig.getAmbiente()));
        if(transaccion.getCodigoGeneracion() == null){
            identificacion.setNumeroControl(String.format("DTE-%s-%s-%015d", identificacion.getTipoDte(), transaccion.getSucursal().getCodigoSucursal(), Integer.valueOf(correlativo)));
        }else{
            identificacion.setNumeroControl(correlativo);
        }
        identificacion.setCodigoGeneracion(codigoGeneracion);
        identificacion.setFecEmi(obtenerFechaEmision());//
        identificacion.setHorEmi(obtenerHoraEmision());
        dteTransaccion.setTransaccion(transaccion);
        dteTransaccion.setCodigoGeneracion(identificacion.getCodigoGeneracion());
        dteTransaccion.setFechaGeneracion(identificacion.getFecEmi());
        dteTransaccion.setVersion(identificacion.getVersion());
        dteTransaccion.setAmbiente(appConfig.getAmbiente());
        dteTransaccion.setNumeroDte(identificacion.getNumeroControl());
        dteTransaccion.setFechaEmision(identificacion.getFecEmi());
        dteTransaccion.setHoraEmision(identificacion.getHorEmi());
        dteTransaccion.setTipoDTE(transaccion.getTipoDTE());
        comprobanteLiquidacionElectronica.setIdentificacion(identificacion);
        return dteTransaccion;
    }

    private void generarEmisor(Transaccion transaccion) {
        var emisor = new Emisor();
        var emisorInfo = companyService.getEmisor();
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
        //emisor.setCodEstableMH(); admite nulo
        //emisor.setCodEstable(); admite nulo
        //emisor.setCodPuntoVentaMH(); admite nulo
        //emisor.setCodPuntoVenta(); admite nulo
        comprobanteLiquidacionElectronica.setEmisor(emisor);
    }

    private void generarReceptor(Cliente cliente){
        var receptor = new Receptor();
        receptor.setNombre(cliente.getNombre());
        receptor.setNit(cliente.getNit());
        receptor.setNrc(cliente.getNrc());
        receptor.setCorreo(cliente.getEmail());
        receptor.setTelefono(cliente.getTelefono());
        receptor.setNombreComercial(cliente.getNombreComercial());
        receptor.setCodActividad(cliente.getActividadEconomica());
        var descActividad = catalogoService.obtenerValorMHPorIDMHYIDCatalogo(cliente.getActividadEconomica(),CatalogoService.CODIGO_DE_ACTIVIDAD_ECONOMICA).orElse(null);
        receptor.setDescActividad(descActividad);
        var direccion = new Direccion();
        direccion.setDepartamento(cliente.getDepartamento());
        direccion.setMunicipio(cliente.getMunicipio());
        direccion.setComplemento(cliente.getDireccion());
        receptor.setDireccion(direccion);
        comprobanteLiquidacionElectronica.setReceptor(receptor);
    }

    private void generarResumen(Transaccion transaccion){
        var resumen = new Resumen();
        resumen.setTotalNoSuj(transaccion.getTotalNosujeto().doubleValue());
        resumen.setTotalGravada(transaccion.getTotalGravado().doubleValue());
        //resumen.setTotalExportacion(); TODO: definir que campo será
        //resumen.setSubTotalVentas(); TODO: definir que campo será
        //resumen.setTributos(); pendiente, admite nulo
        resumen.setMontoTotalOperacion(transaccion.getTotalTransaccion().doubleValue()); //TODO: validar que si es este atributo
        resumen.setIvaPerci(transaccion.getIvaPercivido().doubleValue());
        resumen.setTotal(transaccion.getTotalTransaccion().doubleValue());
        resumen.setTotalLetras(cantidadALetras(transaccion.getTotalTransaccion()));
        resumen.setCondicionOperacion(Resumen.CondicionOperacion.fromValue(transaccion.getCondicionOperacion()));
        comprobanteLiquidacionElectronica.setResumen(resumen);
    }

    private void generarCuerpoDocumento(List<Item> items){
        var cuerpoDocumentoList = new ArrayList<CuerpoDocumento>();
        var contador = 1;
        for (var item : items){
            var cuerpoDocumento = new CuerpoDocumento();
            cuerpoDocumento.setNumItem(contador);
            //cuerpoDocumento.setTipoDte(); TODO: pendiente definir esto
            //cuerpoDocumento.setTipoGeneracion(); TODO: pendiente definir esto
            //cuerpoDocumento.setNumeroDocumento(); TODO: pendiente definir esto
            //cuerpoDocumento.setFechaGeneracion();
            cuerpoDocumento.setVentaNoSuj(item.getVentaNosujeta().doubleValue());
            cuerpoDocumento.setVentaExenta(item.getVentaExenta().doubleValue());
            cuerpoDocumento.setVentaGravada(item.getVentaGravada().doubleValue());
            //cuerpoDocumento.setExportaciones(); TODO: pendiente definir esto
            //cuerpoDocumento.setTributos(); admite nulo
            cuerpoDocumento.setIvaItem(item.getIvaItem().doubleValue());
            //cuerpoDocumento.setObsItem(); TODO: pendiente definir esto
            cuerpoDocumentoList.add(cuerpoDocumento);
            contador++;
        }
        comprobanteLiquidacionElectronica.setCuerpoDocumento(cuerpoDocumentoList);

    }





    @Override
    public Object crearDTEJSON(Transaccion transaccion) throws JsonProcessingException {
        DteTransaccion dteTransaccion;
        var infoCola = colaService.obtenerInfoCola(transaccion);
        if(transaccion.getCodigoGeneracion() == null){
            var tipoDte = catalogoService.obtenerIdCatalogoRegistroPorIDMHyIDCatalogo(transaccion.getTipoDTE(),CatalogoService.TIPO_DE_DOCUMENTO);
            Integer correlativoDte = rangoService.findRangoActivoPorDte(tipoDte.intValue(), transaccion.getSucursal().getIdSucursal());
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
        generarCuerpoDocumento(transaccion.getItems());
        crearDteInfo(dteTransaccion);
        return comprobanteLiquidacionElectronica;
    }




    @Override
    public Object crearJsonInvalidacion(Transaccion transaccion, MotivoAnulacionDTO data) {
        return null;
    }
}
