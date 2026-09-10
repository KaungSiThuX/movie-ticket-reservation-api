package com.kst.movie_ticket_reservation.feat.show_time.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.kst.movie_ticket_reservation.auditing.AuditableBaseEntity;
import com.kst.movie_ticket_reservation.feat.movie.entity.Movie;
import com.kst.movie_ticket_reservation.feat.order.entity.Order;
import com.kst.movie_ticket_reservation.feat.show_date.entity.ShowDate;
import com.kst.movie_ticket_reservation.feat.show_seat.entity.ShowSeat;
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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "show_times")
public class ShowTime extends AuditableBaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", unique = true, nullable = false, updatable = false)
    private String publicId;

    @Column(name = "show_diaplay_time", nullable = false)
    private String showDisplayTime;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_date_id")
    private ShowDate showDate;

    @OneToMany(mappedBy = "showTime")
    private Set<ShowSeat> showSeats = new HashSet<>();

    @OneToMany(mappedBy = "showTime")
    private Set<Order> orders = new HashSet<>();

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
