package com.kst.movie_ticket_reservation.feat.auth.user.repository;

import com.kst.movie_ticket_reservation.feat.auth.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    // Optional<User> findByEmailOrGoogleEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByGoogleEmail(String googleEmail);

    Optional<User> findByEmailOrGoogleEmail(String email, String googleEmail);
}
