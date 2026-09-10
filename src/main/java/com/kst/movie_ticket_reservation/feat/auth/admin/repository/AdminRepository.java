package com.kst.movie_ticket_reservation.feat.auth.admin.repository;

import com.kst.movie_ticket_reservation.feat.auth.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long>
{
    Optional<Admin> findByEmail(String email);
}
