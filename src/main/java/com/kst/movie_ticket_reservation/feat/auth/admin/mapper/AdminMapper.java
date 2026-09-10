package com.kst.movie_ticket_reservation.feat.auth.admin.mapper;

import com.kst.movie_ticket_reservation.feat.auth.admin.dto.req.CreateAdminDto;
import com.kst.movie_ticket_reservation.feat.auth.admin.dto.res.AdminResDto;
import com.kst.movie_ticket_reservation.feat.auth.admin.entity.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AdminMapper
{
    AdminResDto toAdminResDto(Admin admin);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    Admin toAdmin(CreateAdminDto createAdminDto);
}
