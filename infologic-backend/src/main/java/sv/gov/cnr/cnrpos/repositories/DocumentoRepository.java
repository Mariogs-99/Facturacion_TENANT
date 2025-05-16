package sv.gov.cnr.cnrpos.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import sv.gov.cnr.cnrpos.models.dto.DocumentoDTO;
import sv.gov.cnr.cnrpos.models.enums.EstatusCola;
import sv.gov.cnr.cnrpos.models.enums.TipoDTE;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DocumentoRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<DocumentoDTO> getDocumentoNotaCredito(String nitCliente) {

        Long status = (long) EstatusCola.APROBADO.getCodigo();
        String ccf = TipoDTE.COMPROBANTE_CREDITO_FISCAL.getCodigo();
        String cr = TipoDTE.COMPROBANTE_RETENCION.getCodigo();

        String sql = "SELECT t.id_transaccion, t.tipo_dte, t.codigo_generacion, t.numero_dte, dt.fecha_emision, t.total_transaccion, 2 AS tipo_generacion " +
                "FROM transaccion t " +
                "LEFT JOIN ( " +
                "    SELECT d.id_transaccion, d.fecha_emision " +
                "    FROM dte_transaccion d " +
                "    INNER JOIN ( " +
                "        SELECT id_transaccion, MAX(id_dte_transaccion) AS ult_id_dte_transaccion " +
                "        FROM dte_transaccion " +
                "        GROUP BY id_transaccion " +
                "    ) max_t ON d.id_transaccion = max_t.id_transaccion AND d.id_dte_transaccion = max_t.ult_id_dte_transaccion " +
                ") dt ON t.id_transaccion = dt.id_transaccion " +
                "LEFT JOIN cliente c ON t.id_cliente = c.id_cliente " +
                "WHERE t.status = ? AND t.tipo_dte IN (?, ?) AND c.nit_customer = ? ";

        return jdbcTemplate.query(sql, ps -> {
            ps.setLong(1, status);
            ps.setString(2, ccf);
            ps.setString(3, cr);
            ps.setString(4, nitCliente);
        }, new TransactionRowMapper());
    }

    private static class TransactionRowMapper implements RowMapper<DocumentoDTO> {
        @Override
        public DocumentoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            Long idTransaccion = rs.getLong("id_transaccion");
            String tipoDte = rs.getString("tipo_dte");
            String codigoGeneracion = rs.getString("codigo_generacion");
            String numeroDte = rs.getString("numero_dte");
            String fechaEmision = rs.getString("fecha_emision");
            BigDecimal totalTransaccion = rs.getBigDecimal("total_transaccion");
            Long tipoGeneracion = rs.getLong("tipo_generacion");
            return new DocumentoDTO(idTransaccion, tipoDte, codigoGeneracion, numeroDte, fechaEmision, totalTransaccion, tipoGeneracion);
        }
    }
}
