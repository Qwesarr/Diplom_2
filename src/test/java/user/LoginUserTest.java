package user;

import client.*;
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

public class LoginUserTest {
    private static UserClient userClient;
    //тестовые данные для регистрации/входа в систему
    TestData testData = new TestData();
    UserDto userDto = new UserDto(testData.getName(), testData.getEmail(), testData.getPassword());
    //Сообщения с ошибкой
    String userLoginErrorText = "email or password are incorrect";

    @Before
    public void setUp() {
        new UserClient(new RestAssuredClient()).registration(userDto);                                                  //Регистрируем нового пользователя в начале теста
        userClient = new UserClient(new RestAssuredClient());
    }

    @After
    public void clearData() {
        try{
            userClient.delete(userClient.login(new UserDto(userDto.getEmail(), userDto.getPassword())).                      //Удаляем пользователя в конце теста
                    then().
                    statusCode(200).
                    extract().
                    path("accessToken"));}
        catch (AssertionError exception) {
            System.out.println("Пользователь не создан, нечего удалять");                                               // код, который выполнится, если произойдёт исключение AssertionError
        }
    }
    @Test
    @DisplayName("Логин под пользователем с верным логином и паролем")
    public void loginUserWithCorrectLoginPasswordTest() {
        Response responseLogin = userClient.login(new UserDto(userDto.getEmail(), userDto.getPassword()));              //Берем логин и пароль из ранее созданного DTO для создания нового пользователя
        assertEquals(SC_OK, responseLogin.statusCode());                                                               //Проверка кода ответа
        assertTrue(responseLogin.path("success"));                                                                     //Проверка тела ответа - успех запроса
        assertTrue(responseLogin.path("user").toString().contains(testData.getEmail()));                               //Проверка тела ответа - авторизован пользователь с указанным Email
    }

    @Test
    @DisplayName("Логин под пользователем с не верным логином (не успешно)")
    public void loginUserWithIncorrectLoginCorrectPasswordTest() {
        Response responseLogin = userClient.login(new UserDto("TEST"+ userDto.getEmail(), userDto.getPassword()));      //Берем логин и пароль из ранее созданного DTO для создания нового пользователя. Меняем логин.
        assertEquals(SC_UNAUTHORIZED, responseLogin.statusCode());                                                     //Проверка кода ответа
        assertFalse(responseLogin.path("success"));                                                                    //Проверка тела ответа - успех запроса
        assertEquals(userLoginErrorText,responseLogin.path("message"));                                                //Проверка тела ответа - сообщение с ошибкой
    }

    @Test
    @DisplayName("Логин под пользователем с не верным паролем (не успешно)")
    public void loginUserWithCorrectLoginIncorrectPasswordTest() {
        Response responseLogin = userClient.login(new UserDto(userDto.getEmail(),"TEST"+ userDto.getPassword()));       //Берем логин и пароль из ранее созданного DTO для создания нового пользователя. Меняем пароль.
        assertEquals(SC_UNAUTHORIZED, responseLogin.statusCode());                                                     //Проверка кода ответа
        assertFalse(responseLogin.path("success"));                                                                    //Проверка тела ответа - успех запроса
        assertEquals(userLoginErrorText,responseLogin.path("message"));                                                //Проверка тела ответа - сообщение с ошибкой
    }
}
