package com.kst.movie_ticket_reservation.feat.discount_calculation.dto.res;

import java.math.BigDecimal;

public record DiscountCalculationResDto(BigDecimal subTotal,
                                        //  BigDecimal ruleBasedDiscount,
                                        BigDecimal promoDiscount,
                                        BigDecimal tax,
                                        BigDecimal orderTotal
                                        //     List<AppliedDiscount> appliedDiscounts
)
{
}
