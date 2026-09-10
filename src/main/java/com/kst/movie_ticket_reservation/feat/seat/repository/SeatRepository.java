package com.kst.movie_ticket_reservation.feat.seat.repository;

import com.kst.movie_ticket_reservation.feat.seat.entity.Seat;
import com.kst.movie_ticket_reservation.feat.show_seat.entity.ShowSeat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long>
{

    List<Seat> findAllByTheatreId(Long theatreId);

    Page<Seat> findAllByTheatreId(Long theatreId, Pageable pageable);

    Optional<Seat> findByIdAndIsDeletedFalse(Long id);

    Optional<Seat> findByIdAndIsDeletedTrue(Long id);

    boolean existsByTheatreIdAndRowAndSeatNumber(Long theatreId, String row, String seatNumber);
}
