package com.kst.movie_ticket_reservation.feat.seat.dto.req;

import com.kst.movie_ticket_reservation.util.enums.SeatType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class CreateSeatDto
{
    @NotBlank(message = "row is required")
    @Pattern(regexp = "^[A-Z]$", message = "row must uppercase letters")
    @Length(min = 1, max = 2, message = "row must be between 1 and 2 characters")
    private String row;

    @NotNull(message = "seat number is required")
    @Pattern(regexp = "^\\d$", message = "seat number must be digit")
    @Length(min = 1, max = 2, message = "seat number must be between 1 and 2 characters")
    private String seatNumber;

    @NotNull(message = "setType is required")
    private SeatType seatType;
}
