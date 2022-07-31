package order;

import client.OrderClient;
import client.RestAssuredClient;
import client.UserClient;
import dto.IngDto;
import dto.UserDto;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import precondition.TestData;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class CreateNewOrderWithoutAuthUserTest {
    private static OrderClient orderClient;

    //тестовые данные для регистрации/входа в систему и предустановленными корректными ингредиентами для заказа
    TestData testData = new TestData();

    UserDto userDto = new UserDto(testData.getName(), testData.getEmail(), testData.getPassword());
    IngDto ingDto = new IngDto(testData.getIngredientList());

    //Сообщения с ошибкой
    String userOrderIncorrectIngErrorText = "One or more ids provided are incorrect";
    String userOrderWithoutIngErrorText = "Ingredient ids must be provided";

    @Before
    public void setUp() {
        new OrderClient(new RestAssuredClient());
        orderClient = new OrderClient(new RestAssuredClient());

    }

    @Test
    @DisplayName("Создание нового заказа, с корректными ингредиентами для не авторизированного пользователя ")
    public void createOrderWithCorrectIngForAuthUserTest() {
        Response responseCreateOrder = orderClient.create(ingDto);                                                      //Создаем заказ с тестовым набором ингредиентов
        assertEquals(SC_OK, responseCreateOrder.statusCode());                                                          //Проверяем код ответа
        assertTrue(responseCreateOrder.path("success"));                                                                //Проверка тела ответа - успех запроса
        assertFalse(responseCreateOrder.path("order").toString().contains(testData.getIngredient(0)));                  //Проверяем что в заказе создан пустым (без ингредиента)
        assertFalse(responseCreateOrder.path("order").toString().contains(testData.getEmail()));                        //Проверяем что в заказе не указан пользователь
    }

    @Test
    @DisplayName("Создание нового заказа, без ингредиентов для не авторизированного пользователя (не создается)")
    public void createOrderWithoutIngForAuthUserTest() {
        Response responseCreateOrder = orderClient.create(new IngDto());                                                //Создаем заказ с без набора ингредиентов
        assertEquals(SC_BAD_REQUEST, responseCreateOrder.statusCode());                                                 //Проверяем код ответа
        assertFalse(responseCreateOrder.path("success"));                                                               //Проверка тела ответа - успех запроса
        assertEquals(userOrderWithoutIngErrorText,responseCreateOrder.path("message"));                                 //Проверяем сообщения об ошибке
    }

    @Test
    @DisplayName("Создание нового заказа, с не корректным хештегом ингредиента для не авторизированного пользователя (не создается)")
    public void createOrderWithIncorrectIngForAuthUserTest() {
        Response responseCreateOrder = orderClient.create(new IngDto("000000000000000000000000"));                      //Создаем заказ с не верным хешем ингредиентом
        assertEquals(SC_BAD_REQUEST, responseCreateOrder.statusCode());                                                 //Проверяем код ответа
        assertFalse(responseCreateOrder.path("success"));                                                               //Проверка тела ответа - успех запроса
        assertEquals(userOrderIncorrectIngErrorText,responseCreateOrder.path("message"));                               //Проверяем сообщения об ошибке
    }

    @Test
    @DisplayName("Создание нового заказа, с не корректным хештегом ингредиента для не авторизированного пользователя (не создается)")
    public void createOrderWithInvalidIngForAuthUserTest() {
        Response responseCreateOrder = orderClient.create(new IngDto("TEST"));                                          //Создаем заказ с не валидным хешем (не хеш)
        assertEquals(SC_INTERNAL_SERVER_ERROR, responseCreateOrder.statusCode());                                       //Проверяем код ответа

    }
}
