package com.kst.movie_ticket_reservation.auditing;

import com.kst.movie_ticket_reservation.security.current.CurrentPerson;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class ApplicationAuditorAware implements AuditorAware<Long>
{

    @Override
    public Optional<Long> getCurrentAuditor()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal()))
        {
            return Optional.empty();
        }

        CurrentPerson currentPerson = (CurrentPerson) authentication.getPrincipal();

        assert currentPerson != null;
        return Optional.of(currentPerson.getId());
    }
}
