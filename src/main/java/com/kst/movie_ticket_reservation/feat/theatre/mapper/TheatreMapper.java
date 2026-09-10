package com.kst.movie_ticket_reservation.feat.theatre.mapper;

import com.kst.movie_ticket_reservation.feat.seat.mapper.SeatMapper;
import com.kst.movie_ticket_reservation.feat.show_date.mapper.ShowDateMapper;
import com.kst.movie_ticket_reservation.feat.theatre.dto.req.CreateTheatreDto;
import com.kst.movie_ticket_reservation.feat.theatre.dto.req.UpdateTheatreDto;
import com.kst.movie_ticket_reservation.feat.theatre.dto.res.TheatreNameResDto;
import com.kst.movie_ticket_reservation.feat.theatre.dto.res.TheatreResDto;
import com.kst.movie_ticket_reservation.feat.theatre.entity.Theatre;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {SeatMapper.class, ShowDateMapper.class})
public interface TheatreMapper
{
    TheatreResDto toTheatreResDto(Theatre theatre);

    TheatreNameResDto toTheatreNameResDto(Theatre theatre);

    Theatre toTheatre(CreateTheatreDto createTheatreDto);

    void updateTheatreFromDto(UpdateTheatreDto updateTheatreDto, @MappingTarget Theatre theatre);

//    @AfterMapping
//    default void linkChildren(@MappingTarget Theatre theatre)
//    {
//        if (theatre.getSeats() != null && !theatre.getSeats().isEmpty())
//        {
//            theatre.getSeats().forEach(seat -> seat.setTheatre(theatre));
//        }
//
//        if (theatre.getShowDates() != null && !theatre.getShowDates().isEmpty())
//        {
//            theatre.getShowDates().forEach(showDate -> showDate.setTheatre(theatre));
//        }
//    }
}