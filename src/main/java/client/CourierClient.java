package client;

import client.base.Client;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import couriermodel.Courier;
import couriermodel.CourierCredentials;
import static io.restassured.RestAssured.given;

public class CourierClient extends Client {

    private static final String COURIER_URI = "courier/";

    @Step("Create courier {courier}")
    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(getSpec())
                .body(courier)
                .when()
                .post(COURIER_URI)
                .then();
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

    //проблема была в том, что id возвращается только когда залогиниваешься)
    //при создании курьера id не возвращается. Поэтому вот такой костыль:
    //если курьер создается, то я его удаляю, и дальше проверка упадет в тестовом классе
    //больше ничего не смог придумать(
    @Step("Create and login {courier}")
    public ValidatableResponse createAndLogin(Courier courier) {
        ValidatableResponse createResponse = create(courier);
        int statusCode = createResponse.extract().statusCode();

        if (statusCode == 201) {
            ValidatableResponse loginResponse = login(CourierCredentials.from(courier));
            int courierId = loginResponse.extract().path("id");
            delete(courierId);
        }
        return createResponse;
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
