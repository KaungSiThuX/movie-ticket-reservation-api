package com.kst.movie_ticket_reservation.security.security_exceptions;

import org.springframework.http.HttpStatus;

public record CustomExceptionResponseData(int status, String message, String code, String error)
{
}
