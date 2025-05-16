package sv.gov.cnr.cnrpos.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sv.gov.cnr.cnrpos.entities.*;
import sv.gov.cnr.cnrpos.models.ClienteResponse;
import sv.gov.cnr.cnrpos.models.ProductoResponse;
import sv.gov.cnr.cnrpos.models.dto.DocumentoDTO;
import sv.gov.cnr.cnrpos.models.dto.TransaccionDTO;
import sv.gov.cnr.cnrpos.repositories.ClienteRepository;
import sv.gov.cnr.cnrpos.repositories.ProductoRepository;
import sv.gov.cnr.cnrpos.services.CompanyService;
import sv.gov.cnr.cnrpos.services.DocumentoAsociadoService;
import sv.gov.cnr.cnrpos.services.RcatalogoService;
import sv.gov.cnr.cnrpos.services.SucursalService;
import sv.gov.cnr.cnrpos.services.TransaccionService;
import sv.gov.cnr.cnrpos.utils.Utils;
import sv.gov.cnr.cnrpos.models.security.User;
import sv.gov.cnr.cnrpos.security.AuthenticationService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@RequestMapping(path = "/api/v1/transacciones")
public class TransaccionController {

    private final RcatalogoService rcatalogoService;
    private final Utils utils;
    private final TransaccionService transaccionService;
    private final SucursalService sucursalService;
    private final CompanyService companyService;
    private final AuthenticationService authenticationService;
    private final DocumentoAsociadoService documentoAsociadoService;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final RestTemplate restTemplate;


    public TransaccionController(
            RcatalogoService rcatalogoService,
            TransaccionService transaccionService,
            SucursalService sucursalService,
            CompanyService companyService,
            AuthenticationService authenticationService,
            RestTemplate restTemplate,
            DocumentoAsociadoService documentoAsociadoService,
            ClienteRepository clienteRepository,
            ProductoRepository productoRepository,
            Utils utils
    ) {
        this.rcatalogoService = rcatalogoService;
        this.transaccionService = transaccionService;
        this.sucursalService = sucursalService;
        this.companyService = companyService;
        this.authenticationService = authenticationService;
        this.documentoAsociadoService = documentoAsociadoService;
        this.restTemplate = restTemplate;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.utils = utils; // Ahora utils se inyecta correctamente
    }

