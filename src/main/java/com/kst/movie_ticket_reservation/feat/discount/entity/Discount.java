//package com.kst.movie_ticket_reservation.feat.discount.entity;
//
//import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
//import com.kst.movie_ticket_reservation.auditing.AuditableBaseEntity;
//import com.kst.movie_ticket_reservation.util.enums.DiscountScope;
//import com.kst.movie_ticket_reservation.util.enums.DiscountType;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.math.BigDecimal;
//import java.time.Instant;
//import java.time.LocalTime;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "discounts")
//public class Discount extends AuditableBaseEntity
//{
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "public_id", unique = true, nullable = false, updatable = false)
//    private String publicId;
//
//    private String name;
//
//    private String description;
//
//    private DiscountType discountType;
//
//    @Column(name = "discount_value", precision = 10, scale = 2)
//    private BigDecimal discountValue;
//
//    @Column(name = "discount_scope")
//    private DiscountScope discountScope;
//
//    @Column(name = "minimum_spend", precision = 10, scale = 2)
//    private BigDecimal minimumSpend;
//
//    @Column(name = "minmum_seat_quantity")
//    private Integer minimumSeatQuantity;
//
//    @Column(name = "valid_from")
//    private Instant validFrom;
//
//    @Column(name = "valid_until")
//    private Instant validUntil;
//
//    // need to recheck
//    @Column(name = "start_time_of_day")
//    private LocalTime startTimeOfDay;
//
//    @Column(name = "end_time_of_day")
//    private LocalTime endTimeOfDay;
//    // need to recheck
//
//    @Column(name = "usage_limit_total")
//    private Integer usageLimitTotal;
//
//    @Column(name = "usage_limit_per_user")
//    private Integer usageLimitPerUser;
//
//    @Column(name = "usage_total")
//    private Integer usageTotal = 0;
//
//    private Boolean isActive;
//
//    @PrePersist
//    protected void insertPublicId()
//    {
//        if (this.publicId == null)
//        {
//            this.publicId = NanoIdUtils.randomNanoId();
//        }
//    }
//}
