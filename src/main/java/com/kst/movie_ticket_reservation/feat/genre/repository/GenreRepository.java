package com.kst.movie_ticket_reservation.feat.genre.repository;

import com.kst.movie_ticket_reservation.feat.genre.entity.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long>
{
    Page<Genre> findByIsDeletedFalseOrderByIdDesc(Pageable pageable);

    Optional<Genre> findByIdAndIsDeletedFalse(Long id);

    Optional<Genre> findByIdAndIsDeletedTrue(Long id);

    boolean existsByNameIgnoreCaseOrSlugIgnoreCase(String name, String slug);
}
