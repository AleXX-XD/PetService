package PetServiceApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class PetServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetServiceApplication.class, args);
    }
}
