package api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.HttpClientConfig;

/**
 * Thread-safe utility for making API requests using RestAssured.
 * Suitable for parallel test execution and integration with UI tests.
 */
public class RestAssuredUtil {

	private final RequestSpecification request;

	public RestAssuredUtil(String baseUri) {
		this.request = given()
				.baseUri(baseUri)
				.relaxedHTTPSValidation()
				.config(RestAssuredConfig.config().httpClient(HttpClientConfig.httpClientConfig()
						.setParam("http.connection.timeout", 5000)
						.setParam("http.socket.timeout", 5000)
				));
	}

	public RestAssuredUtil setHeaders(Map<String, String> headers) {
		request.headers(headers);
		return this;
	}

	public RestAssuredUtil addHeader(String key, String value) {
		request.header(key, value);
		return this;
	}

	public RestAssuredUtil setQueryParams(Map<String, String> queryParams) {
		request.queryParams(queryParams);
		return this;
	}

	public RestAssuredUtil setPathParams(Map<String, String> pathParams) {
		request.pathParams(pathParams);
		return this;
	}

	public RestAssuredUtil setBody(Object body) {
		request.body(body);
		return this;
	}

	public RestAssuredUtil setContentType(ContentType type) {
		request.contentType(type);
		return this;
	}

	public RestAssuredUtil setBasicAuth(String username, String password) {
		request.auth().preemptive().basic(username, password);
		return this;
	}

	public RestAssuredUtil setBearerToken(String token) {
		request.header("Authorization", "Bearer " + token);
		return this;
	}

	public RestAssuredUtil enableLogging(boolean enable) {
		if (enable) {
			request.log().all();
		}
		return this;
	}

	public RestAssuredUtil setMultiPart(String controlName, File file) {
		request.multiPart(controlName, file);
		return this;
	}

	public Response get(String endpoint) {
		return request.when().get(endpoint);
	}

	public Response post(String endpoint) {
		return request.when().post(endpoint);
	}

	public Response put(String endpoint) {
		return request.when().put(endpoint);
	}

	public Response delete(String endpoint) {
		return request.when().delete(endpoint);
	}

	public boolean validateStatusCode(Response response, int expectedStatusCode) {
		return response.getStatusCode() == expectedStatusCode;
	}

	public void validateJSONSchemaOrFail(Response response, String schemaPath) {
		response.then().assertThat().body(matchesJsonSchemaInClasspath(schemaPath));
	}

	public String extractValue(Response response, String jsonPath) {
		return response.jsonPath().getString(jsonPath);
	}
}
