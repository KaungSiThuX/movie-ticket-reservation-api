package com.kst.movie_ticket_reservation.integration.stripe.service;

import com.kst.movie_ticket_reservation.feat.auth.user.entity.User;
import com.kst.movie_ticket_reservation.feat.auth.user.repository.UserRepository;
import com.kst.movie_ticket_reservation.feat.order.entity.Order;
import com.kst.movie_ticket_reservation.feat.order.repository.OrderRepository;
import com.kst.movie_ticket_reservation.feat.promo_code.entity.PromoCode;
import com.kst.movie_ticket_reservation.feat.promo_code.repository.PromoCodeRepository;
import com.kst.movie_ticket_reservation.feat.promo_code_redemption.entity.PromoCodeRedemption;
import com.kst.movie_ticket_reservation.feat.promo_code_redemption.repository.PromoCodeRedemptionRepository;
import com.kst.movie_ticket_reservation.feat.show_seat.entity.ShowSeat;
import com.kst.movie_ticket_reservation.feat.show_seat.mapper.ShowSeatMapper;
import com.kst.movie_ticket_reservation.feat.show_seat.repository.ShowSeatRepository;
import com.kst.movie_ticket_reservation.feat.show_time.entity.ShowTime;
import com.kst.movie_ticket_reservation.feat.show_time.repository.ShowTimeRepository;
import com.kst.movie_ticket_reservation.integration.stripe.dto.StripeLineItemDto;
import com.kst.movie_ticket_reservation.security.current.CurrentPerson;
import com.kst.movie_ticket_reservation.util.enums.OrderStatus;
import com.kst.movie_ticket_reservation.util.enums.ShowSeatStatus;
import com.kst.movie_ticket_reservation.util.events.SeatSoldEvent;
import com.kst.movie_ticket_reservation.util.exceptions.BadRequestException;
import com.kst.movie_ticket_reservation.util.exceptions.CustomStripeException;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;
import com.kst.movie_ticket_reservation.util.exceptions.UnauthorizedException;
import com.kst.movie_ticket_reservation.util.job_payloads.SeatSoldPayload;
import com.stripe.StripeClient;
import com.stripe.exception.EventDataObjectDeserializationException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.CouponCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class StripeService
{

    @Value("${stripe.webhook.secret}")
    private String stripeWebhookSecret;

    private final StripeClient stripeClient;
    private final OrderRepository orderRepository;
    private final ShowSeatRepository showSeatRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final ShowTimeRepository showTimeRepository;
    private final ShowSeatMapper showSeatMapper;
    private final PromoCodeRedemptionRepository promoCodeRedemptionRepository;
    private final PromoCodeRepository promoCodeRepository;


    public String createCheckoutSession(CurrentPerson currentPerson,
                                        Long orderId, String orderPublicId,
                                        Long showTimeId,
                                        BigDecimal taxAmount,
                                        BigDecimal promoCodeDiscountAmount,
                                        String promoCode,
                                        List<Long> showSeatIds,
                                        List<StripeLineItemDto> stripeLineItemDtoList
    ) throws StripeException
    {
        List<SessionCreateParams.LineItem> lineItemList = this.createLineItems(stripeLineItemDtoList);


        if (taxAmount.compareTo(BigDecimal.ZERO) > 0)
        {
            long taxInCents = taxAmount.multiply(BigDecimal.valueOf(100)).longValue();

            SessionCreateParams.LineItem taxLineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)
                    .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("usd")
                            .setUnitAmount(taxInCents)
                            .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName("5% Tax")
                                    .build())
                            .build())
                    .build();

            lineItemList.add(taxLineItem);
        }

        long expireAtUnix = Instant.now().plusSeconds(1800).getEpochSecond();

        SessionCreateParams.Builder builder =
                SessionCreateParams.builder()
                        .setSuccessUrl("http://localhost:3000/payment/success?" + "session_id={CHECKOUT_SESSION_ID}" +
                                "&order=" + orderPublicId)
                        .setCancelUrl("http://localhost:3000/payment/cancel?order=" + orderPublicId)
                        .setExpiresAt(expireAtUnix)
                        .setCustomerEmail(currentPerson.getEmail())
                        .addAllLineItem(lineItemList)
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .putMetadata("orderId", orderId.toString())
                        .putMetadata("showTimeId", showTimeId.toString())
                        .putMetadata("customerId", currentPerson.getId().toString())
                        .putMetadata("promoCode", promoCode)
                        .putMetadata("showSeatIds", this.objectMapper.writeValueAsString(showSeatIds));

        if (promoCodeDiscountAmount.compareTo(BigDecimal.ZERO) > 0)
        {
            long discountInCents = promoCodeDiscountAmount.multiply(BigDecimal.valueOf(100)).longValue();

            builder.addDiscount(SessionCreateParams.Discount.builder()
                    .setCoupon(stripeClient.v1().coupons().create(
                            CouponCreateParams.builder()
                                    .setAmountOff(discountInCents)
                                    .setCurrency("usd")
                                    .setDuration(CouponCreateParams.Duration.ONCE)
                                    .setName("Promo Code Discount")
                                    .build()
                    ).getId())
                    .build());
        }

        Session session = stripeClient.v1().checkout().sessions().create(builder.build());

        return session.getUrl();
    }

    private List<SessionCreateParams.LineItem> createLineItems(List<StripeLineItemDto> stripeLineItemDtoList)
    {
        List<SessionCreateParams.LineItem> lineItemList = new ArrayList<>();

        for (StripeLineItemDto stripeLineItemDto : stripeLineItemDtoList)
        {
            BigDecimal priceInCents = stripeLineItemDto.price().multiply(BigDecimal.valueOf(100));

            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)
                    .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("usd")
                            .setUnitAmountDecimal(priceInCents)
                            .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName(String.format("%s - %s (%s)", stripeLineItemDto.row(),
                                            stripeLineItemDto.seatNumber(), stripeLineItemDto.seatType()))
                                    .build())
                            .build())
                    .build();

            lineItemList.add(lineItem);
        }

        return lineItemList;
    }


    @Transactional
    public void handleWebhook(String payload, String signatureHeader) throws BadRequestException, NotFoundException,
            UnauthorizedException, CustomStripeException
    {

        // handle by order for idempotent
        // and check with order status
        // and check total amount from order and from payment session success for security

        if (signatureHeader == null)
        {
            throw new BadRequestException("Missing Stripe Signature Header");
        }

        Event event;

        try
        {
            event = Webhook.constructEvent(payload, signatureHeader, stripeWebhookSecret);
        }
        catch (SignatureVerificationException e)
        {
            throw new CustomStripeException(e.getMessage());
        }

        log.info("event type " + event.getType());

        if (event.getType().equals("checkout.session.completed"))
        {
            handleCompleteCheckoutSessionEvent(event);
        }
        else
        {
            log.info("un handle event " + event.getType());
        }

    }

    private void handleCompleteCheckoutSessionEvent(Event event) throws BadRequestException, NotFoundException,
            UnauthorizedException, CustomStripeException
    {
        Session session = this.extractSessionFromEvent(event);

        ShowTime existingShowTime = this.findExistingShowTime(session);

        log.info("existing show time id in webhook " + existingShowTime.getId());

        User existingUser = this.findExistingUser(session);

        Order existingOrder = this.findExistingOrder(session);

        log.info("order id in webhook is " + existingOrder.getId());

        if (existingOrder.getStatus() == OrderStatus.PAID)
        {
            log.info("order for " + existingOrder.getId() + " already paid");
            return;
        }

        BigDecimal exceptedOrderTotal = existingOrder.getOrderTotal();

        log.info("existing order total " + exceptedOrderTotal);

        BigDecimal orderTotalFromPayment = BigDecimal.valueOf(session.getAmountTotal())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        log.info("order total from payment " + orderTotalFromPayment);

        if (exceptedOrderTotal.compareTo(orderTotalFromPayment) != 0)
        {
            throw new CustomStripeException("payment amount mismatch");
        }

        Order updatedOrder = this.updateOrder(existingOrder, session);

        List<ShowSeat> updatedShowSeatStatusList = this.updateShowSeatStatus(updatedOrder, session);

        this.fireSeatSoldEvent(existingUser.getId(), existingShowTime.getId(), updatedShowSeatStatusList);

        this.handlePromoCode(session, existingUser, updatedOrder);
    }

    private ShowTime findExistingShowTime(Session session) throws NotFoundException
    {
        Long showTimeId = Long.parseLong(session.getMetadata().get("showTimeId"));
        return this.showTimeRepository.findById(showTimeId).orElseThrow(() -> new NotFoundException(
                "show time not found"));
    }

    private User findExistingUser(Session session) throws UnauthorizedException
    {
        Long customerId = Long.parseLong(session.getMetadata().get("customerId"));
        return this.userRepository.findById(customerId).orElseThrow(() -> new UnauthorizedException(
                "invalid"));
    }

    private Order findExistingOrder(Session session) throws NotFoundException, BadRequestException
    {
        Long orderId = Long.parseLong(session.getMetadata().get("orderId"));

        return this.orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("order " +
                "not found"));
    }

    private Order updateOrder(Order existinOrder,
                              Session session)
    {
        existinOrder.setStatus(OrderStatus.PAID);
        existinOrder.setStripePaymentIntentId(session.getPaymentIntent());
        existinOrder.setStripeCheckoutSessionId(session.getId());

        return this.orderRepository.save(existinOrder);
    }

    private List<ShowSeat> updateShowSeatStatus(Order updatedOrder, Session session)
    {
        List<Long> showSeatIds = objectMapper.readValue(session.getMetadata().get("showSeatIds"),
                new TypeReference<List<Long>>()
                {
                });

        List<ShowSeat> showSeatList = this.showSeatRepository.findAllByIds(showSeatIds);

        showSeatList.forEach(showSeat ->
        {
            showSeat.setShowSeatStatus(ShowSeatStatus.SOLD);
            showSeat.setOrder(updatedOrder);
        });

        return this.showSeatRepository.saveAll(showSeatList);
    }

    private void fireSeatSoldEvent(Long currentUserId, Long showTimeId, List<ShowSeat> updatedShowSeatStatusList)
    {
        SeatSoldPayload seatSoldPayload = new SeatSoldPayload(currentUserId, showTimeId,
                updatedShowSeatStatusList.stream().map(this.showSeatMapper::toShowSeatResDto).toList());

        this.applicationEventPublisher.publishEvent(new SeatSoldEvent(seatSoldPayload));
    }

    private void handlePromoCode(Session session, User existingUser, Order updatedOrder) throws NotFoundException
    {
        String code = session.getMetadata().get("promoCode");

        if (code != null && !code.isEmpty())
        {
            PromoCode existingPromoCode =
                    this.promoCodeRepository.findByCode(code).orElseThrow(() -> new NotFoundException(
                            "promo code not found"));

            PromoCodeRedemption promoCodeRedemption = new PromoCodeRedemption();
            promoCodeRedemption.setUser(existingUser);
            promoCodeRedemption.setPromoCode(existingPromoCode);
            promoCodeRedemption.setDiscountAmount(updatedOrder.getPromoCodeDiscountAmount());
            promoCodeRedemption.setRedeemedAt(Instant.now());

            PromoCodeRedemption createdPromoCodeRedemption =
                    this.promoCodeRedemptionRepository.save(promoCodeRedemption);

            updatedOrder.setPromoCodeRedemption(createdPromoCodeRedemption);
            this.orderRepository.save(updatedOrder);
        }
    }


    private Session extractSessionFromEvent(Event event) throws BadRequestException
    {
        EventDataObjectDeserializer eventDataObjectDeserializer = event.getDataObjectDeserializer();
        Optional<StripeObject> stripeObject = eventDataObjectDeserializer.getObject();

        if (stripeObject.isPresent())
        {
            return (Session) stripeObject.get();
        }

        try
        {
            return (Session) eventDataObjectDeserializer.deserializeUnsafe();
        }
        catch (EventDataObjectDeserializationException e)
        {
            throw new BadRequestException("fail to process stripe webhook data");
        }
    }

    public Session retrieveSession(String sessionId) throws StripeException
    {
        return stripeClient.v1().checkout().sessions().retrieve(sessionId);
    }
}
