package sv.gov.cnr.cnrpos.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import sv.gov.cnr.cnrpos.entities.Menu;
import sv.gov.cnr.cnrpos.entities.MenuItems;
import sv.gov.cnr.cnrpos.exceptions.CustomException;
import sv.gov.cnr.cnrpos.exceptions.ResourceNotFoundException;
import sv.gov.cnr.cnrpos.models.dto.CategoryDTO;
import sv.gov.cnr.cnrpos.models.dto.MenuDTO;
import sv.gov.cnr.cnrpos.models.dto.MenuItemsDTO;
import sv.gov.cnr.cnrpos.models.dto.UserMenuDTO;
import sv.gov.cnr.cnrpos.models.mappers.MenuItemsMapper;
import sv.gov.cnr.cnrpos.models.mappers.MenuMapper;
import sv.gov.cnr.cnrpos.models.mappers.security.UserMenuMapper;
import sv.gov.cnr.cnrpos.repositories.MenuItemsRepository;
import sv.gov.cnr.cnrpos.repositories.MenuRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;


@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuItemsRepository menuItemsRepository;
    private final MenuMapper menuMapper;
    private final MenuItemsMapper menuItemsMapper;
    private final UserMenuMapper userMenuMapper;

    // Constructor con inyección de dependencias
    public MenuService(
            MenuRepository menuRepository,
            MenuItemsRepository menuItemsRepository,
            MenuMapper menuMapper,
            MenuItemsMapper menuItemsMapper,
            UserMenuMapper userMenuMapper
    ) {
        this.menuRepository = menuRepository;
        this.menuItemsRepository = menuItemsRepository;
        this.menuMapper = menuMapper;
        this.menuItemsMapper = menuItemsMapper;
        this.userMenuMapper = userMenuMapper;
    }


    public List<Menu> getAllMenu() {
        return menuRepository.findAll();
    }

    public List<MenuDTO> getAllMenuDTO() {
        List<Menu> menus = menuRepository.findAll();
        return menuMapper.toDtoList(menus);
    }

    public Page<MenuDTO> getPage(int page, int size) {
        Page<Menu> menus = menuRepository.findAll(PageRequest.of(page, size));
        return menus.map(menuMapper::toDto);
    }


    public Menu getMenu(Long id) {
        return menuRepository.findById(id).orElse(null);
    }

    public Map<String, Object> getMenuMenuItems(Long id) {
        Map<String, Object> menuData = new HashMap<>();

        menuData.put("pages", menuItemsRepository.findAllByMenuId(id, PageRequest.of(0, 5)));
        menuData.put("menu", menuMapper.toDto(getMenu(id)));

        return menuData;
    }

    @Transactional(rollbackFor = CustomException.class)
    public Menu saveMenu(Menu menuReq) {

        if (menuReq.getCreateItemsMenu() != null && menuReq.getCreateItemsMenu() != false) {
            if (menuReq.getApiName() == "" || menuReq.getApiName() == null) {
                throw new ResourceNotFoundException("Es obligatorio ingresar el nombre de la API");
            }
            try {
                Menu menu = menuRepository.save(menuReq);

                Set<MenuItems> menuItems = new HashSet<>();

                menuItems.add(new MenuItems(menu.getIdMenu(), "Index", menuReq.getApiName(), "Creado por defecto", null, "GET"));
                menuItems.add(new MenuItems(menu.getIdMenu(), "Visualizar", menuReq.getApiName() + "/{id}", "Creado por defecto", null, "GET"));
                menuItems.add(new MenuItems(menu.getIdMenu(), "Agregar", menuReq.getApiName(), "Creado por defecto", null, "POST"));
                menuItems.add(new MenuItems(menu.getIdMenu(), "Actualizar", menuReq.getApiName() + "/{id}", "Creado por defecto", null, "PUT"));
                menuItems.add(new MenuItems(menu.getIdMenu(), "Eliminar", menuReq.getApiName() + "/{id}", "Creado por defecto", null, "DELETE"));

                List<MenuItems> menuItemsSaved = menuItemsRepository.saveAll(menuItems);

                menu.setMenuItems(menuItems);

                return menuRepository.save(menu);
            } catch (Exception ex) {
                throw new CustomException("No se completo la operacion");
            }
        }

        return menuRepository.save(menuReq);
    }

    public Menu updateMenu(Long id, Menu menuReq) {
        Menu menu = getMenu(id);

        if (menu == null) {
            throw new ResourceNotFoundException("No se encontró el menu con id " + id);
        }


        menu.setCategory(menuReq.getCategory());
        menu.setIdCategory(menuReq.getIdCategory());
        menu.setNameMenu(menuReq.getNameMenu());
        menu.setDescriptionMenu(menuReq.getDescriptionMenu());
        menu.setSlugMenu(menuReq.getSlugMenu());
        menu.setImageMenu(menuReq.getImageMenu());

        return menuRepository.save(menu);
    }

    public Menu deleteMenu(Long id) {
        Menu menu = getMenu(id);

        if (menu == null) {
            throw new ResourceNotFoundException("No se encontró el menu con id " + id);
        }

        Set<MenuItems> menuItems = menuItemsRepository.findByMenuId(menu.getIdMenu());

        if (menuItems != null) {
            menuItems.forEach(item -> item.setDeletedAt(Timestamp.valueOf(LocalDateTime.now())));

            menuItemsRepository.saveAll(menuItems);
        }
        menu.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
        menu.getMenuItems().clear();
        return menuRepository.save(menu);
    }

    public Map<String, List<MenuDTO>> findMenusByUserId(Long userId) {
        Set<Menu> menuList = menuRepository.findMenusByUserId(userId);
        return menuList.stream().map(menuMapper::toDtoMenuUserList).collect(Collectors.groupingBy(menu -> menu.getCategory().getValor()));
    }

    public Map<String, List<MenuDTO>> findMenuMenuItemsIdsByUserIdv2(Long userId) {
        List<Object[]> results = menuRepository.findMenuMenuItemsIdsByUserId(userId);
        Map<Long, MenuDTO> menuMap = new HashMap<>();

        for (Object[] result : results) {
            Menu menuEntity = (Menu) result[0];
            Long idMenu = menuEntity.getIdMenu(); // assuming getIdMenu() is the method to get id from Menu object
            MenuDTO menu = menuMap.get(idMenu);
            if (menu == null) {
                menu = menuMapper.toDtoMenuUserList(menuEntity);
                menu.setMenuItems(new ArrayList<>());
                menuMap.put(idMenu, menu);
            }

            MenuItems menuItemEntity = (MenuItems) result[1];
            MenuItemsDTO menuItem = menuMapper.toMenuItemsDTO(menuItemEntity);
            menu.getMenuItems().add(menuItem);
        }

        return menuMap.values().stream().collect(Collectors.groupingBy(menu -> menu.getCategory().getValor()));
    }

    public List<MenuDTO> findMenuMenuItemsIdsByUserId(Long userId) {
        List<Object[]> results = menuRepository.findMenuMenuItemsIdsByUserId(userId);
        Map<Long, MenuDTO> menuMap = new HashMap<>();

        for (Object[] result : results) {
            Menu menuEntity = (Menu) result[0];
            Long idMenu = menuEntity.getIdMenu(); // assuming getIdMenu() is the method to get id from Menu object
            MenuDTO menu = menuMap.getOrDefault(idMenu, new MenuDTO());
            menu.setIdMenu(menuEntity.getIdMenu());
            menu.setIdCategory(menuEntity.getCategory().getIdCatalogo()); // assuming getCategory() is the method to get Category object from Menu object
            menu.setNameMenu(menuEntity.getNameMenu());

            CategoryDTO category = new CategoryDTO();
            category.setIdCatalogo(menuEntity.getCategory().getIdCatalogo()); // assuming getIdCatalogo() is the method to get id from Category object
            category.setValor(menuEntity.getCategory().getValor()); // assuming getValor() is the method to get valor from Category object
            category.setGrupo(menuEntity.getCategory().getGrupo());
            menu.setCategory(category);

            MenuItemsDTO menuItem = new MenuItemsDTO();
            //menuItem.setIdMenuItems(((MenuItems) result[1]).getIdMenuItems()); // assuming getIdMenuItems() is the method to get id from MenuItem object
            //menuItem.setIdMenu(idMenu);
            menuItem.setNameMenuItems(((MenuItems) result[1]).getNameMenuItems()); // assuming getNameMenuItems() is the method to get name from MenuItem object
            menuItem.setUriMenuItems(((MenuItems) result[1]).getUriMenuItems());

            menuItem.setDescriptionMenuItems(((MenuItems) result[1]).getDescriptionMenuItems());
            menuItem.setType(((MenuItems) result[1]).getType());
            menu.getMenuItems().add(menuItem);
            menuMap.put(idMenu, menu);
        }

        return new ArrayList<>(menuMap.values());
    }
}
