package requests;

import dtos.BookingDto;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UpdateBookingRequest {

    private static String updateBookingUrl = "https://restful-booker.herokuapp.com/booking/";

    public static Response updateBookingRequest(int bookingId, BookingDto bookingDto, String token) {

        return given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .body(bookingDto)
                .put(updateBookingUrl + bookingId)
                .then().log().all()
                .extract()
                .response();
    }
}
