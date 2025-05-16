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
import sv.gov.cnr.factelectrcnrservice.models.dte.fex.*;
import sv.gov.cnr.factelectrcnrservice.models.dto.MotivoAnulacionDTO;
import sv.gov.cnr.factelectrcnrservice.services.*;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Data
@RequiredArgsConstructor
@Component
public class DTEFacturaExportacion extends DTE{

    private final AppConfig appConfig;
    private final CompanyService companyService;
    private final CatalogoService catalogoService;
    private final RangoService rangoService;
    private final TransaccionService transaccionService;
    private final ColaService colaService;
    private final FacturaExportacion facturaExportacion = new FacturaExportacion();

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
        if(cola.getEsContingencia() && cola.getNotificadoContigencia()){
            identificacion.setTipoModelo(Identificacion.TipoModelo._2);
            identificacion.setTipoOperacion(Identificacion.TipoOperacion._2);//CAT-004 Tipo de Transmisión 1=Transmisión Normal (en el Schema del MH viene como tipoOperacion)
            identificacion.setTipoContingencia(Identificacion.TipoContingencia._1);
            identificacion.setMotivoContigencia(motivoContingencia);
        }else {
            identificacion.setTipoModelo(Identificacion.TipoModelo._1);
            identificacion.setTipoOperacion(Identificacion.TipoOperacion._1);//CAT-004 Tipo de Transmisión 1=Transmisión Normal (en el Schema del MH viene como tipoOperacion)
            identificacion.setTipoContingencia(null);
            identificacion.setMotivoContigencia(null);
        }
        dteTransaccion.setTransaccion(transaccion);
        dteTransaccion.setCodigoGeneracion(identificacion.getCodigoGeneracion());
        dteTransaccion.setFechaGeneracion(identificacion.getFecEmi());
        dteTransaccion.setVersion(identificacion.getVersion());
        dteTransaccion.setAmbiente(appConfig.getAmbiente());
        dteTransaccion.setNumeroDte(identificacion.getNumeroControl());
        dteTransaccion.setFechaEmision(identificacion.getFecEmi());
        dteTransaccion.setHoraEmision(identificacion.getHorEmi());
        facturaExportacion.setIdentificacion(identificacion);
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
        emisor.setCodEstableMH(transaccion.getSucursal().getCodigoSucursal().substring(0,4));
        emisor.setCodEstable(transaccion.getSucursal().getCodigoSucursal().substring(0,4));
        emisor.setCodPuntoVentaMH(transaccion.getSucursal().getCodigoSucursal().substring(4));
        emisor.setCodPuntoVenta(transaccion.getSucursal().getCodigoSucursal().substring(4));
        emisor.setTipoItemExpor(Emisor.TipoItemExpor.fromValue(1)); //Pendiente agregar a tabla Company
        String recintoFiscal = catalogoService
                .obtenerIdMHPorIDMHYIDCatalogo(catalogoService.findById(emisorInfo.getIdRecinto()), CatalogoService.RECINTO_FISCAL)
                .orElse("");
        emisor.setRecintoFiscal(recintoFiscal);
        String regimenFiscal = catalogoService
                .obtenerIdMHPorIDMHYIDCatalogo(catalogoService.findById(emisorInfo.getIdRegimen()), "CAT-028")
                .orElse("");
        emisor.setRegimen(regimenFiscal);
        facturaExportacion.setEmisor(emisor);
    }

    private void generarReceptor(Cliente cliente){
        var receptor = new Receptor();
        receptor.setNombre(cliente.getNombre());
        receptor.setCodPais(Receptor.CodPais.fromValue(cliente.getPais())); //Preguntar si esta homologado con catalogo
        var nombrePais = catalogoService.obtenerValorMHPorIDMHYIDCatalogo(cliente.getPais(), CatalogoService.PAIS).orElse("");
        receptor.setNombrePais(nombrePais);
        receptor.setComplemento(cliente.getDireccion());
        receptor.setTipoDocumento(Receptor.TipoDocumento.fromValue(cliente.getTipoDocumento()));
        receptor.setNumDocumento(cliente.getNumeroDocumento());
        receptor.setNombreComercial(cliente.getNombreComercial());
        receptor.setTipoPersona(Receptor.TipoPersona.fromValue(1)); //TODO: Preguntar si esto depende de esConsumidorFinal
        var descActividad = catalogoService.obtenerValorMHPorIDMHYIDCatalogo(cliente.getActividadEconomica(), CatalogoService.CODIGO_DE_ACTIVIDAD_ECONOMICA).orElse("");
        receptor.setDescActividad(descActividad);
        receptor.setTelefono(cliente.getTelefono());
        receptor.setCorreo(cliente.getEmail());
        facturaExportacion.setReceptor(receptor);
    }

    private void generarCuerpoDocumento(List<Item> items){
        var cuerpoDocumentoList = new ArrayList<CuerpoDocumento>();
        var contador = 1;
        for (var item: items){
            var cuerpoDocumento = new CuerpoDocumento();
            cuerpoDocumento.setNumItem(contador);
            cuerpoDocumento.setCodigo(item.getCodigoProducto());
            cuerpoDocumento.setDescripcion(item.getNombre());
            cuerpoDocumento.setCodigo(item.getCodigoProducto());
            cuerpoDocumento.setCantidad(item.getCantidad().doubleValue());
            cuerpoDocumento.setUniMedida(item.getUnidadMedida());
            cuerpoDocumento.setPrecioUni(item.getPrecioUnitario().doubleValue());
            cuerpoDocumento.setMontoDescu(item.getMontoDescuento().doubleValue());
            cuerpoDocumento.setVentaGravada(item.getVentaGravada().doubleValue());
            cuerpoDocumento.setNoGravado(item.getVentaExenta().doubleValue());
            cuerpoDocumentoList.add(cuerpoDocumento);
        }
        facturaExportacion.setCuerpoDocumento(cuerpoDocumentoList);
    }
    private void generarResumen(Transaccion transaccion){
        var resumen = new Resumen();
        resumen.setTotalGravada(transaccion.getTotalGravado().doubleValue());
        resumen.setDescuento(transaccion.getMontoDescuento().doubleValue());
        resumen.setPorcentajeDescuento(transaccion.getPorcentajeDescuento());
        resumen.setTotalDescu(transaccion.getMontoDescuento().doubleValue()); //Preguntar diferencia entre descuento y totalDescu
        resumen.setMontoTotalOperacion(transaccion.getTotalTransaccion().doubleValue());
        resumen.setTotalNoGravado(transaccion.getTotalExento().doubleValue());
        resumen.setTotalPagar(transaccion.getTotalTransaccion().doubleValue());
        resumen.setTotalLetras(cantidadALetras(transaccion.getTotalTransaccion()));
        resumen.setCondicionOperacion(Resumen.CondicionOperacion.fromValue(transaccion.getCondicionOperacion()));
        //resumen.setPagos(); pendiente pero puede ser nulo
        //resumen.setCodIncoterms(); Pendiente
        //resumen.setDescIncoterms(); Pendiente
        //resumen.setFlete(); Pendiente
        //resumen.setSeguro(); Pendiente
        //resumen.setObservaciones(); pendiente pero puede ser nulo
        facturaExportacion.setResumen(resumen);
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
        generarCuerpoDocumento(transaccion.getItems());
        generarResumen(transaccion);
        crearDteInfo(dteTransaccion);
        return facturaExportacion;
    }

    @Override
    public Object crearJsonInvalidacion(Transaccion transaccion, MotivoAnulacionDTO data) {
        return null;
    }

}
