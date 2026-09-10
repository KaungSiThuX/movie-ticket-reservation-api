package com.kst.movie_ticket_reservation.feat.promo_code.mapper;

import com.kst.movie_ticket_reservation.feat.promo_code.dto.req.CreatePromoCodeDto;
import com.kst.movie_ticket_reservation.feat.promo_code.dto.req.UpdatePromoCodeDto;
import com.kst.movie_ticket_reservation.feat.promo_code.dto.res.PromoCodeResDto;
import com.kst.movie_ticket_reservation.feat.promo_code.entity.PromoCode;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PromoCodeMapper
{
    PromoCode toPromoCode(CreatePromoCodeDto createPromoCodeDto);

    PromoCodeResDto toPromoCodeResDto(PromoCode promoCode);

    void updatePromoCodeFromDto(UpdatePromoCodeDto updatePromoCodeDto, @MappingTarget PromoCode promoCode);
}
