package sv.gov.cnr.cnrpos.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import sv.gov.cnr.cnrpos.models.dto.KpiDTO;
import sv.gov.cnr.cnrpos.models.dto.PuntoVentaDTO;
import sv.gov.cnr.cnrpos.models.dto.SucursalDTO;
import sv.gov.cnr.cnrpos.services.KPIService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
//@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true") // EMTORNO DEV
//@CrossOrigin(origins = "https://tudominio.com", allowedHeaders = "*", allowCredentials = "true") //ENTORNO PROD
@RequestMapping("/api/v1/kpis")
@Service

public class KPIController {

    //Evita dependecias no inicializadas
    private final KPIService kpiService;

    public KPIController(KPIService kpiService) {
        this.kpiService = kpiService;
    }

    @GetMapping(value = "/getKPI", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<KpiDTO> getKPI(){
        List<KpiDTO> listKPI = new ArrayList<>();
        listKPI.add(getInvalidatedKPI());
        listKPI.add(getContingenciaKPI());
        listKPI.add(getRechazadosKPI());
        listKPI.addAll(getTipoDteByTipoPago());

        return listKPI;
    }

    @GetMapping(value = "/listKPIByVenta/{idSucursal}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<KpiDTO> listKPIByVenta(@PathVariable Long idSucursal){
        List<KpiDTO> listKPI = new ArrayList<>();
        listKPI.add(getInvalidatedByVentaKPI(idSucursal));
        listKPI.add(getContingenciaByVentaKPI(idSucursal));
        listKPI.add(getRechazadosByVentaKPI(idSucursal));

        return listKPI;
    }

    private KpiDTO getInvalidatedKPI() {
        KpiDTO kpi = new KpiDTO();
        kpi.setId("kpi1");
        kpi.setName("Documentos invalidados");
        kpi.setValue(kpiService.invalidadosKPI());
        kpi.setTarget(kpiService.cantidadDocumentosEmitidosKPI()); // Ejemplo de target
        kpi.setTrend(0); // Ejemplo de tendencia
        kpi.setDescription("Cantidad de documentos invalidados / Cantidad de documentos emitidos");
        return kpi;
    }

    private KpiDTO getInvalidatedByVentaKPI(@PathVariable Long idSucursal) {
        KpiDTO kpi = new KpiDTO();
        kpi.setId("kpi1");
        kpi.setName("Documentos invalidados por punto de venta");
        kpi.setValue(kpiService.invalidadosByVentaKPI(idSucursal));
        kpi.setTarget(kpiService.cantidadDocumentosEmitidosByVentaKPI(idSucursal));
        kpi.setTrend(0); // Ejemplo de tendencia
        kpi.setDescription("Cantidad de documentos invalidados por punto de venta / Cantidad de DTE´s emitidos en punto de venta");
        return kpi;
    }

    private KpiDTO getContingenciaKPI() {
        KpiDTO kpi = new KpiDTO();
        kpi.setId("kpi2");
        kpi.setName("Documentos en Contigencia");
        kpi.setValue(kpiService.countContigenciaDocumentos());
        kpi.setTarget(kpiService.cantidadDocumentosEmitidosKPI());
        kpi.setTrend(0); // Ejemplo de tendencia
        kpi.setDescription("Cantidad de documentos en Contigencia / Cantidad de documentos emitidos");
        return kpi;
    }

    private KpiDTO getContingenciaByVentaKPI(@PathVariable Long idSucursal) {
        KpiDTO kpi = new KpiDTO();
        kpi.setId("kpi2");
        kpi.setName("Documentos en Contigencia por punto de venta");
        kpi.setValue(kpiService.countContigenciaDocumentosByVenta(idSucursal));
        kpi.setTarget(kpiService.cantidadDocumentosEmitidosByVentaKPI(idSucursal));
        kpi.setTrend(0); // Ejemplo de tendencia
        kpi.setDescription("Cantidad de documentos en Contigencia por punto de venta / Cantidad de DTE´s emitidos en punto de venta");
        return kpi;
    }

    private KpiDTO getRechazadosKPI() {
        KpiDTO kpi = new KpiDTO();
        kpi.setId("kpi3");
        kpi.setName("Documentos Rechazados");
        kpi.setValue(kpiService.countRechazadosDocumentos());
        kpi.setTarget(kpiService.cantidadDocumentosEmitidosKPI());
        kpi.setTrend(0); // Ejemplo de tendencia
        kpi.setDescription("Cantidad de documentos Rechazados / Cantidad de documentos emitidos");
        return kpi;
    }


    private KpiDTO getRechazadosByVentaKPI(@PathVariable Long idSucursal) {
        KpiDTO kpi = new KpiDTO();
        kpi.setId("kpi3");
        kpi.setName("Documentos Rechazados por punto de venta");
        kpi.setValue(kpiService.countRechazadosDocumentosByVenta(idSucursal));
        kpi.setTarget(kpiService.cantidadDocumentosEmitidosByVentaKPI(idSucursal));
        kpi.setTrend(0); // Ejemplo de tendencia
        kpi.setDescription("Cantidad de documentos Rechazados por punto de venta / Cantidad de DTE´s emitidos en punto de venta");
        return kpi;
    }

    private List<KpiDTO> getTipoDteByTipoPago() {
        List<KpiDTO> lista = new ArrayList<>();
        KpiDTO kpi = new KpiDTO();
        kpi.setId("kpi4");
        kpi.setName("Cantidad de FE canceldas en efectivo");
        kpi.setValue(kpiService.countGeneratedDocumentosByTipoPagoAndTipoDoc("01"));
        kpi.setTarget(kpiService.cantidadDocumentosEmitidosKPI()); // Ejemplo de target
        kpi.setTrend(0); // Ejemplo de tendencia
        kpi.setDescription("Cantidad de FE canceldas en efectivo/ Cantidad de DTE´s");

        KpiDTO kpi1 = new KpiDTO();
        kpi1.setId("kpi5");
        kpi1.setName("Cantidad de CCFE canceldos en efectivo");
        kpi1.setValue(kpiService.countGeneratedDocumentosByTipoPagoAndTipoDoc("03"));
        kpi1.setTarget(kpiService.cantidadDocumentosEmitidosKPI()); // Ejemplo de target
        kpi1.setTrend(0); // Ejemplo de tendencia
        kpi1.setDescription("Cantidad de CCFE canceldos en efectivo/ Cantidad de DTE´s emitidos");

        lista.add(kpi);
        lista.add(kpi1);
        return lista;
    }

    @GetMapping(value = "/getSucursalByPuntoVenta", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PuntoVentaDTO> getSucursalByPuntoVenta() {
        return kpiService.findSucursalesWithConcat();
    }

}
