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
import sv.gov.cnr.factelectrcnrservice.models.dte.fse.*;
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
public class DTEFacturaSujetoExcluido extends DTE {

    private final AppConfig appConfig;
    private final CompanyService companyService;
    private final CatalogoService catalogoService;
    private final RangoService rangoService;
    private final TransaccionService transaccionService;
    private final ColaService colaService;
    private final FacturaSujetoExcluido facturaSujetoExcluido = new FacturaSujetoExcluido();

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
            identificacion.setMotivoContin(motivoContingencia);
        }else {
            identificacion.setTipoModelo(Identificacion.TipoModelo._1);
            identificacion.setTipoOperacion(Identificacion.TipoOperacion._1);//CAT-004 Tipo de Transmisión 1=Transmisión Normal (en el Schema del MH viene como tipoOperacion)
            identificacion.setTipoContingencia(null);
            identificacion.setMotivoContin(null);
        }
        dteTransaccion.setFechaEmision(identificacion.getFecEmi());
        dteTransaccion.setHoraEmision(identificacion.getHorEmi());
        dteTransaccion.setTransaccion(transaccion);
        dteTransaccion.setTipoDTE(transaccion.getTipoDTE());
        dteTransaccion.setCodigoGeneracion(identificacion.getCodigoGeneracion());
        dteTransaccion.setFechaGeneracion(identificacion.getFecEmi());
        dteTransaccion.setVersion(identificacion.getVersion());
        dteTransaccion.setAmbiente(appConfig.getAmbiente());
        dteTransaccion.setNumeroDte(identificacion.getNumeroControl());
        facturaSujetoExcluido.setIdentificacion(identificacion);
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
        facturaSujetoExcluido.setEmisor(emisor);
    }

    private void generarSujetoExcluido(Cliente cliente){
        var sujetoExcluido = new SujetoExcluido();
        sujetoExcluido.setNombre(cliente.getNombre());
        var direccion = new Direccion();
        direccion.setDepartamento(Objects.toString(cliente.getDepartamento(),""));
        direccion.setMunicipio(Objects.toString(cliente.getMunicipio(),""));
        direccion.setComplemento(Objects.toString(cliente.getDireccion(),"No Disponible"));
        sujetoExcluido.setDireccion(direccion);
        sujetoExcluido.setCorreo(cliente.getEmail());
        sujetoExcluido.setTelefono(cliente.getTelefono());
        sujetoExcluido.setNumDocumento(cliente.getNumeroDocumento());
        sujetoExcluido.setTipoDocumento(SujetoExcluido.TipoDocumento.fromValue(cliente.getTipoDocumento()));
        sujetoExcluido.setCodActividad(cliente.getActividadEconomica());
        var descActividad = catalogoService.obtenerValorMHPorIDMHYIDCatalogo(cliente.getActividadEconomica(),CatalogoService.CODIGO_DE_ACTIVIDAD_ECONOMICA).orElse(null);
        sujetoExcluido.setDescActividad(descActividad);
        facturaSujetoExcluido.setSujetoExcluido(sujetoExcluido);
    }

    private void generarResumen(Transaccion transaccion) {
        var resumen = new Resumen();

        // Ajustar el total sin descuento dividiéndolo entre 1.13
        BigDecimal totalPagar = transaccion.getTotalTransaccion().subtract(transaccion.getRentaRetenido());


        // Establece los valores del resumen
        // resumen.setTotalCompra(transaccion.getTotalTransaccion().subtract(transaccion.getIvaRetenido()).doubleValue()); // Usar el valor ajustado
        resumen.setTotalCompra(transaccion.getTotalTransaccion().setScale(2, RoundingMode.HALF_UP).doubleValue()); // Usar el valor ajustado
        resumen.setDescu(transaccion.getPorcentajeDescuento());
        resumen.setTotalDescu(transaccion.getMontoDescuento().setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setSubTotal(resumen.getTotalCompra());
        resumen.setIvaRete1(0.00);
        resumen.setReteRenta(transaccion.getRentaRetenido().setScale(2, RoundingMode.HALF_UP).doubleValue());

        // Calcula el total a pagar restando también la renta retenida por ahi creo que redondea
        //BigDecimal totalPagar = totalSinDescuentoMenosIva.subtract(BigDecimal.valueOf(resumen.getReteRenta()));
        resumen.setTotalPagar(totalPagar.setScale(2, RoundingMode.HALF_UP).doubleValue());

        // Convertir el total a pagar en letras
        resumen.setTotalLetras(cantidadALetras(totalPagar.setScale(2, RoundingMode.HALF_UP)));

        // Establece la condición de operación
        resumen.setCondicionOperacion(Resumen.CondicionOperacion.fromValue(transaccion.getCondicionOperacion()));

        // Asigna el resumen a la factura sujeto excluido
        facturaSujetoExcluido.setResumen(resumen);
    }


    private void generarCuerpoDocumento(List<Item> items){
        var cuerpoDocumentoList = new ArrayList<CuerpoDocumento>();
        var contador = 1;
        for (var item : items) {
            var cuerpoDocumento = new CuerpoDocumento();
            cuerpoDocumento.setNumItem(contador);
            cuerpoDocumento.setTipoItem(CuerpoDocumento.TipoItem.fromValue(item.getTipoItem()));
            cuerpoDocumento.setCantidad(Double.valueOf(item.getCantidad()));
            cuerpoDocumento.setCodigo(item.getCodigoProducto());
            cuerpoDocumento.setDescripcion(item.getNombre());

            // Convertir el precio unitario a BigDecimal y dividir por 1.13
            BigDecimal precioUnitario = BigDecimal.valueOf(item.getPrecioUnitario().setScale(2, RoundingMode.HALF_UP).doubleValue());
            BigDecimal precioUnitarioAjustado = precioUnitario.divide(new BigDecimal("1.13"), 2, RoundingMode.HALF_UP);
            cuerpoDocumento.setPrecioUni(item.getPrecioUnitario().doubleValue());

            cuerpoDocumento.setUniMedida(CuerpoDocumento.UniMedida.fromValue(item.getUnidadMedida()));
            cuerpoDocumento.setMontoDescu(item.getMontoDescuento().doubleValue());

            // Convertir el total a BigDecimal y dividir por 1.13
            BigDecimal total = BigDecimal.valueOf(item.getTotal().doubleValue()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal totalAjustado = total.divide(new BigDecimal("1.13"), 2, RoundingMode.HALF_UP);
            cuerpoDocumento.setCompra(item.getTotal().setScale(2, RoundingMode.HALF_UP).doubleValue());

            contador++;
            cuerpoDocumentoList.add(cuerpoDocumento);
        }
        facturaSujetoExcluido.setCuerpoDocumento(cuerpoDocumentoList);

    }



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
        generarSujetoExcluido(transaccion.getCliente());
        generarResumen(transaccion);
        generarCuerpoDocumento(transaccion.getItems());
        crearDteInfo(dteTransaccion);
        return facturaSujetoExcluido;
    }

    @Override
    public Object crearJsonInvalidacion(Transaccion transaccion, MotivoAnulacionDTO data) {
        return null;
    }
}
