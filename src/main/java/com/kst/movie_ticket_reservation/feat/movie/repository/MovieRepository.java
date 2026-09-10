package com.kst.movie_ticket_reservation.feat.movie.repository;

import com.kst.movie_ticket_reservation.feat.movie.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>
{
    @EntityGraph(attributePaths = {"genres"})
    Page<Movie> findByIsDeletedFalse(Pageable pageable);

    @EntityGraph(attributePaths = {"genres"})
    Optional<Movie> findByIdAndIsDeletedFalse(Long id);

    @EntityGraph(attributePaths = {"genres"})
    Optional<Movie> findByIdAndIsDeletedTrue(Long id);

    boolean existsByTitleIgnoreCaseOrSlugIgnoreCase(String title, String slug);

    @Query("SELECT m FROM Movie m WHERE (:cursor IS NULL OR  m.id < :cursor) AND EXISTS (SELECT s FROM m.showDates s)" +
            " ORDER BY m.id DESC")
    Slice<Movie> findAllByCursor(@Param("cursor") Long cursor, Pageable pageable);
}
