package sv.gov.cnr.cnrpos.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import sv.gov.cnr.cnrpos.entities.Menu;
import sv.gov.cnr.cnrpos.entities.MenuItems;
import sv.gov.cnr.cnrpos.exceptions.ResourceNotFoundException;
import sv.gov.cnr.cnrpos.models.dto.MenuDTO;
import sv.gov.cnr.cnrpos.models.dto.MenuItemsDTO;
import sv.gov.cnr.cnrpos.models.mappers.MenuItemsMapper;
import sv.gov.cnr.cnrpos.repositories.MenuItemsRepository;
import sv.gov.cnr.cnrpos.repositories.MenuRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class MenuItemsService {

    private final MenuItemsRepository menuItemsRepository;
    private final MenuRepository menuRepository;
    private final MenuItemsMapper menuItemsMapper;

    public List<MenuItems> getMenuItems() {
        return menuItemsRepository.findAll();
    }

    public Page<MenuItemsDTO> getPage(Long id, int page, int size) {
        Page<MenuItems> menus = menuItemsRepository.findAllByMenuId(id, PageRequest.of(page, size));
        return menus.map(menuItemsMapper::toDto);
    }

    public Map<String, Object> saveMenuItem(MenuItems menuItemReq) {
        //MenuItems menuItem = menuItemsRepository.save(menuItemReq);
//        Menu menu = menuRepository.findById(menuItem.getIdMenu()).orElse(null);
//
//        if (menu != null && !menu.getMenuItems().isEmpty()) {
//            menu.getMenuItems().add(menuItem);
//            menuRepository.save(menu);
//        }
//        return menuItem;

        Map<String, Object> menuItemData = new HashMap<>();
        menuItemData.put("pages", menuItemsRepository.findAllByMenuId(menuItemReq.getIdMenu(), PageRequest.of(0, 5)));
        menuItemData.put("menuItem", menuItemsRepository.save(menuItemReq));

        return menuItemData;
    }

    public Map<String, Object> updateMenuItems(Long id, MenuItems menuItemsReq) {
        MenuItems menuItems = getMenuItem(id);

        if (menuItems == null) {
            throw new ResourceNotFoundException("No se encontró el item de menu con id " + id);
        }

        menuItems.setIdMenu(menuItemsReq.getIdMenu());
        menuItems.setNameMenuItems(menuItemsReq.getNameMenuItems());
        menuItems.setUriMenuItems(menuItemsReq.getUriMenuItems());
        menuItems.setDescriptionMenuItems(menuItemsReq.getDescriptionMenuItems());
        menuItems.setImageMenuItems(menuItemsReq.getImageMenuItems());
        menuItems.setType(menuItemsReq.getType());

//        return menuItemsRepository.save(menuItems);

        Map<String, Object> menuItemData = new HashMap<>();
        menuItemData.put("pages", menuItemsRepository.findAllByMenuId(id, PageRequest.of(0, 5)));
        menuItemData.put("menuItem", menuItemsRepository.save(menuItems));

        return menuItemData;

    }

    public MenuItems getMenuItem(Long id) {
        return menuItemsRepository.findById(id).orElse(null);
    }

    public MenuItemsDTO getMenuItemDto(Long id) {
        return menuItemsMapper.toDto(getMenuItem(id));
    }

    public MenuItems deleteMenuItem(Long id) {
        MenuItems menuItems = menuItemsRepository.findById(id).orElse(null);

        if (menuItems == null) {
            throw new ResourceNotFoundException("No se encontró el menu item con ID " + id);
        }
        Menu menu = menuRepository.findById(menuItems.getIdMenu()).orElse(null);
        if (menu != null && !menu.getMenuItems().isEmpty()) {
            menu.getMenuItems().remove(menuItems);
            menuRepository.save(menu);

        }

        menuItems.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
        return menuItemsRepository.save(menuItems);
    }
}
