package requests;


import dtos.AuthDto;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CreateAuthRequest {

    private static String authUrl = "https://restful-booker.herokuapp.com/auth";

    public static Response authRequest(AuthDto authDto) {

        return given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .body(authDto)
                .post(authUrl)
                .then().log().all()
                .extract()
                .response();
    }
}
