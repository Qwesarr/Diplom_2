package user;

import client.*;
import dto.LoginDto;
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

public class LoginUserTest extends UserClient {
    private String token;
    //тестовые данные для регистрации/входа в систему
    private static TestData testData = new TestData();
    private static UserDto userDto = new UserDto(testData.getName(), testData.getEmail(), testData.getPassword());
    private LoginDto loginDto = new LoginDto(userDto.getEmail(),userDto.getPassword());
    //Сообщения с ошибкой
    private static final String USER_LOGIN_ERROR_TEXT = "email or password are incorrect";

    @Before
    public void setUp() {
        //Получим сразу токен, что бы не нагружать систему запросами, а то она порой падает
        token = registration(userDto).path("accessToken");
    }

    @After
    public void clearData() {
        delete(token);
    }
    @Test
    @DisplayName("Логин под пользователем с верным логином и паролем")
    public void loginUserWithCorrectLoginPasswordTest() {
        Response responseLogin = login(loginDto);                                                                       //Берем логин и пароль
        assertEquals(SC_OK, responseLogin.statusCode());                                                               //Проверка кода ответа
        assertTrue(responseLogin.path("success"));                                                                     //Проверка тела ответа - успех запроса
        assertTrue(responseLogin.path("user").toString().contains(testData.getEmail()));                               //Проверка тела ответа - авторизован пользователь с указанным Email
    }

    @Test
    @DisplayName("Логин под пользователем с не верным логином (не успешно)")
    public void loginUserWithIncorrectLoginCorrectPasswordTest() {
        loginDto.setEmail("TEST" + loginDto.getEmail());                                                               //Меняем логин
        Response responseLogin = login(loginDto);
        assertEquals(SC_UNAUTHORIZED, responseLogin.statusCode());                                                     //Проверка кода ответа
        assertFalse(responseLogin.path("success"));                                                                    //Проверка тела ответа - успех запроса
        assertEquals(USER_LOGIN_ERROR_TEXT,responseLogin.path("message"));                                                //Проверка тела ответа - сообщение с ошибкой
    }

    @Test
    @DisplayName("Логин под пользователем с не верным паролем (не успешно)")
    public void loginUserWithCorrectLoginIncorrectPasswordTest() {
        loginDto.setPassword("TEST" + loginDto.getPassword());                                                          //Меняем пароль
        Response responseLogin = login(loginDto);
        assertEquals(SC_UNAUTHORIZED, responseLogin.statusCode());                                                     //Проверка кода ответа
        assertFalse(responseLogin.path("success"));                                                                    //Проверка тела ответа - успех запроса
        assertEquals(USER_LOGIN_ERROR_TEXT,responseLogin.path("message"));                                                //Проверка тела ответа - сообщение с ошибкой
    }
}
