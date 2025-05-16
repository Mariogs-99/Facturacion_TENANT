package sv.gov.cnr.cnrpos.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sv.gov.cnr.cnrpos.entities.Transaccion;
import sv.gov.cnr.cnrpos.models.enums.EstatusCola;
import sv.gov.cnr.cnrpos.models.security.User;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long>, PagingAndSortingRepository<Transaccion, Long>, JpaSpecificationExecutor<Transaccion> {

    @Query("SELECT t FROM Transaccion t WHERE t.deletedAt IS NULL")
    Page<Transaccion> findAll(Pageable pageable);

    List<Transaccion> findByUserOrderByIdTransaccionDesc(User user);

    List<Transaccion> findAllByOrderByIdTransaccionDesc();

    @Query("SELECT t FROM Transaccion t JOIN t.cliente c WHERE c.nombre LIKE %:nombreCliente% AND t.deletedAt IS NULL")
    Page<Transaccion> findTransaccionesByClienteNombre(@Param("nombreCliente") String nombreCliente, Pageable pageable);

    @Query("SELECT t FROM Transaccion t WHERE t.status = :status AND t.deletedAt IS NULL")
    Page<Transaccion> findTransaccionesByStatusDte(@Param("status") EstatusCola nombreCliente, Pageable pageable);

    @Query("SELECT t FROM Transaccion t WHERE t.totalTransaccion = :total AND t.deletedAt IS NULL")
    Page<Transaccion> findTransaccionesByTotalTransaccion(@Param("total") BigDecimal total, Pageable pageable);

    @Query(value = "SELECT t FROM Transaccion t WHERE t.deletedAt IS NULL",
            countQuery = "SELECT COUNT(s) FROM Transaccion s WHERE s.deletedAt IS NULL")
    Page<Transaccion> findAllByOrderByIdTransaccionDesc(Pageable page);

    @Query(value = "SELECT t FROM Transaccion t WHERE t.deletedAt IS NULL AND t.user = :user",
            countQuery = "SELECT COUNT(s) FROM Transaccion s WHERE s.deletedAt IS NULL AND s.user = :user")
    Page<Transaccion> findByUserOrderByIdTransaccionDesc(@Param("user") User user, Pageable page);

    @Query("SELECT s FROM Transaccion s INNER JOIN s.cliente c WHERE s.tipoDTE IN :tipoDTE AND s.cliente.nit = :nit AND s.deletedAt IS NULL AND s.status = 2 ORDER BY s.idTransaccion DESC")
    List<Transaccion> getTransaccionByTipoDTEandIdCliente(@Param("tipoDTE") List<String> tipoDTE, @Param("nit") String nit);

    @Query(value = "SELECT * FROM transaccion s " +
            "WHERE s.tipoDTE IN :tipoDTE " +
            "AND s.deletedAt IS NULL " +
            "AND s.createdAt >= DATE_SUB(CURRENT_DATE, INTERVAL 3 MONTH) " +
            "AND NOT EXISTS (SELECT 1 FROM cuerpo_doc_cr c WHERE c.numDocumento = s.codigoGeneracion) " +
            "AND s.ivaRetenido != 0 " +
            "AND s.status = 2 " +
            "ORDER BY s.idTransaccion DESC", nativeQuery = true)
    List<Transaccion> getTransaccionByTipoDTE(@Param("tipoDTE") List<String> tipoDTE);

    @Query("SELECT s FROM Transaccion s INNER JOIN s.cliente c WHERE s.tipoDTE IN :tipoDTE AND s.cliente.nit = :nit AND NOT EXISTS (SELECT da FROM DocumentoAsociado da WHERE da.transaccionHija.idTransaccion = s.idTransaccion AND da.transaccion.status NOT IN (3)) AND s.deletedAt IS NULL AND s.status NOT IN (3,5) ORDER BY s.idTransaccion DESC")
    List<Transaccion> getTransaccionByTipoDTEandNit(@Param("tipoDTE") List<String> tipoDTE, @Param("nit") String nit);

    @Query("SELECT s FROM Transaccion s WHERE s.tipoDTE IN :tipoDTE AND s.cliente.nit = :nit AND s.ivaRetenido > 0 AND s.deletedAt IS NULL ORDER BY s.idTransaccion DESC")
    List<Transaccion> getTransaccionesConIVARetenido(@Param("tipoDTE") List<String> tipoDTE, @Param("nit") String nit);

    // MÉTODOS PARA KPI
    @Query("SELECT COUNT(t) FROM Transaccion t WHERE t.status IN (:status1, :status2)")
    long countGeneratedDocuments(@Param("status1") EstatusCola status1, @Param("status2") EstatusCola status2);

    @Query("SELECT COUNT(t) FROM Transaccion t WHERE t.status IN (:status1, :status2) AND t.sucursal.idSucursal = :idSucursal")
    long countGeneratedDocumentsByVenta(@Param("status1") EstatusCola status1, @Param("status2") EstatusCola status2, @Param("idSucursal") Long idSucursal);

    @Query("SELECT COUNT(t) FROM Transaccion t WHERE t.status = :status")
    long countInvalidatedDocuments(@Param("status") EstatusCola status);

    @Query("SELECT COUNT(t) FROM Transaccion t WHERE t.status = :status AND t.sucursal.idSucursal = :idSucursal")
    long countInvalidatedDocumentsByVenta(@Param("status") EstatusCola status, @Param("idSucursal") Long idSucursal);

    @Query("SELECT COUNT(t) FROM Transaccion t WHERE t.status = :status")
    long countContingenciaDocumentos(@Param("status") EstatusCola status);

    @Query("SELECT COUNT(t) FROM Transaccion t WHERE t.status = :status AND t.sucursal.idSucursal = :idSucursal")
    long countContingenciaDocumentosByVenta(@Param("status") EstatusCola status, @Param("idSucursal") Long idSucursal);

    @Query("SELECT COUNT(t) FROM Transaccion t WHERE t.status = :status")
    long countRechazadosDocumentos(@Param("status") EstatusCola status);

    @Query("SELECT COUNT(t) FROM Transaccion t WHERE t.status = :status AND t.sucursal.idSucursal = :idSucursal")
    long countRechazadosDocumentosByVenta(@Param("status") EstatusCola status, @Param("idSucursal") Long idSucursal);

    @Query("SELECT COUNT(t) FROM Transaccion t WHERE t.status IN (:status1, :status2) AND t.formaDePago = '01' AND t.tipoDTE = :tipoDTE")
    long countGeneratedDocumentosByTipoPagoAndTipoDoc(@Param("status1") EstatusCola status1, @Param("status2") EstatusCola status2, @Param("tipoDTE") String tipoDTE);
}
