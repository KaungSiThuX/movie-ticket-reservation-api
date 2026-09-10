package com.kst.movie_ticket_reservation.util.enums;

public enum OrderStatus
{
    PENDING,    // Created, waiting for Stripe payment
    PAID,       // Successfully paid
    EXPIRED,    // User abandoned checkout, seats released
    CANCELLED,  // Admin cancelled or refund issued
    FAILED      // Payment explicitly declined
}
