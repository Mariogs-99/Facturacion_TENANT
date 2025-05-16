package sv.gov.cnr.cnrpos.services;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@Service
@Slf4j
public class ReportService {
    private final DataSource dataSource;
    private JasperReport jasperReport;

    // Constructor con inyección de dependencias
    public ReportService(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public byte[] generarReporte(Map<String, Object> parametros, String nombreReporte, String format) throws Exception {
        // Cargar el archivo .jrxml desde la carpeta static/reports
        Resource jrxmlResource = new ClassPathResource("reports/"+nombreReporte+".jrxml");
        InputStream inputStream = jrxmlResource.getInputStream();

        jasperReport = JasperCompileManager.compileReport(inputStream);
        JasperPrint jasperPrint = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Connection connection = null;

        try {
            // Generar el informe en formato PDF
            connection = dataSource.getConnection();

            jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, connection);

            switch (format.toLowerCase()) {
                case "pdf":
                    JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
                case "xls":
                    SimpleXlsReportConfiguration config = new SimpleXlsReportConfiguration();
                    config.setDetectCellType(true);
                    config.setWhitePageBackground(false);
                    config.setRemoveEmptySpaceBetweenRows(false);
                    config.setRemoveEmptySpaceBetweenColumns(true);
                    config.setIgnoreGraphics(false);
                    config.setIgnoreCellBorder(false);
                    config.setFreezeRow(1);
                    config.setSheetNames(new String[]{nombreReporte});
                    config.setFontSizeFixEnabled(true);
                    config.setAutoFitPageHeight(true); // Ajustar automáticamente la altura de las páginas
                    config.setWrapText(true); // Envolver texto para evitar expansión

                    config.setOnePagePerSheet(false);
                    config.setIgnorePageMargins(true);

                    JRXlsExporter xlsExporter = new JRXlsExporter();
                    xlsExporter.setConfiguration(config);
                    xlsExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
                    xlsExporter.exportReport();
                    break;
                case "csv":
                    JRCsvExporter csvExporter = new JRCsvExporter();
                    csvExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    csvExporter.setExporterOutput(new SimpleWriterExporterOutput(outputStream));
                    SimpleCsvExporterConfiguration csvExporterConfiguration = new SimpleCsvExporterConfiguration();
                    csvExporter.setConfiguration(csvExporterConfiguration);
                    csvExporter.exportReport();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported report format: " + format);
            }
            return outputStream.toByteArray();
        } catch (Exception e) {
            // Manejar la excepción, si es necesario
            log.error("error metodo generarReporte  e: " + e);
            throw new Exception("Database connection error", e);
        } finally {
            // Cerrar la conexión en el bloque finally para garantizar que se cierre siempre
            if (connection != null) {
                //logger.info("opened connection");
                try {
                    connection.close();
                } catch (SQLException e) {
                    // Manejar la excepción si ocurre un error al cerrar la conexión
                    log.error("error metodo generarReporte  e: " + e);
                }
            }
        }
    }

}
