package com.kst.movie_ticket_reservation.util.exceptions;

public class BadRequestException extends Exception
{
    public BadRequestException(String message)
    {
        super(message);
    }
}
