package tn.enicarthage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
@EntityScan("tn.enicarthage.models")
@SpringBootApplication
public class GestionPfaBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionPfaBackApplication.class, args);
	}

}
