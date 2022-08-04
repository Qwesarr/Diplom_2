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

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class ChangeUserDataWithAuthTest extends UserClient {
    private String token;
    //тестовые данные для регистрации/входа в систему
    private static TestData testData = new TestData();
    private UserDto userDto = new UserDto(testData.getName(), testData.getEmail(), testData.getPassword());
    private LoginDto loginDto = new LoginDto(userDto.getEmail(),userDto.getPassword());

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
    @DisplayName("Меняем имя пользователя с авторизацией")
    public void changeNameUserWithAuthTest() {
        userDto.setName("test" + userDto.getName());                                                                    //Записываем новое имя в UserDto
        Response responseChange = change(userDto,token);                                                                //Меняем данные пользователя (меняется имя)
        assertEquals(SC_OK, responseChange.statusCode());                                                               //Проверка кода ответа
        assertTrue(responseChange.path("success"));                                                                  //Проверка тела ответа - успех запроса
        assertTrue(responseChange.path("user").toString().contains(userDto.getName()));                              //Проверка тела ответа - имя изменилось
    }

    @Test
    @DisplayName("Меняем имя Email с авторизацией")
    public void changeEmailUserWithAuthTest() {
        userDto.setEmail("test" + userDto.getEmail());                                                                  //Записываем новое имя в UserDto
        Response responseChange = change(userDto,token);                                                                //Меняем данные пользователя (меняется имя)
        assertEquals(SC_OK, responseChange.statusCode());                                                               //Проверка кода ответа
        assertTrue(responseChange.path("success"));                                                                  //Проверка тела ответа - успех запроса
        assertTrue(responseChange.path("user").toString().contains(userDto.getEmail()));                             //Проверка тела ответа - имя изменилось
    }

    @Test
    @DisplayName("Меняем имя пароль с авторизацией")
    public void changePasswordUserWithAuthTest() {
        userDto.setPassword("test" + userDto.getPassword());                                                            //Записываем новый пароль в UserDto
        loginDto.setPassword(userDto.getPassword());                                                                    //так же меняем пароль в LoginDto
        Response responseChange = change(userDto,token);
        assertEquals(SC_OK, responseChange.statusCode());                                                               //Проверка кода ответа
        assertTrue(responseChange.path("success"));                                                                  //Проверка тела ответа - успех запроса
        //логинимся с новым паролем
        Response responseLogin = login(loginDto);
        assertEquals(SC_OK, responseChange.statusCode());                                                               //Проверка, что удается успешно залогиниться с новым паролем
        assertTrue(responseLogin.path("user").toString().contains(userDto.getEmail()));
    }
}
