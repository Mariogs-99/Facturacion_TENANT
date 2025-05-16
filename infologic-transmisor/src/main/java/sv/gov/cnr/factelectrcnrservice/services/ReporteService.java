package sv.gov.cnr.factelectrcnrservice.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleCsvExporterConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import org.hibernate.boot.ResourceLocator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import pl.allegro.finance.tradukisto.MoneyConverters;
import sv.gov.cnr.factelectrcnrservice.entities.ComprobantePago;
import sv.gov.cnr.factelectrcnrservice.entities.Transaccion;
import sv.gov.cnr.factelectrcnrservice.models.dto.DatosReporteDTO;
import sv.gov.cnr.factelectrcnrservice.models.dto.ReporteIvaContribuyentesDTO;
import sv.gov.cnr.factelectrcnrservice.models.enums.TipoDTE;
import sv.gov.cnr.factelectrcnrservice.models.enums.TipoReporte;
import sv.gov.cnr.factelectrcnrservice.repositories.ReporteRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReporteService {

    private final DteTransaccionService dteTransaccionService;

    private final ResourcePatternResolver resourcePatternResolver;
    private final ReporteRepository reporteRepository;

    private final ComprobantePagoService comprobantePagoService;

    private final CatalogoService catalogoService;

    private transient ConcurrentHashMap<String, JasperReport> cacheReport = new ConcurrentHashMap<>();

    String cantidadALetras(BigDecimal cantidad){
        var converter = MoneyConverters.SPANISH_BANKING_MONEY_VALUE;
        return converter.asWords(cantidad, "$" );
    };

    public ByteArrayOutputStream generateReportToStream(Transaccion transaccion) throws Exception {
        var infoDte = dteTransaccionService.findDteTransaccionForReport(transaccion);
        Boolean estaAnulado = dteTransaccionService.dteAnuladoByTransaccion(transaccion);
        TipoDTE tipoDte = TipoDTE.obtenerPorCodigo(transaccion.getTipoDTE());
        //CAMBIAR TIPIO DTE
        if (transaccion.getTipoDTE().equals("07") && transaccion.getCuerpoDocCR().get(0).getTipoDte().equals("14")) {
            tipoDte = TipoDTE.obtenerPorCodigo("007");
        }
        var reportPath = getReportType(tipoDte);
        List<ComprobantePago> listaComprobantes = comprobantePagoService.listComprobanteTransaccion(transaccion.getIdTransaccion());
        log.info(tipoDte.name());
        Resource logo = new ClassPathResource("templates/Logo-info.png");
        InputStream imagePath = logo.getInputStream();
        Resource fondo = new ClassPathResource("templates/Escudo_Gobierno.png");
        InputStream imagePathFondo = fondo.getInputStream();
        JasperReport report = getCacheReport(reportPath);
        Map<String, Object> params = new HashMap<>();
        params.put("JSON_DATA", infoDte.getDteJson());
        params.put("URL_DTE", "https://admin.factura.gob.sv/consultaPublica?ambiente=%s&codGen=%s&fechaEmi=%s".formatted(infoDte.getAmbiente(), infoDte.getCodigoGeneracion(), infoDte.getFechaEmision()));
        params.put("ESTA_ANULADO", estaAnulado);
        params.put("SELLO_RECEPCION", infoDte.getSelloRecibidoMh());
        params.put("LOGO", imagePath);
        params.put("FONDO", imagePathFondo);
        params.put("NOTA", transaccion.getNotas());
        if(transaccion.getTipoDTE().equals("01") || transaccion.getTipoDTE().equalsIgnoreCase("03")) {
            params.put("COMPROBANTES", comprobantesLista(listaComprobantes));
        }
        if (tipoDte.getCodigo().equals("07") || tipoDte.getCodigo().equals("007")){
            params.put("CONDICION_PAGO", transaccion.getCondicionOperacion().toString());

            params.put("TOTAL_LETRAS", cantidadALetras(transaccion.getTotalTransaccion()));
        }
        if (tipoDte.getCodigo().equals("01") || tipoDte.getCodigo().equals("03")){
            params.put("TOTAL_DESCU", BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN));
        }

        //OBTENER DEPARTAMENTO Y MUNICIPIO
        Optional<String> departamento = Optional.empty();
        Optional<String> municipio = Optional.empty();

        departamento = catalogoService.obtenerValorMHPorIdCatalogoYValor(CatalogoService.DEPARTAMENTO,transaccion.getCliente().getDepartamento());
        municipio = catalogoService.findByCatPadreAndIdMhAndIdMhPadreIgnoreCase(CatalogoService.MUNICIPIO,transaccion.getCliente().getMunicipio(),transaccion.getCliente().getDepartamento());

        StringBuilder direccion = new StringBuilder();
        direccion.append("Municipio: ");
        direccion.append(municipio.orElse("San Salvador"));
        direccion.append(", Departamento: ");
        direccion.append(departamento.orElse("San Salvador"));
        direccion.append(" ");
        direccion.append(" ");
        params.put("DIRECCION", direccion.toString().toUpperCase());


                //OBTENER DEPARTAMENTO Y MUNICIPIO EMISOR
        
                StringBuilder direccionEmisor = new StringBuilder();
                direccionEmisor.append("Municipio: ");
                direccionEmisor.append(catalogoService.findByIdValor(transaccion.getSucursal().getIdMuniBranch()));
                direccionEmisor.append(", Departamento: ");
                direccionEmisor.append(catalogoService.findByIdValor(transaccion.getSucursal().getIdDeptoBranch()));
                direccionEmisor.append(" ");
                direccionEmisor.append(" ");
                params.put("DIRECCION_EMISOR", direccionEmisor.toString().toUpperCase());
        //JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
        InputStream jsonDataStream = new ByteArrayInputStream(infoDte.getDteJson().getBytes(StandardCharsets.UTF_8));
        JsonDataSource dataSource = new JsonDataSource(jsonDataStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(report, params, dataSource);
        ByteArrayOutputStream pdfReportStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, pdfReportStream);
        log.info(params.get("URL_DTE").toString());
        return pdfReportStream;
    }


    private String getReportType(TipoDTE tipoDte) {
        return switch (tipoDte) {
            case FACTURA -> "classpath:reports/FacturaComercial.jrxml";
            case COMPROBANTE_CREDITO_FISCAL -> "classpath:reports/ComprobanteCreditoFiscal.jrxml";
            case COMPROBANTE_LIQUIDACION -> "classpath:reports/ComprobanteLiquidacion.jrxml";
            case COMPROBANTE_RETENCION -> "classpath:reports/ComprobanteRetencion.jrxml";
            case COMPROBANTE_RETENCION2 -> "classpath:reports/ComprobanteRetencion2.jrxml";
            case FACTURAS_EXPORTACION -> "classpath:reports/FacturaExportacion.jrxml";
            case FACTURA_SUJETO_EXCLUIDO -> "classpath:reports/FacturaSujetoExcluido.jrxml";
            case NOTA_CREDITO -> "classpath:reports/NotaCredito.jrxml";
            case NOTA_DEBITO -> "classpath:reports/NotaDebito.jrxml";
            default -> throw new IllegalArgumentException("Reporte de tipo de DTE no implementado: " + tipoDte);
        };
    }


    private JasperReport getCacheReport(String location) throws IOException {
        if (!cacheReport.containsKey(location)) {
            JasperReport compile = getReport(location);
            if (Objects.nonNull(compile)) {
                cacheReport.put(location, compile);
            }
        }
        return cacheReport.get(location);
    }


    private JasperReport getReport(String location) throws IOException {
        Resource res = resourcePatternResolver.getResource(location);
        if (!res.exists()) {
            throw new RuntimeException("El reporte no se encontró: " + location);
        }
        try (InputStream stream = res.getInputStream()) {
            return JasperCompileManager.compileReport(stream);
        } catch (JRException e) {
            throw new RuntimeException("Error compilando el reporte: " + location, e);
        }
    }


    public ByteArrayOutputStream generarReporteComplementario(DatosReporteDTO datosReporte) throws JRException, IOException {
        var pathReporte = getReporteComplementario(datosReporte.getTipoReporte());
        List<?> data;
        if(datosReporte.getTipoReporte().equals(TipoReporte.LibroIvaContribuyentes)){
            data  = reporteRepository.obtenerRegistrosIvaContribuyente(datosReporte);
        }else {
            data = reporteRepository.obtenerRegistrosConsumidorFinal(datosReporte);
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

        JasperReport report = getCacheReport(pathReporte);
        Map<String, Object> params = new HashMap<>();
        params.put("CSV", datosReporte.getEsCSV());
        params.put("FECHA_DESDE", datosReporte.getFechaDesde());
        params.put("FECHA_HASTA", datosReporte.getFechaHasta());
        JasperPrint jasperPrint = JasperFillManager.fillReport(report, params, dataSource);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        if (datosReporte.getEsCSV()) {
            JRCsvExporter exporter = new JRCsvExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleWriterExporterOutput(outputStream));
            SimpleCsvExporterConfiguration configuration = new SimpleCsvExporterConfiguration();
            configuration.setWriteBOM(Boolean.TRUE);
            configuration.setRecordDelimiter("\n");
            exporter.setConfiguration(configuration);
            exporter.exportReport();
        } else {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        }

        return outputStream;
    }

    private String getReporteComplementario(TipoReporte tipoReporte) {
        return switch (tipoReporte) {
            case LibroIvaContribuyentes -> "classpath:reports/LibroIvaContribuyentes.jrxml";
            case LibroIvaConsumidorfinal -> "classpath:reports/LibroIvaConsumidorFinal.jrxml";
            default -> throw new IllegalArgumentException("Tipo de reporte no implementado: " + tipoReporte);
        };
    }

    private String comprobantesLista(List<ComprobantePago> listaComprobantes){
        StringBuilder sb = new StringBuilder();
        for (ComprobantePago comprobante : listaComprobantes) {
            sb.append(comprobante.getNumeroComprobante()).append(", "); // Agregar el numeroComprobante al StringBuilder
        }
        // Eliminar la última coma y espacio adicionales si existen
        if (!listaComprobantes.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }

        String cadena = sb.toString();

        return cadena;
    }
}