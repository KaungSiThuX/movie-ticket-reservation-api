package com.kst.movie_ticket_reservation.util.exceptions;

public class TooManyRequestException extends Exception
{
    public TooManyRequestException(String message)
    {
        super(message);
    }
}
