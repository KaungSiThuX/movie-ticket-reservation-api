package com.kst.movie_ticket_reservation.feat.auth.user.dto.res;

import com.kst.movie_ticket_reservation.util.enums.Role;

import java.util.Set;

public record RegisterResDto(Long id, String email, Set<Role> roles)
{
}
