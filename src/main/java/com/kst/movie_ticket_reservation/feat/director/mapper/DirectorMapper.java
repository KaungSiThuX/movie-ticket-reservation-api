package com.kst.movie_ticket_reservation.feat.director.mapper;

import com.kst.movie_ticket_reservation.feat.director.dto.req.CreateDirectorDto;
import com.kst.movie_ticket_reservation.feat.director.dto.req.UpdateDirectorDto;
import com.kst.movie_ticket_reservation.feat.director.dto.res.DirectorResDto;
import com.kst.movie_ticket_reservation.feat.director.enitty.Director;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DirectorMapper
{
    Director toDirector(CreateDirectorDto createDirectorDto);

    DirectorResDto toDirectorResDto(Director director);

    void updateDirectorFromDto(UpdateDirectorDto updateDirectorDto, @MappingTarget Director director);
}
