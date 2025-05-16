package sv.gov.cnr.cnrpos.services;

import jakarta.persistence.criteria.Expression;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sv.gov.cnr.cnrpos.entities.*;
import sv.gov.cnr.cnrpos.exceptions.TransaccionException;
import sv.gov.cnr.cnrpos.models.ClienteCNR;
import sv.gov.cnr.cnrpos.models.ItemDTO;
import sv.gov.cnr.cnrpos.models.dto.*;
import sv.gov.cnr.cnrpos.models.enums.CondicionOperacion;
import sv.gov.cnr.cnrpos.models.enums.EstatusCola;
import sv.gov.cnr.cnrpos.models.enums.FormaDePago;
import sv.gov.cnr.cnrpos.models.enums.TipoDTE;
import sv.gov.cnr.cnrpos.models.security.User;
import sv.gov.cnr.cnrpos.repositories.DocumentoRepository;
import sv.gov.cnr.cnrpos.repositories.TransaccionRepository;
import sv.gov.cnr.cnrpos.security.AuthenticationService;
import sv.gov.cnr.cnrpos.utils.RequestParamParser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class TransaccionService {

    private final SucursalService sucursalService;
    private final UserService userService;
    private final ClienteService clienteService;
    private final TransaccionRepository transaccionRepository;
    private final ColaService colaService;
    private final DocumentoRepository documentoRepository;
    private static final Integer UNIDAD = 59;
    private final AuthenticationService authenticationService;
    private final RequestParamParser requestParamParser;

    //Evita que se muestren datos sensibles en produccion
    private static final Logger logger = Logger.getLogger(TransaccionService.class.getName());

    @Transactional
    public Transaccion crearTransaccion(TransaccionDTO transaccionDTO) {

        User usuario = (authenticationService.loggedUser());
        Sucursal sucursal = usuario != null ? sucursalService.obtenerSucursal(usuario.getIdBranch()).orElse(null) : null;

        Cliente nuevoCliente;
        if (transaccionDTO.getTipoDTE().getCodigo().equals("07") && transaccionDTO.getClienteCNR().getIdCliente() != null) {
            nuevoCliente = clienteService.obtenerClientePorId(transaccionDTO.getClienteCNR().getIdCliente())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        } else {
            nuevoCliente = clienteService.guardarCliente(transaccionDTO.getClienteCNR());
        }

        var listaItems = obtenerItems(transaccionDTO.getItems());
        var documentosAsociados = transaccionDTO.getDocumentosAsociados() != null ? obtenerDocumentosAsociados(transaccionDTO.getDocumentosAsociados()) : new ArrayList<DocumentoAsociado>();
        var totalSinDescuento = calcularTotalSinDescuento(listaItems);
        var ivaRetenido = Optional.ofNullable(transaccionDTO.getIvaRetenido()).orElse(BigDecimal.ZERO).setScale(8, RoundingMode.HALF_EVEN);
        var porcentajeDescuento = transaccionDTO.getClienteCNR().getPorcentajeDescuento() != null ? transaccionDTO.getClienteCNR().getPorcentajeDescuento() : 0.0;
        var montoDescuento = calcularMontoDescuento(porcentajeDescuento, totalSinDescuento);
        var totalTransaccion = (calcularTotalTransaccion(totalSinDescuento, montoDescuento));
        var listaItems2 = obtenerItems2(transaccionDTO.getItems(), BigDecimal.valueOf(0));
        List<ComprobantePago> comprobantesPago = transaccionDTO.getComprobantePagos() != null ? obtenerComprobantePagos(transaccionDTO.getComprobantePagos()) : new ArrayList<>();
        List<CuerpoDocCR> cuerpoDocCrDTO = transaccionDTO.getCuerpoDocCrDTO() != null ? obtenerCuerpoDocuCNR(transaccionDTO.getCuerpoDocCrDTO()) : new ArrayList<>();
        var nuevaTransaccion = Transaccion.builder().sucursal(sucursal).user(usuario).cliente(nuevoCliente).fechaTransaccion(new Date(transaccionDTO.getHoraTransaccion().getTime())).horaTransaccion(transaccionDTO.getHoraTransaccion()).formaDePago(transaccionDTO.getFormaDePago().getCodigo()).tipoDTE(transaccionDTO.getTipoDTE().getCodigo()).condicionOperacion(transaccionDTO.getTipoDTE().getCodigo() == "07" || transaccionDTO.getTipoDTE().getCodigo() == "14" ? CondicionOperacion.CREDITO.getCodigo() : CondicionOperacion.CONTADO.getCodigo())//al contado por defecto. No hay operaciones a credito.
                .notas(transaccionDTO.getNotas()).status(EstatusCola.PENDIENTE).totalSinDescuento(totalSinDescuento).porcentajeDescuento(nuevoCliente.getPorcentajeDescuento() != null ? nuevoCliente.getPorcentajeDescuento() : 0.0).montoDescuento(montoDescuento)//calculado a partir del porcentaje de descuento que viene en el ClienteCNR
                .descripcionDescuento(nuevoCliente.getDescripcionDescuento()).totalTransaccion(totalTransaccion)//calculado en el backend a partir de los items recibidos. La suma del total de todos los items menos el descuento
                .totalExento(transaccionDTO.getTotalExento()).totalGravado(transaccionDTO.getTotalGravado().add(transaccionDTO.getDescuentoGravado())).totalNogravado(BigDecimal.valueOf(0)).totalNosujeto(transaccionDTO.getTotalNosujeto()).totalIva(transaccionDTO.getTotalIVA()).descuentoGravado(transaccionDTO.getDescuentoGravado()).descuentoExento(transaccionDTO.getDescuentoExento()).descuentoNosujeto(transaccionDTO.getDescuentoNosujeto()).ivaRetenido(transaccionDTO.getIvaRetenido() != null ? transaccionDTO.getIvaRetenido() : BigDecimal.valueOf(0)).rentaRetenido(transaccionDTO.getRentaRetenido() != null ? transaccionDTO.getRentaRetenido() : BigDecimal.valueOf(0)).ivaPercivido(transaccionDTO.getIvaPercivido() != null ? transaccionDTO.getIvaPercivido() : BigDecimal.valueOf(0)).saldoAfavor(transaccionDTO.getSaldoAfavor()).build();
        listaItems2.forEach(item -> item.setTransaccion(nuevaTransaccion));//relacion bidireccional
        nuevaTransaccion.setItems(listaItems2);
        documentosAsociados.forEach(documentoAsociado -> documentoAsociado.setTransaccion(nuevaTransaccion));
        nuevaTransaccion.setDocumentoAsociados(documentosAsociados);
        comprobantesPago.forEach(comprobantePago -> comprobantePago.setTransaccion(nuevaTransaccion));
        nuevaTransaccion.setComprobantePagos(comprobantesPago);
        cuerpoDocCrDTO.forEach(cuerpoDocCR -> cuerpoDocCR.setTransaccion(nuevaTransaccion));
        nuevaTransaccion.setCuerpoDocCR(cuerpoDocCrDTO);
        log.info("Nueva transaccion creada {}", nuevaTransaccion);
        var transaccionCreada = transaccionRepository.save(nuevaTransaccion);
        colaService.encolarTransaccion(transaccionCreada);
        return transaccionCreada;
    }

    private List<Item> obtenerItems(List<ItemDTO> itemDTOList) {
        var listaItems = new ArrayList<Item>();
        for (var detalle : itemDTOList) {

            BigDecimal ivaItem;
            BigDecimal totalGravada = BigDecimal.valueOf(detalle.getCantidad()).multiply(BigDecimal.valueOf(detalle.getPrecioUnitario())).setScale(8, RoundingMode.HALF_EVEN);

            ivaItem = totalGravada.divide(BigDecimal.valueOf(1.13d).multiply(BigDecimal.valueOf(0.13d)), 8, // Specify the scale
                    RoundingMode.HALF_EVEN // Specify the rounding mode
            );

            var nuevoItem = Item.builder().tipoItem(detalle.getItemCNR().getTipoItem().getCodigo()).unidadMedida(UNIDAD)//Ligado a CAT-014 Unidad de Medida. Por defecto: 59 = "Unidad"
                    .nombre(detalle.getItemCNR().getNombreItem() != null && detalle.getItemCNR().getNombreItem().length() > 250 ? detalle.getItemCNR().getNombreItem().substring(0, 250) : detalle.getItemCNR().getNombreItem()).codigoIngreso(detalle.getCodigoIngreso()).codigoProducto(detalle.getCodigoProducto()).editable(detalle.getEditable()).nroDocumento(detalle.getNroDocumento()).descripcion(detalle.getItemCNR().getDescripcion()).total(calcularPrecioTotal(detalle.getCantidad(), detalle.getPrecioUnitario()))//cantidad * precioUnitario
                    .cantidad(detalle.getCantidad()).precioUnitario(BigDecimal.valueOf(detalle.getPrecioUnitario())).ivaItem(ivaItem).ventaNosujeta(detalle.getVentaNosujeta()).ventaExenta(detalle.getVentaExenta()).ventaGravada(detalle.getVentaGravada()).ventaNogravada(detalle.getVentaNogravada()).build();
            listaItems.add(nuevoItem);
        }
        return listaItems;
    }


    private List<Item> obtenerItems2(List<ItemDTO> itemDTOList, BigDecimal montoDescuento) {
        var listaItems = new ArrayList<Item>();
        for (var detalle : itemDTOList) {
            var precioTotal = calcularPrecioTotal(detalle.getCantidad(), detalle.getPrecioUnitario());
            var descuento = precioTotal.multiply(montoDescuento.divide(BigDecimal.valueOf(100.0)));
            var precioConDescuento = precioTotal.subtract(descuento);
            BigDecimal ivaItem;
            BigDecimal totalGravada = BigDecimal.valueOf(detalle.getCantidad()).multiply(BigDecimal.valueOf(detalle.getPrecioUnitario())).setScale(8, RoundingMode.HALF_EVEN);

            ivaItem = totalGravada.divide(BigDecimal.valueOf(1.13d), 8, // Specify the scale
                    RoundingMode.HALF_EVEN // Specify the rounding mode
            ).multiply(BigDecimal.valueOf(0.13d));

            var nuevoItem = Item.builder().tipoItem(detalle.getItemCNR().getTipoItem().getCodigo()).unidadMedida(UNIDAD).nombre(detalle.getItemCNR().getNombreItem()).descripcion(detalle.getItemCNR().getDescripcion()).codigoIngreso(detalle.getCodigoIngreso()).codigoProducto(detalle.getCodigoProducto()).editable(detalle.getEditable()).nroDocumento(detalle.getNroDocumento()).total(precioConDescuento).cantidad(detalle.getCantidad()).precioUnitario(BigDecimal.valueOf(detalle.getPrecioUnitario())).ventaNosujeta(detalle.getVentaNosujeta()).ventaExenta(detalle.getVentaExenta()).ventaGravada(detalle.getVentaGravada()).ivaItem(ivaItem.setScale(8, RoundingMode.HALF_EVEN)).montoDescuento(descuento) // Asignar el monto de descuento al item
                    .ventaNogravada(detalle.getVentaNogravada()).build();
            listaItems.add(nuevoItem);
        }
        return listaItems;
    }


    private BigDecimal calcularPrecioTotal(Integer cantidad, Double precioUnitario) {
        if (cantidad == null || precioUnitario == null || cantidad < 0 || precioUnitario < 0) {
            throw new TransaccionException("La cantidad y el precio unitario deben ser valores no nulos y no negativos.");
        }
        return BigDecimal.valueOf(cantidad).multiply(BigDecimal.valueOf(precioUnitario)).setScale(8, RoundingMode.HALF_EVEN);
    }

    private BigDecimal calcularTotalSinDescuento(List<Item> listaItems) {
        var totalSinDescuento = listaItems.stream().map(Item::getTotal)  // Obtener el campo total de cada Item
                .filter(Objects::nonNull)  // Filtrar valores no nulos
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalSinDescuento;
    }

    private BigDecimal calcularMontoDescuento(Double porcentajeDescuento, BigDecimal totalSinDescuento) {
        var porcentaje = Optional.ofNullable(porcentajeDescuento).orElse(0.0);
        var total = Optional.ofNullable(totalSinDescuento).orElse(BigDecimal.ZERO);

        // Si el porcentaje de descuento es 100, retorna 0
        if (porcentaje == 100.0) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(porcentaje / 100.0).multiply(total).setScale(8, RoundingMode.HALF_EVEN);
    }


    private BigDecimal calcularTotalTransaccion(BigDecimal totalSinDescuento, BigDecimal montoDescuento) {
        var total = Optional.ofNullable(totalSinDescuento).orElse(BigDecimal.ZERO);
        var descuento = Optional.ofNullable(montoDescuento).orElse(BigDecimal.ZERO);
        return total.subtract(descuento).setScale(8, RoundingMode.HALF_EVEN);
    }

    public List<Transaccion> getAllTransacions() {
        User usuario = (authenticationService.loggedUser());
        Sort sortByIDDesc = Sort.by(Sort.Direction.DESC, "idTransaccion");
        List<Transaccion> transacciones = transaccionRepository.findAll(sortByIDDesc);
        return transacciones;
    }


    public List<Transaccion> getAllVentas(List<String> tipoDTE, String nit) {
        return transaccionRepository.getTransaccionByTipoDTEandNit(tipoDTE, nit);
    }

    public List<Transaccion> getAllVentasByTipoDTE(List<String> tipoDTE) {
        return transaccionRepository.getTransaccionByTipoDTE(tipoDTE);
    }

    public List<Transaccion> getAllVentasByIdCliente(List<String> tipoDTE, String nit) {
        List<Transaccion> transacciones = transaccionRepository.getTransaccionByTipoDTEandNit(tipoDTE, nit);
        return transacciones;
    }

    public List<Transaccion> getTransaccionesConIVARetenido(List<String> tipoDTE, String nit) {
        return transaccionRepository.getTransaccionesConIVARetenido(tipoDTE, nit);
    }

    public List<DocumentoAsociado> obtenerDocumentosAsociados(List<DocumentoAsociadoDTO> documentosAsociados) {
        var listaDocumentos = new ArrayList<DocumentoAsociado>();
        for (DocumentoAsociadoDTO documento : documentosAsociados) {
            var nuevoDocAsociado = DocumentoAsociado.builder().tipoDTE(documento.getTipoDte()).tipoGeneracion(documento.getTipoGeneracion()).nroPreimpreso(documento.getNroPreimpreso()).fechaEmision(documento.getFechaEmision()).serieDocumento(documento.getSerieDocumento()).montoDocumento(documento.getMonto()).codigoGeneracion(documento.getCodigoGeneracion()).transaccionHija(transaccionRepository.findById(documento.getIdTransaccionHija()).orElseThrow()).build();
            listaDocumentos.add(nuevoDocAsociado);
        }
        return listaDocumentos;
    }


    public List<ComprobantePago> obtenerComprobantePagos(List<ComprobantePagoDTO> comprobantePagoDTO) {
        var listaComprobantes = new ArrayList<ComprobantePago>();
        for (ComprobantePagoDTO comprobante : comprobantePagoDTO) {
            var nuevoComprobante = ComprobantePago.builder().numeroComprobante(comprobante.getNumeroComprobante()).build();
            listaComprobantes.add(nuevoComprobante);
        }
        return listaComprobantes;
    }

    public List<CuerpoDocCR> obtenerCuerpoDocuCNR(List<CuerpoDocCrDTO> cuerpoDocCrDTO) {
        var lista = new ArrayList<CuerpoDocCR>();
        Long numeroItem = 1L;
        for (CuerpoDocCrDTO entidad : cuerpoDocCrDTO) {
            var nuevo = CuerpoDocCR.builder().numeroItem(numeroItem).tipoDte(entidad.getTipoDte()).tipoDoc(entidad.getTipoDoc()).numDocumento(entidad.getNumDocumento()).fechaEmision(entidad.getFechaEmision()).montoSujeto(entidad.getMontoSujeto()).ivaRetenido(entidad.getIvaRetenido()).descripcion(entidad.getDescripcion()).tipoRetencion(entidad.getTipoRetencion()).build();
            lista.add(nuevo);
            numeroItem++;
        }
        return lista;
    }


    public List<DocumentoDTO> getDocumentosNotaCredito(String nitCliente) {
        return documentoRepository.getDocumentoNotaCredito(nitCliente);
    }

    public List<Transaccion> getTransaccionesByUser(User user) {
        try {
            // Recuperar todas las transacciones del usuario especificado ordenadas por ID descendente
            List<Transaccion> transacciones = transaccionRepository.findByUserOrderByIdTransaccionDesc(user);

            // Para cada transacción, obtener el estadoDte y establecerlo
            for (Transaccion transaccion : transacciones) {
                // Obtener el estadoDte desde la lista de DteTransaccion
                Optional<DteTransaccion> dteTransaccionOpt = transaccion.getDteTransacciones().stream().findFirst();
                String estadoDte = dteTransaccionOpt.map(DteTransaccion::getEstadoDte).orElse(null);

                // Establecer el estadoDte en la transacción
                transaccion.setEstadoDte(estadoDte);

            }

            return transacciones;
        } catch (Exception e) {
            // Manejar cualquier excepción que pueda ocurrir y, si es necesario, registrar el error
            System.err.println("Error al recuperar las transacciones: " + e.getMessage());
            // Devolver una lista vacía o manejar la excepción según las necesidades del negocio
            return List.of();
        }
    }


    public List<Transaccion> getTransaccionesRoot() {
        try {
            // Recuperar todas las transacciones del usuario especificado ordenadas por ID descendente
            List<Transaccion> transacciones = transaccionRepository.findAllByOrderByIdTransaccionDesc();

            // Para cada transacción, obtener el estadoDte y establecerlo
            for (Transaccion transaccion : transacciones) {
                // Obtener el estadoDte desde la lista de DteTransaccion
                Optional<DteTransaccion> dteTransaccionOpt = transaccion.getDteTransacciones().stream().findFirst();
                String estadoDte = dteTransaccionOpt.map(DteTransaccion::getEstadoDte).orElse(null);

                // Establecer el estadoDte en la transacción
                transaccion.setEstadoDte(estadoDte);
            }

            return transacciones;
        } catch (Exception e) {
            // Manejar cualquier excepción que pueda ocurrir y, si es necesario, registrar el error
            System.err.println("Error al recuperar las transacciones: " + e.getMessage());
            // Devolver una lista vacía o manejar la excepción según las necesidades del negocio
            return List.of();
        }
    }


    public Page<Transaccion> getTransaccionesByUserPage(User user, int page, int size, String filterBy, String sortBy) {
        System.out.println("getTransaccionesByUserPage");
        Sort sort = sortBy.isEmpty() ? Sort.by("createdAt").descending() : requestParamParser.parseSortBy(sortBy).and(Sort.by("createdAt").descending());
        Map<String, String> filterParams = requestParamParser.parseFilterBy(filterBy);

        System.out.println("transaction service " + sort);
        System.out.println("transaction service filter params" + filterParams);
        Pageable pageable = PageRequest.of(page, size, sort);
        try {
            Page<Transaccion> transacciones = transaccionRepository.findByUserOrderByIdTransaccionDesc(user, pageable);

//            for (Transaccion transaccion : transacciones) {
//                Optional<DteTransaccion> dteTransaccionOpt = transaccion.getDteTransacciones().stream().findFirst();
//                String estadoDte = dteTransaccionOpt.map(DteTransaccion::getEstadoDte).orElse(null);
//                transaccion.setEstadoDte(estadoDte);
//            }

            return transacciones;
        } catch (Exception e) {
            return Page.empty();
        }
    }

//    public Page<Transaccion> getTransaccionesRootPage(int page, int size, String filterBy, String sortBy) {
//        try {
//            // Ordenación predeterminada o personalizada
//            Sort sort = sortBy.isEmpty() ? Sort.by("createdAt").descending() : requestParamParser.parseSortBy(sortBy);
//
//            // Filtros dinámicos
//            Map<String, String> filterParams = requestParamParser.parseFilterBy(filterBy);
//            Pageable pageable = PageRequest.of(page, size, sort);
//
//            filterParams = filterParams.entrySet().stream()
//                    .collect(Collectors.toMap(
//                            entry -> entry.getKey(),
//                            entry -> entry.getValue() != null ? entry.getValue().toUpperCase() : null
//                    ));
//
//            Page<Transaccion> transacciones;
//
//            if (!filterParams.isEmpty()) {
//
//                System.out.println("filter params" + filterParams);
//
//                if (filterParams.containsKey("cliente") && !filterParams.get("cliente").isEmpty()) {
//                    transacciones = transaccionRepository.findTransaccionesByClienteNombre(filterParams.get("cliente"), pageable);
//                } else if (filterParams.containsKey("status") && !filterParams.get("status").isEmpty()) {
//                    transacciones = transaccionRepository.findTransaccionesByStatusDte(EstatusCola.values()[Integer.parseInt(filterParams.get("status"))], pageable);
//                } else if (filterParams.containsKey("totalTransaccion") && !filterParams.get("totalTransaccion").isEmpty()) {
//                    transacciones = transaccionRepository.findTransaccionesByTotalTransaccion(new BigDecimal(filterParams.get("totalTransaccion")), pageable);
//                } else {
//                    transacciones = transaccionRepository.findAll(requestParamParser.withFilters(filterParams), pageable);
//                }
//            } else {
//                if (sort.iterator().next().getProperty().equals("cliente")) {
//                    transacciones = transaccionRepository.findTransaccionesByClienteNombre("", pageable);
//                } else {
//                    transacciones = transaccionRepository.findAllByOrderByIdTransaccionDesc(pageable);
//                }
//            }
//
//            // Cálculo del estadoDte (opcional)
//            transacciones.forEach(t -> {
//                Optional<DteTransaccion> dteOpt = t.getDteTransacciones().stream().findFirst();
//                t.setEstadoDte(dteOpt.map(DteTransaccion::getEstadoDte).orElse(null));
//            });
//
//            return transacciones;
//        } catch (Exception e) {
//            log.error("Error al obtener transacciones", e);
//            throw new RuntimeException("Error al procesar transacciones", e);
//        }
//    }

    public Page<Transaccion> getTransaccionesRootPage(int page, int size, String filterBy, String sortBy) {
        try {
            // Ordenación predeterminada o personalizada
            Sort sort = sortBy.isEmpty() ? Sort.by("createdAt").descending() : requestParamParser.parseSortBy(sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);

            // Filtros dinámicos
            final Map<String, String> filterParams = requestParamParser.parseFilterBy(filterBy);

            // Crear una copia de los parámetros de filtro y asegurar que sus valores sean en mayúsculas
            Map<String, String> filterParamsCopy = filterParams.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue() != null ? entry.getValue().toUpperCase() : null));

            Specification<Transaccion> spec = Specification.where(null);

            // Aplicar filtros dinámicos
            if (filterParamsCopy.containsKey("fechaTransaccion") && !filterParamsCopy.get("fechaTransaccion").isEmpty()) {
                try {
                    String fechaStr = filterParamsCopy.get("fechaTransaccion");

                    // Define el formato de la fecha en 'dd-MM-yyyy'
                    SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");

                    // Parseamos la fecha
                    java.util.Date date = inputFormat.parse(fechaStr);

                    // Convertimos la fecha a java.sql.Date
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                    // Usamos la función TO_CHAR de Oracle para formatear la fecha
                    spec = spec.and((root, query, criteriaBuilder) -> {
                        // Usamos la función TO_CHAR para formatear la fecha como 'yyyy-MM-dd'
                        Expression<String> formattedDate = criteriaBuilder.function(
                                "TO_CHAR", String.class, root.get("fechaTransaccion"), criteriaBuilder.literal("yyyy-MM-dd"));
                        return criteriaBuilder.like(formattedDate, "%" + sqlDate.toString() + "%");
                    });

                } catch (Exception e) {
                    //e.printStackTrace();
                    logger.log(Level.SEVERE, "Error en myMethod: " + e.getMessage(), e);
                }
            }


                if (filterParamsCopy.containsKey("cliente") && !filterParamsCopy.get("cliente").isEmpty()) {
                    spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("cliente").get("nombre")), "%" + filterParamsCopy.get("cliente") + "%"));
                }

            if (filterParamsCopy.containsKey("status") && !filterParamsCopy.get("status").isEmpty()) {
                int statusIndex = Integer.parseInt(filterParamsCopy.get("status"));
                EstatusCola status = EstatusCola.values()[statusIndex];
                spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status));
            }

            if (filterParamsCopy.containsKey("totalTransaccion") && !filterParamsCopy.get("totalTransaccion").isEmpty()) {
                BigDecimal total = new BigDecimal(filterParamsCopy.get("totalTransaccion"));
                spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("totalTransaccion"), total));
            }

            if (filterParamsCopy.containsKey("codigoGeneracion") && !filterParamsCopy.get("codigoGeneracion").isEmpty()) {
                String codigoGeneracion = filterParamsCopy.get("codigoGeneracion");
                spec = spec.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.like(criteriaBuilder.upper(root.get("codigoGeneracion")), "%" + codigoGeneracion.toUpperCase() + "%"));
            }

            if (filterParamsCopy.containsKey("numeroDTE") && !filterParamsCopy.get("numeroDTE").isEmpty()) {
                String numeroDTE = filterParamsCopy.get("numeroDTE");
                spec = spec.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.like(criteriaBuilder.upper(root.get("numeroDTE")), "%" + numeroDTE.toUpperCase() + "%"));
            }

            if (filterParamsCopy.containsKey("tipoDTE") && !filterParamsCopy.get("tipoDTE").isEmpty()) {
                spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tipoDTE"), filterParamsCopy.get("tipoDTE")));
            }

            // Obtener resultados paginados con el filtro
            Page<Transaccion> transacciones = transaccionRepository.findAll(spec, pageable);

            // Cálculo opcional de estadoDte
            transacciones.forEach(t -> {
                Optional<DteTransaccion> dteOpt = t.getDteTransacciones().stream().findFirst();
                t.setEstadoDte(dteOpt.map(DteTransaccion::getEstadoDte).orElse(null));
            });

            return transacciones;
        } catch (Exception e) {
            log.error("Error al obtener transacciones", e);
            throw new RuntimeException("Error al procesar transacciones", e);
        }
    }


}