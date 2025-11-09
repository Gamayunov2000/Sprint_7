import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.example.steps.CourierSteps;
import org.example.pojo.CourierCreateRequest;
import org.example.pojo.CourierLoginRequest;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.apache.http.HttpStatus.*;

public class CourierCreateTest {

    private final String login = "jonsmit";
    private final String password = "qwerty123";
    private final String firstName = "Джон";
    private final CourierSteps courierSteps = new CourierSteps();
    private final CourierLoginRequest courierLoginRequest = new CourierLoginRequest(login, password);
    private boolean courierCreated = false;

    @After
    public void tearDown() {
        if (courierCreated) {
            courierSteps.courierDeleteAfterLogin(courierLoginRequest);
        }
    }

    @Test
    @DisplayName("Создание нового курьера")
    @Description("Проверяем, что курьера можно создать с валидными данными")
    public void createNewCourier() {
        CourierCreateRequest courierCreateRequest = new CourierCreateRequest(login, password, firstName);

        courierSteps.courierCreate(courierCreateRequest)
                .assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(SC_CREATED);

        courierCreated = true;
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    @Description("Попытка создать двух курьеров с одинаковым набором данных. Создание второго курьера должно провалиться")
    public void createTwoIdenticalCouriers() {
        CourierCreateRequest courierCreateRequest = new CourierCreateRequest(login, password, firstName);

        courierSteps.courierCreate(courierCreateRequest)
                .assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(SC_CREATED);

        courierCreated = true;

        courierSteps.courierCreate(courierCreateRequest)
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(SC_CONFLICT);
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Попытка создать курьера без передачи поля login. Создание курьера должно провалиться")
    public void createCourierWithoutLogin() {
        CourierCreateRequest courierCreateRequest = new CourierCreateRequest(null, password, firstName);

        courierSteps.courierCreate(courierCreateRequest)
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Попытка создать курьера без передачи поля password. Создание курьера должно провалиться")
    public void createCourierWithoutPassword() {
        CourierCreateRequest courierCreateRequest = new CourierCreateRequest(login, null, firstName);

        courierSteps.courierCreate(courierCreateRequest)
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }
}