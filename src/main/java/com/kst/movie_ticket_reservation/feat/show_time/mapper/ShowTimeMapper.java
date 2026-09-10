package com.kst.movie_ticket_reservation.feat.show_time.mapper;

import com.kst.movie_ticket_reservation.feat.genre.dto.req.UpdateGenreDto;
import com.kst.movie_ticket_reservation.feat.genre.entity.Genre;
import com.kst.movie_ticket_reservation.feat.show_time.dto.req.CreateShowTimeDto;
import com.kst.movie_ticket_reservation.feat.show_time.dto.req.UpdateShowTimeDto;
import com.kst.movie_ticket_reservation.feat.show_time.dto.res.ShowTimeResDto;
import com.kst.movie_ticket_reservation.feat.show_time.entity.ShowTime;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ShowTimeMapper
{
    ShowTime toShowTime(CreateShowTimeDto createShowTimeDto);

    ShowTimeResDto toShowTimeResDto(ShowTime showTime);

    void updateShowTimeFromDto(UpdateShowTimeDto updateShowTimeDto, @MappingTarget ShowTime showTime);
}
