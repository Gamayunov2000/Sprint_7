package org.example.steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.example.constants.ApiEndpoint;
import org.example.pojo.OrderCreateRequest;

import static io.restassured.RestAssured.given;
import static org.example.constants.ApiEndpoint.*;

public class OrderSteps {

    public static RequestSpecification requestSpecification() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(ApiEndpoint.BASE_URL);
    }

    @Step("Создание нового заказа")
    public ValidatableResponse orderCreate(OrderCreateRequest orderCreateRequest) {
        return requestSpecification()
                .body(orderCreateRequest)
                .post(ORDER_POST_CREATE)
                .then();
    }

    @Step("Получение списка заказов")
    public ValidatableResponse orderGetList() {
        return requestSpecification()
                .get(ORDER_GET_LIST)
                .then();
    }

    @Step("Удаление заказа по треку")
    public void orderDelete(int track) {
        requestSpecification()
                .queryParam("track", track)
                .put(ORDER_PUT_CANCEL)
                .then()
                .log().all();
    }
}
