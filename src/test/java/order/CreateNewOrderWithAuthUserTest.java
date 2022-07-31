package order;

import client.OrderClient;
import client.RestAssuredClient;
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

public class CreateNewOrderWithAuthUserTest {
    private static UserClient userClient;
    private static OrderClient orderClient;
    String token;
    //тестовые данные для регистрации/входа в систему и предустановленными корректными ингредиентами для заказа
    TestData testData = new TestData();
    UserDto userDto = new UserDto(testData.getName(), testData.getEmail(), testData.getPassword());


    //Сообщения с ошибкой
    String userOrderIncorrectIngErrorText = "One or more ids provided are incorrect";
    String userOrderWithoutIngErrorText = "Ingredient ids must be provided";

    @Before
    public void setUp() {
        new UserClient(new RestAssuredClient()).registration(userDto);                                                  //Регистрируем нового пользователя в начале теста
        new OrderClient(new RestAssuredClient());
        userClient = new UserClient(new RestAssuredClient());
        orderClient = new OrderClient(new RestAssuredClient());
        token = userClient.login(new UserDto(userDto.getEmail(), userDto.getPassword())).                               //Получаем токен пользователя
                then().
                extract().
                path("accessToken");
    }

    @After
    public void clearData() {
        try{
            userClient.delete(userClient.login(new UserDto(userDto.getEmail(), userDto.getPassword())).                      //Удаляем пользователя в конце теста
                    then().
                    extract().
                    path("accessToken"));}
        catch (AssertionError exception) {
            System.out.println("Пользователь не создан, нечего удалять");                                               // код, который выполнится, если произойдёт исключение AssertionError
        }
    }

    @Test
    @DisplayName("Создание нового заказа, с корректными ингредиентами для авторизированного пользователя")
    public void createOrderWithCorrectIngForAuthUserTest() {
        IngDto ingDto = new IngDto(testData.getIngredientList());
        Response responseCreateOrder = orderClient.create(ingDto,token);                                                      //Создаем заказ с тестовым набором ингредиентов
        assertEquals(SC_OK, responseCreateOrder.statusCode());                                                                //Проверяем код ответа
        assertTrue(responseCreateOrder.path("success"));                                                                      //Проверка тела ответа - успех запроса
        assertTrue(responseCreateOrder.path("order").toString().contains(testData.getIngredient(0)));                         //Проверяем что в заказе есть ингредиент
        assertTrue(responseCreateOrder.path("order").toString().contains(testData.getEmail()));                               //Проверяем что в заказе указан пользователь
    }

    @Test
    @DisplayName("Создание нового заказа, без ингредиентов для авторизированного пользователя (не создается)")
    public void createOrderWithoutIngForAuthUserTest() {
        Response responseCreateOrder = orderClient.create(new IngDto(),token);                                                //Создаем заказ с без набора ингредиентов
        assertEquals(SC_BAD_REQUEST, responseCreateOrder.statusCode());                                                       //Проверяем код ответа
        assertFalse(responseCreateOrder.path("success"));                                                                     //Проверка тела ответа - успех запроса
        assertEquals(userOrderWithoutIngErrorText,responseCreateOrder.path("message"));                                       //Проверяем сообщения об ошибке
    }

    @Test
    @DisplayName("Создание нового заказа, с не корректным хештегом ингредиента для авторизированного пользователя (не создается)")
    public void createOrderWithIncorrectIngForAuthUserTest() {
        Response responseCreateOrder = orderClient.create(new IngDto("000000000000000000000000"),token);                      //Создаем заказ с не верным хешем ингредиентом
        assertEquals(SC_BAD_REQUEST, responseCreateOrder.statusCode());                                                       //Проверяем код ответа
        assertFalse(responseCreateOrder.path("success"));                                                                     //Проверка тела ответа - успех запроса
        assertEquals(userOrderIncorrectIngErrorText,responseCreateOrder.path("message"));                                     //Проверяем сообщения об ошибке
    }

    @Test
    @DisplayName("Создание нового заказа, с не корректным хештегом ингредиента для авторизированного пользователя (не создается)")
    public void createOrderWithInvalidIngForAuthUserTest() {
        Response responseCreateOrder = orderClient.create(new IngDto("TEST"),token);                                          //Создаем заказ с не валидным хешем (не хеш)
        assertEquals(SC_INTERNAL_SERVER_ERROR, responseCreateOrder.statusCode());                                             //Проверяем код ответа

    }
}
