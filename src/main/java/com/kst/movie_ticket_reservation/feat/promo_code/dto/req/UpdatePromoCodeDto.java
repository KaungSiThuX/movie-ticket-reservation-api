package com.kst.movie_ticket_reservation.feat.promo_code.dto.req;

import com.kst.movie_ticket_reservation.util.enums.DiscountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class UpdatePromoCodeDto
{
    @NotBlank(message = "code is required")
    @Length(min = 1, max = 20, message = "code must be between 1 and 20 characters")
    @Pattern(regexp = "^[A-Z0-9]*$", message = "The field must contain only uppercase letters and digits")
    private String code;

    private DiscountType discountType;

    private BigDecimal discountValue;

    private Instant startAt;

    private Instant expireAt;

    private Integer usageLimitTotal;

    private Integer usageLimitPerUser;

    private BigDecimal minimumSpend;

    private Boolean isActive;
}
