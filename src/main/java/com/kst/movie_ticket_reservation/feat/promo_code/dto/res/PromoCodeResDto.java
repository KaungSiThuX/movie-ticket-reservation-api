package com.kst.movie_ticket_reservation.feat.promo_code.dto.res;

import com.kst.movie_ticket_reservation.util.enums.DiscountType;

import java.math.BigDecimal;
import java.time.Instant;

public record PromoCodeResDto(Long id, String publicId, String code, DiscountType discountType,
                              BigDecimal discountValue,
                              Instant validFrom, Instant validUntil, Integer totalUsageLimit, Integer perUserUsageLimit,
                              BigDecimal minimumSpend, Boolean isActive)
{
}
