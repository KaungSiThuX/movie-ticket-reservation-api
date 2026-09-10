package com.kst.movie_ticket_reservation.util.services;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UtilService
{
    private final SecureRandom secureRandom = new SecureRandom(); // Thread-safe
    private final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();

    public String generateSecureToken(int byteLength)
    {
        byte[] randomBytes = new byte[byteLength];
        secureRandom.nextBytes(randomBytes);
        // Encode to a safe URL-friendly string format
        return base64Encoder.encodeToString(randomBytes);
    }
}
