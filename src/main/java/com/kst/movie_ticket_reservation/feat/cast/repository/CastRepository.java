package com.kst.movie_ticket_reservation.feat.cast.repository;

import com.kst.movie_ticket_reservation.feat.cast.entity.Cast;
import com.kst.movie_ticket_reservation.util.enums.CastType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CastRepository extends JpaRepository<Cast, Long>
{
    boolean existsByNameIgnoreCaseOrSlugIgnoreCase(String name, String slug);

    Optional<Cast> findByNameIgnoreCaseAndSlugIgnoreCaseAndCastType(String name, String slug, CastType castType);
}
