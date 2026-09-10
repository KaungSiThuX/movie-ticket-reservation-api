package com.kst.movie_ticket_reservation.feat.show_seat.repository;

import com.kst.movie_ticket_reservation.feat.show_seat.entity.ShowSeat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long>
{
    @Query("SELECT s FROM ShowSeat s WHERE s.publicId IN :publicIds AND s.showSeatStatus = 'AVAILABLE'")
    List<ShowSeat> findAllAvailableByPublicId(@Param("publicIds") List<String> publicIds);

    @Query("SELECT s FROM ShowSeat s WHERE s.publicId IN :publicIds")
    List<ShowSeat> findAllByPublicId(@Param("publicIds") List<String> publicIds);

    @Query("SELECT s FROM ShowSeat s WHERE s.id IN :ids")
    List<ShowSeat> findAllByIds(@Param("ids") List<Long> ids);

    Optional<ShowSeat> findByPublicId(@Param("publicId") String publicId);

    List<ShowSeat> findAllByOrderId(@Param("orderId") Long orderId);

    List<ShowSeat> findAllByShowTimeId(Long showTimeId);

    Page<ShowSeat> findAllByShowTimeId(Long showTimeId, Pageable pageable);
}
