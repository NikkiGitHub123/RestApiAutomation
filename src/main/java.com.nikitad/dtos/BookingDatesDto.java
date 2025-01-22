package dtos;

import lombok.Data;

import java.awt.print.Book;

@Data
public class BookingDatesDto {
    private String checkin;
    private String checkout;

    public BookingDatesDto() {
        checkin = "2025-10-10";
        checkout="2025-10-20";
    }

    public BookingDatesDto(String checkin, String checkout)
    {
        this.checkin=checkin;
        this.checkout=checkout;
    }
}
