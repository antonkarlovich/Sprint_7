package client;

import client.base.Client;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import couriermodel.Courier;
import couriermodel.CourierCredentials;
import static io.restassured.RestAssured.given;

public class CourierClient extends Client {

    private static final String COURIER_URI = BASE_URI + "courier/";

    @Step("Create courier {courier}")
    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(getSpec())
                .body(courier).log().all()
                .when()
                .post(COURIER_URI)
                .then().log().all();
    }


    @Step("Login as {courierCredentials}")
    public ValidatableResponse login(CourierCredentials courierCredentials) {
        return given()
                .spec(getSpec())
                .body(courierCredentials)
                .when()
                .post(COURIER_URI + "login/")
                .then();
    }

    @Step("Delete courier {id}")
    public ValidatableResponse delete(int id) {
        return given()
                .spec(getSpec())
                .when()
                .delete(COURIER_URI + id)
                .then();
    }
}
