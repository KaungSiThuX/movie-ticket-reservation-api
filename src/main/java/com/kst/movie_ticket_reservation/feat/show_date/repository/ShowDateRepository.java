package com.kst.movie_ticket_reservation.feat.show_date.repository;

import com.kst.movie_ticket_reservation.feat.movie.entity.Movie;
import com.kst.movie_ticket_reservation.feat.show_date.entity.ShowDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShowDateRepository extends JpaRepository<ShowDate, Long>
{
    Optional<ShowDate> findByTheatreIdAndMovieIdAndShowDisplayDate(Long theatreId, Long movieId,
                                                                   Instant showDisplayDate);

    Optional<ShowDate> findByPublicId(String publicId);

    boolean existsByShowDisplayDateAndTheatreIdAndMovieId(Instant showDisplayDate, Long theatreId, Long movieId);

    boolean existsByShowDisplayDateAndTheatreIdAndMovieIdAndIsDeletedFalse(
            Instant showDisplayDate, Long theatreId, Long movieId
    );

    List<ShowDate> findByMovieId(Long movieId);
}
