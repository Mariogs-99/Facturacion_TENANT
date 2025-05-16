package sv.gov.cnr.cnrpos.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sv.gov.cnr.cnrpos.entities.Item;
import sv.gov.cnr.cnrpos.repositories.ItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public List<Item> itemsByDte(String codigoGeneracion){
        List<Item> itemsUsados = itemRepository.itemsUsadosNotas(codigoGeneracion);
        List<Item> itemsDte = itemRepository.itemsByDte(codigoGeneracion);
        itemsDte.forEach(itemDte -> {
            Optional<Item> matchingItemUsadoOptional = itemsUsados.stream()
                    .filter(itemUsado -> itemUsado.getCodigoProducto().equals(itemDte.getCodigoProducto()))
                    .findFirst();
            matchingItemUsadoOptional.ifPresent(matchingItemUsado -> {
                int adjustedCantidad = itemDte.getCantidad() - matchingItemUsado.getCantidad();
                itemDte.setCantidad(Math.max(0, adjustedCantidad));
            });
        });
        return itemsDte;
    }

    public List<Item> itemsByMultipleDtes(List<String> codigosGeneracion) {
        // Obtener todos los ítems para los códigos de generación dados
        List<Item> allItems = itemRepository.itemsByDtes(codigosGeneracion);

        // Obtener ítems usados para cada código de generación
        Map<String, List<Item>> itemsUsadosPorCodigo = codigosGeneracion.stream()
                .collect(Collectors.toMap(
                        codigo -> codigo,
                        codigo -> itemRepository.itemsUsadosNotas(codigo)
                ));

        // Remover cantidad de ítems usados para cada código de generación
        allItems.forEach(item -> {
            List<Item> itemsUsados = itemsUsadosPorCodigo.get(item.getNroDocumento());
            if (itemsUsados != null) {
                itemsUsados.forEach(itemUsado -> {
                    if (itemUsado.getCodigoProducto().equals(item.getCodigoProducto())) {
                        int adjustedCantidad = item.getCantidad() - itemUsado.getCantidad();
                        item.setCantidad(Math.max(0, adjustedCantidad));
                    }
                });
            }
        });

        // Concatenar todas las listas de ítems en una sola lista
        List<Item> resultList = new ArrayList<>();
        itemsUsadosPorCodigo.values().forEach(resultList::addAll);
        return resultList;
    }


}
