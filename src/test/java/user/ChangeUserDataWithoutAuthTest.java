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

public class ChangeUserDataWithoutAuthTest {
    private static UserClient userClient;

    //тестовые данные для регистрации/входа в систему
    TestData testData = new TestData();
    UserDto userDto = new UserDto(testData.getName(), testData.getEmail(), testData.getPassword());
    //Сообщения с ошибкой
    String userChangeErrorText = "You should be authorised";

    @Before
    public void setUp() {
        new UserClient(new RestAssuredClient()).registration(userDto);                                                   //Регистрируем нового пользователя в начале теста
        userClient = new UserClient(new RestAssuredClient());
    }

    @After
    public void clearData() {
        try{
            userClient.delete(userClient.login(new UserDto(testData.getEmail(), testData.getPassword())).                //Удаляем пользователя в конце теста (берем не измененные данные)
                    then().
                    statusCode(200).
                    extract().
                    path("accessToken"));}
        catch (AssertionError exception) {
            System.out.println("Пользователь не создан, нечего удалять");                                               // код, который выполнится, если произойдёт исключение AssertionError
        }
    }

    @Test
    @DisplayName("Меняем имя пользователя без авторизации")
    public void changeNameUserWithoutAuthTest() {
        userDto.setName("test" + userDto.getName());                                                                    //Записываем новое имя в UserDto
        Response responseChange = userClient.change(userDto);                                                           //Меняем данные пользователя (меняется имя)
        assertEquals(SC_UNAUTHORIZED, responseChange.statusCode());                                                     //Проверка кода ответа
        assertFalse(responseChange.path("success"));                                                                    //Проверка тела ответа - успех запроса
        assertEquals(userChangeErrorText,responseChange.path("message"));                                               //Проверка сообщения об ошибке
    }

    @Test
    @DisplayName("Меняем имя Email без авторизации")
    public void changeEmailUserWithoutAuthTest() {
        userDto.setEmail("test" + userDto.getEmail());                                                                  //Записываем новое имя в UserDto
        Response responseChange = userClient.change(userDto);                                                           //Меняем данные пользователя (меняется имя)
        assertEquals(SC_UNAUTHORIZED, responseChange.statusCode());                                                     //Проверка кода ответа
        assertFalse(responseChange.path("success"));                                                                    //Проверка тела ответа - успех запроса
        assertEquals(userChangeErrorText,responseChange.path("message"));                                               //Проверка сообщения об ошибке
    }

    @Test
    @DisplayName("Меняем имя пароль без авторизации")
    public void changePasswordUserWithoutAuthTest() {
        userDto.setPassword("test" + userDto.getPassword());                                                            //Записываем новое имя в UserDto
        Response responseChange = userClient.change(userDto);                                                           //Меняем данные пользователя (меняется имя)
        assertEquals(SC_UNAUTHORIZED, responseChange.statusCode());                                                     //Проверка кода ответа
        assertFalse(responseChange.path("success"));                                                                    //Проверка тела ответа - успех запроса
        assertEquals(userChangeErrorText,responseChange.path("message"));                                               //Проверка сообщения об ошибке
    }
}
