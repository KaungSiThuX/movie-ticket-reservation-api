package com.kst.movie_ticket_reservation.util.exceptions;

public class JwtTokenExpireException extends Exception
{
    public JwtTokenExpireException(String message)
    {
        super(message);
    }
}
