package sv.gov.cnr.factelectrcnrservice.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sv.gov.cnr.factelectrcnrservice.config.AppConfig;
import sv.gov.cnr.factelectrcnrservice.entities.*;
import sv.gov.cnr.factelectrcnrservice.models.dte.Direccion;
import sv.gov.cnr.factelectrcnrservice.models.dte.nc.*;
import sv.gov.cnr.factelectrcnrservice.models.dto.MotivoAnulacionDTO;
import sv.gov.cnr.factelectrcnrservice.services.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Data
@Component
@RequiredArgsConstructor
public class DTENotaCredito extends DTE {

    private final AppConfig appConfig;
    private final CompanyService companyService;
    private final CatalogoService catalogoService;
    private final RangoService rangoService;
    private final TransaccionService transaccionService;
    private final ColaService colaService;
    private final NotaCredito notaCredito = new NotaCredito();

    @Value("${configuracion.contingencia.tipo-contingencia}")
    private Integer tipoContingencia;
    @Value("${configuracion.contingencia.motivo-contingencia}")
    private String motivoContingencia;


    private DteTransaccion generarIdentificacion(Transaccion transaccion, String correlativo, String codigoGeneracion, Cola cola ){
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
        if(cola.getEsContingencia() && cola.getNotificadoContigencia()){
            identificacion.setTipoModelo(Identificacion.TipoModelo.DIFERIDA);
            identificacion.setTipoOperacion(Identificacion.TipoOperacion._2);
            identificacion.setTipoContingencia(Identificacion.TipoContingencia._1);
            identificacion.setMotivoContin(motivoContingencia);
        }else {
            identificacion.setTipoModelo(Identificacion.TipoModelo.PREVIA);
            identificacion.setTipoOperacion(Identificacion.TipoOperacion._1);
            identificacion.setTipoContingencia(null);
            identificacion.setMotivoContin(null);
        }
        identificacion.setTipoMoneda(Identificacion.TipoMoneda.USD);
        dteTransaccion.setTransaccion(transaccion);
        dteTransaccion.setCodigoGeneracion(identificacion.getCodigoGeneracion());
        dteTransaccion.setFechaGeneracion(identificacion.getFecEmi());
        dteTransaccion.setVersion(identificacion.getVersion());
        dteTransaccion.setAmbiente(appConfig.getAmbiente());
        dteTransaccion.setNumeroDte(identificacion.getNumeroControl());
        dteTransaccion.setFechaEmision(identificacion.getFecEmi());
        dteTransaccion.setHoraEmision(identificacion.getHorEmi());
        dteTransaccion.setTipoDTE(transaccion.getTipoDTE());
        notaCredito.setIdentificacion(identificacion);
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
        notaCredito.setEmisor(emisor);
    }

    private void generarReceptor(Cliente cliente){
        var receptor = new Receptor();
        receptor.setNombre(cliente.getNombre());
        receptor.setNit(cliente.getNit());
        receptor.setNrc(cliente.getNrc());
        receptor.setCodActividad(cliente.getActividadEconomica());
        var descActividad = catalogoService.obtenerValorMHPorIDMHYIDCatalogo(cliente.getActividadEconomica(),CatalogoService.CODIGO_DE_ACTIVIDAD_ECONOMICA).orElse(null);
        receptor.setDescActividad(descActividad);
        receptor.setNombreComercial(cliente.getNombreComercial());
        var direccion = new Direccion();
        direccion.setDepartamento(Objects.toString(cliente.getDepartamento(),""));
        direccion.setMunicipio(Objects.toString(cliente.getMunicipio(),""));
        direccion.setComplemento(Objects.toString(cliente.getDireccion(),"No Disponible"));
        receptor.setDireccion(direccion);
        receptor.setTelefono(cliente.getTelefono());
        receptor.setCorreo(cliente.getEmail());
        notaCredito.setReceptor(receptor);
    }

    private void generarDocumentoRelacionado(List<DocumentoAsociado> documentosAsociados){
        var documentosAsociadosList = new ArrayList<DocumentoRelacionado>();
        for (var doc : documentosAsociados){
            var documentoRelacionado = new DocumentoRelacionado();
            documentoRelacionado.setTipoDocumento(DocumentoRelacionado.TipoDocumento.fromValue(doc.getTipoDTE()));
            documentoRelacionado.setNumeroDocumento(doc.getCodigoGeneracion());
            documentoRelacionado.setFechaEmision(doc.getFechaEmision());
            documentoRelacionado.setTipoGeneracion(DocumentoRelacionado.TipoGeneracion.fromValue(doc.getTipoGeneracion()));
            documentosAsociadosList.add(documentoRelacionado);
        }
        notaCredito.setDocumentoRelacionado(documentosAsociadosList);
    }

