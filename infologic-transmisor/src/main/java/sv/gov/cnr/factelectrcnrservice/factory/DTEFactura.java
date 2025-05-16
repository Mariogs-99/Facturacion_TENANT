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
import sv.gov.cnr.factelectrcnrservice.models.dte.fc.*;
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
public class DTEFactura extends DTE {

    private final AppConfig appConfig;
    private final CompanyService companyService;
    private final CatalogoService catalogoService;
    private final RangoService rangoService;
    private final TransaccionService transaccionService;
    private final ColaService colaService;
    private FacturaElectronica facturaElectronica = new FacturaElectronica();

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
        identificacion.setFecEmi(obtenerFechaEmision());
        identificacion.setHorEmi(obtenerHoraEmision());
        identificacion.setCodigoGeneracion(codigoGeneracion);
        if (cola.getEsContingencia() && cola.getNotificadoContigencia()) {
            identificacion.setTipoModelo(Identificacion.TipoModelo.DIFERIDO);
            identificacion.setTipoOperacion(Identificacion.TipoOperacion._2);// CAT-004 Tipo de Transmisión
                                                                             // 1=Transmisión Normal (en el Schema del
                                                                             // MH viene como tipoOperacion)
            identificacion.setTipoContingencia(Identificacion.TipoContingencia._1);
            identificacion.setMotivoContin(motivoContingencia);
        } else {
            identificacion.setTipoModelo(Identificacion.TipoModelo.PREVIO);
            identificacion.setTipoOperacion(Identificacion.TipoOperacion._1);// CAT-004 Tipo de Transmisión
                                                                             // 1=Transmisión Normal (en el Schema del
                                                                             // MH viene como tipoOperacion)
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
        facturaElectronica.setIdentificacion(identificacion);
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
        facturaElectronica.setEmisor(emisor);
    }

    private void generarReceptor(Cliente cliente) {
        var receptor = new Receptor();
        receptor.setTipoDocumento(Receptor.TipoDocumento.fromValue(cliente.getTipoDocumento()));
        receptor.setNumDocumento(cliente.getNumeroDocumento());
        receptor.setNrc(cliente.getNrc());
        receptor.setNombre(cliente.getNombre());
        receptor.setCodActividad(cliente.getActividadEconomica());// Codigo en el catalogo
        var descActividad = catalogoService.obtenerValorMHPorIDMHYIDCatalogo(cliente.getActividadEconomica(),
                CatalogoService.CODIGO_DE_ACTIVIDAD_ECONOMICA).orElse(null);
        receptor.setDescActividad(descActividad);// Valor en el catalogo
        var direccion = new Direccion();
        direccion.setDepartamento(Objects.toString(cliente.getDepartamento(), ""));
        direccion.setMunicipio(Objects.toString(cliente.getMunicipio(), ""));
        direccion.setComplemento(Objects.toString(cliente.getDireccion(), "No Disponible"));
        receptor.setDireccion(direccion);
        receptor.setTelefono(cliente.getTelefono());
        receptor.setCorreo(cliente.getEmail());
        facturaElectronica.setReceptor(receptor);
    }

