package priv.ilyan;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

@QuarkusTest
public class AdminResourceTest {
    private String token;
	@BeforeEach
	public void setup() {
		Map<String, String> form = new HashMap<>();
		form.put("username", "admin");
		form.put("password", "admin");
		Response response = given().
				accept(ContentType.JSON).
				contentType(ContentType.JSON).
				body(form)
				.post("/auth/login")
				.andReturn();

		token = response.jsonPath().get("token");
		assertNotNull(token);
	}

	@Test
	public void adminAllowed(){
		Map<String, String> headers = new HashMap<>();
		headers.put("Accept", "application/json");
		headers.put("Authorization", "Bearer "+token);
		given()
				.headers(headers)
				.when()
				.get("/resource/admin")
				.then()
				.assertThat()
				.statusCode(200);
	}
}
