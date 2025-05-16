package sv.gov.cnr.cnrpos.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class DynamicDataSource extends AbstractRoutingDataSource {


    @Override
    protected Object determineCurrentLookupKey() {
        // Aquí Spring decide qué base de datos usar
        return TenantContext.getCurrentTenant();
    }



    public void addTargetDataSource(String tenantId, DataSource dataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>(super.getResolvedDataSources());
        targetDataSources.put(tenantId, dataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

}
