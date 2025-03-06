package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiTest {
    public Response getRequest(String url) {
        return RestAssured.get(url);
    }
}
