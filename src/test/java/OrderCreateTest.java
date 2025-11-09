import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.example.pojo.OrderCreateRequest;
import org.example.steps.OrderSteps;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.apache.http.HttpStatus.SC_CREATED;

@RunWith(Parameterized.class)
public class OrderCreateTest {

    private List<String> color;
    private final OrderSteps orderSteps = new OrderSteps();
    private Integer createdTrack = null;

    public OrderCreateTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters (name = "Цвет самоката - {0}")
    public static Object[][] dataGen() {
        return new Object[][] {
                {List.of("BLACK", "GREY")},
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of()}
        };
    }

    @After
    public void tearDown() {
        if (createdTrack != null) {
            orderSteps.orderDelete(createdTrack);
        }
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Создание заказа с самокатами разных цветов через параметризованный тест")
    public void orderCreate() {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(color);

        createdTrack = orderSteps.orderCreate(orderCreateRequest)
                .assertThat().body("track", instanceOf(Integer.class))
                .and()
                .statusCode(SC_CREATED)
                .extract()
                .path("track");
    }

}
