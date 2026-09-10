package com.kst.movie_ticket_reservation.feat.cast.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.kst.movie_ticket_reservation.auditing.AuditableBaseEntity;
import com.kst.movie_ticket_reservation.feat.movie.entity.Movie;
import com.kst.movie_ticket_reservation.util.enums.CastType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "casts")
public class Cast extends AuditableBaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", unique = true, nullable = false, updatable = false)
    private String publicId;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String slug;

    @Enumerated(EnumType.STRING)
    // @Column(nullable = false)
    private CastType castType;

    @ManyToMany(mappedBy = "casts")
    private Set<Movie> movies = new HashSet<>();

    @PrePersist
    protected void insertPublicId()
    {
        if (this.publicId == null)
        {
            this.publicId = NanoIdUtils.randomNanoId();
        }
    }
}
