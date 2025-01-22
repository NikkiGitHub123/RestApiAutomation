package bookings;

import dtos.AuthDto;
import dtos.BookingDatesDto;
import dtos.BookingDto;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;
import requests.CreateAuthRequest;
import requests.CreateBookingRequest;
import requests.DeleteBookingRequest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateBookingTests {

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

    @Test(dataProvider = "guestNames")
    public void createBookingWithValidNamesIsSuccessful(String firstName, String lastName)
    {
        BookingDto validBooking = new BookingDto();
        BookingDatesDto dates = new BookingDatesDto();

        dates.setCheckin("2026-01-12");
        dates.setCheckout("2026-01-16");
        validBooking.setFirstname(firstName);
        validBooking.setLastname(lastName);
        validBooking.setDepositpaid(false);
        validBooking.setBookingdates(dates);

        Response createBookingResponse = CreateBookingRequest.createBookingRequest(validBooking);
        JsonPath createBookingJsonPath = createBookingResponse.jsonPath();

        //Validate Response Code
        assertThat(createBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        //BUG: Http Response for POST endpoint should be 201 Created

        bookingId = createBookingJsonPath.getInt("bookingid");

        //Validate Response Data
        assertThat(createBookingJsonPath.getString("booking.firstname")).isEqualTo(validBooking.getFirstname());
        assertThat(createBookingJsonPath.getString("booking.lastname")).isEqualTo(validBooking.getLastname());
        assertThat(createBookingJsonPath.getInt("booking.totalprice")).isEqualTo(validBooking.getTotalprice());
        assertThat(createBookingJsonPath.getBoolean("booking.depositpaid")).isEqualTo(validBooking.isDepositpaid());
        assertThat(createBookingJsonPath.getString("booking.bookingdates.checkin")).isEqualTo(validBooking.getBookingdates().getCheckin());
        assertThat(createBookingJsonPath.getString("booking.bookingdates.checkout")).isEqualTo(validBooking.getBookingdates().getCheckout());
    }

    @Test(dataProvider = "validInputs")
    public void createBookingWithValidDataIsSuccessful(int totalprice, boolean depositPaid, String checkin, String checkout ) {
        BookingDto validBooking = new BookingDto();
        BookingDatesDto dates = new BookingDatesDto();

        dates.setCheckin(checkin);
        dates.setCheckout(checkout);
        validBooking.setDepositpaid(depositPaid);
        validBooking.setTotalprice(totalprice);
        validBooking.setBookingdates(dates);

        Response createBookingResponse = CreateBookingRequest.createBookingRequest(validBooking);
        JsonPath createBookingJsonPath = createBookingResponse.jsonPath();

        //Validate Response Code
        assertThat(createBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        //BUG: Http Response for POST endpoint should be 201 Created

        bookingId = createBookingJsonPath.getInt("bookingid");

        //Validate Response Data
        assertThat(createBookingJsonPath.getString("booking.firstname")).isEqualTo(validBooking.getFirstname());
        assertThat(createBookingJsonPath.getString("booking.lastname")).isEqualTo(validBooking.getLastname());
        assertThat(createBookingJsonPath.getFloat("booking.totalprice")).isEqualTo(validBooking.getTotalprice());
        assertThat(createBookingJsonPath.getBoolean("booking.depositpaid")).isEqualTo(validBooking.isDepositpaid());
        assertThat(createBookingJsonPath.getString("booking.bookingdates.checkin")).isEqualTo(validBooking.getBookingdates().getCheckin());
        assertThat(createBookingJsonPath.getString("booking.bookingdates.checkout")).isEqualTo(validBooking.getBookingdates().getCheckout());
    }

    @Test
    public void createBookingWithInvalidDateFormat()
    {
        BookingDto invalidBooking = new BookingDto();
        BookingDatesDto dates = new BookingDatesDto();

        dates.setCheckin("20220112");
        dates.setCheckout("20220116");
        invalidBooking.setBookingdates(dates);

        Response createBookingResponse = CreateBookingRequest.createBookingRequest(invalidBooking);
        JsonPath createBookingJsonPath = createBookingResponse.jsonPath();

        //Validate Response Code
        assertThat(createBookingResponse.statusCode()).as("This is an API bug. createBookingWithInvalidDateFormat must return 400 Bad Request").isEqualTo(HttpStatus.SC_BAD_REQUEST);
        //BUG - Response should be 400 Bad Request with message "Invalid Date"
    }

    @AfterMethod
    void deleteBooking()
    {
        if(bookingId != 0)
        {
            Response response = DeleteBookingRequest.deleteBookingRequest(bookingId, token);
            assertThat(response.statusCode()).as("Post test clean up failed. Could not delete Test booking with bookingId: " + bookingId).isEqualTo(HttpStatus.SC_CREATED);
        }
        //BUG: If the booking does not exist, return a 404 NOT FOUND. Currently the API returns 405 Method Not Allowed
    }

    @DataProvider (name = "guestNames")
    public Iterator<Object []> guestNames () {
        final List<Object []> postData = new ArrayList<>();
        postData.add (new Object [] { "Foreign", "Princess" });
        postData.add (new Object [] { "Foreign", "Guard" });
        postData.add (new Object [] { "Foreign", "Security1" });
        postData.add (new Object [] { "Foreign", "Security2" });
        return postData.iterator ();
    }

    @DataProvider(name="validInputs")
    public Iterator<Object []> validInputs() {
        final List<Object[]> postData = new ArrayList<>();
        postData.add(new Object[] {1200, true, "2021-12-10", "2021-12-01"});
        postData.add(new Object[] {10000, false, "2025-03-10", "2025-04-26"});
        return postData.iterator();
    }


}
