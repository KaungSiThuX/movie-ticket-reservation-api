package com.kst.movie_ticket_reservation.feat.movie.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.kst.movie_ticket_reservation.auditing.AuditableBaseEntity;
import com.kst.movie_ticket_reservation.feat.cast.entity.Cast;
import com.kst.movie_ticket_reservation.feat.director.enitty.Director;
import com.kst.movie_ticket_reservation.feat.genre.entity.Genre;
import com.kst.movie_ticket_reservation.feat.show_date.entity.ShowDate;
import com.kst.movie_ticket_reservation.util.enums.MPARatingType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "movies")
public class Movie extends AuditableBaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", unique = true, nullable = false, updatable = false)
    private String publicId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(length = 2000)
    private String description;

    private String poster;

    private String trailer;

    @Column(name = "release_year")
    private String releaseYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "mpa_rating_type", nullable = false)
    private MPARatingType mpaRatingType;

    @Column(name = "run_time_minutes")
    private Integer runTimeMinutes;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @ManyToMany
    @JoinTable(
            name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    //  @SQLRestriction("is_deleted = false")
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "movie_directors",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "director_id")
    )
    //  @SQLRestriction("is_deleted = false")
    private Set<Director> directors = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "movie_casts",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "cast_id")
    )
    //  @SQLRestriction("is_deleted = false")
    private Set<Cast> casts = new HashSet<>();

    @OneToMany(mappedBy = "movie")
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

        if (this.showDates != null && !this.showDates.isEmpty())
        {
            this.showDates.forEach(ShowDate::hide);
        }
    }

    public void show()
    {
        this.isDeleted = false;
        this.deletedAt = null;

        if (this.showDates != null && !this.showDates.isEmpty())
        {
            this.showDates.forEach(ShowDate::show);
        }
    }
}
