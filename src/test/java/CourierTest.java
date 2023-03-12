import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;

import couriermodel.Courier;
import client.CourierClient;
import couriermodel.CourierCredentials;
import couriermodel.CourierGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;


public class CourierTest {

    private CourierClient courierClient;
    private int idCourier;

    @Before
    public void setUp() {
       courierClient = new CourierClient();
    }

    @After
    public void cleanUp() {
        courierClient.delete(idCourier);
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Проверка, что курьер успешно создается")
    public void courierCanBeCreatedWithValidDate() {
        Courier courier = CourierGenerator.getRandom();

        courierClient.create(courier)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .assertThat()
                .body("ok", is(true));

        idCourier = courierClient.login(CourierCredentials.from(courier))
                .extract()
                .path("id");
        assertThat(idCourier, notNullValue());
    }

    @Test
    @DisplayName("Создание двух курьеров с одинаковыми логинами")
    @Description("Проверка невозможности создания курьеров с одинаковыми логинами")
    public void notNotBeCreatedTwoEqualCourier() {
        Courier courier = new Courier("Sergay", "dfgdf", "Сергей");
        courierClient.create(courier);
        courierClient.create(courier)
                .statusCode(SC_CONFLICT)
                .and()
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Проверка невозможности создания курьера без логина")
    public void notBeCreatedWithoutLogin() {
        Courier courier = new Courier("", "dfgаdf", "Иван");
        courierClient.create(courier)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверка невозможности создания курьера без пароля")
    public void notBeCreatedWithoutPassword() {
        Courier courier = new Courier("Petr", "", "Петр");
        courierClient.create(courier)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без имени")
    @Description("Проверка возможности создания курьера без имени")
    public void notBeCreatedWithoutFirstName() {
        Courier courier = CourierGenerator.getRandomWithoutFirstName();
        courierClient.create(courier)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .assertThat().body("ok", is(true));
    }
}
