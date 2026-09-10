package com.kst.movie_ticket_reservation.feat.promo_code.controller;

import com.kst.movie_ticket_reservation.feat.genre.dto.res.GenreOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.genre.dto.res.GenreResDto;
import com.kst.movie_ticket_reservation.feat.promo_code.dto.req.CreatePromoCodeDto;
import com.kst.movie_ticket_reservation.feat.promo_code.dto.req.UpdatePromoCodeDto;
import com.kst.movie_ticket_reservation.feat.promo_code.dto.res.PromoCodeOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.promo_code.dto.res.PromoCodeResDto;
import com.kst.movie_ticket_reservation.feat.promo_code.service.PromoCodeService;
import com.kst.movie_ticket_reservation.util.api_responses.OffsetPaginationResponse;
import com.kst.movie_ticket_reservation.util.api_responses.SuccessApiResponse;
import com.kst.movie_ticket_reservation.util.exceptions.ConflictException;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;
import com.kst.movie_ticket_reservation.util.handlers.SuccessApiResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("promo-codes")
@RequiredArgsConstructor
public class PromoCodeController
{
    private final PromoCodeService promoCodeService;
    private final SuccessApiResponseHandler successApiResponseHandler;

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @GetMapping
    ResponseEntity<OffsetPaginationResponse<List<PromoCodeResDto>>> findMany(@RequestParam(required = false,
            defaultValue = "0") int offset, @RequestParam(required = false, defaultValue = "10") int limit)
    {
        PromoCodeOffsetPaginationResDto promoCodeOffsetPaginationResDto = this.promoCodeService.findMany(offset, limit);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "FIND_MANY_PROMO_CODE_SUCCESS", promoCodeOffsetPaginationResDto.offsetPaginationApiMetaData(),
                promoCodeOffsetPaginationResDto.promoCodeResDtoList());
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PostMapping
    ResponseEntity<SuccessApiResponse<PromoCodeResDto>> create(@Valid @RequestBody CreatePromoCodeDto createPromoCodeDto) throws ConflictException
    {
        PromoCodeResDto promoCodeResDto = this.promoCodeService.create(createPromoCodeDto);

        return this.successApiResponseHandler.response(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(),
                "CREATE_PROMO_CODE_SUCCESS", promoCodeResDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PutMapping("{id}")
    ResponseEntity<SuccessApiResponse<PromoCodeResDto>> update(@PathVariable Long id,
                                                               @Valid @RequestBody UpdatePromoCodeDto updatePromoCodeDto) throws ConflictException, NotFoundException
    {
        PromoCodeResDto promoCodeResDto = this.promoCodeService.update(id, updatePromoCodeDto);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "UPDATE_PROMO_CODE_SUCCESS", promoCodeResDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @DeleteMapping("{id}")
    ResponseEntity<SuccessApiResponse<PromoCodeResDto>> delete(@PathVariable Long id) throws
            NotFoundException
    {
        PromoCodeResDto promoCodeResDto = this.promoCodeService.delete(id);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "DELETE_PROMO_CODE_SUCCESS", promoCodeResDto);
    }
}
