package sv.gov.cnr.cnrpos.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import sv.gov.cnr.cnrpos.entities.Menu;
import sv.gov.cnr.cnrpos.entities.MenuItems;
import sv.gov.cnr.cnrpos.exceptions.ResourceNotFoundException;
import sv.gov.cnr.cnrpos.models.dto.MenuDTO;
import sv.gov.cnr.cnrpos.models.dto.PermissionDTO;
import sv.gov.cnr.cnrpos.models.mappers.security.PermisosMapper;
import sv.gov.cnr.cnrpos.models.security.Permission;
import sv.gov.cnr.cnrpos.repositories.MenuItemsRepository;
import sv.gov.cnr.cnrpos.repositories.security.PermissionRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final MenuItemsRepository menuItemsRepository;
    private final PermisosMapper permisosMapper;

    // Constructor con inyección de dependencias
    public PermissionService(
            PermissionRepository permissionRepository,
            MenuItemsRepository menuItemsRepository,
            PermisosMapper permisosMapper
    ) {
        this.permissionRepository = permissionRepository;
        this.menuItemsRepository = menuItemsRepository;
        this.permisosMapper = permisosMapper;
    }


    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    public List<PermissionDTO> getAllPermissionsDto() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream().map(permisosMapper::toDto).collect(Collectors.toList());
    }

    public PermissionDTO getPermission(Long id) {
        Permission permission = permissionRepository.findById(id).orElse(null);

        return permisosMapper.toDto(permission);
    }

    public Page<PermissionDTO> getPage(int page, int size) {
        Page<Permission> permission = permissionRepository.findAll(PageRequest.of(page, size));
        return permission.map(permisosMapper::toDtoList);
    }

    public Permission savePermission(Permission permission) {

        Set<MenuItems> menuItems = permission.getMenuItemsIds() != null && !permission.getMenuItemsIds().isEmpty() ? new HashSet<>(menuItemsRepository.findAllById(permission.getMenuItemsIds())) : null;

        if (menuItems != null) {
            permission.setMenuItems(menuItems);
        }
        return permissionRepository.save(permission);
    }

    public Permission updatePermission(Long id, Permission permissionReq) {

        Permission permission = permissionRepository.findById(id).orElse(null);

        if (permission == null) {
            throw new ResourceNotFoundException("No se encontró el permiso con id " + id);
        }
        permission.setNamePermissions(permissionReq.getNamePermissions());
        permission.setDescriptionPermissions(permissionReq.getDescriptionPermissions());


        Set<MenuItems> menuItems = permissionReq.getMenuItemsIds() != null && !permissionReq.getMenuItemsIds().isEmpty() ? new HashSet<>(menuItemsRepository.findAllById(permissionReq.getMenuItemsIds())) : null;

        if (menuItems != null) {
            permission.setMenuItems(menuItems);
        } else {
            permission.getMenuItems().clear();
        }

        return permissionRepository.save(permission);
    }

    public Permission deletePermission(Long id) {
        Permission permission = permissionRepository.findById(id).orElse(null);

        if (permission == null) {
            throw new ResourceNotFoundException("No se encontró el permiso con ID " + id);
        }

        permission.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
        return permissionRepository.save(permission);
    }
}
