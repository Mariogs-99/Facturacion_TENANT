package sv.gov.cnr.cnrpos.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.gov.cnr.cnrpos.entities.Cliente;
import sv.gov.cnr.cnrpos.models.ClienteCNR;
import sv.gov.cnr.cnrpos.services.ClienteService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // ✅ Obtener todos los clientes
    @GetMapping
    public List<Cliente> obtenerClientes() {
        return clienteService.obtenerTodosLosClientes();
    }

    // ✅ Obtener clientes paginados
    @GetMapping("/page")
    public ResponseEntity<Map<String, Object>> listarClientesPaginados(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Page<Cliente> clientes = clienteService.obtenerClientesPaginados(page, size);
        Map<String, Object> response = new HashMap<>();
        response.put("pages", clientes);
        return ResponseEntity.ok(response);
    }

    // ✅ Obtener un cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerClientePorId(@PathVariable Long id) {
        Cliente cliente = clienteService.obtenerClientePorId(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return ResponseEntity.ok(cliente);
    }

    // ✅ Guardar un cliente con ClienteCNR
    @PostMapping
    public ResponseEntity<Cliente> guardarCliente(@RequestBody ClienteCNR clienteCNR) {
        Cliente nuevoCliente = clienteService.guardarCliente(clienteCNR);
        return ResponseEntity.ok(nuevoCliente);
    }

    // ✅ Actualizar un cliente con ClienteCNR
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long id, @RequestBody ClienteCNR clienteCNR) {
        Cliente clienteActualizado = clienteService.actualizarCliente(id, clienteCNR)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return ResponseEntity.ok(clienteActualizado);
    }

    // ✅ Eliminar un cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        boolean eliminado = clienteService.eliminarCliente(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
