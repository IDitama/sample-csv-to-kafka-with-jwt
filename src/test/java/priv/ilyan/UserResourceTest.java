package priv.ilyan;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class UserResourceTest {
    private String token;
	@BeforeEach
	public void setup() {
		Map<String, String> form = new HashMap<>();
		form.put("username", "user");
		form.put("password", "user");
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
				.get("/resource/user")
				.then()
				.assertThat()
				.statusCode(200);
    }
}
