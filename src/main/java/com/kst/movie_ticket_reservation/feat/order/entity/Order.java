package com.kst.movie_ticket_reservation.feat.order.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.kst.movie_ticket_reservation.feat.auth.user.entity.User;
import com.kst.movie_ticket_reservation.feat.promo_code_redemption.entity.PromoCodeRedemption;
import com.kst.movie_ticket_reservation.feat.show_seat.entity.ShowSeat;
import com.kst.movie_ticket_reservation.feat.show_time.entity.ShowTime;
import com.kst.movie_ticket_reservation.util.entities.BaseEntity;
import com.kst.movie_ticket_reservation.util.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", unique = true, nullable = false, updatable = false)
    private String publicId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_time_id")
    private ShowTime showTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(unique = true, name = "stripe_payment_intent_id")
    private String stripePaymentIntentId;

    @Column(unique = true, name = "stripe_checkout_session_id")
    private String stripeCheckoutSessionId;

    @Column(name = "sub_total", precision = 10, scale = 2)
    private BigDecimal subTotal;

    @Column(name = "order_total", precision = 10, scale = 2)
    private BigDecimal orderTotal;

    @Column(name = "promo_code_discount_amount", precision = 10, scale = 2)
    private BigDecimal promoCodeDiscountAmount = BigDecimal.ZERO;

    @Column(name = "rule_based_discount_amount", precision = 10, scale = 2)
    private BigDecimal ruleBasedDiscountAmount = BigDecimal.ZERO; // need to update name

    @Column(precision = 10, scale = 2)
    private BigDecimal tax = BigDecimal.ZERO;

    @OneToMany(mappedBy = "order")
    @Column(name = "show_seats")
    private Set<ShowSeat> showSeats = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "promo_code_redemption_id", referencedColumnName = "id")
    private PromoCodeRedemption promoCodeRedemption;

    @PrePersist
    protected void insetPublicId()
    {
        if (this.publicId == null)
        {
            this.publicId = NanoIdUtils.randomNanoId();
        }
    }
}
