package com.kst.movie_ticket_reservation.feat.seat.mapper;

import com.kst.movie_ticket_reservation.feat.seat.dto.req.CreateSeatDto;
import com.kst.movie_ticket_reservation.feat.seat.dto.req.UpdateSeatDto;
import com.kst.movie_ticket_reservation.feat.seat.dto.res.SeatResDto;
import com.kst.movie_ticket_reservation.feat.seat.entity.Seat;
import com.kst.movie_ticket_reservation.feat.theatre.mapper.TheatreMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SeatMapper
{
    SeatResDto toSeatResDto(Seat seat);

    Seat toSeat(CreateSeatDto createSeatDto);

    void updateSeatFromDto(UpdateSeatDto updateSeatDto, @MappingTarget Seat seat);
}
