package com.kst.movie_ticket_reservation.feat.public_api.mapper;

import com.kst.movie_ticket_reservation.feat.genre.entity.Genre;
import com.kst.movie_ticket_reservation.feat.movie.entity.Movie;
import com.kst.movie_ticket_reservation.feat.public_api.dto.res.*;
import com.kst.movie_ticket_reservation.feat.show_date.entity.ShowDate;
import com.kst.movie_ticket_reservation.feat.show_seat.entity.ShowSeat;
import com.kst.movie_ticket_reservation.feat.show_time.entity.ShowTime;
import com.kst.movie_ticket_reservation.feat.show_time.mapper.ShowTimeMapper;
import com.kst.movie_ticket_reservation.feat.theatre.entity.Theatre;
import com.kst.movie_ticket_reservation.feat.theatre.mapper.TheatreMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE
        // uses = {TheatreMapper.class, ShowTimeMapper.class}
)
public interface PublicApiMapper
{
    @Mapping(target = "id", source = "publicId")
    PublicApiShowDateResDto toPublicApiShowDateResDto(ShowDate showDate);

    @Mapping(target = "id", source = "publicId")
    PublicApiShowTimeResDto toPublicApiShowTimeResDto(ShowTime showTime);

    @Mapping(target = "id", source = "publicId")
    PublicApiTheatreResDto toPublicApiTheatreResDto(Theatre theatre);

    PublicApiGenreResDto toPublicApiGenreResDto(Genre genre);

    PublicApiMovieResDto toPublicApiMovieResDto(Movie movie);

    PublicApiMovieDetailsResDto toPublicApiMovieDetailsResDto(Movie movie);

    @Mapping(target = "id", source = "publicId")
    @Mapping(target = "row", source = "showSeat.seat.row")
    @Mapping(target = "seatNumber", source = "showSeat.seat.seatNumber")
    @Mapping(target = "seatType", source = "showSeat.seat.seatType")
    PublicApiShowSeatResDto toPublicApiShowSeatResDto(ShowSeat showSeat);
}