    private void generarCuerpoDeDocumento(List<Item> itemsList) {
        var cuerpoDocList = new ArrayList<CuerpoDocumento>();
        var contador = 1;

        for (var item : itemsList) {
            var cuerpoDoc = new CuerpoDocumento();
            cuerpoDoc.setNumItem(contador);
            cuerpoDoc.setTipoItem(CuerpoDocumento.TipoItem.fromValue(item.getTipoItem()));
            cuerpoDoc.setCantidad(Double.valueOf(item.getCantidad()));
            cuerpoDoc.setUniMedida(item.getUnidadMedida());
            cuerpoDoc.setCodigo(item.getCodigoProducto());
            cuerpoDoc.setDescripcion(item.getNombre());
            cuerpoDoc.setPrecioUni(item.getPrecioUnitario().doubleValue());

            // Ajustar el montoDescu a cero si la ventaNoSujeta es mayor que cero
            if (item.getVentaNosujeta().doubleValue() > 0) {
                cuerpoDoc.setMontoDescu(0.0); // Monto de descuento a cero
            } else {
                cuerpoDoc.setMontoDescu(item.getMontoDescuento().doubleValue()); // Mantener el monto de descuento
                                                                                 // original
            }

            cuerpoDoc.setPsv(item.getPrecioUnitario().doubleValue());
            cuerpoDoc.setVentaExenta(item.getVentaExenta().doubleValue());
            // cuerpoDoc.setVentaGravada(item.getVentaGravada().doubleValue());

            if (item.getVentaGravada().doubleValue() > 0.00) {
                BigDecimal cantidad = new BigDecimal(item.getCantidad()); // Convert Integer to BigDecimal
                BigDecimal precioUnitario = item.getPrecioUnitario(); // Convert Integer to BigDecimal
                BigDecimal ventaGravada = cantidad.multiply(precioUnitario);
                cuerpoDoc.setVentaGravada(ventaGravada.setScale(2, RoundingMode.HALF_UP).doubleValue());
            } else
            {
                cuerpoDoc.setVentaGravada(0.00);

            }

            if (item.getVentaExenta().doubleValue() > 0.00) {
                BigDecimal cantidad = new BigDecimal(item.getCantidad()); // Convert Integer to BigDecimal
                BigDecimal precioUnitario = item.getPrecioUnitario(); // Convert Integer to BigDecimal
                BigDecimal ventaExenta = cantidad.multiply(precioUnitario);
                cuerpoDoc.setVentaExenta(ventaExenta.setScale(2, RoundingMode.HALF_UP).doubleValue());
            } else
            {
                cuerpoDoc.setVentaExenta(0.00);

            }
            
            

            
            cuerpoDoc.setVentaNoSuj(item.getVentaNosujeta().setScale(2, RoundingMode.HALF_UP).doubleValue());
            cuerpoDoc.setNoGravado(item.getVentaNogravada().setScale(2, RoundingMode.HALF_UP).doubleValue());
            // cuerpoDoc.setIvaItem(0d);
            cuerpoDoc.setIvaItem(item.getIvaItem().setScale(2, RoundingMode.HALF_UP).doubleValue());


            contador++;
            cuerpoDocList.add(cuerpoDoc);
        }

        facturaElectronica.setCuerpoDocumento(cuerpoDocList);
    }

