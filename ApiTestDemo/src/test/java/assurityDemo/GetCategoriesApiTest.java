package assurityDemo;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;
import org.junit.Before;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GetCategoriesApiTest {
	@Before
	public void setup() {
		RestAssured.baseURI = "https://api.tmsandbox.co.nz";
	}

	@Test
	public void isNameCarbonCredits() {
		given().param("catalogue", "false").when().get("/v1/Categories/6327/Details.json").then().assertThat()
				.statusCode(200).and().body("Name", equalTo("Carbon credits"));
	}

	@Test
	public void isCanRelistTrue() {
		Response res = given().param("catalogue", "false").when().get("/v1/Categories/6327/Details.json").then()
				.assertThat().statusCode(200).extract().response();
		String reponseString = res.asString();
		JsonPath jp = new JsonPath(reponseString);
		boolean status = jp.get("CanRelist");
		assertEquals(status, true);
	}

	@Test
	public void isPromotionGalleryDescription2xLargerImage() {
		Response res = given().param("catalogue", "false").when().get("/v1/Categories/6327/Details.json").then()
				.assertThat().statusCode(200).extract().response();
		List<String> promos = res.path("Promotions");
		String reponseString = res.asString();
		JsonPath jp = new JsonPath(reponseString);
		for (int i = 0; i < promos.size(); i++) {
			if ((jp.get("Promotions[" + i + "].Name")).equals("Gallery")) {
				final String description = jp.get("Promotions[" + i + "].Description");
				assertTrue(description.contains("2x larger image"));
			}
		}
	}

}
