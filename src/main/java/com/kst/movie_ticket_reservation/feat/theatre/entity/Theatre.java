package com.kst.movie_ticket_reservation.feat.theatre.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.kst.movie_ticket_reservation.auditing.AuditableBaseEntity;
import com.kst.movie_ticket_reservation.feat.order.entity.Order;
import com.kst.movie_ticket_reservation.feat.seat.entity.Seat;
import com.kst.movie_ticket_reservation.feat.show_date.entity.ShowDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "theatres")
public class Theatre extends AuditableBaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", unique = true, nullable = false, updatable = false)
    private String publicId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @OneToMany(mappedBy = "theatre", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Seat> seats = new HashSet<>();

    @OneToMany(mappedBy = "theatre")
    private Set<ShowDate> showDates = new HashSet<>();

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

        if (this.seats != null && !this.seats.isEmpty())
        {
            this.seats.forEach(Seat::hide);
        }

        if (this.showDates != null && !this.showDates.isEmpty())
        {
            this.showDates.forEach(ShowDate::hide);
        }
    }

    public void show()
    {
        this.isDeleted = false;
        this.deletedAt = null;

        if (this.seats != null && !this.seats.isEmpty())
        {
            this.seats.forEach(Seat::show);
        }

        if (this.showDates != null && !this.showDates.isEmpty())
        {
            this.showDates.forEach(ShowDate::show);
        }
    }
}
