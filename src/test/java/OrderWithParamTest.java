import client.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import ordermodel.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;

@RunWith(Parameterized.class)
public class OrderWithParamTest {
    private final List<String> color;
    private OrderClient orderClient;
    private int track;
    public OrderWithParamTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Цвет самоката: {0}")
    public static Object[][] getScooterColor() {
        return new Object[][] {
                new List[]{List.of("GRAY")},
                new List[]{List.of("BLACK")},
                new List[]{List.of("GRAY", "BLACK")},
                new List[]{List.of("")},
        };
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @After
    public void cleanUp() {
        orderClient.cancelOrder(track);
    }

    @Test
    @DisplayName("Оформление заказа с разными цветами самокатов")
    @Description("Проверяем корректность размещения заказа с самокатами разных цветов")
    public void createOrderScootersWithDifferentColors() {
        Order order = new Order(color);
        orderClient.create(order)
                .assertThat()
                .statusCode(SC_CREATED)
                .body("track", is(notNullValue()));
    }
}
