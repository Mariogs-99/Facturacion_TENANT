package sv.gov.cnr.cnrpos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.sql.DataSource;

@SpringBootApplication
@EnableJpaAuditing
public class FactElectrCnrPuntoDeVentaBackendApplication {
    private final DataSource dataSource;
    public FactElectrCnrPuntoDeVentaBackendApplication(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public static void main(String[] args) {
        SpringApplication.run(FactElectrCnrPuntoDeVentaBackendApplication.class, args);
    }


}
