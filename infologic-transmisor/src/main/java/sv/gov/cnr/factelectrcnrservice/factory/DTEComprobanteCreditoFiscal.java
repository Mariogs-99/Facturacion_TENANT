package sv.gov.cnr.factelectrcnrservice.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sv.gov.cnr.factelectrcnrservice.config.AppConfig;
import sv.gov.cnr.factelectrcnrservice.entities.*;
import sv.gov.cnr.factelectrcnrservice.models.dte.Direccion;
import sv.gov.cnr.factelectrcnrservice.models.dte.ccf.*;
import sv.gov.cnr.factelectrcnrservice.models.dto.MotivoAnulacionDTO;
import sv.gov.cnr.factelectrcnrservice.services.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
@RequiredArgsConstructor
@Component
public class DTEComprobanteCreditoFiscal extends DTE {

    private final AppConfig appConfig;
    private final CompanyService companyService;
    private final CatalogoService catalogoService;
    private final RangoService rangoService;
    private final TransaccionService transaccionService;
    private final ColaService colaService;
    private final ComprobanteCreditoFiscal comprobanteCreditoFiscal = new ComprobanteCreditoFiscal();

    @Value("${configuracion.contingencia.tipo-contingencia}")
    private Integer tipoContingencia;
    @Value("${configuracion.contingencia.motivo-contingencia}")
    private String motivoContingencia;

    private DteTransaccion generarIdentificacion(Transaccion transaccion, String correlativo, String codigoGeneracion,
            Cola cola) {
        DteTransaccion dteTransaccion = new DteTransaccion();
        var identificacion = new Identificacion();
        identificacion.setAmbiente(Identificacion.Ambiente.fromValue(appConfig.getAmbiente()));
        if (transaccion.getCodigoGeneracion() == null) {
            identificacion.setNumeroControl(String.format("DTE-%s-%s-%015d", identificacion.getTipoDte(),
                    transaccion.getSucursal().getCodigoSucursal(), Integer.valueOf(correlativo)));
        } else {
            identificacion.setNumeroControl(correlativo);
        }
        identificacion.setCodigoGeneracion(codigoGeneracion);
        identificacion.setFecEmi(obtenerFechaEmision());//
        identificacion.setHorEmi(obtenerHoraEmision());
        if (cola.getEsContingencia() && cola.getNotificadoContigencia()) {
            identificacion.setTipoModelo(Identificacion.TipoModelo.DIFERIDO);
            identificacion.setTipoOperacion(Identificacion.TipoOperacion._2);
            identificacion.setTipoContingencia(Identificacion.TipoContingencia._1);
            identificacion.setMotivoContin(motivoContingencia);
        } else {
            identificacion.setTipoModelo(Identificacion.TipoModelo.PREVIO);
            identificacion.setTipoOperacion(Identificacion.TipoOperacion._1);
            identificacion.setTipoContingencia(null);
            identificacion.setMotivoContin(null);
        }
        dteTransaccion.setTransaccion(transaccion);
        dteTransaccion.setCodigoGeneracion(identificacion.getCodigoGeneracion());
        dteTransaccion.setFechaGeneracion(identificacion.getFecEmi());
        dteTransaccion.setVersion(identificacion.getVersion());
        dteTransaccion.setAmbiente(appConfig.getAmbiente());
        dteTransaccion.setNumeroDte(identificacion.getNumeroControl());
        dteTransaccion.setFechaEmision(identificacion.getFecEmi());
        dteTransaccion.setHoraEmision(identificacion.getHorEmi());
        dteTransaccion.setTipoDTE(transaccion.getTipoDTE());
        comprobanteCreditoFiscal.setIdentificacion(identificacion);
        return dteTransaccion;
    }

    private void generarEmisor(Transaccion transaccion) {
        var emisorInfo = companyService.getEmisor();
        var emisor = new Emisor();
        emisor.setNit(emisorInfo.getNit());
        emisor.setNrc(emisorInfo.getNrc());
        emisor.setNombre(emisorInfo.getNameCompany());
        emisor.setCodActividad(catalogoService.findById(emisorInfo.getIdActividadMH()));
        var descActividad = catalogoService
                .obtenerValorMHPorIDMHYIDCatalogo(catalogoService.findById(emisorInfo.getIdActividadMH()),
                        CatalogoService.CODIGO_DE_ACTIVIDAD_ECONOMICA)
                .orElse("");
        emisor.setDescActividad(descActividad);
        emisor.setNombreComercial(emisorInfo.getSocialReasonCompany());
        emisor.setTipoEstablecimiento(Emisor.TipoEstablecimiento._01);// 01 = Sucursal/Agencia CAT-009 Tipo de
                                                                      // establecimiento
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
        comprobanteCreditoFiscal.setEmisor(emisor);
    }

