package com.kst.movie_ticket_reservation.feat.promo_code.repository;

import com.kst.movie_ticket_reservation.feat.promo_code.entity.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, Long>
{
    Optional<PromoCode> findByCode(@Param("code") String code);

    @Query("SELECT p FROM PromoCode p WHERE p.code = :code AND p.isActive = true AND :now BETWEEN p.validFrom AND p" +
            ".validUntil")
    Optional<PromoCode> findValidCode(@Param("code") String code, @Param("now") Instant now);
}
