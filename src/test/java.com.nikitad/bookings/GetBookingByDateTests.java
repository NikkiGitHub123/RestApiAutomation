package bookings;

import dtos.AuthDto;
import dtos.BookingDto;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import requests.CreateAuthRequest;
import requests.CreateBookingRequest;
import requests.DeleteBookingRequest;
import requests.GetBookingRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingByDateTests {

    private static int bookingId = 0;
    private static BookingDto bookingDto = new BookingDto();
    private Map<String, String> queryParams;


    @BeforeMethod
    void createBooking()
    {
        queryParams = new HashMap<>();
        BookingDto bookingDto = new BookingDto();
        Response createBookingResponse = CreateBookingRequest.createBookingRequest(bookingDto);
        JsonPath createBookingJsonPath = createBookingResponse.jsonPath();
        bookingId = createBookingJsonPath.getInt("bookingid");
    }

    @Test
    public void getBookingByDateReturnsRightBooking() {
        //        Dates in the booking:
        //        checkin = "2025-10-10";
        //        checkout="2025-10-20";
        queryParams.put("checkin", "2025-10-09");
        queryParams.put("checkout", bookingDto.getBookingdates().getCheckout());
        Response getBookingResponse = GetBookingRequest.getBookingRequestByDates(queryParams);
        JsonPath getBookingResponseJson = getBookingResponse.jsonPath();

        assertThat(getBookingResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);

        List<Integer> bookingIdsInResponse = getBookingResponseJson.getList("bookingid", Integer.class);
        assertThat(bookingIdsInResponse).contains(bookingId);
    }

    @Test
    public void getBookingByCheckinDateReturnsRightBooking() {
        //        Dates in the booking:
        //        checkin = "2025-10-10";
        //        checkout="2025-10-20";
        queryParams.put("checkin", "2025-10-09");
        Response getBookingResponse = GetBookingRequest.getBookingRequestByDates(queryParams);
        JsonPath getBookingResponseJson = getBookingResponse.jsonPath();

        assertThat(getBookingResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);

        List<Integer> bookingIdsInResponse = getBookingResponseJson.getList("bookingid", Integer.class);
        assertThat(bookingIdsInResponse).contains(bookingId);
    }

    @Test
    public void getBookingByCheckoutDateReturnsRightBooking() {
        //        Dates in the booking:
        //        checkin = "2025-10-10";
        //        checkout="2025-10-20";
        queryParams.put("checkout", "2025-10-20");
        Response getBookingResponse = GetBookingRequest.getBookingRequestByDates(queryParams);
        JsonPath getBookingResponseJson = getBookingResponse.jsonPath();

        assertThat(getBookingResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);

        List<Integer> bookingIdsInResponse = getBookingResponseJson.getList("bookingid", Integer.class);
        assertThat(bookingIdsInResponse).contains(bookingId);
    }

    @Test
    public void getBookingOutsideRangeDoesNotReturnBooking()
    {
        //        Dates in the booking:
        //        checkin = "2025-10-10";
        //        checkout="2025-10-20";
        queryParams.put("checkin", "2025-10-30");
        Response getBookingResponse = GetBookingRequest.getBookingRequestByDates(queryParams);
        JsonPath getBookingResponseJson = getBookingResponse.jsonPath();

        assertThat(getBookingResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);

        List<Integer> bookingIdsInResponse = getBookingResponseJson.getList("bookingid", Integer.class);
        assertThat(bookingIdsInResponse).doesNotContain(bookingId);
    }

    @AfterMethod
    void deleteBooking()
    {
        // Call Auth endpoint to extract token
        AuthDto authData = new AuthDto();
        authData.setUsername("admin");
        authData.setPassword("password123");
        Response createTokenResponse = CreateAuthRequest.authRequest(authData);
        String token = createTokenResponse.jsonPath().getString("token");

        // Use token to delete test booking
        Response response = DeleteBookingRequest.deleteBookingRequest(bookingId, token);
        assertThat(response.statusCode()).as("Post test cleanup failed. Could not delete Test booking with bookingId: " + bookingId).isEqualTo(HttpStatus.SC_CREATED);
    }
}
