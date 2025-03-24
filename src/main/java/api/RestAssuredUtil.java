package api;

import static io.restassured.RestAssured.given;

import java.util.Map;

import org.json.JSONObject;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

/*
 * Utils to play with api's using rest assured
 * 
 * @author Selva 
 */
public class RestAssuredUtil {

	private RequestSpecification request;

	public RestAssuredUtil(String baseURI) {
		RestAssured.baseURI = baseURI;
		request = given();
	}

	/** Set Headers */
	public RestAssuredUtil setHeaders(Map<String, String> headers) {
		request.headers(headers);
		return this;
	}

	/** Set Query Parameters */
	public RestAssuredUtil setQueryParams(Map<String, String> queryParams) {
		request.queryParams(queryParams);
		return this;
	}

	/** Set Path Parameters */
	public RestAssuredUtil setPathParams(Map<String, String> pathParams) {
		request.pathParams(pathParams);
		return this;
	}

	/** Set Request Body (JSON Object) */
	public RestAssuredUtil setBody(JSONObject body) {
		request.body(body.toString());
		return this;
	}

	/** Set Request Body (String) */
	public RestAssuredUtil setBody(String body) {
		request.body(body);
		return this;
	}

	/** Set Authentication (Basic Auth) */
	public RestAssuredUtil setBasicAuth(String username, String password) {
		request.auth().preemptive().basic(username, password);
		return this;
	}

	/** Set Authentication (Bearer Token) */
	public RestAssuredUtil setBearerToken(String token) {
		request.header("Authorization", "Bearer " + token);
		return this;
	}

	/** Set Content Type */
	public RestAssuredUtil setContentType(ContentType type) {
		request.contentType(type);
		return this;
	}

	/** Perform GET Request */
	public Response get(String endpoint) {
		return request.when().get(endpoint);
	}

	/** Perform POST Request */
	public Response post(String endpoint) {
		return request.when().post(endpoint);
	}

	/** Perform PUT Request */
	public Response put(String endpoint) {
		return request.when().put(endpoint);
	}

	/** Perform DELETE Request */
	public Response delete(String endpoint) {
		return request.when().delete(endpoint);
	}

	/** Validate Response Status Code */
	public boolean validateStatusCode(Response response, int expectedStatusCode) {
		return response.getStatusCode() == expectedStatusCode;
	}

	/** Validate Response JSON Schema */
	public boolean validateJSONSchema(Response response, String schemaPath) {
		return response.then().assertThat().body(matchesJsonSchemaInClasspath(schemaPath)) != null;
	}

	/** Extract Value from Response JSON */
	public String extractValue(Response response, String jsonPath) {
		return response.jsonPath().getString(jsonPath);
	}

	/** Log Request and Response */
	public void enableLogging() {
		request.log().all();
	}
}
