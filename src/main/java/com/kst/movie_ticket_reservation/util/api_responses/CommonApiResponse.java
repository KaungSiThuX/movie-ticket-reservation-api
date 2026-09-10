package com.kst.movie_ticket_reservation.util.api_responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommonApiResponse
{
    private int status;
    private String message;
    private String code;        // code can be HttpStatus or something configure
}
