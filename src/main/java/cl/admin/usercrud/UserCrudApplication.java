package cl.admin.usercrud;

import cl.admin.usercrud.services.impl.JWTServicesImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserCrudApplication {

	public static void main(String[] args) {

		SpringApplication.run(UserCrudApplication.class, args);
	}

}
