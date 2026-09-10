package com.kst.movie_ticket_reservation.feat.show_time.repository;

import com.kst.movie_ticket_reservation.feat.show_time.entity.ShowTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShowTimeRepository extends JpaRepository<ShowTime, Long>
{
    Optional<ShowTime> findByPublicId(String publicId);

    Page<ShowTime> findByShowDateId(Long showDateId, Pageable pageable);
}