    private void generarResumen(Transaccion transaccion) {
        var resumen = new Resumen();

        // Acumular descuentos de los items
        BigDecimal totalDescuentos = BigDecimal.ZERO;
        BigDecimal totalNoSuj = BigDecimal.ZERO;
        BigDecimal descuNoSuj = BigDecimal.ZERO;

        for (Item item : transaccion.getItems()) {
            totalDescuentos = totalDescuentos.add(item.getMontoDescuento());

       
            if (item.getVentaGravada().doubleValue() > 0.00) {
                BigDecimal cantidad = BigDecimal.valueOf(item.getCantidad());
                BigDecimal precioUnitario = item.getPrecioUnitario();
                BigDecimal ventaGravada = cantidad.multiply(precioUnitario);
                item.setVentaGravada(ventaGravada);
            }
            


            // Si el item está clasificado como "no sujeto", lo sumamos a los totales no
            // sujetos
            if (item.getVentaNosujeta().doubleValue() != 0.00) {
                totalNoSuj = totalNoSuj.add(item.getVentaNosujeta());
                descuNoSuj = totalNoSuj;
                totalDescuentos = totalNoSuj;
            }
        }

        // Establecer los totales
        resumen.setTotalNoSuj(totalNoSuj.setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setTotalExenta(transaccion.getTotalExento().setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setTotalGravada(transaccion.getTotalGravado().setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setTotalNoGravado(transaccion.getTotalNogravado().setScale(2, RoundingMode.HALF_UP).doubleValue());

        // Calcular el subtotal de ventas como la suma de las ventas grabadas, no
        // sujetas y exentas
        BigDecimal subtotalVentas = transaccion.getTotalGravado().add(totalNoSuj).add(transaccion.getTotalExento())
                .setScale(8, RoundingMode.HALF_UP);

        // Establecer el subtotal de ventas
        resumen.setSubTotalVentas(subtotalVentas.setScale(2, RoundingMode.HALF_UP).doubleValue());

        resumen.setDescuExenta(transaccion.getDescuentoExento().setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setDescuNoSuj(descuNoSuj.setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setDescuGravada(transaccion.getDescuentoGravado().setScale(2, RoundingMode.HALF_UP).doubleValue());

        // Calcular porcentaje de descuento
        BigDecimal porcentajeDescuento = BigDecimal.ZERO;

            porcentajeDescuento = BigDecimal.valueOf(transaccion.getPorcentajeDescuento());
  
        resumen.setPorcentajeDescuento(porcentajeDescuento.doubleValue());

        // Total descuento acumulado
        resumen.setTotalDescu(transaccion.getMontoDescuento().setScale(2, RoundingMode.HALF_UP).doubleValue());


        if (resumen.getTotalNoSuj().doubleValue() != 0.00) {
            resumen.setTotalDescu(totalDescuentos.setScale(2, RoundingMode.HALF_UP).doubleValue());

        }

        // Resto del resumen
        // BigDecimal subtotal = subtotalVentas.subtract(transaccion.getDescuentoGravado()).setScale(8, RoundingMode.HALF_EVEN);

        BigDecimal subtotal = transaccion.getTotalTransaccion().setScale(2, RoundingMode.HALF_EVEN);  // Convertir el Double a BigDecimal
    
resumen.setSubTotal(subtotal.doubleValue());

        resumen.setIvaRete1(transaccion.getIvaRetenido().setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setReteRenta(transaccion.getRentaRetenido().setScale(2, RoundingMode.HALF_UP).doubleValue());

        // BigDecimal montoTotalOperacion = transaccion.getTotalSinDescuento().subtract(totalDescuentos).setScale(8,
        //         RoundingMode.HALF_UP);

        // Condición para establecer el total a pagar
        if (totalNoSuj.compareTo(BigDecimal.ZERO) > 0) {
            resumen.setTotalPagar(0.00); // Si hay ventas no sujetas, el total a pagar es 0
            resumen.setSubTotal(0.00); // Si hay ventas no sujetas, el total a pagar es 0
        } else {
            resumen.setTotalPagar(
                    transaccion.getTotalTransaccion().subtract(transaccion.getIvaRetenido()).setScale(2, RoundingMode.HALF_UP).doubleValue());
        }
        
        BigDecimal montoTotalOperacion = BigDecimal.valueOf(resumen.getSubTotal()).setScale(2, RoundingMode.HALF_EVEN);
     


        resumen.setMontoTotalOperacion(montoTotalOperacion.doubleValue());



        // Recalcular la cantidad en letras
        resumen.setTotalLetras(cantidadALetras(BigDecimal.valueOf(resumen.getTotalPagar()).setScale(2, RoundingMode.HALF_UP)));
        resumen.setTotalIva(transaccion.getTotalIva().setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setSaldoFavor(transaccion.getSaldoAfavor().setScale(2, RoundingMode.HALF_UP).doubleValue());
        resumen.setCondicionOperacion(Resumen.CondicionOperacion.fromValue(transaccion.getCondicionOperacion()));

        facturaElectronica.setResumen(resumen);
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
        generarCuerpoDeDocumento(transaccion.getItems());
        generarResumen(transaccion);
        crearDteInfo(dteTransaccion);
        return facturaElectronica;
    }

    @Override
    public Object crearJsonInvalidacion(Transaccion transaccion, MotivoAnulacionDTO data) {
        return null;
    }

}