package client;

import dto.IngDto;
import dto.UserDto;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;

public class OrderClient extends RestAssuredClient{

    public Response create(IngDto ingDto, @NotNull String token) {
        return post("orders", ingDto, token.substring("Bearer ".length()));
    }

    public Response create(IngDto ingDto) {
        return post("orders", ingDto);
    }

    public Response getOrderClient(@NotNull String token) {
        return get("orders", token.substring("Bearer ".length()));
    }

    public Response getOrderClient() {
        return get("orders");
    }

}
