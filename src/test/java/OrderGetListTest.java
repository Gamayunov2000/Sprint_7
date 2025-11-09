import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.example.steps.OrderSteps;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.apache.http.HttpStatus.SC_OK;

public class OrderGetListTest {

    private final OrderSteps orderSteps = new OrderSteps();

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Получение списка заказов, проверка наличия списка")
    public void orderGetList() {
        orderSteps.orderGetList()
                .assertThat().body("orders", notNullValue())
                .and()
                .statusCode(SC_OK);
    }

}
