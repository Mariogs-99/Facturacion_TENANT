package sv.gov.cnr.cnrpos.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();

        HikariDataSource master = new HikariDataSource();
        master.setJdbcUrl("jdbc:mysql://localhost:3306/master");
        master.setUsername("root");
        master.setPassword("123456");
        master.setDriverClassName("com.mysql.cj.jdbc.Driver");

        Map<Object, Object> sources = new HashMap<>();
        sources.put("master", master);

        dynamicDataSource.setDefaultTargetDataSource(master);
        dynamicDataSource.setTargetDataSources(sources);

        // 🔥 Esta línea es clave para que no sea null
        DynamicDataSourceManager.init(dynamicDataSource);

        return dynamicDataSource;
    }

}
