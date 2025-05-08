package sampletests;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

import api.RestAssuredUtil;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/*
 * Sample test method for each methods from restassuredutils
 * 
 * @author Selva 
 */
public class APISampleTest {

	private RestAssuredUtil restAssuredUtil;

	@BeforeClass
	public void setup() {
		restAssuredUtil = new RestAssuredUtil("baseurl");
	}

	/** Test GET Request */

	public void testGetRequest() {
		Response response = restAssuredUtil.get("endpoint");
		Assert.assertTrue(restAssuredUtil.validateStatusCode(response, 200), "GET request failed");
		System.out.println("Response: " + response.asString());
	}

	/** Test POST Request with JSON Body */

	public void testPostRequest() {
		JSONObject requestBody = new JSONObject();
		requestBody.put("", "");
		Response response = restAssuredUtil.setContentType(ContentType.JSON).setBody(requestBody).post("endpoint");
		Assert.assertTrue(restAssuredUtil.validateStatusCode(response, 201), "POST request failed");
		System.out.println("Response: " + response.asString());
	}

	/** Test PUT Request (Update User) */

	public void testPutRequest() {
		JSONObject requestBody = new JSONObject();
		requestBody.put("", "");
		Response response = restAssuredUtil.setContentType(ContentType.JSON).setBody(requestBody).put("endpoint");
		Assert.assertTrue(restAssuredUtil.validateStatusCode(response, 200), "PUT request failed");
		System.out.println("Updated Response: " + response.asString());
	}

	/** Test DELETE Request */

	public void testDeleteRequest() {
		Response response = restAssuredUtil.delete("endpoint");
		Assert.assertTrue(restAssuredUtil.validateStatusCode(response, 204), "DELETE request failed");
		System.out.println("Delete Status Code: " + response.getStatusCode());
	}

	/** Test Query & Path Parameters */

	public void testQueryAndPathParams() {
		Response response = restAssuredUtil
				.setQueryParams(Map.of("", ""))
				.setPathParams(Map.of("", ""))
				.get("endpoint");
		Assert.assertTrue(restAssuredUtil.validateStatusCode(response, 200), "Query & Path Params test failed");
		System.out.println("User Details: " + response.asString());
	}

	/** Test Bearer Token Authentication */

	public void testBearerTokenAuth() {
		Response response = restAssuredUtil.setBearerToken("token").get("endpoint");
		Assert.assertTrue(restAssuredUtil.validateStatusCode(response, 200), "Bearer Token Authentication failed");
		System.out.println("Secure Data: " + response.asString());
	}

	/** Test JSON Schema Validation */

	public void testRequestAndValidateJSONSchema() {
		Response response = restAssuredUtil.get("");
		restAssuredUtil.validateJSONSchemaOrFail(response, "schema.json");
		System.out.println("JSON Schema Validation Passed");
	}
	
	
	public void multiAttributesRequest() {
		Map<String, String> headers = new HashMap<String, String>();
		Map<String, String> pathParams = new HashMap<String, String>();
		Map<String, String> queryParams = new HashMap<String, String>();
		restAssuredUtil.setBearerToken("").setContentType(ContentType.JSON).setHeaders(headers)
				.setPathParams(pathParams).setQueryParams(queryParams).post("endpoint");
	}
}
