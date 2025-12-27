// package nextpos.app.nextpos;

// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.web.client.TestRestTemplate;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;

// // import com.jayway.jsonpath.DocumentContext;
// // import com.jayway.jsonpath.JsonPath;

// import static org.assertj.core.api.Assertions.assertThat;

// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// class NextposApplicationTests {
// 	@Autowired
// 	TestRestTemplate restTemplate;

// 	@Test
// 	void shouldReturnACashCardWhenDataIsSaved() {
// 		ResponseEntity<String> response = restTemplate.getForEntity("/test/user/99", String.class);

// 		// Assertions
// 		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

// 		// DocumentContext documentContext = JsonPath.parse(response.getBody());
// 		// Number id = documentContext.read("$.id");
// 		// assertThat(id).isEqualTo(99);
// 	}
// }