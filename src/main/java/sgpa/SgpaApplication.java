package sgpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SgpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SgpaApplication.class, args);
	}
}
