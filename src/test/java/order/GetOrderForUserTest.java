package order;

import client.*;
import dto.IngDto;
import dto.UserDto;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import precondition.TestData;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class GetOrderForUserTest extends OrderClient {
    private static final UserClient userClient = new UserClient();
    private String token;
    //тестовые данные для регистрации/входа в систему
    private static final TestData testData = new TestData();
    //Сообщения с ошибкой
    private static final String userOrderErrorText = "You should be authorised";

    @Before
    public void setUp() {
        //Получим сразу токен, что бы не нагружать систему запросами, а то она порой падает
        token = userClient.registration(new UserDto(testData.getName(), testData.getEmail(), testData.getPassword())).path("accessToken");
        //Добавим в заказ 1 ингредиент
        create(new IngDto(testData.getIngredient(0)),token);
    }

    @After
    public void clearData() {
        userClient.delete(token);
    }

    @Test
    @DisplayName("Получаем список заказов пользователя с авторизацией")
    public void getOrderWithAuthTest() {
        Response responseOrder = getOrderClient(token);                                                                 //Запрос списка заказов пользователя
        assertEquals(SC_OK, responseOrder.statusCode());                                                                //Проверка кода ответа
        assertTrue(responseOrder.path("success"));                                                                   //Проверка тела ответа - успех запроса
        assertTrue(responseOrder.path("orders").toString().contains(testData.getIngredient(0)));               //Проверка тела ответа - есть ингредиент
    }

    @Test
    @DisplayName("Получаем список заказов пользователя без авторизации")
    public void getOrderWithoutAuthTest() {
        Response responseOrder = getOrderClient();
        assertEquals(SC_UNAUTHORIZED, responseOrder.statusCode());
        assertFalse(responseOrder.path("success"));
        assertEquals(userOrderErrorText,responseOrder.path("message"));
    }

}
