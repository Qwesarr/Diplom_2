package order;

import client.OrderClient;
import dto.IngDto;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import precondition.TestData;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class CreateNewOrderWithoutAuthUserTest extends OrderClient {
    private static TestData testData = new TestData();
    private IngDto ingDto = new IngDto();

    //Сообщения с ошибкой
    private static final String USER_ORDER_INCORRECT_ING_ERROR_TEXT = "One or more ids provided are incorrect";
    private static final String USER_ORDER_WITHOUT_ING_ERROR_TEXT = "Ingredient ids must be provided";

    @Test
    @DisplayName("Создание нового заказа, с корректными ингредиентами для не авторизированного пользователя ")
    public void createOrderWithCorrectIngForAuthUserTest() {
        ingDto.addIngredients(testData.getIngredientList());
        Response responseCreateOrder = create(ingDto);                                                                  //Создаем заказ с тестовым набором ингредиентов
        assertEquals(SC_OK, responseCreateOrder.statusCode());                                                          //Проверяем код ответа
        assertTrue(responseCreateOrder.path("success"));                                                             //Проверка тела ответа - успех запроса
        assertFalse(responseCreateOrder.path("order").toString().contains(testData.getIngredient(0)));         //Проверяем что в заказе создан пустым (без ингредиента)
        assertFalse(responseCreateOrder.path("order").toString().contains(testData.getEmail()));                     //Проверяем что в заказе не указан пользователь
    }

    @Test
    @DisplayName("Создание нового заказа, без ингредиентов для не авторизированного пользователя (не создается)")
    public void createOrderWithoutIngForAuthUserTest() {
        Response responseCreateOrder = create(ingDto);                                                                  //Создаем заказ с без набора ингредиентов
        assertEquals(SC_BAD_REQUEST, responseCreateOrder.statusCode());                                                 //Проверяем код ответа
        assertFalse(responseCreateOrder.path("success"));                                                            //Проверка тела ответа - успех запроса
        assertEquals(USER_ORDER_WITHOUT_ING_ERROR_TEXT,responseCreateOrder.path("message"));                              //Проверяем сообщения об ошибке
    }

    @Test
    @DisplayName("Создание нового заказа, с не корректным хештегом ингредиента для не авторизированного пользователя (не создается)")
    public void createOrderWithIncorrectIngForAuthUserTest() {
        ingDto.addIngredients("000000000000000000000000");
        Response responseCreateOrder = create(ingDto);                                                                  //Создаем заказ с не верным хешем ингредиентом
        assertEquals(SC_BAD_REQUEST, responseCreateOrder.statusCode());                                                 //Проверяем код ответа
        assertFalse(responseCreateOrder.path("success"));                                                            //Проверка тела ответа - успех запроса
        assertEquals(USER_ORDER_INCORRECT_ING_ERROR_TEXT,responseCreateOrder.path("message"));                            //Проверяем сообщения об ошибке
    }

    @Test
    @DisplayName("Создание нового заказа, с не корректным хештегом ингредиента для не авторизированного пользователя (не создается)")
    public void createOrderWithInvalidIngForAuthUserTest() {
        ingDto.addIngredients("TEST");
        Response responseCreateOrder = create(ingDto);                                                                  //Создаем заказ с не валидным хешем (не хеш)
        assertEquals(SC_INTERNAL_SERVER_ERROR, responseCreateOrder.statusCode());                                       //Проверяем код ответа

    }
}
