package client;

import dto.IngDto;
import dto.UserDto;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;

public class OrderClient {
    private final RestAssuredClient restAssuredClient;

    public OrderClient(RestAssuredClient restAssuredClient) {
        this.restAssuredClient = restAssuredClient;
    }


    public Response create(IngDto ingDto, @NotNull String token) {
        return restAssuredClient.post("orders", ingDto, token.substring("Bearer ".length()));
    }

    public Response create(IngDto ingDto) {
        return restAssuredClient.post("orders", ingDto);
    }

    public Response getOrderClient(@NotNull String token) {
        return restAssuredClient.get("orders", token.substring("Bearer ".length()));
    }

    public Response getOrderClient() {
        return restAssuredClient.get("orders");
    }

    public Response getAllOrderClient() {
        return restAssuredClient.get("orders/all");
    }
}
