package com.kst.movie_ticket_reservation.util.exceptions;

public class CustomStripeException extends Exception
{
    public CustomStripeException(String message)
    {
        super(message);
    }
}
