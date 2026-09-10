package com.kst.movie_ticket_reservation.feat.auth.admin.controller;

import com.kst.movie_ticket_reservation.feat.auth.admin.dto.req.AdminSignInDto;
import com.kst.movie_ticket_reservation.feat.auth.admin.dto.req.CreateAdminDto;
import com.kst.movie_ticket_reservation.feat.auth.admin.dto.req.TokenRefreshDto;
import com.kst.movie_ticket_reservation.feat.auth.admin.dto.res.AdminResDto;
import com.kst.movie_ticket_reservation.feat.auth.admin.service.AdminService;
import com.kst.movie_ticket_reservation.feat.auth.dto.res.CommonAuthResDto;
import com.kst.movie_ticket_reservation.security.current.CurrentPerson;
import com.kst.movie_ticket_reservation.util.api_responses.SuccessApiResponse;
import com.kst.movie_ticket_reservation.util.exceptions.ConflictException;
import com.kst.movie_ticket_reservation.util.exceptions.UnauthorizedException;
import com.kst.movie_ticket_reservation.util.handlers.SuccessApiResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth/admin")
@RequiredArgsConstructor
public class AdminController
{
    private final AdminService adminService;
    private final SuccessApiResponseHandler successApiResponseHandler;

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping
    ResponseEntity<SuccessApiResponse<AdminResDto>> createAdmin(@Valid @RequestBody CreateAdminDto createAdminDto,
                                                                @AuthenticationPrincipal CurrentPerson currentPerson) throws ConflictException
    {
//        for (GrantedAuthority role : currentPerson.getAuthorities())
//        {
//            log.info("role is " + role);
//        }
        AdminResDto adminResDto = this.adminService.createAdmin(createAdminDto);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "CREATE_ADMIN_SUCCESS", adminResDto);
    }

    @PostMapping("sign-in")
    ResponseEntity<SuccessApiResponse<CommonAuthResDto<AdminResDto>>> signIn(@Valid @RequestBody AdminSignInDto adminSignInDto) throws UnauthorizedException
    {
        log.info("hit sign in endpoint");
        log.info("hit mail " + adminSignInDto.getEmail());
        log.info("hit password " + adminSignInDto.getPassword());
        CommonAuthResDto<AdminResDto> commonAuthResDto = this.adminService.signIn(adminSignInDto);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "ADMIN_SIGN_IN_SUCCESS",
                commonAuthResDto);
    }

    @PostMapping("refresh")
    ResponseEntity<SuccessApiResponse<CommonAuthResDto<AdminResDto>>> refresh(@Valid @RequestBody TokenRefreshDto tokenRefreshDto) throws UnauthorizedException
    {
        CommonAuthResDto<AdminResDto> commonAuthResDto = this.adminService.refresh(tokenRefreshDto);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "ADMIN_TOKEN_REFRESH_SUCCESS",
                commonAuthResDto);
    }

}
