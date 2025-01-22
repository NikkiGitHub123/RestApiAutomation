package bookings;

import dtos.AuthDto;
import dtos.BookingDatesDto;
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
import requests.UpdateBookingRequest;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateBookingTests {

    private static String token;
    private static int bookingId=0;

    @BeforeMethod
    void createToken()
    {
        AuthDto authData = new AuthDto();
        authData.setUsername("admin");
        authData.setPassword("password123");

        Response createTokenResponse = CreateAuthRequest.authRequest(authData);
        token = createTokenResponse.jsonPath().getString("token");
    }

    @Test
    public void updateBookingIsSuccessful()
    {
        BookingDto updatedBooking = new BookingDto();
        BookingDatesDto updatedDates = new BookingDatesDto();

        updatedBooking.setFirstname("UpdatedFirstname");
        updatedBooking.setLastname("UpdatedLastName");
        updatedBooking.setDepositpaid(false);
        updatedBooking.setAdditonalNeeds("Breakfast Lunch Dinner");
        updatedDates.setCheckin("2025-02-01");
        updatedDates.setCheckout("2025-02-21");
        updatedBooking.setBookingdates(updatedDates);

        BookingDto newBooking = new BookingDto();

        Response createBookingResponse = CreateBookingRequest.createBookingRequest(newBooking);
        JsonPath createBookingJsonPath = createBookingResponse.jsonPath();

        //Validate Response Code
        assertThat(createBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        //BUG: Http Response for POST endpoint should be 201 Created

        bookingId = createBookingJsonPath.getInt("bookingid");

        Response updateBookingResponse = UpdateBookingRequest.updateBookingRequest(bookingId, updatedBooking, token);
        JsonPath updateBookingJsonPath = updateBookingResponse.jsonPath();

        assertThat(updateBookingResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);

        //Validate Response Data
        assertThat(updateBookingJsonPath.getString("firstname")).isEqualTo(updatedBooking.getFirstname());
        assertThat(updateBookingJsonPath.getString("lastname")).isEqualTo(updatedBooking.getLastname());
        assertThat(updateBookingJsonPath.getInt("totalprice")).isEqualTo(updatedBooking.getTotalprice());
        assertThat(updateBookingJsonPath.getBoolean("depositpaid")).isEqualTo(updatedBooking.isDepositpaid());
        assertThat(updateBookingJsonPath.getString("bookingdates.checkin")).isEqualTo(updatedBooking.getBookingdates().getCheckin());
        assertThat(updateBookingJsonPath.getString("bookingdates.checkout")).isEqualTo(updatedBooking.getBookingdates().getCheckout());
    }

    @Test
    public void updateNonExistentBookingFails()
    {
        BookingDto updatedBooking = new BookingDto();
        Response updateBookingResponse = UpdateBookingRequest.updateBookingRequest(0, updatedBooking, token);

        assertThat(updateBookingResponse.getStatusCode()).isEqualTo(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void updateInvalidDateFormatFails()
    {
        BookingDto newBooking = new BookingDto();

        Response createBookingResponse = CreateBookingRequest.createBookingRequest(newBooking);
        JsonPath createBookingJsonPath = createBookingResponse.jsonPath();

        //Validate Response Code
        assertThat(createBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        //BUG: Http Response for POST endpoint should be 201 Created

        bookingId = createBookingJsonPath.getInt("bookingid");

        BookingDto updatedBooking = new BookingDto();
        BookingDatesDto updatedDates = new BookingDatesDto();
        updatedDates.setCheckin("20250202");
        updatedDates.setCheckout("20250220");
        updatedBooking.setBookingdates(updatedDates);

        Response updateBookingResponse = UpdateBookingRequest.updateBookingRequest(bookingId, updatedBooking, token);

        assertThat(updateBookingResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        //BUG: Invalid date format should return an error response
    }

    @AfterMethod
    void deleteBooking()
    {
        if(bookingId != 0) {
            Response response = DeleteBookingRequest.deleteBookingRequest(bookingId, token);
            assertThat(response.statusCode()).as("Post test clean up failed. Could not delete Test booking with bookingId: " + bookingId).isEqualTo(HttpStatus.SC_CREATED);
            //BUG: If the booking does not exist, return a 404 NOT FOUND. Currently the API returns 405 Method Not Allowed
        }
    }

}
