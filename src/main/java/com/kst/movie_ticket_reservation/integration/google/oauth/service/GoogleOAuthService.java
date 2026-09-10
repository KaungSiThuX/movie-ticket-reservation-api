package com.kst.movie_ticket_reservation.integration.google.oauth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoogleOAuthService
{
    private final GoogleIdTokenVerifier googleIdTokenVerifier;
    
    public Map<String, Object> verifyIdToken(String idToken) throws Exception
    {
        Map<String, Object> userDetails = new HashMap<>();
        try
        {
            GoogleIdToken googleIdToken = this.googleIdTokenVerifier.verify(idToken);

            if (googleIdToken != null)
            {
                GoogleIdToken.Payload payload = googleIdToken.getPayload();

                userDetails.put("googleId", payload.getSubject());
                userDetails.put("googleEmail", payload.getEmail());
                userDetails.put("name", payload.getNonce());
            }
            else
            {
                userDetails.put("googleId", null);
                userDetails.put("googleEmail", null);
                userDetails.put("name", null);
            }
            return userDetails;
        }
        catch (Exception e)
        {
            throw new Exception(e.getLocalizedMessage());
        }
    }
}
