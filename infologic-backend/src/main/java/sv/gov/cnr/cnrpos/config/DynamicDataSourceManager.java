package sv.gov.cnr.cnrpos.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DynamicDataSourceManager {

    private static final Map<String, DataSource> dataSourceMap = new HashMap<>();
    private static DynamicDataSource dynamicDataSource;

    public static void init(DynamicDataSource ds) {
        dynamicDataSource = ds;
    }

    public static void setDataSource(String tenantId, String url, String username, String password) {
        TenantContext.setCurrentTenant(tenantId);

        if (!dataSourceMap.containsKey(tenantId)) {
            DataSource ds = createDataSource(url, username, password);
            dataSourceMap.put(tenantId, ds);
            dynamicDataSource.addTargetDataSource(tenantId, ds);
            log.info("🔄 Nuevo DataSource agregado para tenant {}", tenantId);
        }

        log.info("✅ Se ha cambiado correctamente el Tenant a: {}", tenantId);
    }

    private static DataSource createDataSource(String url, String username, String password) {
        HikariDataSource config = new HikariDataSource();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return config;
    }
}
