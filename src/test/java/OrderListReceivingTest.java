import client.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderListReceivingTest {

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка возможности получения списка заказов")
    public void getOrderList() {
        OrderClient orderClient = new OrderClient();
        orderClient.getOrderList()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("orders", notNullValue());
    }
}
