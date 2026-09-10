package com.kst.movie_ticket_reservation.feat.auth.user.dto.res;

import com.kst.movie_ticket_reservation.util.enums.Role;

import java.util.Set;

public record GoogleSignInResDto(Long id, String name, String email, String googleEmail, Set<Role> roles)
{
}
