package dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BookingDto {

    private String firstname = "Foreign";
    private String lastname = "Prince";

    @JsonProperty("totalprice")
    private int totalprice = 1000;

    @JsonProperty("depositpaid")
    private boolean depositpaid = true;

    @JsonProperty("bookingdates")
    private BookingDatesDto bookingdates = new BookingDatesDto();

    @JsonProperty("additionalNeeds")
    private  String additonalNeeds="Refrigerator";
}
