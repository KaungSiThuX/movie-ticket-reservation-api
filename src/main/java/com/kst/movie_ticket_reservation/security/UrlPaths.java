package com.kst.movie_ticket_reservation.security;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class UrlPaths
{
    private final String[] WHITE_LIST = {
            "/hello/**",
            "/auth/admin/sign-in",

            // user auth
            "/auth/user/google-sign-in", "/auth/user/request-otp", "/auth/user/request-otp-token", "/auth/user/verify" +
            "-otp",
            "/auth/user/register", "/auth/user/sign-in", "/auth/user/refresh",

            // payment webhook
            "/payment/webhook", "/checkouts/webhook", "/orders/webhook",

            "/auth/admin/refresh",
            "/public-api/**",
            "/sse/**",
            // Swagger UI
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/v3/api-docs",

            "/actuator/**",

            "/public-api/**",
            "/sse/**"
    };
}
