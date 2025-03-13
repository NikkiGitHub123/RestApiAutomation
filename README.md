<h1>restful-booker REST api test automation</h1>

Restful-Booker is Web API playground created by Mark Winteringham. It offers authentication, CRUD operations and is loaded with bugs (for the purpose of learning).
This test automation framework is written around Restful-Booker.

**App links**
Restful-Booker API Docs - https://restful-booker.herokuapp.com/apidoc/index.html#api-Ping-Ping
Restful-Booker Postman Collection - https://www.postman.com/automation-in-testing/restful-booker-collections/collection/55eh7vh/restful-booker

**Tools & Technology used**
  * Java
  * TestNG (Test runner)
  * Rest Assured (REST client)
  * AssertJ
  * Lombok (This requires the plugin to be installed in Intellij or Eclipse)

**Installation**
Clone this repo either using Git's graphical user interface or using the 'git clone' commmand.

**Authentication**
As per the API docs, the following endpoints require an auth token: PUT , PATCH, DELETE
To generate the auth token, needed information is saved in the login.properties file

**Running the tests**
1. using "Run test" option in Intellij Idea IDE
2. using 'mvn clean test' command in the terminal

**Known Bugs**
Restful Booker API has some bugs for the fun of its testers.
1. Create booking with invalid date format should return 400 Bad Request with message 'Invalid Date Format'. It returns a success response with malformed dates in the response. Like in the response body below:
{
    "firstname": "Foreign",
    "lastname": "Prince",
    "totalprice": 1000,
    "depositpaid": true,
    "bookingdates": {
        "checkin": "0NaN-aN-aN",
        "checkout": "0NaN-aN-aN"
    }
}
2. Successful creation for a new Booking should be 201 Created. It is 200 OK.
3. Successful response for the ping endpoint should be 200 OK. It is 201 Created.
4. Failure to retrieve a booking should return 404 Not Found. It returns 405 Method Not Allowed.
You will find the above bugs in the tests as comments beginning with //BUG

_Disclaimer: Opinions expressed here are solely my own and do not express the views or opinions of my employer._


