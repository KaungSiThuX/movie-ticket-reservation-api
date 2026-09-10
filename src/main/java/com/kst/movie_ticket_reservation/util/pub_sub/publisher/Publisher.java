package com.kst.movie_ticket_reservation.util.pub_sub.publisher;

public interface Publisher
{
    void publish(String topic, Object message);
}
