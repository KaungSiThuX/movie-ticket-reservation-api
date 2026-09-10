package com.kst.movie_ticket_reservation.integration.stripe.config;

import com.stripe.StripeClient;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig
{
    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Bean
    StripeClient stripeClient()
    {
        return new StripeClient(this.stripeSecretKey);
    }

}
