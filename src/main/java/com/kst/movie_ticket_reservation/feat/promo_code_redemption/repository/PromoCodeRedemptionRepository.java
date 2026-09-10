package com.kst.movie_ticket_reservation.feat.promo_code_redemption.repository;

import com.kst.movie_ticket_reservation.feat.promo_code_redemption.entity.PromoCodeRedemption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromoCodeRedemptionRepository extends JpaRepository<PromoCodeRedemption, Long>
{
    Optional<PromoCodeRedemption> findByUserIdAndPromoCodeCode(Long userId, String code);
}
