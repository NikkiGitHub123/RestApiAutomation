package requests;

import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class GetBookingRequest {

    private static String getBookingUrl = "https://restful-booker.herokuapp.com/booking";

    public static Response getBookingRequestById(int bookingId)
    {
        return given().when().log().all()
                .get(getBookingUrl + "/" + bookingId)
                .then().log().all()
                .extract()
                .response();
    }

    public static Response getBookingRequest()
    {
        return given().when().log().all()
                .get(getBookingUrl)
                .then().log().all()
                .extract()
                .response();
    }

    public static Response getBookingRequestByFirstLastName(Map<String, String> queryParams)
    {
        return given().when().log().all()
                .queryParams(queryParams)
                .get(getBookingUrl)
                .then().log().all()
                .extract()
                .response();
    }

    public static Response getBookingRequestByDates(Map<String, String> queryParams)
    {
        return given().when().log().all()
                .queryParams(queryParams)
                .get(getBookingUrl)
                .then().log().all()
                .extract()
                .response();
    }
}