    private void generarResumen(Transaccion transaccion){
        var resumen = new Resumen();
        resumen.setTotalNoSuj(transaccion.getTotalNosujeto().setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setTotalExenta(transaccion.getTotalExento().setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setTotalGravada(transaccion.getTotalGravado().setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setSubTotalVentas(transaccion.getTotalSinDescuento().setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setDescuNoSuj(transaccion.getDescuentoNosujeto().setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setDescuExenta(transaccion.getDescuentoExento().setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setDescuGravada(transaccion.getDescuentoGravado().setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setTotalDescu(transaccion.getMontoDescuento().setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setIvaPerci1(transaccion.getIvaPercivido().setScale(2, RoundingMode.HALF_UP).doubleValue());//Pendiente
        resumen.setIvaRete1(transaccion.getIvaRetenido().setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setReteRenta(transaccion.getRentaRetenido().setScale(2, RoundingMode.HALF_UP).doubleValue());
        if(transaccion.getTotalIva().compareTo(BigDecimal.valueOf(0.00))>0){
            var tributo = new Tributo();
            tributo.setCodigo("20");
            tributo.setDescripcion("Impuesto al Valor Agregado 13%");
            tributo.setValor(transaccion.getTotalIva().setScale(2, RoundingMode.HALF_UP).doubleValue());
            Set<Tributo> tributosList = Set.of(tributo);
            resumen.setTributos(tributosList);
        }
        // resumen.setMontoTotalOperacion(transaccion.getTotalTransaccion().add(transaccion.getTotalIva()).setScale(8, RoundingMode.HALF_UP).doubleValue());
        BigDecimal totalOperacion = transaccion.getTotalTransaccion().add(transaccion.getTotalIva());
        resumen.setMontoTotalOperacion(
            (totalOperacion.setScale(2, RoundingMode.HALF_UP).doubleValue()));
        resumen.setSubTotal(transaccion.getTotalTransaccion().setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setTotalLetras(cantidadALetras((transaccion.getTotalTransaccion().add(transaccion.getTotalIva())).setScale(2, RoundingMode.HALF_UP)));
        resumen.setCondicionOperacion(Resumen.CondicionOperacion.fromValue(transaccion.getCondicionOperacion()));
       


        
        notaCredito.setResumen(resumen);
    }

    private void generarCuerpoDocumento(List<Item> items){
        var cuerpoDocumentoList = new ArrayList<CuerpoDocumento>();
        var contador = 1;
        for (var item: items){
            var cuerpoDocumento = new CuerpoDocumento();
            cuerpoDocumento.setNumItem(contador);
            cuerpoDocumento.setTipoItem(CuerpoDocumento.TipoItem.fromValue(item.getTipoItem()));
            if(item.getVentaGravada().compareTo(BigDecimal.valueOf(0.00))> 0){
                String tributoIVA = "20";
                Set<String> tributosList = Set.of(tributoIVA);
                cuerpoDocumento.setTributos(tributosList);
            }
            cuerpoDocumento.setNumeroDocumento(item.getNroDocumento());
            cuerpoDocumento.setCantidad(item.getCantidad().doubleValue());
            cuerpoDocumento.setDescripcion(item.getNombre());
            cuerpoDocumento.setCodigo(item.getCodigoProducto());
            cuerpoDocumento.setUniMedida(item.getUnidadMedida());
            cuerpoDocumento.setPrecioUni(item.getPrecioUnitario().setScale(2, RoundingMode.HALF_UP).doubleValue());
            cuerpoDocumento.setMontoDescu(item.getMontoDescuento().setScale(2, RoundingMode.HALF_UP).doubleValue());
            cuerpoDocumento.setVentaNoSuj(item.getVentaNosujeta().setScale(2, RoundingMode.HALF_UP).doubleValue());
            cuerpoDocumento.setVentaExenta(item.getVentaExenta().setScale(2, RoundingMode.HALF_UP).doubleValue());
            cuerpoDocumento.setVentaGravada(item.getVentaGravada().setScale(2, RoundingMode.HALF_UP).doubleValue());

            if (item.getVentaGravada().doubleValue() > 0.00) {
                BigDecimal cantidad = new BigDecimal(item.getCantidad()); // Convert Integer to BigDecimal
                BigDecimal precioUnitario = item.getPrecioUnitario(); // Convert Integer to BigDecimal
                BigDecimal ventaGravada = cantidad.multiply(precioUnitario);
                cuerpoDocumento.setVentaGravada(ventaGravada.setScale(2, RoundingMode.HALF_UP).doubleValue());
            }
            else
            {
                cuerpoDocumento.setVentaGravada(0.00);

            }
            cuerpoDocumentoList.add(cuerpoDocumento);
            contador++;
        }
        notaCredito.setCuerpoDocumento(cuerpoDocumentoList);
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
        generarDocumentoRelacionado(transaccion.getDocumentoAsociados());
        generarCuerpoDocumento(transaccion.getItems());
        crearDteInfo(dteTransaccion);
        return notaCredito;
    }

    @Override
    public Object crearJsonInvalidacion(Transaccion transaccion, MotivoAnulacionDTO data) {
        return null;
    }
}
