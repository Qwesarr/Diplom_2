package client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RestAssuredClient {
    private RequestSpecification requestSpecification = new RequestSpecBuilder()
            .addFilter(new AllureRestAssured())
            .log(LogDetail.ALL)
            .setContentType(ContentType.JSON)
            .setBaseUri("https://stellarburgers.nomoreparties.site/")
            .setBasePath("/api/")
            .build();

    public RequestSpecification getRequestSpecification() {
        return requestSpecification;
    }

    public void setRequestSpecification(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification;
    }
    //Без авторизации
    public <T> Response post(String path, T body, Object... pathParams) {
        return given()
                .spec(getRequestSpecification())
                .body(body)
                .when()
                .post(path, pathParams);
    }

    //С авторизацией
    public <T> Response post(String path, T body, String token, Object... pathParams) {
        return given()
                .spec(getRequestSpecification())
                .body(body)
                .when()
                .auth()
                .oauth2(token)
                .post(path, pathParams);
    }

    //С авторизацией
    public <T> Response patch(String path, T body, String token, Object... pathParams) {
        return given()
                .spec(getRequestSpecification())
                .body(body)
                .when()
                .auth()
                .oauth2(token)
                .patch(path, pathParams);
    }
    //Без авторизации
    public <T> Response patch(String path, T body, Object... pathParams) {
        return given()
                .spec(getRequestSpecification())
                .body(body)
                .when()
                .patch(path, pathParams);
    }

    public Response delete(String path, String token) {
        return given()
                .spec(getRequestSpecification())
                .when()
                .auth()
                .oauth2(token)
                .delete(path);
    }
    //С авторизацией
    public Response get(String path, String token) {
        return given()
                .spec(getRequestSpecification())
                .when()
                .auth()
                .oauth2(token)
                .get(path);
    }
    //Без авторизации
    public Response get(String path) {
        return given()
                .spec(getRequestSpecification())
                .when()
                .get(path);
    }
}
