package sv.gov.cnr.cnrpos.controllers;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sv.gov.cnr.cnrpos.entities.Sucursal;
import sv.gov.cnr.cnrpos.entities.TesoreriaReporte;
import sv.gov.cnr.cnrpos.models.SucursalProjection;
import sv.gov.cnr.cnrpos.models.dto.ReportSeccionDTO;
import sv.gov.cnr.cnrpos.models.dto.ReporteDTO;
import sv.gov.cnr.cnrpos.models.dto.ReporteTesoreriaDTO;
import sv.gov.cnr.cnrpos.models.security.User;
import sv.gov.cnr.cnrpos.security.AuthenticationService;
import sv.gov.cnr.cnrpos.services.ReportService;
import sv.gov.cnr.cnrpos.services.SucursalService;
import sv.gov.cnr.cnrpos.services.TesoreriaReporteService;
import sv.gov.cnr.cnrpos.utils.Utils;


import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@RestController
@RequestMapping("/api/v1/report")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@Slf4j
public class ReportController {

    private ReportService reportService;
    TesoreriaReporteService tesoreriaReporteService;
    private Utils utils;
    private SucursalService sucursalService;
    private final AuthenticationService authenticationService;

    @GetMapping(value = "/listar", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TesoreriaReporte> listarReportes(){
        List<TesoreriaReporte> listaReportes = tesoreriaReporteService.findAllConta();
        return  listaReportes;
    }

    @GetMapping(value = "/listarTesoreria", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TesoreriaReporte> listarReportesTesoreria(){
        List<TesoreriaReporte> listaReportes = tesoreriaReporteService.findAllTesoreria();
        return  listaReportes;
    }

    @GetMapping(value = "/reportesPorSeccion", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReportSeccionDTO> getReportesPorSeccion() {
        return tesoreriaReporteService.obtenerReportesPorSeccion();
    }

    @GetMapping(value = "/listarSucursal", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SucursalProjection> getSucursalCounts() {
        return sucursalService.getSucursalCounts();
    }

    @GetMapping(value = "/listarSucursalPV", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Sucursal> getSucursalPV() {
        return sucursalService.getAllActiveSucursales();
    }

    @GetMapping(value = "/listarModulos", produces = MediaType.APPLICATION_JSON_VALUE)
    List<String> modulosSucursales(){
        return sucursalService.modulosSucursales();
    }

    @PostMapping("/generar")
    public ResponseEntity<byte[]> getReport(@RequestBody ReporteDTO reporteDTO) {
        try {

            TesoreriaReporte tesoreriaReporte = tesoreriaReporteService.findById(reporteDTO.getReporteName());
            // Ruta al archivo .jasper en resources
            String reportName = reporteDTO.getReporteName();
            Map<String, Object> params = new HashMap<>();
            params.put("FECHA_DESDE", reporteDTO.getFechaDesde());
            params.put("FECHA_HASTA", reporteDTO.getFechaHasta());
            if (reporteDTO.getFormato().equalsIgnoreCase("csv") && tesoreriaReporte.getCsv().equalsIgnoreCase("S")){
                params.put("CSV" , true);
            }else{
                params.put("CSV" , false);
            }
            // Genera el reporte en el formato solicitado
            byte[] report = reportService.generarReporte(params, reportName, reporteDTO.getFormato().toLowerCase());

            // Configura las cabeceras de la respuesta
            HttpHeaders headers = new HttpHeaders();
            switch (reporteDTO.getFormato().toLowerCase()) {
                case "pdf":
                    headers.setContentType(MediaType.APPLICATION_PDF);
                    headers.setContentDispositionFormData("inline", reporteDTO.getReporteName().toLowerCase() + "-" + obtenerMesAnio(reporteDTO.getFechaDesde())  + ".pdf");
                    break;
                case "xls":
                    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                    headers.setContentDispositionFormData("attachment", reporteDTO.getReporteName().toLowerCase() + "-" + obtenerMesAnio(reporteDTO.getFechaDesde())  + ".xls");
                    break;
                case "csv":
                    headers.setContentType(MediaType.TEXT_PLAIN);
                    headers.setContentDispositionFormData("attachment", reporteDTO.getReporteName().toLowerCase() + "-" + obtenerMesAnio(reporteDTO.getFechaDesde())  + ".csv");
                    break;
                default:
                    throw new IllegalArgumentException("Formato no disponible: " + reporteDTO.getFormato().toLowerCase());
            }

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(report);

        } catch (Exception e) {
            log.error("error metodo getReport  e: " + e);
            return ResponseEntity.status(500).build();
        }
    }

    private String obtenerMesAnio(String fecha){
        LocalDate fechaNueva = LocalDate.parse(fecha);
        String mes = fechaNueva.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        int anio = fechaNueva.getYear();

        return mes + "-" + anio;
    }

    @PostMapping("/generar64")
    public ResponseEntity<String> generarReporteBase64(@RequestBody ReporteDTO reporteDTO){
        // Ruta al archivo .jasper en resources
        String reportName = reporteDTO.getReporteName();
        Map<String, Object> params = new HashMap<>();
        params.put("FECHA_DESDE", reporteDTO.getFechaDesde());
        params.put("FECHA_HASTA", reporteDTO.getFechaHasta());
        params.put("CSV" , false);
        // Genera el reporte en el formato solicitado
        try {
            byte[] report = reportService.generarReporte(params, reportName, reporteDTO.getFormato().toLowerCase());
            String base64Report = Base64.getEncoder().encodeToString(report);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename="+reportName+".pdf");
            headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
            headers.add(HttpHeaders.PRAGMA, "no-cache");
            headers.add(HttpHeaders.EXPIRES, "0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_JSON) // Ajuste el tipo de contenido a JSON
                    .body(base64Report);
        }catch (Exception e) {
            log.error("error generarReporteBase64 getReport  e: " + e);
            return ResponseEntity.status(500).build();
        }
    }


    @PostMapping("/generarTesoreria")
    public ResponseEntity<byte[]> generarTesoreria(@RequestBody ReporteTesoreriaDTO reporteTesoreriaDTO) {
        try {

            TesoreriaReporte tesoreriaReporte = tesoreriaReporteService.findById(reporteTesoreriaDTO.getReporteName());

            String reportName = reporteTesoreriaDTO.getReporteName();
            Map<String, Object> params = generarParametros(reporteTesoreriaDTO);

            // Genera el reporte en el formato solicitado
            byte[] report = reportService.generarReporte(params, reportName, reporteTesoreriaDTO.getFormato().toLowerCase());

            // Configura las cabeceras de la respuesta
            HttpHeaders headers = new HttpHeaders();
            switch (reporteTesoreriaDTO.getFormato().toLowerCase()) {
                case "pdf":
                    headers.setContentType(MediaType.APPLICATION_PDF);
                    headers.setContentDispositionFormData("inline", reporteTesoreriaDTO.getReporteName().toLowerCase()+ ".pdf");
                    break;
                case "xls":
                    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                    headers.setContentDispositionFormData("attachment", reporteTesoreriaDTO.getReporteName().toLowerCase() + ".xls");
                    break;
                case "csv":
                    headers.setContentType(MediaType.TEXT_PLAIN);
                    headers.setContentDispositionFormData("attachment", reporteTesoreriaDTO.getReporteName().toLowerCase() + ".csv");
                    break;
                default:
                    throw new IllegalArgumentException("Formato no disponible: " + reporteTesoreriaDTO.getFormato().toLowerCase());
            }

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(report);

        } catch (Exception e) {
            log.error("error metodo generarReporte  e: " + e);
            return ResponseEntity.status(500).build();
        }
    }


    //MÉTODO PARA GENERAR LOS PARAMETROS QUE SE MANDARAN AL REPORTE SE DETERMINA POR NOMBRE DE REPORTT
    private Map<String, Object> generarParametros(ReporteTesoreriaDTO reporteTesoreriaDTO) throws IOException {
        Map<String, Object> params = new HashMap<>();
        Resource logo = new ClassPathResource("reports/Logo-CNR.png");
        InputStream imagePath = logo.getInputStream();
        params.put("LOGO", imagePath);
        if (reporteTesoreriaDTO.getReporteName().equalsIgnoreCase("rpt_cantidad_dte")  ||
                reporteTesoreriaDTO.getReporteName().equalsIgnoreCase("rpt_rango_fechas") || reporteTesoreriaDTO.getReporteName().equalsIgnoreCase("rpt_dte_sucursales")){
            params.put("FECHA_DESDE", reporteTesoreriaDTO.getFechaDesde());
            params.put("FECHA_HASTA", reporteTesoreriaDTO.getFechaHasta());
        }else if (reporteTesoreriaDTO.getReporteName().equalsIgnoreCase("rpt_consolidado_derpatamentales")  ||
                reporteTesoreriaDTO.getReporteName().equalsIgnoreCase("rpt_dia_exento_gravado")){
            params.put("MES", reporteTesoreriaDTO.getMes());
            params.put("ANIO", reporteTesoreriaDTO.getAnio());
            params.put("MODULO", reporteTesoreriaDTO.getModulo());
        }else if (reporteTesoreriaDTO.getReporteName().equalsIgnoreCase("rpt_invalidaciones_dte")){
            params.put("MES", reporteTesoreriaDTO.getMes());
            params.put("ANIO", reporteTesoreriaDTO.getAnio());
            params.put("ID_SUCURSAL", reporteTesoreriaDTO.getSucursal());
            params.put("USUARIO", reporteTesoreriaDTO.getUsuario());
        }else if (reporteTesoreriaDTO.getReporteName().equalsIgnoreCase("rpt_departamental_diario")){
            User usuario = (authenticationService.loggedUser());
            params.put("USUARIO",usuario.getUsername());
            params.put("FECHA", reporteTesoreriaDTO.getFecha());
            params.put("ID_SUCURSAL", reporteTesoreriaDTO.getSucursal());
        }else if (reporteTesoreriaDTO.getReporteName().equalsIgnoreCase("rpt_facturacion_delegacion")){
            params.put("FECHA_DESDE", reporteTesoreriaDTO.getFechaDesde());
            params.put("FECHA_HASTA", reporteTesoreriaDTO.getFechaHasta());
            params.put("ID_SUCURSAL", reporteTesoreriaDTO.getSucursal());
        }

        return params;
    }

    @PostMapping("/generarPreview")
    public ResponseEntity<String> generarReporteBase64(@RequestBody ReporteTesoreriaDTO reporteTesoreriaDTO){

        // Genera el reporte en el formato solicitado
        try {
            byte[] report = generarTesoreria(reporteTesoreriaDTO).getBody();
            String base64Report = Base64.getEncoder().encodeToString(report);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename="+ reporteTesoreriaDTO.getReporteName()+".pdf");
            headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
            headers.add(HttpHeaders.PRAGMA, "no-cache");
            headers.add(HttpHeaders.EXPIRES, "0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_JSON) // Ajuste el tipo de contenido a JSON
                    .body(base64Report);
        }catch (Exception e) {
            log.error("error metodo generarReporteBase64  e: " + e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("resporteUsuarios/{estado}/{formato}")
    public ResponseEntity<byte[]> reporteUsuarios(@PathVariable String estado, @PathVariable String formato) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("ESTADO", estado);
        Resource logo = new ClassPathResource("reports/Logo-CNR.png");
        InputStream imagePath = logo.getInputStream();
        params.put("LOGO", imagePath);

        try {
            byte[] report = reportService.generarReporte(params, "rpt_usuarios", formato);

            // Configura las cabeceras de la respuesta
            HttpHeaders headers = new HttpHeaders();
            switch (formato.toLowerCase()) {
                case "pdf":
                    headers.setContentType(MediaType.APPLICATION_PDF);
                    headers.setContentDispositionFormData("inline", "rpt_usuarios.pdf");
                    break;
                case "xls":
                    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                    headers.setContentDispositionFormData("attachment", "rpt_usuarios.xls");
                    break;
                default:
                    throw new IllegalArgumentException("Formato no disponible: " + formato);
            }

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(report);

        } catch (Exception e) {
            log.error("error metodo reporteUsuarios  e: " + e);
            return ResponseEntity.status(500).build();
        }

    }

}
