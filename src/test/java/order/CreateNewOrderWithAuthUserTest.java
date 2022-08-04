package order;

import client.OrderClient;
import client.UserClient;
import dto.IngDto;
import dto.UserDto;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import precondition.TestData;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class CreateNewOrderWithAuthUserTest extends OrderClient {
    private static final UserClient userClient = new UserClient();
    private String token;
    //тестовые данные для регистрации/входа в систему и предустановленными корректными ингредиентами для заказа
    private static final TestData testData = new TestData();
    private final IngDto ingDto = new IngDto();

    //Сообщения с ошибкой
    private static final String userOrderIncorrectIngErrorText = "One or more ids provided are incorrect";
    private static final String userOrderWithoutIngErrorText = "Ingredient ids must be provided";

    @Before
    public void setUp() {
        //Получим сразу токен, что бы не нагружать систему запросами, а то она порой падает
        token = userClient.registration(new UserDto(testData.getName(), testData.getEmail(), testData.getPassword())).path("accessToken");
    }

    @After
    public void clearData() {
        userClient.delete(token);
    }

    @Test
    @DisplayName("Создание нового заказа, с корректными ингредиентами для авторизированного пользователя")
    public void createOrderWithCorrectIngForAuthUserTest() {
        ingDto.addIngredients(testData.getIngredientList());                                                            //Заполняем список ингридиентов тестовыми, валидными данными
        Response responseCreateOrder = create(ingDto,token);                                                            //Создаем заказ с тестовым набором ингредиентов
        assertEquals(SC_OK, responseCreateOrder.statusCode());                                                          //Проверяем код ответа
        assertTrue(responseCreateOrder.path("success"));                                                             //Проверка тела ответа - успех запроса
        assertTrue(responseCreateOrder.path("order").toString().contains(testData.getIngredient(0)));           //Проверяем что в заказе есть ингредиент
        assertTrue(responseCreateOrder.path("order").toString().contains(testData.getEmail()));                      //Проверяем что в заказе указан пользователь
    }

    @Test
    @DisplayName("Создание нового заказа, без ингредиентов для авторизированного пользователя (не создается)")
    public void createOrderWithoutIngForAuthUserTest() {
        Response responseCreateOrder = create(ingDto);                                                                  //Создаем заказ без набора ингредиентов
        assertEquals(SC_BAD_REQUEST, responseCreateOrder.statusCode());                                                 //Проверяем код ответа
        assertFalse(responseCreateOrder.path("success"));                                                            //Проверка тела ответа - успех запроса
        assertEquals(userOrderWithoutIngErrorText,responseCreateOrder.path("message"));                              //Проверяем сообщения об ошибке
    }

    @Test
    @DisplayName("Создание нового заказа, с не корректным хештегом ингредиента для авторизированного пользователя (не создается)")
    public void createOrderWithIncorrectIngForAuthUserTest() {
        ingDto.addIngredients("000000000000000000000000");                                                              //Добавляем в dto ингридент, которого нет в списке ингридиентов
        Response responseCreateOrder = create(ingDto,token);                                                            //Создаем заказ с не верным хешем ингредиентом
        assertEquals(SC_BAD_REQUEST, responseCreateOrder.statusCode());                                                 //Проверяем код ответа
        assertFalse(responseCreateOrder.path("success"));                                                            //Проверка тела ответа - успех запроса
        assertEquals(userOrderIncorrectIngErrorText,responseCreateOrder.path("message"));                            //Проверяем сообщения об ошибке
    }

    @Test
    @DisplayName("Создание нового заказа, с не корректным хештегом ингредиента для авторизированного пользователя (не создается)")
    public void createOrderWithInvalidIngForAuthUserTest() {
        ingDto.addIngredients("TEST");                                                                                  //Добавляем в dto не валидный хеш (не хеш)
        Response responseCreateOrder = create(ingDto,token);                                                            //Создаем заказ с не валидным хешем (не хеш)
        assertEquals(SC_INTERNAL_SERVER_ERROR, responseCreateOrder.statusCode());                                       //Проверяем код ответа

    }
}
