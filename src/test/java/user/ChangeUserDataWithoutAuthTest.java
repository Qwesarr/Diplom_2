package user;

import client.*;
import dto.UserDto;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import precondition.TestData;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class ChangeUserDataWithoutAuthTest extends UserClient {
    private String token;
    //тестовые данные для регистрации/входа в систему
    private static final TestData testData = new TestData();
    private static final UserDto userDto = new UserDto(testData.getName(), testData.getEmail(), testData.getPassword());
    //Сообщения с ошибкой
    private static final String userChangeErrorText = "You should be authorised";

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
    @DisplayName("Меняем имя пользователя без авторизации")
    public void changeNameUserWithoutAuthTest() {
        userDto.setName("test" + userDto.getName());                                                                    //Записываем новое имя в UserDto
        Response responseChange = change(userDto);                                                                      //Меняем данные пользователя (меняется имя)
        assertEquals(SC_UNAUTHORIZED, responseChange.statusCode());                                                     //Проверка кода ответа
        assertFalse(responseChange.path("success"));                                                                    //Проверка тела ответа - успех запроса
        assertEquals(userChangeErrorText,responseChange.path("message"));                                               //Проверка сообщения об ошибке
    }

    @Test
    @DisplayName("Меняем имя Email без авторизации")
    public void changeEmailUserWithoutAuthTest() {
        userDto.setEmail("test" + userDto.getEmail());                                                                  //Записываем новое имя в UserDto
        Response responseChange = change(userDto);                                                                     //Меняем данные пользователя (меняется имя)
        assertEquals(SC_UNAUTHORIZED, responseChange.statusCode());                                                     //Проверка кода ответа
        assertFalse(responseChange.path("success"));                                                                    //Проверка тела ответа - успех запроса
        assertEquals(userChangeErrorText,responseChange.path("message"));                                               //Проверка сообщения об ошибке
    }

    @Test
    @DisplayName("Меняем имя пароль без авторизации")
    public void changePasswordUserWithoutAuthTest() {
        userDto.setPassword("test" + userDto.getPassword());                                                            //Записываем новое имя в UserDto
        Response responseChange = change(userDto);                                                                       //Меняем данные пользователя (меняется имя)
        assertEquals(SC_UNAUTHORIZED, responseChange.statusCode());                                                     //Проверка кода ответа
        assertFalse(responseChange.path("success"));                                                                    //Проверка тела ответа - успех запроса
        assertEquals(userChangeErrorText,responseChange.path("message"));                                               //Проверка сообщения об ошибке
    }
}
