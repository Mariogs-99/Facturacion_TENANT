package sv.gov.cnr.factelectrcnrservice.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sv.gov.cnr.factelectrcnrservice.entities.MasterCompany;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class DynamicDataSourceManager {

    private final DynamicDataSource dynamicDataSource;

    // Repositorio interno de dataSources por tenant
    private final Map<String, DataSource> tenantDataSources = new ConcurrentHashMap<>();

    public DynamicDataSourceManager(DataSource dataSource) {
        this.dynamicDataSource = (DynamicDataSource) dataSource;
    }

    public void setTenantDataSource(MasterCompany company) {
        String tenantId = company.getNit();

        // Reutilizar si ya existe
        if (!tenantDataSources.containsKey(tenantId)) {
            log.info("🔧 Creando nuevo pool de conexiones para {}", tenantId);
            HikariDataSource ds = new HikariDataSource();
            ds.setJdbcUrl(company.getDatabaseUrl());
            ds.setUsername(company.getDatabaseUsername());
            ds.setPassword(company.getDatabasePassword());
            ds.setDriverClassName("com.mysql.cj.jdbc.Driver");

            tenantDataSources.put(tenantId, ds);
        }

        // Apuntar al dataSource existente
        dynamicDataSource.setTargetDataSources(new ConcurrentHashMap<>(tenantDataSources));
        dynamicDataSource.setDefaultTargetDataSource(tenantDataSources.get(tenantId));
        dynamicDataSource.afterPropertiesSet();

        TenantContext.setCurrentTenant(tenantId);
    }

    public void resetToMaster() {
        TenantContext.clear();
        dynamicDataSource.setTargetDataSources(new ConcurrentHashMap<>(tenantDataSources));
        dynamicDataSource.setDefaultTargetDataSource(dynamicDataSource.getMasterDataSource());
        dynamicDataSource.afterPropertiesSet();
    }
}
