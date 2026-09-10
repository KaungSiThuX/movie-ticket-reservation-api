package com.kst.movie_ticket_reservation.feat.promo_code.service.impl;

import com.kst.movie_ticket_reservation.feat.promo_code.dto.req.CreatePromoCodeDto;
import com.kst.movie_ticket_reservation.feat.promo_code.dto.req.UpdatePromoCodeDto;
import com.kst.movie_ticket_reservation.feat.promo_code.dto.res.PromoCodeOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.promo_code.dto.res.PromoCodeResDto;
import com.kst.movie_ticket_reservation.feat.promo_code.entity.PromoCode;
import com.kst.movie_ticket_reservation.feat.promo_code.mapper.PromoCodeMapper;
import com.kst.movie_ticket_reservation.feat.promo_code.repository.PromoCodeRepository;
import com.kst.movie_ticket_reservation.feat.promo_code.service.PromoCodeService;
import com.kst.movie_ticket_reservation.feat.promo_code_redemption.entity.PromoCodeRedemption;
import com.kst.movie_ticket_reservation.feat.promo_code_redemption.repository.PromoCodeRedemptionRepository;
import com.kst.movie_ticket_reservation.feat.show_seat.entity.ShowSeat;
import com.kst.movie_ticket_reservation.util.api_responses.OffsetPaginationApiMetaData;
import com.kst.movie_ticket_reservation.util.exceptions.BadRequestException;
import com.kst.movie_ticket_reservation.util.exceptions.ConflictException;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;
import com.kst.movie_ticket_reservation.util.services.pagination.OffsetPaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PromoCodeServiceImpl implements PromoCodeService
{
    private final PromoCodeRepository promoCodeRepository;
    private final PromoCodeMapper promoCodeMapper;
    private final PromoCodeRedemptionRepository promoCodeRedemptionRepository;
    private final OffsetPaginationService offsetPaginationService;

    @Override
    public PromoCodeResDto create(CreatePromoCodeDto createPromoCodeDto) throws ConflictException
    {
        Optional<PromoCode> existingPromoCode = this.promoCodeRepository.findByCode(createPromoCodeDto.getCode());

        if (existingPromoCode.isPresent())
        {
            throw new ConflictException("code already exist");
        }

        PromoCode createdPromoCode =
                this.promoCodeRepository.save(this.promoCodeMapper.toPromoCode(createPromoCodeDto));

        return this.promoCodeMapper.toPromoCodeResDto(createdPromoCode);
    }

    @Override
    @Transactional
    public PromoCodeResDto update(Long id, UpdatePromoCodeDto updatePromoCodeDto) throws NotFoundException
    {
        PromoCode existingPromoCode = this.findExistingPromoCodeById(id);

        this.promoCodeMapper.updatePromoCodeFromDto(updatePromoCodeDto, existingPromoCode);

        PromoCode updatedPromoCode = this.promoCodeRepository.save(existingPromoCode);

        return this.promoCodeMapper.toPromoCodeResDto(updatedPromoCode);
    }

    @Override
    @Transactional
    public PromoCodeResDto delete(Long id) throws NotFoundException
    {
        PromoCode existingPromoCode = this.findExistingPromoCodeById(id);

        this.promoCodeRepository.deleteById(id);

        return this.promoCodeMapper.toPromoCodeResDto(existingPromoCode);
    }

    @Override
    public PromoCodeOffsetPaginationResDto findMany(int offset, int limit)
    {
        Page<PromoCode> promoCodePage =
                this.promoCodeRepository.findAll(this.offsetPaginationService.calculatePageable(offset, limit));

        List<PromoCodeResDto> promoCodeResDtoList =
                promoCodePage.getContent().stream().map(this.promoCodeMapper::toPromoCodeResDto).toList();

        OffsetPaginationApiMetaData offsetPaginationApiMetaData =
                this.offsetPaginationService.generateMetaData(promoCodePage);

        return new PromoCodeOffsetPaginationResDto(promoCodeResDtoList, offsetPaginationApiMetaData);
    }

    private PromoCode findExistingPromoCodeById(Long id) throws NotFoundException
    {
        return this.promoCodeRepository.findById(id).orElseThrow(() -> new NotFoundException(
                "promo code not found"));
    }

    @Override
    public PromoCode validatePromoCode(Long customerId, String code, List<ShowSeat> showSeatList) throws BadRequestException
    {
        PromoCode existingPromoCode =
                this.promoCodeRepository.findValidCode(code, Instant.now()).orElseThrow(() -> new BadRequestException(
                        "promo code is invalid"));

        Optional<PromoCodeRedemption> existingPromoCodeRedemption =
                this.promoCodeRedemptionRepository.findByUserIdAndPromoCodeCode(customerId, code);

        if (existingPromoCodeRedemption.isPresent())
        {
            throw new BadRequestException("you already use this promo code");
        }

        BigDecimal subTotal = showSeatList.stream()
                .map(ShowSeat::getBasePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        if (subTotal.compareTo(existingPromoCode.getMinimumSpend()) < 0)
        {
            throw new BadRequestException("you must be used least amount " + existingPromoCode.getMinimumSpend() + " " +
                    "to use this promo code");
        }

        return existingPromoCode;
    }
}
