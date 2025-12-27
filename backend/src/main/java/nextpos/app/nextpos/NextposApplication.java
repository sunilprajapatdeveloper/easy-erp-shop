package nextpos.app.nextpos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@SpringBootApplication
@RequestMapping("/test")
public class NextposApplication {

	@GetMapping("/home")
	public String home() {
		String result = "Hello! Welcome to the user homepage";
		return result;
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<String> findUserById(@PathVariable String userId) {
		return ResponseEntity.ok(userId);
	}

	public static void main(String[] args) {
		SpringApplication.run(NextposApplication.class, args);
	}
}