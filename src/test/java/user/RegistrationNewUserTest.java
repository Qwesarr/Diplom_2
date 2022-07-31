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

public class RegistrationNewUserTest {
    private static UserClient userClient;
    //тестовые данные для регистрации/входа в систему
    TestData testData = new TestData();
    UserDto userDto = new UserDto(testData.getName(), testData.getEmail(), testData.getPassword());
    //Сообщения с ошибкой
    String userExistsText = "User already exists";
    String userCreateErrorText = "Email, password and name are required fields";

    @Before
    public void setUp() {
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
    @DisplayName("Создаем нового пользователя, которого нет еще в системе.")
    public void createNewUserTrueRegistrationTest() {
        Response responseCreate = userClient.registration(userDto);
        assertEquals(SC_OK, responseCreate.statusCode());                                                               //Проверка кода ответа
        assertTrue(responseCreate.path("success"));                                                                     //Проверка тела ответа - успех запроса
        assertTrue(responseCreate.path("user").toString().contains(testData.getEmail()));                               //Проверка тела ответа - создан пользователь с указанным Email
    }

    @Test
    @DisplayName("Создаем нового пользователя, который уже есть в системе (не успешно)")
    public void createPresentUserFalseRegistrationTest() {
        userClient.registration(userDto);                                                                               //Создаем пользователя
        Response responseCreate = userClient.registration(userDto);                                                     //Пытаемся создать еще раз такого же пользователя
        assertEquals(SC_FORBIDDEN, responseCreate.statusCode());                                                        //Проверка кода ответа
        assertFalse(responseCreate.path("success"));                                                                    //Проверка тела ответа - успех запроса
        assertEquals(userExistsText,responseCreate.path("message"));                                                    //Проверка тела ответа - сообщение с ошибкой
    }

    @Test
    @DisplayName("Создаем нового пользователя, без пароля (не успешно)")
    public void createNewWOPassUserFalseRegistrationTest() {
        userDto.setPassword("");
        Response responseCreate = userClient.registration(userDto);                                             //Пытаемся создать еще раз такого же пользователя
        assertEquals(SC_FORBIDDEN, responseCreate.statusCode());                                                        //Проверка кода ответа
        assertFalse(responseCreate.path("success"));                                                                    //Проверка тела ответа - успех запроса
        assertEquals(userCreateErrorText,responseCreate.path("message"));                                               //Проверка тела ответа - сообщение с ошибкой
    }

    @Test
    @DisplayName("Создаем нового пользователя, без имени (не успешно)")
    public void createNewWONameUserFalseRegistrationTest() {
        userDto.setName("");
        Response responseCreate = userClient.registration(userDto);                                             //Пытаемся создать еще раз такого же пользователя
        assertEquals(SC_FORBIDDEN, responseCreate.statusCode());                                                        //Проверка кода ответа
        assertFalse(responseCreate.path("success"));                                                                    //Проверка тела ответа - успех запроса
        assertEquals(userCreateErrorText,responseCreate.path("message"));                                               //Проверка тела ответа - сообщение с ошибкой
    }

    @Test
    @DisplayName("Создаем нового пользователя, без Email (не успешно)")
    public void createNewWOEmailUserFalseRegistrationTest() {
        userDto.setEmail("");
        Response responseCreate = userClient.registration(userDto);                                             //Пытаемся создать еще раз такого же пользователя
        assertEquals(SC_FORBIDDEN, responseCreate.statusCode());                                                        //Проверка кода ответа
        assertFalse(responseCreate.path("success"));                                                                    //Проверка тела ответа - успех запроса
        assertEquals(userCreateErrorText,responseCreate.path("message"));                                               //Проверка тела ответа - сообщение с ошибкой
    }

}
