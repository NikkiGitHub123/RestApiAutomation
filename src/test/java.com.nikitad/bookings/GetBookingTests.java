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

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;


public class GetBookingTests {

    private static int bookingId = 0;
    private static BookingDto bookingDto = new BookingDto();

    @BeforeMethod
    void createBooking()
    {
        BookingDto bookingDto = new BookingDto();
        Response createBookingResponse = CreateBookingRequest.createBookingRequest(bookingDto);
        JsonPath createBookingJsonPath = createBookingResponse.jsonPath();
        bookingId = createBookingJsonPath.getInt("bookingid");
    }

    @Test
    public void getBookingReturnsAllBookings()
    {
        Response getBookingResponse = GetBookingRequest.getBookingRequest();
        JsonPath getBookingResponseJson = getBookingResponse.jsonPath();

        assertThat(getBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        List<Integer> bookingIds = getBookingResponseJson.getList("id");
        assertThat(bookingIds).isNotNull();
    }

    @Test
    public void getBookingByIdReturnsRightBooking()
    {
        Response getBookingResponse = GetBookingRequest.getBookingRequestById(bookingId);
        JsonPath getBookingResponseJson = getBookingResponse.jsonPath();

        assertThat(getBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(getBookingResponseJson.getString("firstname")).isEqualTo(bookingDto.getFirstname());
        assertThat(getBookingResponseJson.getString("lastname")).isEqualTo(bookingDto.getLastname());
        assertThat(getBookingResponseJson.getInt("totalprice")).isEqualTo(bookingDto.getTotalprice());
        assertThat(getBookingResponseJson.getBoolean("depositpaid")).isEqualTo(bookingDto.isDepositpaid());
        assertThat(getBookingResponseJson.getString("bookingdates.checkin")).isEqualTo(bookingDto.getBookingdates().getCheckin());
        assertThat(getBookingResponseJson.getString("bookingdates.checkout")).isEqualTo(bookingDto.getBookingdates().getCheckout());
    }

    @Test
    public void getBookingByInvalidIdReturnsNotFound()
    {
        Response getBookingResponse = GetBookingRequest.getBookingRequestById(0);

        assertThat(getBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_NOT_FOUND);
        assertThat(getBookingResponse.asString()).isEqualTo("Not Found");
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

