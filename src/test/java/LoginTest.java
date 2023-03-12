
import client.CourierClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import couriermodel.Courier;
import couriermodel.CourierCredentials;
import couriermodel.CourierGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class LoginTest {
    private CourierClient courierClient;
    Courier courier;
    private int idCourier;


    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getRandom();
    }

    @After
    public void cleanUp() {
        if (idCourier != 0) {
            courierClient.delete(idCourier);
        }
    }

    @Test
    @DisplayName("Авторизация курьера в системе")
    @Description("Проверка авторизации с корректными логином и паролем")
    public void checkLoginCourier() {
        Courier courier = CourierGenerator.getRandom();
        courierClient.create(courier);
        idCourier = courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .statusCode(SC_OK)
                .extract()
                .path("id");
        assertThat(idCourier, notNullValue());
    }

    @Test
    @DisplayName("Авторизация курьера без логина (негатив)")
    @Description("Проверка авторизации без логина")
    public void checkLoginWithoutLogin() {
        courierClient.create(courier);
        CourierCredentials courierCredentials = new CourierCredentials("", courier.getPassword());
        courierClient.login(courierCredentials)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));

    }

    @Test
    @DisplayName("Авторизация курьера без пароля (негатив)")
    @Description("Проверка авторизации без пароля")
    public void checkLoginWithoutPassword() {
        courierClient.create(courier);
        CourierCredentials courierCredentials = new CourierCredentials(courier.getLogin(), "");
        courierClient.login(courierCredentials)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация с несуществующими данными")
    @Description("Проверка авторизации по несуществующим данным")
    public void checkLoginWithNonExistDate() {
        CourierCredentials courierCredentials = new CourierCredentials("45546hrthtrgf", "5657876434");
        courierClient.login(courierCredentials)
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация курьера с некорректным логином (негатив)")
    @Description("Проверка авторизации с некорректным логином")
    public void checkLoginWithIncorrectLogin() {
        courierClient.create(courier);
        CourierCredentials courierCredentials = new CourierCredentials("kakoytoKurier", courier.getPassword());
        courierClient.login(courierCredentials)
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));

    }

    @Test
    @DisplayName("Авторизация курьера с некорректным паролем (негатив)")
    @Description("Проверка авторизации с некорректным паролем")
    public void checkLoginWithIncorrectPassword() {
        courierClient.create(courier);
        CourierCredentials courierCredentials = new CourierCredentials(courier.getLogin(), "kakoytoParol");
        courierClient.login(courierCredentials)
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }






}
