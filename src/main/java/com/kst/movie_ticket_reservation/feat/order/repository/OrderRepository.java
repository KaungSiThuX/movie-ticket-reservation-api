package com.kst.movie_ticket_reservation.feat.order.repository;

import com.kst.movie_ticket_reservation.feat.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>
{
    Optional<Order> findByPublicId(String publicId);

    Optional<Order> findByStripeCheckoutSessionId(String stripeCheckoutSessionId);
}
