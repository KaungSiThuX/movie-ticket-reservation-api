package com.kst.movie_ticket_reservation.auditing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class AuditorAwareConfig
{
    @Bean
    public AuditorAware<Long> auditorAware()
    {
        return new ApplicationAuditorAware();
    }
}
