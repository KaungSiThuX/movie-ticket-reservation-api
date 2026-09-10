package com.kst.movie_ticket_reservation.feat.show_date.mapper;

import com.kst.movie_ticket_reservation.feat.show_date.dto.req.CreateShowDateDto;
import com.kst.movie_ticket_reservation.feat.show_date.dto.req.UpdateShowDateDto;
import com.kst.movie_ticket_reservation.feat.show_date.dto.res.ShowDateResDto;
import com.kst.movie_ticket_reservation.feat.show_date.entity.ShowDate;
import com.kst.movie_ticket_reservation.feat.theatre.dto.req.UpdateTheatreDto;
import com.kst.movie_ticket_reservation.feat.theatre.entity.Theatre;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ShowDateMapper
{
    ShowDate toShowDate(CreateShowDateDto createShowDateDto);

    ShowDateResDto toShowDateResDto(ShowDate showDate);

    void updateShowDateFromDto(UpdateShowDateDto updateShowDateDto, @MappingTarget ShowDate showDate);

}
