package client;

import client.base.Client;
import couriermodel.Courier;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ordermodel.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {

    private static final String CREATE_ORDER_URI = "orders";
    private static final String GET_ORDER_LIST = BASE_URI + "orders";
    private static final String CANCEL_ORDER_URI = BASE_URI + "orders/cancel";

    @Step("Create order {order}")
    public ValidatableResponse create(Order order) {
        return given()
                .spec(getSpec())
                .body(order).log().all()
                .when()
                .post(CREATE_ORDER_URI)
                .then().log().all();
    }

    @Step("Получение списка заказов")
    public ValidatableResponse getOrderList() {
        return given()
                .spec(getSpec())
                .when()
                .get(GET_ORDER_LIST)
                .then();
    }

    @Step("Отмена заказа")
    public ValidatableResponse cancelOrder(int track) {
        return given()
                .spec(getSpec())
                .body(track)
                .when()
                .put(CANCEL_ORDER_URI)
                .then();
}
}