    @PostMapping
    public ResponseEntity<String> crear(@RequestBody @Valid TransaccionDTO transaccionDTO) {
        transaccionService.crearTransaccion(transaccionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Transaccion Creada");
    }

    @PostMapping("/crearTransaccion")
    public Object crearTransaccion(@RequestBody @Valid TransaccionDTO transaccionDTO, @RequestParam(defaultValue = "0", name = "page") int page, @RequestParam(defaultValue = "10", name = "size") int size, @RequestParam(defaultValue = "", name = "sortBy") String sortBy, @RequestParam(required = false, defaultValue = "", name = "filterBy") String filterBy) {

        try {
            return utils.jsonResponse(200, "Transacción Agregada con éxito", transaccionService.crearTransaccion(transaccionDTO));
        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se pudo agregar la Transacción", ex.getMessage());
        }
    }

    @GetMapping(path = "/index")
    public Object ventasIndex(@RequestParam(defaultValue = "0", name = "page") int page, @RequestParam(defaultValue = "10", name = "size") int size, @RequestParam(defaultValue = "", name = "sortBy") String sortBy, @RequestParam(required = false, defaultValue = "", name = "filterBy") String filterBy) {

        User usuario = (authenticationService.loggedUser());

        List<Sucursal> sucursals = sucursalService.getAllSucursales();
        List<Company> companies = companyService.getAllCompanies();
        // Filtrar las compañías cuyo deletedAt sea nulo o vacío
        List<Company> activeCompanies = companies.stream()
                .filter(company -> company.getDeletedAt() == null
                        || company.getDeletedAt().toLocalDateTime().toString().isEmpty())
                .collect(Collectors.toList());



        // Verificar si el usuario tiene el rol "root"
        boolean esRoot = usuario.getRoles().stream()
                .anyMatch(rol -> "Root".equals(rol.getNameRole()));

        // Obtener las transacciones basadas en el rol del usuario

        /**
         * Se agrago la paginacion y ordenamiento
         */
        Page<Transaccion> transaccions = esRoot
                ? transaccionService.getTransaccionesRootPage(page, size, filterBy, sortBy)
                : transaccionService.getTransaccionesByUserPage(usuario, page, size, filterBy, sortBy);

        List<Map<String, Object>> sucursalesArray = new ArrayList<>();
        for (Sucursal sucursal : sucursals) {
            if (sucursal.getDeletedAt() == null || sucursal.getDeletedAt().toLocalDateTime().toString().isEmpty()) {
                Map<String, Object> sucursalMap = new HashMap<>();
                sucursalMap.put("nombre", sucursal.getNombre());
                sucursalMap.put("email", sucursal.getEmail());
                sucursalMap.put("direccion", sucursal.getDireccion());
                sucursalMap.put("telefono", sucursal.getTelefono());
                sucursalMap.put("idMuniBranch", sucursal.getIdMuniBranch());
                sucursalMap.put("idCompany", sucursal.getIdCompany());
                sucursalMap.put("nombreMunicipioBranch", sucursal.getNombreMunicipioBranch());
                sucursalMap.put("idSucursal", sucursal.getIdSucursal());
                sucursalMap.put("codigoSucursal", sucursal.getCodigoSucursal());
                sucursalMap.put("idTipoEstablecimiento", sucursal.getIdTipoEstablecimiento());

                // Obteniendo el nombre de la empresa
                String nombreEmpresa = "";
                for (Company company : companies) {
                    if (company.getIdCompany().equals(sucursal.getIdCompany())) {
                        nombreEmpresa = company.getNameCompany();
                        break;
                    }
                }
                sucursalMap.put("nombreCompany", nombreEmpresa);

                sucursalesArray.add(sucursalMap);
            }
        }
        // APIS CNR
        List<ClienteResponse> entidades = obtenerEntidadesCNR();
        List<ProductoResponse> items = obtenerItemsCNR();

        // AMBIENTE LOCAL
        // List<Map<String, Object>> entidades = obtenerEntidades();
        //List<Map<String, Object>> entidades = obtenerEntidades();
        //List<Map<String, Object>> items = obtenerItems();

        Map<String, Object> catalogos = new HashMap<>();

        catalogos = obtenerCatalogos();


        List<Long> idsDocumentosAsociados = documentoAsociadoService.getAllTransactionIdsHijas();

        Map<String, Object> data = new HashMap<>();
        data.put("companies", !companies.isEmpty() ? companies : Collections.emptyList());
        data.put("transactions", transaccions != null ? transaccions : Collections.emptyList());
        data.put("branches", sucursalesArray);
        data.put("catalogos", catalogos);
        data.put("clientes", entidades);
        data.put("items", items);
        data.put("idsDocumentosAsociados",idsDocumentosAsociados);

        return utils.jsonResponse(200, "Index de Transacciones", data);
    }

    @GetMapping(path = "/ventasCCF/{nit}")
    public Object ventasCCF(@PathVariable String nit) {
        List<String> tiposDTE = Arrays.asList("03");

        List<Transaccion> transaccions = transaccionService.getAllVentasByIdCliente(tiposDTE, nit);
        Map<String, Object> catalogos;
        catalogos = obtenerCatalogos();
        transaccions.forEach(transaccion -> {
            transaccion.getItems().forEach(item -> {
                item.setNroDocumento(transaccion.getCodigoGeneracion());
            });
        });
        Map<String, Object> data = new HashMap<>();
        data.put("transactions", !transaccions.isEmpty() ? transaccions : Collections.emptyList());
        data.put("catalogos", catalogos);
        return utils.jsonResponse(200, "Index de Ventas CCF", data);
    }

    @GetMapping(path = "/ventasFCF/{nit}")
    public Object ventasFCF(@PathVariable String nit) {
        List<String> tiposDTE = Arrays.asList("03");

        List<Transaccion> transaccions = transaccionService.getAllVentasByIdCliente(tiposDTE, nit);
        Map<String, Object> catalogos = new HashMap<>();
        catalogos = obtenerCatalogos();
        Map<String, Object> data = new HashMap<>();
        transaccions.forEach(transaccion -> {
            transaccion.getItems().forEach(item -> {
                item.setNroDocumento(transaccion.getCodigoGeneracion());
            });
        });

        data.put("transactions", !transaccions.isEmpty() ? transaccions : Collections.emptyList());
        data.put("catalogos", catalogos);
        return utils.jsonResponse(200, "Index de Ventas FCF", data);
    }

    @GetMapping(path = "/ventas/")
    public Object ventas() {
        List<String> tiposDTE = Arrays.asList("03", "04", "45");
        // String nitCliente = "123456789"; // Reemplaza con el NIT del cliente que
        // deseas consultar

        List<Transaccion> transacciones = transaccionService.getAllVentasByTipoDTE(tiposDTE);
        Map<String, Object> catalogos = obtenerCatalogos();

        transacciones.forEach(transaccion -> {
            transaccion.getItems().forEach(item -> {
                item.setNroDocumento(transaccion.getCodigoGeneracion());
            });
        });

        Map<String, Object> data = new HashMap<>();
        data.put("transactions", !transacciones.isEmpty() ? transacciones : Collections.emptyList());
        data.put("catalogos", catalogos);

        return utils.jsonResponse(200, "Index de Ventas CCF", data);
    }


    @GetMapping(path = "/ventas/iva-retenido/")
    public Object ventasConIVARetenido() {
        List<String> tiposDTE = Arrays.asList("14");
        // List<String> tiposDTE = Arrays.asList("03", "14", "01");
        // String nitCliente = "123456789"; // Reemplaza con el NIT del cliente que
        // deseas consultar

        List<Transaccion> transacciones = transaccionService.getAllVentasByTipoDTE(tiposDTE);
        Map<String, Object> catalogos = obtenerCatalogos();

        transacciones.forEach(transaccion -> {
            transaccion.getItems().forEach(item -> {
                item.setNroDocumento(transaccion.getCodigoGeneracion());
            });
        });

        Map<String, Object> data = new HashMap<>();
        data.put("transactions", transacciones);
        data.put("catalogos", catalogos);

        return utils.jsonResponse(200, "Index de Ventas CCF", data);
    }


    // @GetMapping(path = "/ventas/iva-retenido/{nit}")
    // public Object ventasConIVARetenido(@PathVariable String nit) {

    //     List<String> tiposDTE = Arrays.asList("03", "04", "45");


    //     List<Transaccion> transacciones = transaccionService.getTransaccionesConIVARetenido(tiposDTE, nit);
    //     Map<String, Object> catalogos = obtenerCatalogos();

    //     transacciones.forEach(transaccion -> {
    //         transaccion.getItems().forEach(item -> {
    //             item.setNroDocumento(transaccion.getCodigoGeneracion());
    //         });
    //     });

    //     Map<String, Object> data = new HashMap<>();
    //     data.put("transactions", transacciones != null ? transacciones : Collections.emptyList());
    //     data.put("catalogos", catalogos);

    //     return utils.jsonResponse(200, "Index de Ventas con IVA Retenido", data);
    // }

    @GetMapping(path = "/documentos/nitCliente/{nitCliente}")
    public ResponseEntity<Object> getDocumentosNotas(@PathVariable String nitCliente) {
        try {
            List<DocumentoDTO> documentos = transaccionService.getDocumentosNotaCredito(nitCliente);
            return Utils.jsonResponse(200, "Petición realizada exitosamente", documentos);
        } catch (Exception e) {
            return Utils.jsonResponse(500, "Error: " + e.getMessage(), null);
        }
    }

    public Map<String, Object> obtenerCatalogos() {
        Object municipios = rcatalogoService.getMunicipios();
        Object departamentos = rcatalogoService.getDepartamentos();
        Object tipos_establecimientos = rcatalogoService.getRCatalogosAsList("TIPO_ESTABLECIMIENTO");
        Object tipo_rango = rcatalogoService.getRCatalogosAsList("TIPO_DOCUMENTO");

        User usuario = (authenticationService.loggedUser());

        Long departamento = sucursalService.getSucursal(usuario.getIdBranch()).getIdDeptoBranch();

        Object formaDePago = null;
        if (departamento == 80) {
            formaDePago = rcatalogoService.getRCatalogosAsList("FORMA_DE_PAGO");
        } else {
            formaDePago = rcatalogoService.getRCatalogosAsList1(464L);
        }


        Object tipoDocumento = rcatalogoService.getRCatalogosAsList("TIPO_DOCUMENTO");
        Object regimens = rcatalogoService.getRCatalogosAsList("REGIMEN");
        Object giros = rcatalogoService.getRCatalogosAsList("ACTIVIDAD_ECONOMICA");
        Object recintos = rcatalogoService.getRCatalogosAsList("RECINTO_FISCAL");
        Object tipoIdentificacion = rcatalogoService.getRCatalogosAsList("TIPO_DOCUMENTO_IDENTIFICACION");
        Object tipoAnulacion = rcatalogoService.getRCatalogosAsList("TIPO_INVALIDACION");

        Map<String, Object> catalogos = new HashMap<>();
        catalogos.put("municipios", municipios != null ? municipios : Collections.emptyList());
        catalogos.put("departamentos", departamentos != null ? departamentos : Collections.emptyList());
        catalogos.put("tipos_establecimientos",
                tipos_establecimientos != null ? tipos_establecimientos : Collections.emptyList());
        catalogos.put("tipo_rango", tipo_rango != null ? tipo_rango : Collections.emptyList());
        catalogos.put("formaDePago", formaDePago != null ? formaDePago : Collections.emptyList());
        catalogos.put("tipoDocumento", tipoDocumento != null ? tipoDocumento : Collections.emptyList());
        catalogos.put("tipoAnulacion", tipoAnulacion != null ? tipoAnulacion : Collections.emptyList());
        catalogos.put("regimens", regimens != null ? regimens : Collections.emptyList());
        catalogos.put("actividad_economica", giros != null ? giros : Collections.emptyList());
        catalogos.put("recintos", recintos != null ? recintos : Collections.emptyList());
        catalogos.put("tipoIdentificacion", tipoIdentificacion != null ? tipoIdentificacion : Collections.emptyList());

        return catalogos;

    }

    public List<Map<String, Object>> obtenerEntidades() {
        List<Map<String, Object>> entidades = new ArrayList<>();

        // Entidad 1
        Map<String, Object> entidad1 = new HashMap<>();
        entidad1.put("id", "1");
        entidad1.put("name", "ADMINISTRACION NACIONAL DE ACUEDUCTOS Y ALCANTARILLADOS");
        entidad1.put("nombreConcatenado", "ADMINISTRACION NACIONAL DE ACUEDUCTOS Y ALCANTARILLADOS");
        entidad1.put("tipo_documento", 36);
        entidad1.put("nit", "06142101230059");
        entidad1.put("nrc", "328049");
        entidad1.put("direccion", "COL. LIBERTAD  AV. DON BOSCO ED. ANDA");
        entidad1.put("actividad_codigo", "36000");
        entidad1.put("actividad_nombre", "Captación, tratamiento y suministro de agua");
        entidad1.put("departamento_codigo", "06");
        entidad1.put("departamento_nombre", "SAN SALVADOR");
        entidad1.put("municipio_codigo", "14");
        entidad1.put("municipio_nombre", "SAN SALVADOR");
        entidad1.put("correo", "gonzalez.nelson@globalsolutionslt.com");
        entidad1.put("telefono", "77778888");
        entidad1.put("descuento", 0);
        entidad1.put("aplica_retencion", "S");
        entidades.add(entidad1);

        // Entidad 3
        Map<String, Object> entidad3 = new HashMap<>();
        entidad3.put("id", "3");
        entidad3.put("name", "COMISION EJECUTIVA PORTUARIA AUTONOMA");
        entidad3.put("nombreConcatenado", "COMISION EJECUTIVA PORTUARIA AUTONOMA");
        entidad3.put("tipo_documento", 36);
        entidad3.put("nit", "06141402370078");
        entidad3.put("nrc", "2437");
        entidad3.put("direccion", "BLVD. LOS HEROES ED. T.ROBLE");
        entidad3.put("actividad_codigo", "52219");
        entidad3.put("actividad_nombre", "Servicios para el transporte por vía terrestre n.c.p.");
        entidad3.put("departamento_codigo", "06");
        entidad3.put("departamento_nombre", "SAN SALVADOR");
        entidad3.put("municipio_codigo", "14");
        entidad3.put("municipio_nombre", "SAN SALVADOR");
        entidad3.put("correo", "gonzalez.nelson@globalsolutionslt.com");
        entidad3.put("telefono", "77778888");
        entidad3.put("descuento", 0);
        entidad3.put("aplica_retencion", "N");
        entidades.add(entidad3);

        // Entidad 7
        Map<String, Object> entidad7 = new HashMap<>();
        entidad7.put("id", "7");
        entidad7.put("name", "SARBELIO JOSE VAQUERANO RAMIREZ");
        entidad7.put("nombreConcatenado", "SARBELIO JOSE VAQUERANO RAMIREZ");
        entidad7.put("tipo_documento", 36);
        entidad7.put("nit", "06140508801309");
        entidad7.put("dui", "055555555");
        entidad7.put("nrc", "2526818");
        entidad7.put("direccion", "PJE. 11 COL. S. BENITO N°73");
        entidad7.put("actividad_codigo", "69100");
        entidad7.put("actividad_nombre", "Actividades jurídicas");
        entidad7.put("departamento_codigo", "06");
        entidad7.put("departamento_nombre", "SAN SALVADOR");
        entidad7.put("municipio_codigo", "14");
        entidad7.put("municipio_nombre", "SAN SALVADOR");
        entidad7.put("correo", "gonzalez.nelson@globalsolutionslt.com");
        entidad7.put("telefono", "77777777");
        entidad7.put("descuento", null);
        entidad7.put("aplica_retencion", "N");
        entidades.add(entidad7);

        Map<String, Object> entidad8 = new HashMap<>();
        entidad8.put("id", "8");
        entidad8.put("name", "ALCALDIA MUNICIPAL DE EL PAISNAL");
        entidad8.put("nombreConcatenado", "ALCALDIA MUNICIPAL DE EL PAISNAL");
        entidad8.put("tipo_documento", 36);
        entidad8.put("nit", "06050610790019");
        entidad8.put("dui", "989898989");
        entidad8.put("nrc", "1691200");
        entidad8.put("direccion", "sin direccion");
        entidad8.put("actividad_codigo", "84111");
        entidad8.put("actividad_nombre", "Alcaldías municipales");
        entidad8.put("departamento_codigo", "06");
        entidad8.put("departamento_nombre", "SAN SALVADOR");
        entidad8.put("municipio_codigo", "05");
        entidad8.put("municipio_nombre", "EL PAISNAL");
        entidad8.put("correo", "gonzalez.nelson@globalsolutionslt.com");
        entidad8.put("telefono", "77778888");
        entidad8.put("descuento", 50);
        entidad8.put("aplica_retencion", "N");
        entidades.add(entidad8);

        Map<String, Object> entidad9 = new HashMap<>();
        entidad9.put("id", "add2");
        entidad9.put("name", "USUARIO GENERAL");
        entidad9.put("nombreConcatenado", "USUARIO GENERAL");
        entidad9.put("tipo_documento", null);
        entidad9.put("nit", "06050610790019");
        entidad9.put("dui", "989898983");
        entidad9.put("nrc", "123123");
        entidad9.put("direccion", "Sin Dirección");
        entidad9.put("actividad_codigo", null);
        entidad9.put("actividad_nombre", null);
        entidad9.put("departamento_codigo", "06");
        entidad9.put("departamento_nombre", "SAN SALVADOR");
        entidad9.put("municipio_codigo", "14");
        entidad9.put("municipio_nombre", "SAN SALVADOR");
        entidad9.put("correo", "gonzalez.nelson@globalsolutionslt.com");
        entidad9.put("telefono", "99999999");
        entidad9.put("descuento", null);
        entidad9.put("aplica_retencion", "S");
        entidades.add(entidad9);

        return entidades;
    }

    //Obtener lista de clientes:
    public List<ClienteResponse> obtenerEntidadesCNR() {
        List<ClienteResponse> clientes = new ArrayList<>();

        // Crear y agregar un cliente extra
        ClienteResponse clienteExtra = new ClienteResponse();
        clienteExtra.setName("Cliente Extra");
        clienteExtra.setTipo_documento(0);
        clienteExtra.setNit("00000000000000");
        clienteExtra.setDui(null);
        clienteExtra.setNrc("00000");
        clienteExtra.setDireccion("Dirección Extra");
        clienteExtra.setActividad_codigo("00000");
        clienteExtra.setActividad_nombre("OTROS");
        clienteExtra.setDepartamento_codigo("80");
        clienteExtra.setDepartamento_nombre("SAN SALVADOR");
        clienteExtra.setMunicipio_codigo("199");
        clienteExtra.setMunicipio_nombre("SAN SALVADOR");
        clienteExtra.setCorreo("extra@cliente.com");
        clienteExtra.setTelefono("00000000");
        clienteExtra.setDescuento(null);
        clienteExtra.setContribuyente("Extra");

        clienteExtra.concatenarCampos();
        clientes.add(clienteExtra);

        // Obtener clientes de la base de datos y convertirlos a ClienteResponse
        List<Cliente> clientesBD = clienteRepository.findAll();
        for (Cliente cliente : clientesBD) {
            ClienteResponse response = convertirAClienteResponse(cliente);
            response.concatenarCampos();
            clientes.add(response);
        }

        return clientes;
    }

    private ClienteResponse convertirAClienteResponse(Cliente cliente) {
        ClienteResponse response = new ClienteResponse();
        response.setName(cliente.getNombre()); // <-- Aquí está el cambio
        response.setTipo_documento(Integer.parseInt(cliente.getTipoDocumento())); // Convertimos String a Integer
        response.setNit(cliente.getNit());
        response.setDui(cliente.getNumeroDocumento());
        response.setNrc(cliente.getNrc());
        response.setDireccion(cliente.getDireccion());
        response.setActividad_codigo(cliente.getActividadEconomica()); // Ajustar si es necesario
        response.setActividad_nombre("Actividad no especificada"); // No existe en la entidad Cliente
        response.setDepartamento_codigo(cliente.getDepartamento()); // Asumiendo que el código del depto está en "departamento"
        response.setDepartamento_nombre(cliente.getDepartamento()); // Si necesitas el nombre, ajusta aquí
        response.setMunicipio_codigo(cliente.getMunicipio()); // Asumiendo que el código del municipio está en "municipio"
        response.setMunicipio_nombre(cliente.getMunicipio()); // Si necesitas el nombre, ajusta aquí
        response.setCorreo(cliente.getEmail());
        response.setTelefono(cliente.getTelefono());
        response.setDescuento(cliente.getPorcentajeDescuento() != null ? String.valueOf(cliente.getPorcentajeDescuento()) : null);
        response.setContribuyente(cliente.getDescripcionDescuento()); // Ajusta si es necesario
        return response;
    }


    public List<Map<String, Object>> obtenerItems() {
        List<Map<String, Object>> items = new ArrayList<>();

        // Item 1
        Map<String, Object> item1 = new HashMap<>();
        item1.put("cod_id", 1);
        item1.put("clasificacion", "Mapas de la República de El Salvador");
        item1.put("codigo_producto", "1-003");
        item1.put("nombre", "Mapa Oficial de la República de El Salvador");
        item1.put("descripcion", "Esc. 1:500.000");
        item1.put("codigo_ingreso", "14101");
        item1.put("precio", 3);
        item1.put("iva", 0);
        item1.put("tipo", "E");
        item1.put("total", 3);
        item1.put("estado", "A");
        item1.put("editable", "N");
        items.add(item1);

        // Item 4
        Map<String, Object> item4 = new HashMap<>();
        item4.put("cod_id", 4);
        item4.put("clasificacion", "Mapas de la República de El Salvador");
        item4.put("codigo_producto", "1-023");
        item4.put("nombre", "Mapa Turistico del municipio de La Libertad");
        item4.put("descripcion", "Mapa Turistico");
        item4.put("codigo_ingreso", "14101");
        item4.put("precio", 2);
        item4.put("iva", 0);
        item4.put("tipo", "E");
        item4.put("total", 2);
        item4.put("estado", "A");
        item4.put("editable", "N");
        items.add(item4);

        // Item 3
        Map<String, Object> item2 = new HashMap<>();
        item2.put("cod_id", 3);
        item2.put("clasificacion", "Mapas de la República de El Salvador");
        item2.put("codigo_producto", "1-026");
        item2.put("nombre", "Mapa Turistico de El Salvador 4a. edición");
        item2.put("descripcion", "Mapa Turistico de El Salvador");
        item2.put("codigo_ingreso", "12104");
        item2.put("precio", 2.04);
        item2.put("iva", 0.26);
        item2.put("tipo", "G");
        item2.put("total", 2.3);
        item2.put("estado", "A");
        item2.put("editable", "N");
        items.add(item2);

        Map<String, Object> item8 = new HashMap<>();
        item8.put("cod_id", 34);
        item8.put("clasificacion", "Servicios Diversos");
        item8.put("codigo_producto", "29-026");
        item8.put("nombre", "Certificación de Solvencia");
        item8.put("descripcion", "Certificación de Solvencia");
        item8.put("codigo_ingreso", "14299");
        item8.put("precio", 0);
        item8.put("iva", 0);
        item8.put("tipo", "G");
        item8.put("total", 0);
        item8.put("estado", "A");
        item8.put("editable", "S");
        items.add(item8);

        // Item 3
        Map<String, Object> item3 = new HashMap<>();
        item3.put("cod_id", 30);
        item3.put("clasificacion", "Servicios Diversos");
        item3.put("codigo_producto", "29-024");
        item3.put("nombre", "Tramite de Tarifa");
        item3.put("descripcion", "Tramite de Tarifa");
        item3.put("codigo_ingreso", "14299");
        item3.put("precio", 0);
        item3.put("iva", 0);
        item3.put("tipo", "G");
        item3.put("total", 0);
        item3.put("estado", "A");
        item3.put("editable", "S");
        items.add(item3);

        Map<String, Object> item11 = new HashMap<>();
        item11.put("cod_id", 11);
        item11.put("clasificacion", "Mapas de la República de El Salvador");
        item11.put("codigo_producto", "1-006");
        item11.put("nombre", "Mapa de El Salvador(ploteado)");
        item11.put("descripcion", "Esc.1.200.000");
        item11.put("codigo_ingreso", "12104");
        item11.put("precio", 32);
        item11.put("iva", 4.16);
        item11.put("tipo", "G");
        item11.put("total", 36.16);
        item11.put("estado", "I");
        item11.put("editable", "N");
        items.add(item11);

        return items;
    }

    //Obtener lista de productos
//    public List<ProductoResponse> obtenerItemsCNR() {
//        List<ProductoResponse> items = new ArrayList<>();
//
//        try {
//            ResponseEntity<List<ProductoResponse>> responseEntity = restTemplate.exchange(
//                    urlProductos,
//                    HttpMethod.GET, null, new ParameterizedTypeReference<List<ProductoResponse>>() {
//                    });
//
//            // Si la respuesta no es null, asignar a items
//            if (responseEntity.getBody() != null) {
//                items = responseEntity.getBody();
//            }
//        } catch (RestClientException e) {
//            // Manejar la excepción (por ejemplo, loguear el error)
//            System.err.println("Error al obtener items CNR: " + e.getMessage());
//            // Aquí podrías agregar más lógica de manejo de errores si es necesario
//        }
//
//        // Devolver la lista de items (vacía si hubo un error)
//        return items;
//    }


    //Obtener lista de productos
    public List<ProductoResponse> obtenerItemsCNR() {
        return productoRepository.findAll()
                .stream()
                .map(this::convertirAProductoResponse)
                .collect(Collectors.toList());
    }

    private ProductoResponse convertirAProductoResponse(Producto producto) {
        return ProductoResponse.builder()
                .idProducto(producto.getIdProducto()) // ⚠️ Se cambió de getIdProducto() a getCod_id()
                .clasificacion(producto.getClasificacion())
                .codigo_producto(producto.getCodigo_producto()) // ⚠️ Se cambió de getCodigoProducto() a getCodigo_producto()
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .codigo_ingreso(producto.getCodigo_ingreso()) // ⚠️ Se cambió de getCodigoIngreso() a getCodigo_ingreso()
                .precio(producto.getPrecio())
                .iva(producto.getIva())
                .tipo(producto.getTipo())
                .total(producto.getTotal())
                .estado(producto.getEstado())
                .editable(producto.getEditable())
                .createdAt(producto.getCreatedAt())
                .updatedAt(producto.getUpdatedAt())
                .deletedAt(producto.getDeletedAt())
                .build();
    }






}
