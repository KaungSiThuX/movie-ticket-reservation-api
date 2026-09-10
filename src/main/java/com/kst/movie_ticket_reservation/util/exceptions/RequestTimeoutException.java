package com.kst.movie_ticket_reservation.util.exceptions;

public class RequestTimeoutException extends Exception
{
    public RequestTimeoutException(String message)
    {
        super(message);
    }
}
