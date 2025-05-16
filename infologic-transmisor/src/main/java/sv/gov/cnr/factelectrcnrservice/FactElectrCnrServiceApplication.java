package sv.gov.cnr.factelectrcnrservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FactElectrCnrServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FactElectrCnrServiceApplication.class, args);
	}

}
