import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.example.steps.CourierSteps;
import org.example.pojo.CourierCreateRequest;
import org.example.pojo.CourierLoginRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.apache.http.HttpStatus.*;

public class CourierLoginTest {

    private final String login = "jonsmit";
    private final String password = "qwerty123";
    private final String firstName = "Джон";
    private final CourierSteps courierSteps = new CourierSteps();
    private final CourierCreateRequest courierCreateRequest = new CourierCreateRequest(login, password, firstName);
    private final CourierLoginRequest courierLoginRequest = new CourierLoginRequest(login, password);
    private boolean courierCreated = false;

    @Before
    public void setUp() {
        courierSteps.courierCreate(courierCreateRequest);
        courierCreated = true;
    }

    @After
    public void tearDown() {
        if (courierCreated) {
            courierSteps.courierDeleteAfterLogin(courierLoginRequest);
        }
    }

    @Test
    @DisplayName("Авторизация курьера")
    @Description("Проверка, что курьер может авторизоваться с набором валидных данных")
    public void loginCourier() {
        courierSteps.courierLogin(courierLoginRequest)
                .assertThat().body("id", instanceOf(Integer.class))
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Авторизация курьера без логина")
    @Description("Проверка, что курьер НЕ может авторизоваться без передачи поля login")
    public void loginCourierWithoutLogin() {
        CourierLoginRequest invalidLogin = new CourierLoginRequest(null, password);

        courierSteps.courierLogin(invalidLogin)
                .assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Авторизация курьера без пароля")
    @Description("Проверка, что курьер НЕ может авторизоваться без передачи поля password")
    public void loginCourierWithoutPassword() {
        CourierLoginRequest invalidPassword = new CourierLoginRequest(login, null);

        courierSteps.courierLogin(invalidPassword)
                .assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Авторизация курьера, используя несуществующие данные")
    @Description("Проверка, что курьер НЕ может авторизоваться, используя несуществующие данные для входа")
    public void loginCourierWithNonExistentCredential() {
        CourierLoginRequest invalidCreds = new CourierLoginRequest("nonexistent", "wrongpass");

        courierSteps.courierLogin(invalidCreds)
                .assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(SC_NOT_FOUND);
    }
}