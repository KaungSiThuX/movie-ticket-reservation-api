package com.kst.movie_ticket_reservation.util.enums;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority
{
    ROLE_SUPER_ADMIN,
    ROLE_ADMIN,
    ROLE_USER;

    @Override
    public String getAuthority()
    {
        return this.name();
    }
}
