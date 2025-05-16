package sv.gov.cnr.cnrpos.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import sv.gov.cnr.cnrpos.entities.Compra;
import sv.gov.cnr.cnrpos.exceptions.ResourceNotFoundException;
import sv.gov.cnr.cnrpos.models.dto.CompraDTO;
import sv.gov.cnr.cnrpos.models.dto.PermissionDTO;
import sv.gov.cnr.cnrpos.models.mappers.CompraMapper;
import sv.gov.cnr.cnrpos.models.security.Permission;
import sv.gov.cnr.cnrpos.models.security.Rol;
import sv.gov.cnr.cnrpos.models.security.User;
import sv.gov.cnr.cnrpos.repositories.CompraRepository;
import sv.gov.cnr.cnrpos.repositories.SucursalRepository;
import sv.gov.cnr.cnrpos.security.AuthenticationService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service

public class CompraService {

    private final CompraRepository compraRepository;
    private final SucursalRepository sucursalRepository;
    private final AuthenticationService authenticationService;
    private final CompraMapper compraMapper;

    public CompraService(
            CompraRepository compraRepository,
            SucursalRepository sucursalRepository,
            AuthenticationService authenticationService,
            CompraMapper compraMapper
    ) {
        this.compraRepository = compraRepository;
        this.sucursalRepository = sucursalRepository;
        this.authenticationService = authenticationService;
        this.compraMapper = compraMapper;
    }

    public Page<CompraDTO> getPage(int page, int size) {
        Page<Compra> compras = compraRepository.findAll(PageRequest.of(page, size));
        return compras.map(compraMapper::toDtoList);
    }

    public CompraDTO getCompraDTO(Long id) {
        Compra compra = compraRepository.findById(id).orElse(null);

        return compraMapper.toDto(compra);
    }

    public Compra getCompra(Long id) {
        return compraRepository.findById(id).orElse(null);
    }

    public Compra saveCompra(Compra compraReq) {

        User usuario = authenticationService.loggedUser();
        compraReq.setUser(usuario);
        compraReq.setSucursal(sucursalRepository.findByIdDeletedAtIsNull(usuario.getIdBranch()));
        return compraRepository.save(compraReq);
    }

    public Compra updateCompra(Long id, Compra compraReq) {
        Compra compra = getCompra(id);

        if (compra == null) {
            throw new ResourceNotFoundException("No se encontró la compra con id " + id);
        }
        User usuario = authenticationService.loggedUser();

        compra.setCodigoGeneracion(compraReq.getCodigoGeneracion());
        compra.setNumeroControl(compraReq.getNumeroControl());
        compra.setFechaEmision(compraReq.getFechaEmision());
        compra.setTipoOperacion(compraReq.getTipoOperacion());
        compra.setTipoDocumento(compraReq.getTipoDocumento());
        compra.setSelloRecepcion(compraReq.getSelloRecepcion());
        compra.setIdProveedor(compraReq.getIdProveedor());
        compra.setTotalGravado(compraReq.getTotalGravado());
        compra.setTotalExento(compraReq.getTotalExento());
        compra.setTotalOperacion(compraReq.getTotalOperacion());
        compra.setUser(usuario);
        compra.setSucursal(sucursalRepository.findByIdDeletedAtIsNull(usuario.getIdBranch()));

        return compraRepository.save(compra);
    }

    public Compra deleteCompra(Long id) {
        Compra compra = getCompra(id);

        if (compra == null) {
            throw new ResourceNotFoundException("No se encontró la compra con id " + id);
        }

        compra.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));

        return compraRepository.save(compra);
    }
}
