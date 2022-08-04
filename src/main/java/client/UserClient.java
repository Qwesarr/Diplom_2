package client;

import dto.LoginDto;
import dto.UserDto;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import lombok.extern.java.Log;

@Log
public class UserClient extends RestAssuredClient {
    public Response change(UserDto userDto, @NotNull String token) {
        return patch("auth/user", userDto, token.substring("Bearer ".length()));
    }

    public Response change(UserDto userDto) {
        return patch("auth/user", userDto);
    }

    public void delete(String token) {
        try {
            delete("auth/user", token.substring("Bearer ".length()));
            } catch (NullPointerException e){
            log.log(Level.WARNING,"Не удалось получить токен для пользователя. Нечего удалять. -  " + e);
            }
    }

    public Response login(LoginDto loginDto) {
        return post("auth/login", loginDto);
    }

    public Response registration(UserDto createDto) {
        return post("auth/register", createDto);
    }

}
