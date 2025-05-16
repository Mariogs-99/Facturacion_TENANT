package sv.gov.cnr.factelectrcnrservice.procesador;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sv.gov.cnr.factelectrcnrservice.config.DynamicDataSourceManager;
import sv.gov.cnr.factelectrcnrservice.entities.Cola;
import sv.gov.cnr.factelectrcnrservice.entities.MasterCompany;
import sv.gov.cnr.factelectrcnrservice.services.ColaService;
import sv.gov.cnr.factelectrcnrservice.services.MasterCompanyService;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ColaScannerService {

    private final MasterCompanyService masterCompanyService;
    private final DynamicDataSourceManager dynamicDataSourceManager;
    private final ColaService colaService;
    private final EjecutorDeCola ejecutorDeCola;

    @Scheduled(fixedRate = 5000)
    public void escanearColasClientes() {
        try {
            // 🧭 Cambiar a base de datos maestra antes de obtener las empresas
            dynamicDataSourceManager.resetToMaster();

            List<MasterCompany> empresas = masterCompanyService.obtenerEmpresasActivas();

            for (MasterCompany empresa : empresas) {
                try {

                    //! Obtener el nombre del cliente referenciado al NIT
                    log.info("🔍 Escaneando cola para la empresa: {} ({})", empresa.getNameCompany(), empresa.getNit());

                    // Cambiar al datasource correspondiente del cliente
                    dynamicDataSourceManager.setTenantDataSource(empresa);

                    // Buscar si hay registros pendientes en cola
                    Optional<Cola> registroEnColaOpt = colaService.obtenerRegistroOperacionNormal();

                    if (registroEnColaOpt.isPresent()) {
                        log.info("📦 Se encontró un registro en la cola para la empresa {}", empresa.getNit());
                        ejecutorDeCola.procesarRegistro(registroEnColaOpt.get());
                    } else {
                        log.info("✅ No hay elementos en cola para la empresa {}", empresa.getNameCompany(), empresa.getNit());
                    }

                } catch (Exception e) {
                    log.error("❌ Error procesando cola para empresa {}: {}", empresa.getNit(), e.getMessage());
                } finally {
                    // Volver al master después de cada empresa
                    dynamicDataSourceManager.resetToMaster();
                }
            }
        } catch (Exception ex) {
            log.error("🚨 Error al escanear colas de clientes: {}", ex.getMessage(), ex);
        }
    }
}
