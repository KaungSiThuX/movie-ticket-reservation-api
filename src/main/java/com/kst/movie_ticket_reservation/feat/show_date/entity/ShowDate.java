package com.kst.movie_ticket_reservation.feat.show_date.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.kst.movie_ticket_reservation.auditing.AuditableBaseEntity;
import com.kst.movie_ticket_reservation.feat.show_time.entity.ShowTime;
import com.kst.movie_ticket_reservation.feat.theatre.entity.Theatre;
import com.kst.movie_ticket_reservation.feat.movie.entity.Movie;
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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "show_dates", uniqueConstraints = {@UniqueConstraint(name = "uk_theatre_id_movie_id_show_display_date",
        columnNames = {"theatre_id", "movie_id", "show_display_date"})})
public class ShowDate extends AuditableBaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", unique = true, nullable = false, updatable = false)
    private String publicId;

    @Column(nullable = false, name = "show_display_date")
    private Instant showDisplayDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theatre_id")
    private Theatre theatre;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @OneToMany(mappedBy = "showDate")
    private Set<ShowTime> showTimes = new HashSet<>();

//    @OneToMany(mappedBy = "showDate")
//    private Set<ShowSeat> showSeats = new HashSet<>();

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

        if (this.showTimes != null && !this.showTimes.isEmpty())
        {
            this.showTimes.forEach(ShowTime::hide);
        }
    }

    public void show()
    {
        this.isDeleted = false;
        this.deletedAt = null;

        if (this.showTimes != null && !this.showTimes.isEmpty())
        {
            this.showTimes.forEach(ShowTime::show);
        }
    }
}
