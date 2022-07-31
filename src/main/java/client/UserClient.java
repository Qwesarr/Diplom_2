package client;

import dto.UserDto;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;

public class UserClient {
    private final RestAssuredClient restAssuredClient;

    public UserClient(RestAssuredClient restAssuredClient) {
        this.restAssuredClient = restAssuredClient;
    }

    public Response change(UserDto userDto, @NotNull String token) {
        return restAssuredClient.patch("auth/user", userDto, token.substring("Bearer ".length()));
    }

    public Response change(UserDto userDto) {
        return restAssuredClient.patch("auth/user", userDto);
    }

    public Response delete(@NotNull String token) {
        return restAssuredClient.delete("auth/user", token.substring("Bearer ".length()));
    }

    public Response login(UserDto userDto) {
        return restAssuredClient.post("auth/login", userDto);
    }

    public Response registration(UserDto createDto) {
        return restAssuredClient.post("auth/register", createDto);
    }

}
