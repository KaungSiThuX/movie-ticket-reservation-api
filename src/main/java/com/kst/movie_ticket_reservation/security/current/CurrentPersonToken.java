package com.kst.movie_ticket_reservation.security.current;

import com.kst.movie_ticket_reservation.util.enums.Role;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CurrentPersonToken extends AbstractAuthenticationToken
{
    private final Object principal;

    public CurrentPersonToken(CurrentPerson currentPerson, Collection<? extends GrantedAuthority> authorities)
    {
        super(authorities);
        this.principal = currentPerson;
        super.setAuthenticated(true);
    }

    @Override
    public @Nullable Object getCredentials()
    {
        return null;
    }

    @Override
    public @Nullable Object getPrincipal()
    {
        return this.principal;
    }
}
