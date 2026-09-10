package com.kst.movie_ticket_reservation.feat.auth.admin.dto.res;

import com.kst.movie_ticket_reservation.util.enums.Role;

import java.util.Set;

public record AdminResDto(Long id, String name, String email, Set<Role> roles)
{
}
