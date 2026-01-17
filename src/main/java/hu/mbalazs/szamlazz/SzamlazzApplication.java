package hu.mbalazs.szamlazz;

import hu.mbalazs.szamlazz.api.ReceiptController;
import hu.mbalazs.szamlazz.xmlhandling.XmlWriterService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SzamlazzApplication {

	public static void main(String[] args) {
		SpringApplication.run(SzamlazzApplication.class, args);
	}
}
