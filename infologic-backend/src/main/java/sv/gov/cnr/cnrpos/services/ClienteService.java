package sv.gov.cnr.cnrpos.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sv.gov.cnr.cnrpos.entities.Cliente;
import sv.gov.cnr.cnrpos.entities.RCatalogos;
import sv.gov.cnr.cnrpos.models.ClienteCNR;
import sv.gov.cnr.cnrpos.repositories.ClienteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final RcatalogoService rcatalogoService; // Se agrega el servicio de catálogos

    public ClienteService(ClienteRepository clienteRepository, RcatalogoService rcatalogoService) {
        this.clienteRepository = clienteRepository;
        this.rcatalogoService = rcatalogoService;
    }

    // ✅ Obtener todos los clientes
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    // ✅ Obtener un cliente por ID
    public Optional<Cliente> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    // ✅ Actualizar un cliente con ClienteCNR
    public Optional<Cliente> actualizarCliente(Long id, ClienteCNR clienteCNR) {
        return clienteRepository.findById(id).map(existingCliente -> {
            Cliente clienteActualizado = convertirClienteCNRaCliente(clienteCNR);
            clienteActualizado.setIdCliente(existingCliente.getIdCliente()); // Mantener ID
            return clienteRepository.save(clienteActualizado);
        });
    }

    // ✅ Eliminar un cliente
    public boolean eliminarCliente(Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // ✅ Obtener clientes paginados
    public Page<Cliente> obtenerClientesPaginados(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("idCliente").ascending());
        return clienteRepository.findAll(pageable);
    }

    // ✅ Guardar un cliente con ClienteCNR
    public Cliente guardarCliente(ClienteCNR clienteCNR) {
        Cliente nuevoCliente = convertirClienteCNRaCliente(clienteCNR);
        return clienteRepository.save(nuevoCliente);
    }

    // ✅ Conversión de ClienteCNR a Cliente con conversión de actividad económica
    private Cliente convertirClienteCNRaCliente(ClienteCNR clienteCNR) {
        String actividadEconomicaCodigo = null;

        // 🔍 Convertir la actividad económica a su ID_MH si está disponible en el catálogo
        if (clienteCNR.getActividadEconomica() != null && !clienteCNR.getActividadEconomica().isEmpty()) {
            Optional<RCatalogos> catalogo = rcatalogoService.getRCatalogoByValor("ACTIVIDAD_ECONOMICA", clienteCNR.getActividadEconomica());
            actividadEconomicaCodigo = catalogo.map(RCatalogos::getIdMh).orElse(null);
        }

        // Limpiar NIT
        String nitLimpio = clienteCNR.getNit() != null ? clienteCNR.getNit().replaceAll("[^0-9]", "") : null;

        return Cliente.builder()
                .nombre(clienteCNR.getNombre())
                .nombreComercial(clienteCNR.getNombreComercial())
                .tipoDocumento(clienteCNR.getTipoDocumento().getCodigo())
                .numeroDocumento(clienteCNR.getNumeroDocumento())
                .pais(clienteCNR.getPais())
                .departamento(clienteCNR.getDepartamento())
                .municipio(clienteCNR.getMunicipio())
                .direccion(clienteCNR.getDireccion())
                .colonia(clienteCNR.getColonia())
                .calle(clienteCNR.getCalle())
                .apartamentoLocal(clienteCNR.getApartamentoLocal())
                .numeroCasa(clienteCNR.getNumeroCasa())
                .telefono(clienteCNR.getTelefono())
                .actividadEconomica(actividadEconomicaCodigo) // Se almacena el código en lugar del nombre
                .email(clienteCNR.getEmail())
                .nit(nitLimpio)
                .nrc(clienteCNR.getNrc())
                .porcentajeDescuento(clienteCNR.getPorcentajeDescuento())
                .descripcionDescuento(clienteCNR.getDescripcionDescuento())
                .esExtranjero(clienteCNR.getEsExtranjero())
                .esGranContribuyente(clienteCNR.getEsGranContribuyente())
                .esGobierno(clienteCNR.getEsGobierno())
                .esConsumidorFinal(clienteCNR.getEsConsumidorFinal())
                .build();
    }
}
