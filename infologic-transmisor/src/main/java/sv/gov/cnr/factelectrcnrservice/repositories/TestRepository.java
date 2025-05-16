package sv.gov.cnr.factelectrcnrservice.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import sv.gov.cnr.factelectrcnrservice.models.dto.DocumentosDTO;
import sv.gov.cnr.factelectrcnrservice.models.enums.EstatusCola;
import sv.gov.cnr.factelectrcnrservice.models.enums.TipoDTE;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TestRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<DocumentosDTO> getDocumentoNotaCredito(String nitCliente) {

        Long status = (long) EstatusCola.APROBADO.getCodigo();
        String ccf = TipoDTE.COMPROBANTE_CREDITO_FISCAL.getCodigo();
        String cr = TipoDTE.COMPROBANTE_RETENCION.getCodigo();


        String sql = "SELECT t.ID_TRANSACCION, t.TIPO_DTE, t.CODIGO_GENERACION, t.NUMERO_DTE, dt.FECHA_EMISION, t.TOTAL_TRANSACCION, 2 AS TIPO_GENERACION " +
                "FROM TRANSACCION t " +
                "LEFT JOIN ( " +
                "    SELECT d.ID_TRANSACCION, d.FECHA_EMISION " +
                "    FROM DTE_TRANSACCION d " +
                "    INNER JOIN ( " +
                "        SELECT ID_TRANSACCION, MAX(ID_DTE_TRANSACCION) AS ULT_ID_DTE_TRANSACCION " +
                "        FROM DTE_TRANSACCION " +
                "        GROUP BY ID_TRANSACCION " +
                "    ) MAX_T ON d.ID_TRANSACCION = MAX_T.ID_TRANSACCION AND d.ID_DTE_TRANSACCION = MAX_T.ULT_ID_DTE_TRANSACCION " +
                ") dt ON t.ID_TRANSACCION = dt.ID_TRANSACCION " +
                "LEFT JOIN CLIENTE c ON t.ID_CLIENTE = c.ID_CLIENTE " +
                "WHERE t.STATUS = ? AND t.TIPO_DTE IN (?, ?) AND c.NIT_CUSTOMER = ? ";

        return jdbcTemplate.query(sql, ps -> {
            ps.setLong(1, status);
            ps.setString(2, ccf);
            ps.setString(3, cr);
            ps.setString(4, nitCliente);
        }, new TransactionRowMapper());


    }

    private static class TransactionRowMapper implements RowMapper<DocumentosDTO> {
        @Override
        public DocumentosDTO mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
            Long idTransaccion = rs.getLong("ID_TRANSACCION");
            String tipoDte = rs.getString("TIPO_DTE");
            String codigoGeneracion = rs.getString("CODIGO_GENERACION");
            String numeroDte = rs.getString("NUMERO_DTE");
            String fechaEmision = rs.getString("FECHA_EMISION");
            BigDecimal totalTransaccion = rs.getBigDecimal("TOTAL_TRANSACCION");
            Long tipoGeneracion = rs.getLong("TIPO_GENERACION");
            return new DocumentosDTO(idTransaccion, tipoDte, codigoGeneracion, numeroDte, fechaEmision, totalTransaccion, tipoGeneracion);
        }
    }


}
