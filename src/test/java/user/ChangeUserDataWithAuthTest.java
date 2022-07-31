package user;

import client.*;
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

public class ChangeUserDataWithAuthTest {
    //private static LoginUser loginUser;
    private static UserClient userClient;

    String token;
    //тестовые данные для регистрации/входа в систему
    TestData testData = new TestData();
    UserDto userDto = new UserDto(testData.getName(), testData.getEmail(), testData.getPassword());
    //Сообщения с ошибкой

    @Before
    public void setUp() {
        new UserClient(new RestAssuredClient()).registration(userDto);                                                  //Регистрируем нового пользователя в начале теста
        userClient = new UserClient(new RestAssuredClient());
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
    @DisplayName("Меняем имя пользователя с авторизацией")
    public void changeNameUserWithAuthTest() {
        userDto.setName("test" + userDto.getName());                                                                    //Записываем новое имя в UserDto
        Response responseChange = userClient.change(userDto,token);                                                     //Меняем данные пользователя (меняется имя)
        assertEquals(SC_OK, responseChange.statusCode());                                                               //Проверка кода ответа
        assertTrue(responseChange.path("success"));                                                                     //Проверка тела ответа - успех запроса
        assertTrue(responseChange.path("user").toString().contains(userDto.getName()));                                 //Проверка тела ответа - имя изменилось
    }

    @Test
    @DisplayName("Меняем имя Email с авторизацией")
    public void changeEmailUserWithAuthTest() {
        userDto.setEmail("test" + userDto.getEmail());                                                                  //Записываем новое имя в UserDto
        Response responseChange = userClient.change(userDto,token);                                                     //Меняем данные пользователя (меняется имя)
        assertEquals(SC_OK, responseChange.statusCode());                                                               //Проверка кода ответа
        assertTrue(responseChange.path("success"));                                                                     //Проверка тела ответа - успех запроса
        assertTrue(responseChange.path("user").toString().contains(userDto.getEmail()));                                //Проверка тела ответа - имя изменилось
    }

    @Test
    @DisplayName("Меняем имя пароль с авторизацией")
    public void changePasswordUserWithAuthTest() {
        userDto.setPassword("test" + userDto.getPassword());                                                            //Записываем новое имя в UserDto
        Response responseChange = userClient.change(userDto,token);                                                     //Меняем данные пользователя (меняется имя)
        assertEquals(SC_OK, responseChange.statusCode());                                                               //Проверка кода ответа
        assertTrue(responseChange.path("success"));                                                                     //Проверка тела ответа - успех запроса
        //логинимся с новым паролем
        Response responseLogin = userClient.login(userDto);
        assertEquals(SC_OK, responseChange.statusCode());                                                               //Проверка, что удается успешно залогиниться с новым паролем
        assertTrue(responseLogin.path("user").toString().contains(userDto.getEmail()));
    }
}
