package com.kst.movie_ticket_reservation.feat.director.repository;

import com.kst.movie_ticket_reservation.feat.director.enitty.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Long>
{
    boolean existsByNameIgnoreCaseOrSlugIgnoreCase(String name, String slug);
}
