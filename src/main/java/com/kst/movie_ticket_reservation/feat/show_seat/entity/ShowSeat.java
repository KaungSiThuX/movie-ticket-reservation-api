package com.kst.movie_ticket_reservation.feat.show_seat.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.kst.movie_ticket_reservation.feat.order.entity.Order;
import com.kst.movie_ticket_reservation.feat.seat.entity.Seat;
import com.kst.movie_ticket_reservation.feat.show_time.entity.ShowTime;
import com.kst.movie_ticket_reservation.util.entities.BaseEntity;
import com.kst.movie_ticket_reservation.util.enums.DiscountType;
import com.kst.movie_ticket_reservation.util.enums.ShowSeatStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "show_seats")
public class ShowSeat extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", unique = true, nullable = false, updatable = false)
    private String publicId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private Seat seat;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "show_date_id")
//    private ShowDate showDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_time_id")
    private ShowTime showTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "base_price", precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "payable_amount", precision = 10, scale = 2)
    private BigDecimal payableAmount;

    private BigDecimal discountAmount;

    private DiscountType discountType;

    @Enumerated(EnumType.STRING)
    @Column(name = "show_seat_status")
    private ShowSeatStatus showSeatStatus;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @PrePersist
    protected void insertPublicId()
    {
        if (this.publicId == null)
        {
            this.publicId = NanoIdUtils.randomNanoId();
        }
    }

    public void hide()
    {
        this.isDeleted = true;
        this.deletedAt = Instant.now();
    }

    public void show()
    {
        this.isDeleted = false;
        this.deletedAt = null;
    }
}
