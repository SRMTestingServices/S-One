package steps;

import api.RestAssuredUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

public class APISampleTest {
	private RestAssuredUtil apiUtil;
	private Response response;

	@Given("I send a GET request to {string} on base URI {string}")
	public void i_send_a_get_request_to_on_base_uri(String endpoint, String baseUri) {
		String username = JsonUtils.getString("users.json", "users[0].username");
		System.out.println(username);
		apiUtil = new RestAssuredUtil(baseUri)
				.setContentType(ContentType.JSON)
				.enableLogging(true); // Optional: Enable detailed logging

		response = apiUtil.get(endpoint);
	}

	@Then("the response status code should be {int}")
	public void the_response_status_code_should_be(Integer expectedStatusCode) {
		Assert.assertTrue(apiUtil.validateStatusCode(response, expectedStatusCode),"Expected status code: " + expectedStatusCode + ", but got: " + response.getStatusCode());
	}

	@Given("I send a POST request to {string} on base URI {string} with JSON body")
	public void i_send_a_post_request_with_json(String endpoint, String baseUri) {
		JSONObject body = new JSONObject();
		body.put("name", "Deepan");
		body.put("job", "Test Architect");

		apiUtil = new RestAssuredUtil(baseUri);
		apiUtil.enableLogging(true); // log to debug

		response = apiUtil
				.setContentType(ContentType.JSON)
				.setBody(body.toString())
				.post(endpoint);
	}


	@Then("the POST response status code should be {int}")
	public void validate_post_status_code(int expectedStatusCode) {
		Assert.assertTrue(apiUtil.validateStatusCode(response, expectedStatusCode),"Expected status " + expectedStatusCode + ", but got " + response.getStatusCode());
	}

	@Then("the response should contain field {string}")
	public void the_response_should_contain_field(String fieldName) {
		String value = apiUtil.extractValue(response, fieldName);
    }

	/** Optional: Default headers if needed */
	private Map<String, String> getDefaultHeaders() {
		Map<String, String> headers = new HashMap<>();
		headers.put("Accept", "application/json");
		return headers;
	}
}
