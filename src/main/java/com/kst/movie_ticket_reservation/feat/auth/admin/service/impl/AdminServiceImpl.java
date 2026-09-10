package com.kst.movie_ticket_reservation.feat.auth.admin.service.impl;

import com.kst.movie_ticket_reservation.feat.auth.admin.dto.req.AdminSignInDto;
import com.kst.movie_ticket_reservation.feat.auth.admin.dto.req.CreateAdminDto;
import com.kst.movie_ticket_reservation.feat.auth.admin.dto.req.TokenRefreshDto;
import com.kst.movie_ticket_reservation.feat.auth.admin.dto.res.AdminResDto;
import com.kst.movie_ticket_reservation.feat.auth.admin.entity.Admin;
import com.kst.movie_ticket_reservation.feat.auth.admin.mapper.AdminMapper;
import com.kst.movie_ticket_reservation.feat.auth.admin.repository.AdminRepository;
import com.kst.movie_ticket_reservation.feat.auth.admin.service.AdminService;
import com.kst.movie_ticket_reservation.feat.auth.dto.res.CommonAuthResDto;
import com.kst.movie_ticket_reservation.security.jwt.JwtService;
import com.kst.movie_ticket_reservation.util.enums.Role;
import com.kst.movie_ticket_reservation.util.exceptions.ConflictException;
import com.kst.movie_ticket_reservation.util.exceptions.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService
{
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public CommonAuthResDto<AdminResDto> signIn(AdminSignInDto adminSignInDto) throws UnauthorizedException
    {
        Admin admin =
                this.adminRepository.findByEmail(adminSignInDto.getEmail()).orElseThrow(() -> new UnauthorizedException("Invalid Credentials"));

        if (!passwordEncoder.matches(adminSignInDto.getPassword(), admin.getPassword()))
        {
            throw new UnauthorizedException("Invalid Credentials");
        }

        String accessToken = this.jwtService.generateAccessToken(admin.getId(), admin.getEmail(), admin.getRoles());

        String refreshToken = this.jwtService.generateRefreshToken(admin.getId(), admin.getEmail(), admin.getRoles());

        AdminResDto adminResDto = this.adminMapper.toAdminResDto(admin);

        return new CommonAuthResDto<>(accessToken, refreshToken, adminResDto);
    }

    @Override
    public AdminResDto createAdmin(CreateAdminDto createAdminDto) throws ConflictException
    {
        Optional<Admin> existingAdmin = this.adminRepository.findByEmail(createAdminDto.getEmail());

        if (existingAdmin.isPresent())
        {
            throw new ConflictException("admin with this email already exist");
        }

        String hash = this.passwordEncoder.encode(createAdminDto.getPassword());

        Set<Role> roles = new HashSet<>(Set.of(Role.ROLE_ADMIN));

        Admin newAdmin = this.adminMapper.toAdmin(createAdminDto);
        newAdmin.setPassword(hash);
        newAdmin.setRoles(roles);

        Admin createdAdmin = this.adminRepository.save(newAdmin);

        return this.adminMapper.toAdminResDto(createdAdmin);
    }

    @Override
    public CommonAuthResDto<AdminResDto> refresh(TokenRefreshDto tokenRefreshDto) throws UnauthorizedException
    {
        String email = this.jwtService.extractEmail(tokenRefreshDto.getRefreshToken());

        if (email == null || email.isEmpty())
        {
            throw new UnauthorizedException("unauthorized");
        }

        Admin existingAdmin = this.adminRepository.findByEmail(email).orElseThrow(() -> new UnauthorizedException(
                "invalid admin"));

        String accessToken = this.jwtService.generateAccessToken(existingAdmin.getId(), existingAdmin.getEmail(),
                existingAdmin.getRoles());

        String refreshToken = this.jwtService.generateRefreshToken(existingAdmin.getId(), existingAdmin.getEmail(),
                existingAdmin.getRoles());

        return new CommonAuthResDto<>(accessToken, refreshToken, new AdminResDto(existingAdmin.getId(),
                existingAdmin.getName(), existingAdmin.getEmail(), existingAdmin.getRoles()));
    }
}
