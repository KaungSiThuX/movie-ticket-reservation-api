package com.kst.movie_ticket_reservation.feat.order.service.impl;

import com.kst.movie_ticket_reservation.feat.auth.user.entity.User;
import com.kst.movie_ticket_reservation.feat.auth.user.repository.UserRepository;
import com.kst.movie_ticket_reservation.feat.order.dto.req.CreateOrderDto;
import com.kst.movie_ticket_reservation.feat.order.dto.res.CreateOrderResDto;
import com.kst.movie_ticket_reservation.feat.order.dto.res.OrderDetailResDto;
import com.kst.movie_ticket_reservation.feat.order.entity.Order;
import com.kst.movie_ticket_reservation.feat.order.mapper.OrderMapper;
import com.kst.movie_ticket_reservation.feat.order.repository.OrderRepository;
import com.kst.movie_ticket_reservation.feat.order.service.OrderService;
import com.kst.movie_ticket_reservation.feat.promo_code.entity.PromoCode;
import com.kst.movie_ticket_reservation.feat.promo_code.service.PromoCodeService;
import com.kst.movie_ticket_reservation.feat.promo_code_redemption.entity.PromoCodeRedemption;
import com.kst.movie_ticket_reservation.feat.promo_code_redemption.repository.PromoCodeRedemptionRepository;
import com.kst.movie_ticket_reservation.feat.seat_lock.service.SeatLockService;
import com.kst.movie_ticket_reservation.feat.show_seat.entity.ShowSeat;
import com.kst.movie_ticket_reservation.feat.show_seat.mapper.ShowSeatMapper;
import com.kst.movie_ticket_reservation.feat.show_seat.repository.ShowSeatRepository;
import com.kst.movie_ticket_reservation.feat.show_time.entity.ShowTime;
import com.kst.movie_ticket_reservation.feat.show_time.repository.ShowTimeRepository;
import com.kst.movie_ticket_reservation.integration.redis.service.RedisService;
import com.kst.movie_ticket_reservation.integration.stripe.dto.StripeLineItemDto;
import com.kst.movie_ticket_reservation.integration.stripe.service.StripeService;
import com.kst.movie_ticket_reservation.security.current.CurrentPerson;
import com.kst.movie_ticket_reservation.util.enums.DiscountType;
import com.kst.movie_ticket_reservation.util.enums.OrderStatus;
import com.kst.movie_ticket_reservation.util.enums.ShowSeatStatus;
import com.kst.movie_ticket_reservation.util.events.SeatLockEvent;
import com.kst.movie_ticket_reservation.util.exceptions.BadRequestException;
import com.kst.movie_ticket_reservation.util.exceptions.CustomStripeException;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;
import com.kst.movie_ticket_reservation.util.exceptions.UnauthorizedException;
import com.kst.movie_ticket_reservation.util.job_payloads.SeatLockPayload;
import com.kst.movie_ticket_reservation.util.services.report.ReportService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService
{
    private final ShowTimeRepository showTimeRepository;
    private final ShowSeatRepository showSeatRepository;
    private final StripeService stripeService;
    private final SeatLockService seatLockService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final OrderRepository orderRepository;
    private final ShowSeatMapper showSeatMapper;
    private final PromoCodeService promoCodeService;
    private final OrderMapper orderMapper;
    private final RedisService redisService;
    private final UserRepository userRepository;
    private final ReportService reportService;

    @Override
    @Transactional
    public CreateOrderResDto create(CurrentPerson currentPerson, CreateOrderDto createOrderDto) throws NotFoundException, BadRequestException, CustomStripeException, UnauthorizedException
    {

        // need to create order in this
        // for reids, need to check lock with lua script

        if (createOrderDto.getShowSeatPublicIds().size() > 10)
        {
            throw new BadRequestException("you can only order 10 ticket per time");
        }

        User existingUser =
                this.userRepository.findById(currentPerson.getId()).orElseThrow(() -> new UnauthorizedException(
                        "invalid user"));

        ShowTime existingShowTime = this.validateShowTime(createOrderDto.getShowTimePublicId());

        List<ShowSeat> showSeatList = this.validateShowSeatList(createOrderDto.getShowSeatPublicIds());

        this.validateShowSeatInLock(existingShowTime.getId(), showSeatList);

        PromoCode promoCode = this.validatePromoCode(currentPerson.getId(), createOrderDto.getPromoCode(),
                showSeatList);

        BigDecimal subTotal = showSeatList.stream().map(ShowSeat::getBasePrice).reduce(BigDecimal.ZERO,
                BigDecimal::add).setScale(2, RoundingMode.HALF_UP);

        BigDecimal tax = subTotal.multiply(new BigDecimal("0.05")).setScale(2, RoundingMode.HALF_UP);

        BigDecimal promoCodeDiscountAmount = this.calculateDiscount(subTotal, promoCode);

        BigDecimal orderTotal = subTotal
                .subtract(promoCodeDiscountAmount)
                .add(tax)
                .setScale(2, RoundingMode.HALF_UP);


        Order order = new Order();
        order.setUser(existingUser);
        order.setShowTime(existingShowTime);
        order.setStatus(OrderStatus.PENDING);
        order.setSubTotal(subTotal);
        order.setTax(tax);
        order.setPromoCodeDiscountAmount(promoCodeDiscountAmount);
        order.setOrderTotal(orderTotal);

        Order createdOrder = this.orderRepository.save(order);


        String promoCodeForCheckoutSession = (promoCode != null) ? promoCode.getCode() : "";

//        this.checkStripeCheckoutSessionUrlCache(currentPerson.getId(), existingShowTime.getId(),
//                promoCodeForCheckoutSession, showSeatList);

        this.fireSeatLockEvent(currentPerson.getId(), existingShowTime.getId(), showSeatList);

        List<Long> showSeatIds = showSeatList.stream().map(ShowSeat::getId).toList();

        List<StripeLineItemDto> stripeLineItemDtoList = this.processStripeLineItemDtoList(showSeatList);

        try
        {
            String stripeCheckoutSessionUrl = this.generateStripeCheckoutSessionUrl(currentPerson,
                    createdOrder, existingShowTime.getId(), promoCodeForCheckoutSession, showSeatIds,
                    stripeLineItemDtoList);

//            this.setStripeCheckoutSessionUrlToCache(currentPerson.getId(),
//                    existingShowTime.getId(),
//                    promoCodeForCheckoutSession, showSeatList, stripeCheckoutSessionUrl);

            return new CreateOrderResDto(stripeCheckoutSessionUrl);
        }
        catch (StripeException e)
        {
            throw new CustomStripeException(e.getMessage());
        }
    }

    private ShowTime validateShowTime(String showTimePublicId) throws NotFoundException
    {
        return this.showTimeRepository.findByPublicId(showTimePublicId)
                .orElseThrow(() -> new NotFoundException("show time not found"));
    }

    private List<ShowSeat> validateShowSeatList(List<String> showSeatPublicIds) throws BadRequestException
    {
        List<ShowSeat> showSeatList =
                this.showSeatRepository.findAllAvailableByPublicId(showSeatPublicIds);

        if (showSeatList.size() != showSeatPublicIds.size())
        {
            throw new BadRequestException("one or more seat are not available");
        }

        return showSeatList;
    }

    private void validateShowSeatInLock(Long showTimeId, List<ShowSeat> showSeatList) throws BadRequestException
    {
        Set<Long> lockedSeatIds = this.seatLockService.getLockedSeatIdsForShowTime(showTimeId);

        Optional<ShowSeat> lockedSeat =
                showSeatList.stream().filter(showSeat -> lockedSeatIds.contains(showSeat.getId())).findFirst();

        if (lockedSeat.isPresent())
        {
            throw new BadRequestException("seat " + lockedSeat.get().getSeat().getRow() + lockedSeat.get().getSeat().getSeatNumber() + " is in lock");
        }

    }

    private PromoCode validatePromoCode(Long currentPersonId, String code, List<ShowSeat> showSeatList) throws NotFoundException, BadRequestException
    {
        PromoCode promoCode = null;

        if (code != null && !code.isEmpty())
        {
            promoCode = this.promoCodeService.validatePromoCode(currentPersonId, code, showSeatList);
        }

        return promoCode;
    }

    private void checkStripeCheckoutSessionUrlCache(Long currentPersonId, Long showTimeId,
                                                    String promoCodeForCheckoutSession,
                                                    List<ShowSeat> showSeatList)
    {
        String cachedStripeCheckoutSessionUrl =
                this.redisService.getString(this.generateRawPayloadKey(currentPersonId,
                        showTimeId, promoCodeForCheckoutSession, showSeatList));

        if (cachedStripeCheckoutSessionUrl != null && !cachedStripeCheckoutSessionUrl.isEmpty())
        {
            log.info("hit cache for stripe checkout session url");
            new CreateOrderResDto(cachedStripeCheckoutSessionUrl);
        }
    }

    private List<StripeLineItemDto> processStripeLineItemDtoList(List<ShowSeat> showSeatList)
    {
        return showSeatList.stream()
                .map(showSeat -> new StripeLineItemDto(
                        showSeat.getId(),
                        showSeat.getPublicId(),
                        showSeat.getSeat().getRow(),
                        showSeat.getSeat().getSeatNumber(),
                        showSeat.getSeat().getSeatType(),
                        showSeat.getBasePrice()))
                .toList();
    }

    private BigDecimal calculateDiscount(BigDecimal totalAmount, PromoCode promoCode)
    {
        if (promoCode == null)
        {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal discountValue = promoCode.getDiscountValue();

        if (promoCode.getDiscountType() == DiscountType.PERCENTAGE)
        {
            return totalAmount.multiply(discountValue)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }
        else if (promoCode.getDiscountType() == DiscountType.FIXED_AMOUNT)
        {
            return discountValue.min(totalAmount).setScale(2, RoundingMode.HALF_UP);
        }

        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    private String generateStripeCheckoutSessionUrl(CurrentPerson currentPerson, Order order, Long showTimeId,
                                                    String promoCode,
                                                    List<Long> showSeatIds,
                                                    List<StripeLineItemDto> stripeLineItemDtoList) throws StripeException
    {
        return this.stripeService.createCheckoutSession(currentPerson, order.getId(), order.getPublicId(), showTimeId
                , order.getTax(),
                order.getPromoCodeDiscountAmount(), promoCode, showSeatIds, stripeLineItemDtoList);

    }

    private void setStripeCheckoutSessionUrlToCache(Long currentPersonId, Long showTimeId,
                                                    String promoCodeForCheckoutSession, List<ShowSeat> showSeatList,
                                                    String stripeCheckoutSessionUrl)
    {
        this.redisService.setString(this.generateRawPayloadKey(currentPersonId, showTimeId,
                promoCodeForCheckoutSession, showSeatList), stripeCheckoutSessionUrl, Duration.ofHours(23));
    }

    private String generateRawPayloadKey(Long currentPersonId, Long showTimeId, String promoCode,
                                         List<ShowSeat> showSeatList)
    {
        String sortedShowSeatIds =
                showSeatList.stream().map(ShowSeat::getId).sorted().toList()
                        .stream().map(Object::toString).collect(Collectors.joining(","));

        return String.format("user%d|showTime%d|promoCode%s|showSeats:%s", currentPersonId,
                showTimeId, promoCode, sortedShowSeatIds);
    }

    private void fireSeatLockEvent(Long currentPersonId, Long showTimeId, List<ShowSeat> showSeatList)
    {
        SeatLockPayload seatLockPayload = new SeatLockPayload(currentPersonId, showTimeId,
                showSeatList.stream().map(this.showSeatMapper::toShowSeatResDto).toList());

        this.applicationEventPublisher.publishEvent(new SeatLockEvent(seatLockPayload));
    }


    @Override
    public void handleWebhook(String payload, String signatureHeader) throws StripeException, BadRequestException,
            NotFoundException, UnauthorizedException, CustomStripeException
    {
        this.stripeService.handleWebhook(payload, signatureHeader);
    }

    @Override
    public OrderDetailResDto getOrderDetail(String orderPublicId) throws NotFoundException
    {
        Order existingOrder =
                this.orderRepository.findByPublicId(orderPublicId).orElseThrow(() -> new NotFoundException("order not" +
                        " found"));

        List<ShowSeat> showSeatList = this.showSeatRepository.findAllByOrderId(existingOrder.getId());

        return this.orderMapper.toOrderDetailResDto(existingOrder, showSeatList);
    }

    //  @Override
//    public byte[] exportPdf(Instant from, Instant to)
//    {
//        List<Order> orderList = this.orderRepository.findAll();
//    }
}
