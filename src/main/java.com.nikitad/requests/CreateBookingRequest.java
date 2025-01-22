package requests;

import dtos.BookingDto;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CreateBookingRequest {

    private static String createBookingUrl = "https://restful-booker.herokuapp.com/booking";
    public static Response createBookingRequest(BookingDto bookingDto) {

        return given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .body(bookingDto)
                .post(createBookingUrl)
                .then().log().all()
                .extract()
                .response();
    }
}
