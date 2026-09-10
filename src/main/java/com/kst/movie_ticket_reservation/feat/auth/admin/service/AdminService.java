package com.kst.movie_ticket_reservation.feat.auth.admin.service;

import com.kst.movie_ticket_reservation.feat.auth.admin.dto.req.AdminSignInDto;
import com.kst.movie_ticket_reservation.feat.auth.admin.dto.req.CreateAdminDto;
import com.kst.movie_ticket_reservation.feat.auth.admin.dto.req.TokenRefreshDto;
import com.kst.movie_ticket_reservation.feat.auth.admin.dto.res.AdminResDto;
import com.kst.movie_ticket_reservation.feat.auth.admin.dto.res.CreateAdminResDto;
import com.kst.movie_ticket_reservation.feat.auth.dto.res.CommonAuthResDto;
import com.kst.movie_ticket_reservation.util.exceptions.ConflictException;
import com.kst.movie_ticket_reservation.util.exceptions.UnauthorizedException;

public interface AdminService
{
    CommonAuthResDto<AdminResDto> signIn(AdminSignInDto adminSignInDto) throws UnauthorizedException;

    AdminResDto createAdmin(CreateAdminDto createAdminDto) throws ConflictException;

    CommonAuthResDto<AdminResDto> refresh(TokenRefreshDto tokenRefreshDto) throws UnauthorizedException;
}
