package com.kst.movie_ticket_reservation.security.current;

import com.kst.movie_ticket_reservation.util.enums.Role;
import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public class CurrentPerson implements UserDetails
{
    @Getter
    private final Long id;

    @Getter
    private final String email;

    private final Set<Role> roles;

    public CurrentPerson(Long id, String email, Set<Role> roles)
    {
        this.id = id;
        this.email = email;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return this.roles;
    }

    @Override
    public @Nullable String getPassword()
    {
        return "";
    }

    @Override
    public String getUsername()
    {
        return "";
    }
}
