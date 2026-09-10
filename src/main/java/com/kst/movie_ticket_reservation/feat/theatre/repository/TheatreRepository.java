package com.kst.movie_ticket_reservation.feat.theatre.repository;

import com.kst.movie_ticket_reservation.feat.theatre.entity.Theatre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Long>
{
    Optional<Theatre> findByIdAndIsDeletedFalse(Long id);

    Optional<Theatre> findByIdAndIsDeletedTrue(Long id);

    Page<Theatre> findByIsDeletedFalse(Pageable pageable);

    boolean existsByNameIgnoreCaseOrSlugIgnoreCase(String name, String slug);
}