    private void generarReceptor(Cliente cliente) {
        var receptor = new Receptor();
        receptor.setNit(cliente.getNit());
        receptor.setNrc(cliente.getNrc());
        receptor.setNombre(cliente.getNombre());
        receptor.setCodActividad(cliente.getActividadEconomica());
        var descActividad = catalogoService.obtenerValorMHPorIDMHYIDCatalogo(cliente.getActividadEconomica(),
                CatalogoService.CODIGO_DE_ACTIVIDAD_ECONOMICA).orElse(null);
        receptor.setDescActividad(descActividad);
        receptor.setNombreComercial(cliente.getNombreComercial());
        var direccion = new Direccion();
        direccion.setDepartamento(cliente.getDepartamento());
        direccion.setMunicipio(cliente.getMunicipio());
        direccion.setComplemento(Objects.toString(cliente.getDireccion(), "No Disponible"));
        receptor.setDireccion(direccion);
        receptor.setTelefono(cliente.getTelefono());
        receptor.setCorreo(cliente.getEmail());
        comprobanteCreditoFiscal.setReceptor(receptor);
    }

    private void generarResumen(Transaccion transaccion) {

        var resumen = new Resumen();

        var totalNeto = transaccion.getTotalSinDescuento().subtract(transaccion.getMontoDescuento());

        resumen.setTotalNoSuj(transaccion.getTotalNosujeto().setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setTotalExenta(transaccion.getTotalExento().setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setTotalGravada(transaccion.getTotalGravado().setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setDescuNoSuj(transaccion.getDescuentoNosujeto().setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setDescuExenta(transaccion.getDescuentoExento().setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setDescuGravada(transaccion.getDescuentoGravado().setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setPorcentajeDescuento(transaccion.getPorcentajeDescuento());
        resumen.setTotalDescu(transaccion.getMontoDescuento().setScale(2, RoundingMode.HALF_UP).doubleValue());

        resumen.setTotalExenta(transaccion.getTotalExento().setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setTotalGravada(transaccion.getTotalGravado().setScale(2, RoundingMode.HALF_UP).doubleValue());
        // Calcular el subtotal de ventas como la suma de las ventas grabadas, no
        // sujetas y exentas
        BigDecimal subtotalVentas = transaccion.getTotalGravado()
                .add(transaccion.getTotalNosujeto())
                .add(transaccion.getTotalExento())
                .setScale(8, RoundingMode.HALF_UP);
        // resumen.setSubTotal((transaccion.getTotalSinDescuento().subtract(transaccion.getMontoDescuento())).setScale(2,
        // RoundingMode.HALF_UP).doubleValue());
        // resumen.setSubTotal(subtotalVentas);

        BigDecimal subtotal = subtotalVentas.subtract(transaccion.getDescuentoGravado()).setScale(8, RoundingMode.HALF_EVEN);
        // resumen.setSubTotal(subtotal.setScale(8, RoundingMode.HALF_UP).doubleValue());


        resumen.setTotalGravada(transaccion.getTotalGravado().doubleValue());
        

        resumen.setSubTotal(transaccion.getTotalTransaccion().setScale(2, RoundingMode.HALF_UP).doubleValue());

        resumen.setSubTotalVentas(transaccion.getTotalSinDescuento().setScale(2, RoundingMode.HALF_UP).doubleValue());

        // Establecer los totales
        // resumen.setTotalNoSuj(totalNoSuj.setScale(2,
        // RoundingMode.HALF_UP).doubleValue());

        // Establecer el subtotal de ventas
        resumen.setSubTotalVentas(subtotalVentas.setScale(2, RoundingMode.HALF_UP).doubleValue());

        resumen.setIvaPerci1(transaccion.getIvaPercivido().setScale(2, RoundingMode.HALF_UP).doubleValue()); // Pendiente
        resumen.setReteRenta(transaccion.getRentaRetenido().setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setIvaRete1(transaccion.getIvaRetenido().setScale(2, RoundingMode.HALF_UP).doubleValue());

        BigDecimal totalOperacion = totalNeto.add(transaccion.getTotalIva());
        resumen.setMontoTotalOperacion(
                (totalOperacion.setScale(2, RoundingMode.HALF_UP).doubleValue()));
        resumen.setTotalNoGravado(transaccion.getTotalNogravado().setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setTotalPagar((BigDecimal.valueOf(resumen.getSubTotal()).add(transaccion.getTotalIva())
                .subtract(transaccion.getIvaRetenido()))
                .setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setTotalLetras(cantidadALetras(BigDecimal.valueOf(resumen.getTotalPagar()).setScale(2, RoundingMode.HALF_UP)));
        resumen.setSaldoFavor(transaccion.getSaldoAfavor().doubleValue());
        if (transaccion.getTotalIva().compareTo(BigDecimal.valueOf(0.00)) > 0) {
            var tributo = new Tributo();
            tributo.setCodigo("20");
            tributo.setDescripcion("Impuesto al Valor Agregado 13%");
            tributo.setValor(transaccion.getTotalIva().setScale(2, RoundingMode.HALF_UP).doubleValue());
            Set<Tributo> tributosList = Set.of(tributo);
            resumen.setTributos(tributosList);
        }
        resumen.setCondicionOperacion(Resumen.CondicionOperacion.fromValue(transaccion.getCondicionOperacion()));
        comprobanteCreditoFiscal.setResumen(resumen);
    }

    private void generarCuerpoDocumento(List<Item> itemsList) {
        var cuerpoDocList = new ArrayList<CuerpoDocumento>();
        var contador = 1;
        for (var item : itemsList) {
            var cuerpoDoc = new CuerpoDocumento();
            cuerpoDoc.setNumItem(contador);
            cuerpoDoc.setTipoItem(CuerpoDocumento.TipoItem.fromValue(item.getTipoItem()));
            // cuerpoDoc.setNumeroDocumento(); puede ser nulo
            if (item.getVentaGravada().compareTo(BigDecimal.valueOf(0.00)) > 0) {
                String tributoIVA = "20";
                Set<String> tributosList = Set.of(tributoIVA);
                cuerpoDoc.setTributos(tributosList);
            }
          
            cuerpoDoc.setCodigo(item.getCodigoProducto());
            cuerpoDoc.setDescripcion(item.getNombre());
            cuerpoDoc.setCantidad(Double.valueOf(item.getCantidad()));
            cuerpoDoc.setUniMedida(item.getUnidadMedida());
            cuerpoDoc.setPrecioUni(item.getPrecioUnitario().setScale(5, RoundingMode.HALF_UP).doubleValue());
            cuerpoDoc.setMontoDescu(item.getMontoDescuento().setScale(2, RoundingMode.HALF_UP).doubleValue());
            cuerpoDoc.setVentaNoSuj(item.getVentaNosujeta().setScale(2, RoundingMode.HALF_UP).doubleValue());
            cuerpoDoc.setVentaExenta(item.getVentaExenta().setScale(2, RoundingMode.HALF_UP).doubleValue());
            cuerpoDoc.setVentaGravada(item.getVentaGravada().setScale(2, RoundingMode.HALF_UP).doubleValue());
            cuerpoDoc.setNoGravado(item.getVentaNogravada().setScale(2, RoundingMode.HALF_UP).doubleValue());
            cuerpoDoc.setPsv(item.getPrecioUnitario().setScale(5, RoundingMode.HALF_UP).doubleValue());
            if (item.getVentaGravada().doubleValue() > 0.00) {
                BigDecimal cantidad = new BigDecimal(item.getCantidad()); // Convert Integer to BigDecimal
                BigDecimal precioUnitario = item.getPrecioUnitario(); // Convert Integer to BigDecimal
                BigDecimal ventaGravada = cantidad.multiply(precioUnitario);
                cuerpoDoc.setVentaGravada(ventaGravada.setScale(2, RoundingMode.HALF_UP).doubleValue());
            }
            else
            {
                cuerpoDoc.setVentaGravada(0.00);

            }
            contador++;
            cuerpoDocList.add(cuerpoDoc);
        }
        comprobanteCreditoFiscal.setCuerpoDocumento(cuerpoDocList);
    }

    @Override
    public Object crearDTEJSON(Transaccion transaccion) throws JsonProcessingException {
        DteTransaccion dteTransaccion;
        var infoCola = colaService.obtenerInfoCola(transaccion);
        if (transaccion.getCodigoGeneracion() == null) {
            var tipoDte = catalogoService.obtenerIdCatalogoRegistroPorIDMHyIDCatalogo(transaccion.getTipoDTE(),
                    CatalogoService.TIPO_DE_DOCUMENTO);
            Integer correlativoDte = rangoService.findRangoActivoPorDte(tipoDte.intValue(),
                    transaccion.getSucursal().getIdSucursal());
            String codigoGeneracion = obtenerCodigoGeneracion();
            dteTransaccion = generarIdentificacion(transaccion, correlativoDte.toString(), codigoGeneracion, infoCola);
            rangoService.updateRango(tipoDte.intValue(), correlativoDte, transaccion.getSucursal().getIdSucursal());
            transaccion.setCodigoGeneracion(codigoGeneracion);
            transaccion.setNumeroDTE("DTE-%s-%s-%015d".formatted(transaccion.getTipoDTE(),
                    transaccion.getSucursal().getCodigoSucursal(), correlativoDte));
            transaccionService.actualizarTransaccion(transaccion);
        } else {
            dteTransaccion = generarIdentificacion(transaccion, transaccion.getNumeroDTE(),
                    transaccion.getCodigoGeneracion(), infoCola);
        }
        generarEmisor(transaccion);
        generarReceptor(transaccion.getCliente());
        generarCuerpoDocumento(transaccion.getItems());
        generarResumen(transaccion);
        crearDteInfo(dteTransaccion);
        return comprobanteCreditoFiscal;
    }

    @Override
    public Object crearJsonInvalidacion(Transaccion transaccion, MotivoAnulacionDTO data) {
        return null;
    }
}
