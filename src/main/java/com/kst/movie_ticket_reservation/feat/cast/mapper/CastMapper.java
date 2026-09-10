package com.kst.movie_ticket_reservation.feat.cast.mapper;

import com.kst.movie_ticket_reservation.feat.cast.dto.req.CreateCastDto;
import com.kst.movie_ticket_reservation.feat.cast.dto.req.UpdateCastDto;
import com.kst.movie_ticket_reservation.feat.cast.dto.res.CastResDto;
import com.kst.movie_ticket_reservation.feat.cast.entity.Cast;
import com.kst.movie_ticket_reservation.feat.director.dto.req.CreateDirectorDto;
import com.kst.movie_ticket_reservation.feat.director.dto.req.UpdateDirectorDto;
import com.kst.movie_ticket_reservation.feat.director.dto.res.DirectorResDto;
import com.kst.movie_ticket_reservation.feat.director.enitty.Director;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CastMapper
{
    Cast toCast(CreateCastDto createCastDto);

    CastResDto toCastResDto(Cast cast);

    void updateCastFromDto(UpdateCastDto updateCastDto, @MappingTarget Cast cast);
}
