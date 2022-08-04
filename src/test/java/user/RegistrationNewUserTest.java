package user;

import client.*;
import dto.LoginDto;
import dto.UserDto;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import precondition.TestData;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class RegistrationNewUserTest extends UserClient {
    //тестовые данные для регистрации/входа в систему
    private static TestData testData = new TestData();
    private UserDto userDto = new UserDto(testData.getName(), testData.getEmail(), testData.getPassword());
    //Сообщения с ошибкой
    private static final String USER_ALREADY_EXISTS_ERROR = "User already exists";
    private static final String REQUIRED_FIELDS_ERROR = "Email, password and name are required fields";


    @After
    public void clearData() {
        //Получаем данные для LoginDto из актуальных для теста данных UserDto
        delete(login(new LoginDto(userDto.getEmail(), userDto.getPassword())).path("accessToken"));
     }

    @Test
    @DisplayName("Создаем нового пользователя, которого нет еще в системе.")
    public void createNewUserTrueRegistrationTest() {
        Response responseCreate = registration(userDto);
        assertEquals(SC_OK, responseCreate.statusCode());                                                               //Проверка кода ответа
        assertTrue(responseCreate.path("success"));                                                                  //Проверка тела ответа - успех запроса
        assertTrue(responseCreate.path("user").toString().contains(userDto.getEmail()));                             //Проверка тела ответа - создан пользователь с указанным Email
    }

    @Test
    @DisplayName("Создаем нового пользователя, который уже есть в системе (не успешно)")
    public void createPresentUserFalseRegistrationTest() {
        registration(userDto);                                                                                          //Создаем пользователя
        Response responseCreate = registration(userDto);                                                                //Пытаемся создать еще раз такого же пользователя
        assertEquals(SC_FORBIDDEN, responseCreate.statusCode());                                                        //Проверка кода ответа
        assertFalse(responseCreate.path("success"));                                                                 //Проверка тела ответа - успех запроса
        assertEquals(USER_ALREADY_EXISTS_ERROR,responseCreate.path("message"));                                                 //Проверка тела ответа - сообщение с ошибкой
    }

    @Test
    @DisplayName("Создаем нового пользователя, без пароля (не успешно)")
    public void createNewWOPassUserFalseRegistrationTest() {
        userDto.setPassword("");
        Response responseCreate = registration(userDto);                                                                //Пытаемся создать еще раз такого же пользователя
        assertEquals(SC_FORBIDDEN, responseCreate.statusCode());                                                        //Проверка кода ответа
        assertFalse(responseCreate.path("success"));                                                                 //Проверка тела ответа - успех запроса
        assertEquals(REQUIRED_FIELDS_ERROR,responseCreate.path("message"));                                            //Проверка тела ответа - сообщение с ошибкой
    }

    @Test
    @DisplayName("Создаем нового пользователя, без имени (не успешно)")
    public void createNewWONameUserFalseRegistrationTest() {
        userDto.setName("");
        Response responseCreate = registration(userDto);                                                                //Пытаемся создать еще раз такого же пользователя
        assertEquals(SC_FORBIDDEN, responseCreate.statusCode());                                                        //Проверка кода ответа
        assertFalse(responseCreate.path("success"));                                                                 //Проверка тела ответа - успех запроса
        assertEquals(REQUIRED_FIELDS_ERROR,responseCreate.path("message"));                                            //Проверка тела ответа - сообщение с ошибкой
    }

    @Test
    @DisplayName("Создаем нового пользователя, без Email (не успешно)")
    public void createNewWOEmailUserFalseRegistrationTest() {
        userDto.setEmail("");
        Response responseCreate = registration(userDto);                                                                //Пытаемся создать еще раз такого же пользователя
        assertEquals(SC_FORBIDDEN, responseCreate.statusCode());                                                        //Проверка кода ответа
        assertFalse(responseCreate.path("success"));                                                                 //Проверка тела ответа - успех запроса
        assertEquals(REQUIRED_FIELDS_ERROR,responseCreate.path("message"));                                            //Проверка тела ответа - сообщение с ошибкой
    }

}
