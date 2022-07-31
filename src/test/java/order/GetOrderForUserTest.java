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

public class GetOrderForUserTest {
    private static UserClient userClient;
    private static OrderClient orderClient;

    String token;
    //тестовые данные для регистрации/входа в систему
    TestData testData = new TestData();
    UserDto userDto = new UserDto(testData.getName(), testData.getEmail(), testData.getPassword());
    IngDto ingDto = new IngDto();
    //IngDto ingDto = new IngDto(testData.getIngredientsList());
    //Сообщения с ошибкой
    String userOrderErrorText = "You should be authorised";

    @Before
    public void setUp() {
        new UserClient(new RestAssuredClient()).registration(userDto);                                                  //Регистрируем нового пользователя в начале теста
        new OrderClient(new RestAssuredClient());
        userClient = new UserClient(new RestAssuredClient());
        orderClient = new OrderClient(new RestAssuredClient());

        token = userClient.login(new UserDto(userDto.getEmail(), userDto.getPassword())).                               //Получаем токен пользователя
                then().
                statusCode(200).
                extract().
                path("accessToken");
    }

    @After
    public void clearData() {
        userClient.delete(token);
    }

    @Test
    @DisplayName("Получаем список заказов пользователя с авторизацией")
    public void getOrderWithAuthTest() {
        Response responseOrder = orderClient.getOrderClient(token);                                                     //Запрос списка заказов пользователя
        assertEquals(SC_OK, responseOrder.statusCode());                                                                //Проверка кода ответа
        assertTrue(responseOrder.path("success"));                                                                      //Проверка тела ответа - успех запроса
        assertTrue(responseOrder.path("").toString().contains("orders"));                                               //Проверка тела ответа -
    }

    @Test
    @DisplayName("Получаем список заказов пользователя без авторизации")
    public void getOrderWithoutAuthTest() {
        Response responseOrder = orderClient.getOrderClient();
        assertEquals(SC_UNAUTHORIZED, responseOrder.statusCode());
        assertFalse(responseOrder.path("success"));
        assertEquals(userOrderErrorText,responseOrder.path("message"));
    }

}
