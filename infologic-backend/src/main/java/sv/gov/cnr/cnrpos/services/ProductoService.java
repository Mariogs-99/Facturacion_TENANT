package sv.gov.cnr.cnrpos.services;

import org.springframework.stereotype.Service;
import sv.gov.cnr.cnrpos.entities.Producto;
import sv.gov.cnr.cnrpos.repositories.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // ✅ Obtener todos los productos
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    // ✅ Obtener un producto por ID
    public Optional<Producto> obtenerProductoPorId(Integer id) {
        return productoRepository.findById(id);
    }

    // ✅ Guardar un producto
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    // ✅ Actualizar un producto
    public Optional<Producto> actualizarProducto(Integer id, Producto producto) {
        return productoRepository.findById(id).map(existingProduct -> {
            existingProduct.setClasificacion(producto.getClasificacion());
            existingProduct.setCodigo_producto(producto.getCodigo_producto());
            existingProduct.setNombre(producto.getNombre());
            existingProduct.setDescripcion(producto.getDescripcion());
            existingProduct.setCodigo_ingreso(producto.getCodigo_ingreso());
            existingProduct.setPrecio(producto.getPrecio());
            existingProduct.setIva(producto.getIva());
            existingProduct.setTipo(producto.getTipo());
            existingProduct.setTotal(producto.getTotal());
            existingProduct.setEstado(producto.getEstado());
            existingProduct.setEditable(producto.getEditable());
            return productoRepository.save(existingProduct);
        });
    }

    // ✅ Eliminar un producto
    public boolean eliminarProducto(Integer id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Page<Producto> obtenerProductosPaginados(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("idProducto").ascending());
        return productoRepository.findAll(pageable);
    }



}
