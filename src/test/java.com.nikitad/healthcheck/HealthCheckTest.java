package healthcheck;

import org.apache.http.HttpStatus;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;

public class HealthCheckTest {

    @Test
    public void healthcheck()
    {
        given()
                .when()
                .get("https://restful-booker.herokuapp.com/ping")
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        //BUG: Http Response should be 200 OK
    }
}
