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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingByNameTests {

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
    public void getBookingByNameReturnsRightBooking()
    {
        queryParams.put("firstname" , bookingDto.getFirstname());
        queryParams.put("lastname", bookingDto.getLastname());
        Response getBookingResponse = GetBookingRequest.getBookingRequestByFirstLastName(queryParams);
        JsonPath getBookingResponseJson = getBookingResponse.jsonPath();

        assertThat(getBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        List<Integer> bookingIds = getBookingResponseJson.getList("bookingid", Integer.class);
        bookingId = bookingIds.get(0);
        assertThat(bookingId).isNotNull();
    }

    @Test
    public void getNonExistingNameReturnsNoBookings()
    {
        queryParams.put("firstname", "Nonexistent");
        queryParams.put("lastname", "Name");
        Response getBookingResponse = GetBookingRequest.getBookingRequestByFirstLastName(queryParams);

        assertThat(getBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(getBookingResponse.asString()).isEqualTo("[]");
    }

    @Test
    public void getMissingLastnameNameReturnsNoBookings()
    {
        queryParams.put("firstname", bookingDto.getFirstname());
        queryParams.put("lastname", "");
        Response getBookingResponse = GetBookingRequest.getBookingRequestByFirstLastName(queryParams);

        assertThat(getBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(getBookingResponse.asString()).isEqualTo("[]");
    }

    @Test
    public void getMissingFirstnameNameReturnsNoBookings()
    {
        queryParams.put("firstname", "");
        queryParams.put("lastname", bookingDto.getLastname());
        Response getBookingResponse = GetBookingRequest.getBookingRequestByFirstLastName(queryParams);

        assertThat(getBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(getBookingResponse.asString()).isEqualTo("[]");
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

