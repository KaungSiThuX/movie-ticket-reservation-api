package com.kst.movie_ticket_reservation.feat.seat.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.kst.movie_ticket_reservation.auditing.AuditableBaseEntity;
import com.kst.movie_ticket_reservation.feat.theatre.entity.Theatre;
import com.kst.movie_ticket_reservation.feat.show_seat.entity.ShowSeat;
import com.kst.movie_ticket_reservation.util.enums.SeatType;
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
@Table(name = "seats", uniqueConstraints = {
        @UniqueConstraint(name = "uk_theatre_id_row_seat_number", columnNames = {"theatre_id", "row", "seat_number"})
})
public class Seat extends AuditableBaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", unique = true, nullable = false, updatable = false)
    private String publicId;

    @Column(nullable = false, length = 2)
    private String row;

    @Column(nullable = false, name = "seat_number")
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_type", nullable = false)
    private SeatType seatType;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theatre_id")
    private Theatre theatre;

    @OneToMany(mappedBy = "seat")
    private Set<ShowSeat> showSeats = new HashSet<>();

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

        if (this.showSeats != null && !this.showSeats.isEmpty())
        {
            this.showSeats.forEach(ShowSeat::hide);
        }
    }

    public void show()
    {
        this.isDeleted = false;
        this.deletedAt = null;

        if (this.showSeats != null && !this.showSeats.isEmpty())
        {
            this.showSeats.forEach(ShowSeat::show);
        }
    }
}
