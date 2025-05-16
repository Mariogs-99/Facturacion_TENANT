package sv.gov.cnr.cnrpos.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sv.gov.cnr.cnrpos.models.dto.PermissionDTO;
import sv.gov.cnr.cnrpos.models.dto.RolDTO;
import sv.gov.cnr.cnrpos.models.mappers.security.RolMapper;
import sv.gov.cnr.cnrpos.models.security.Permission;
import sv.gov.cnr.cnrpos.models.security.Rol;
import sv.gov.cnr.cnrpos.exceptions.ResourceNotFoundException;
import sv.gov.cnr.cnrpos.repositories.security.PermissionRepository;
import sv.gov.cnr.cnrpos.repositories.security.RolRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service

public class RolService {

    private final RolRepository rolRepository;
    private final PermissionRepository permissionRepository;
    private final RolMapper rolMapper;

    // Constructor con inyección de dependencias
    public RolService(
            RolRepository rolRepository,
            PermissionRepository permissionRepository,
            RolMapper rolMapper
    ) {
        this.rolRepository = rolRepository;
        this.permissionRepository = permissionRepository;
        this.rolMapper = rolMapper;
    }

    public List<Rol> getAllRoles() {
        return rolRepository.findAll();
    }

    public Page<RolDTO> getPage(int page, int size) {
        Page<Rol> roles = rolRepository.findAll(PageRequest.of(page, size));
        return roles.map(rolMapper::toDto);
    }

    public Rol saveRol(Rol role) {

        Set<Permission> permissions = role.getPermissionIds() != null && !role.getPermissionIds().isEmpty() ? new HashSet<>(permissionRepository.findAllById(role.getPermissionIds())) : null;

        if (permissions != null) {
            role.setPermission(permissions);
        }

        return rolRepository.save(role);
    }

    public Rol updateRol(Long id, Rol rolReq) {

        Rol rol = getRol(id);

        if (rol == null) {
            throw new ResourceNotFoundException("No se encontró el rol con id " + id);
        }

        rol.setNameRole(rolReq.getNameRole());
        rol.setDescriptionRole(rolReq.getDescriptionRole());

        Set<Permission> permissions = rolReq.getPermissionIds() != null && !rolReq.getPermissionIds().isEmpty() ? new HashSet<>(permissionRepository.findAllById(rolReq.getPermissionIds())) : null;

        if (permissions != null) {
            rol.setPermission(permissions);
        } else {
            rol.getPermission().clear();
        }

        return rolRepository.save(rol);
    }

    public Rol getRol(Long id) {
        return rolRepository.findById(id).orElse(null);
    }

    public RolDTO getRolDto(Long id) {
        Rol rol = rolRepository.findById(id).orElse(null);
        return rolMapper.toDto(rol);
    }

    public void deleteRol(Long id) {
        Optional<Rol> rol = rolRepository.findById(id);

        if (rol.isEmpty()) {
            throw new ResourceNotFoundException("No se encontró el rol con ID " + id);
        }

        rolRepository.delete(rol.get());
    }
}
