package com.kst.movie_ticket_reservation.feat.show_seat.mapper;

import com.kst.movie_ticket_reservation.feat.show_seat.dto.req.CreateShowSeatDto;
import com.kst.movie_ticket_reservation.feat.show_seat.dto.res.ShowSeatResDto;
import com.kst.movie_ticket_reservation.feat.show_seat.entity.ShowSeat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ShowSeatMapper
{
    ShowSeat toShowSeat(CreateShowSeatDto createShowSeatDto);

    @Mapping(target = "row", source = "seat.row")
    @Mapping(target = "seatNumber", source = "seat.seatNumber")
    @Mapping(target = "seatType", source = "seat.seatType")
        // @Mapping(target = "basePrice", source = "basePrice")
    ShowSeatResDto toShowSeatResDto(ShowSeat showSeat);
}
