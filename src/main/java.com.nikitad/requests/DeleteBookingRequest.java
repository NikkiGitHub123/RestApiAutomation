package requests;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class DeleteBookingRequest {

    private static String deleteBookingUrl = "https://restful-booker.herokuapp.com/booking/";

    public static Response deleteBookingRequest(int bookingId, String token)
    {
        return given()
                .when().log().all()
                .header("Cookie", "token=" + token)
                .delete(deleteBookingUrl+ bookingId)
                .then().log().all()
                .extract()
                .response();
    }
}
