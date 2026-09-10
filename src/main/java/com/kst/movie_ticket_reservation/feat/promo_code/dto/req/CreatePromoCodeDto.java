package com.kst.movie_ticket_reservation.feat.promo_code.dto.req;

import com.kst.movie_ticket_reservation.util.enums.DiscountType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class CreatePromoCodeDto
{
    @NotBlank(message = "code is required")
    @Length(min = 1, max = 20, message = "code must be between 1 and 20 characters")
    @Pattern(regexp = "^[A-Z0-9-]*$", message = "code must contain only uppercase letters, digits and hyphens")
    private String code;

    @NotNull(message = "discount type is required")
    private DiscountType discountType;

    private BigDecimal discountValue;

    @FutureOrPresent(message = "start at must be in present or future")
    private Instant startAt;

    @FutureOrPresent(message = "expire at must be in present or future")
    private Instant expireAt;

    @NotNull(message = "usage limit total is required")
    private Integer usageLimitTotal;

    @NotNull(message = "usage limit per user is required")
    private Integer usageLimitPerUser = 1;

    private BigDecimal minimumSpend;

    private Boolean isActive;
}
