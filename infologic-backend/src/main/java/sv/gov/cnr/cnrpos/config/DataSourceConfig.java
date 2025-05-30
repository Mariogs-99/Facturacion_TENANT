package sv.gov.cnr.cnrpos.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String masterDbUrl;

    @Value("${spring.datasource.username}")
    private String masterDbUsername;

    @Value("${spring.datasource.password}")
    private String masterDbPassword;

    @Value("${spring.datasource.driver-class-name}")
    private String masterDbDriver;

    @Bean
    @Primary
    public DataSource dataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();

        HikariDataSource master = new HikariDataSource();
        master.setJdbcUrl(masterDbUrl);
        master.setUsername(masterDbUsername);
        master.setPassword(masterDbPassword);
        master.setDriverClassName(masterDbDriver);

        Map<Object, Object> sources = new HashMap<>();
        sources.put("master", master);

        dynamicDataSource.setDefaultTargetDataSource(master);
        dynamicDataSource.setTargetDataSources(sources);

        DynamicDataSourceManager.init(dynamicDataSource);

        return dynamicDataSource;
    }
}
