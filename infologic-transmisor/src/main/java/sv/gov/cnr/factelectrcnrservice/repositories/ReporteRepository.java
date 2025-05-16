package sv.gov.cnr.factelectrcnrservice.repositories;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import sv.gov.cnr.factelectrcnrservice.models.dto.DatosReporteDTO;
import sv.gov.cnr.factelectrcnrservice.models.dto.ReporteIvaConsumidorFinalDTO;
import sv.gov.cnr.factelectrcnrservice.models.dto.ReporteIvaContribuyentesDTO;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReporteRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<ReporteIvaContribuyentesDTO> obtenerRegistrosIvaContribuyente(DatosReporteDTO datosReporte) {

        String fechaDesde = datosReporte.getFechaDesde();
        String fechaHasta = datosReporte.getFechaHasta();
        String sql = " SELECT "
                + "     dt.fecha_emision, "
                + "     4 AS clase_documento, "
                + "     t.tipo_dte AS tipo_documento, "
                + "     REPLACE(t.numero_dte, '-', '') AS numero_resolucion, "
                + "     dt.sello_recibido_mh AS numero_serie, "
                + "     REPLACE(dt.codigo_generacion, '-', '') AS numero_documento, "
                + "     t.id_transaccion AS numero_control, "
                + "     CASE WHEN c.nit_customer IS NOT NULL THEN c.nit_customer ELSE c.nrc_customer END AS nit, "
                + "     c.nombre_cliente, "
                + "     TO_CHAR(t.total_exento, 'FM9999999990.00') AS total_exento, "
                + "     TO_CHAR(t.total_nosujeto, 'FM9999999990.00') AS total_no_sujeto, "
                + "     TO_CHAR(t.total_gravado, 'FM9999999990.00') AS total_gravado, "
                + "     CASE WHEN t.tipo_dte = '06' THEN TO_CHAR(t.total_transaccion, 'FM9999999990.00') ELSE '0.00' END AS debito_fiscal, "
                + "     '0.00' AS venta_terceros, "
                + "     '0.00' AS debito_terceros, "
                + "     TO_CHAR(t.total_transaccion, 'FM9999999990.00') AS total_ventas, "
                + "     CASE WHEN c.nit_customer IS NULL AND c.nrc_customer IS NULL AND c.tipo_documento = '13' THEN c.numero_documento ELSE NULL END AS dui, "
                + "     1 AS anexo "
                + " FROM "
                + "     transaccion t "
                + " LEFT JOIN ( "
                + "     SELECT d.id_transaccion, d.fecha_emision, d.sello_recibido_mh, d.codigo_generacion "
                + "     FROM dte_transaccion d "
                + "     INNER JOIN ( "
                + "         SELECT id_transaccion, MAX(id_dte_transaccion) AS ult_id_dte_transaccion "
                + "         FROM dte_transaccion "
                + "         GROUP BY id_transaccion "
                + "     ) max_t ON d.id_transaccion = max_t.id_transaccion AND d.id_dte_transaccion = max_t.ult_id_dte_transaccion "
                + " ) dt ON t.id_transaccion = dt.id_transaccion "
                + " LEFT JOIN cliente c ON t.id_cliente = c.id_cliente "
                + " WHERE "
                + "     t.status = 2 "
                + "     AND t.tipo_dte IN ('03', '05', '06') "
                + "     AND TO_DATE(dt.fecha_emision, 'YYYY-MM-DD') BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD')";

        return jdbcTemplate.query(sql, ps -> {
            ps.setString(1, fechaDesde);
            ps.setString(2, fechaHasta);
        }, new TransactionRowMapperC());
    }


    public List<ReporteIvaConsumidorFinalDTO> obtenerRegistrosConsumidorFinal(DatosReporteDTO datosReporte) {
        String fechaDesde = datosReporte.getFechaDesde();
        String fechaHasta = datosReporte.getFechaHasta();

        String sql = "SELECT dt.fecha_emision, 4 AS clase_documento, t.tipo_dte AS tipo_documento, "
                + "REPLACE(t.numero_dte, '-', '') AS numero_resolucion, "
                + "dt.sello_recibido_mh AS numero_serie, "
                + "NULL AS numero_interno_del, "
                + "NULL AS numero_interno_al, "
                + "NULL AS numero_documento_del, "
                + "NULL AS numero_documento_al, "
                + "NULL AS nro_maquina_registradora, "
                + "TO_CHAR(t.total_exento, 'FM9999999990.00') AS total_exento, "
                + "'0.00' AS total_exento_no_sujeto, "
                + "TO_CHAR(t.total_nosujeto, 'FM9999999990.00') AS ventas_no_sujetas, "
                + "'0.00' AS ventas_gravadas_locales, "
                + "'0.00' AS exportaciones_centroamerica, "
                + "'0.00' AS exportaciones_no_centroamerica, "
                + "'0.00' AS exportaciones_servicios, "
                + "'0.00' AS ventas_zona_franca, "
                + "'0.00' AS cuenta_terceros_no_domiciliados, "
                + "TO_CHAR(t.total_transaccion, 'FM9999999990.00') AS total_ventas, "
                + "2 AS anexo "
                + "FROM transaccion t "
                + "LEFT JOIN ( "
                + "    SELECT d.id_transaccion, d.fecha_emision, d.sello_recibido_mh, d.codigo_generacion "
                + "    FROM dte_transaccion d "
                + "    INNER JOIN ( "
                + "        SELECT id_transaccion, MAX(id_dte_transaccion) AS ult_id_dte_transaccion "
                + "        FROM dte_transaccion "
                + "        GROUP BY id_transaccion "
                + "    ) max_t ON d.id_transaccion = max_t.id_transaccion AND d.id_dte_transaccion = max_t.ult_id_dte_transaccion "
                + ") dt ON t.id_transaccion = dt.id_transaccion "
                + "LEFT JOIN cliente c ON t.id_cliente = c.id_cliente "
                + "WHERE t.status = 2 AND t.tipo_dte IN ('01', '11') "
                + "AND TO_DATE(dt.fecha_emision, 'YYYY-MM-DD') BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD')";

        return jdbcTemplate.query(sql, ps -> {
            ps.setString(1, fechaDesde);
            ps.setString(2, fechaHasta);
        }, new TransactionRowMapperCF());
    }


    private static class TransactionRowMapperC implements RowMapper<ReporteIvaContribuyentesDTO> {
        @Override
            public ReporteIvaContribuyentesDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                String fechaEmision = rs.getString("FECHA_EMISION");
                BigDecimal claseDocumento = rs.getBigDecimal("CLASE_DOCUMENTO");
                String tipoDocumento = rs.getString("TIPO_DOCUMENTO");
                String numeroResolucion = rs.getString("NUMERO_RESOLUCION");
                String numeroSerie = rs.getString("NUMERO_SERIE");
                String numeroDocumento = rs.getString("NUMERO_DOCUMENTO");
                String numeroControl = rs.getString("NUMERO_CONTROL");
                String nit = rs.getString("NIT");
                String nombreCliente = rs.getString("NOMBRE_CLIENTE");
                String totalExento = rs.getString("TOTAL_EXENTO");
                String totalNoSujeto = rs.getString("TOTAL_NO_SUJETO");
                String totalGravado = rs.getString("TOTAL_GRAVADO");
                String debitoFiscal = rs.getString("DEBITO_FISCAL");
                String ventaTerceros = rs.getString("VENTA_TERCEROS");
                String debitoTerceros = rs.getString("DEBITO_TERCEROS");
                String totalVentas = rs.getString("TOTAL_VENTAS");
                String dui = rs.getString("DUI");
                BigDecimal anexo = rs.getBigDecimal("ANEXO");

                return new ReporteIvaContribuyentesDTO(
                        fechaEmision,
                        claseDocumento,
                        tipoDocumento,
                        numeroResolucion,
                        numeroSerie,
                        numeroDocumento,
                        numeroControl,
                        nit,
                        nombreCliente,
                        Double.valueOf(totalExento),
                        Double.valueOf(totalNoSujeto),
                        Double.valueOf(totalGravado),
                        Double.valueOf(debitoFiscal),
                        Double.valueOf(ventaTerceros),
                        Double.valueOf(debitoTerceros),
                        Double.valueOf(totalVentas),
                        dui,
                        anexo
                );
        }
    }

    private static class TransactionRowMapperCF implements RowMapper<ReporteIvaConsumidorFinalDTO> {

        @Override
        public ReporteIvaConsumidorFinalDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            String fechaEmision = rs.getString("FECHA_EMISION");
            BigDecimal claseDocumento = rs.getBigDecimal("CLASE_DOCUMENTO");
            String tipoDocumento = rs.getString("TIPO_DOCUMENTO");
            String numeroResolucion = rs.getString("NUMERO_RESOLUCION");
            String numeroSerie = rs.getString("NUMERO_SERIE");
            String numeroInternoDel = rs.getString("NUMERO_INTERNO_DEL");
            String numeroInternoAl = rs.getString("NUMERO_INTERNO_AL");
            String numeroDocumentoDel = rs.getString("NUMERO_DOCUMENTO_DEL");
            String numeroDocumentoAl = rs.getString("NUMERO_DOCUMENTO_AL");
            String nroMaquinaRegistradora = rs.getString("NRO_MAQUINA_REGISTRADORA");
            Double totalExento = rs.getDouble("TOTAL_EXENTO");
            Double totalExentoNoSujeto = rs.getDouble("TOTAL_EXENTO_NO_SUJETO");
            Double ventasNoSujetas = rs.getDouble("VENTAS_NO_SUJETAS");
            Double ventasGravadasLocales = rs.getDouble("VENTAS_GRAVADAS_LOCALES");
            Double exportacionesCentroamerica = rs.getDouble("EXPORTACIONES_CENTROAMERICA");
            Double exportacionesNoCentroamerica = rs.getDouble("EXPORTACIONES_NO_CENTROAMERICA");
            Double exportacionesServicios = rs.getDouble("EXPORTACIONES_SERVICIOS");
            Double ventasZonaFranca = rs.getDouble("VENTAS_ZONA_FRANCA");
            Double cuentaTercerosNoDomiciliados = rs.getDouble("CUENTA_TERCEROS_NO_DOMICILIADOS");
            Double totalVentas = rs.getDouble("TOTAL_VENTAS");
            BigDecimal anexo = rs.getBigDecimal("ANEXO");

            return new ReporteIvaConsumidorFinalDTO(
                    fechaEmision, claseDocumento, tipoDocumento, numeroResolucion, numeroSerie, numeroInternoDel,
                    numeroInternoAl, numeroDocumentoDel, numeroDocumentoAl, nroMaquinaRegistradora,
                    totalExento, totalExentoNoSujeto, ventasNoSujetas, ventasGravadasLocales,
                    exportacionesCentroamerica, exportacionesNoCentroamerica, exportacionesServicios,
                    ventasZonaFranca, cuentaTercerosNoDomiciliados, totalVentas, anexo
            );
        }
    }

}
