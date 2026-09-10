package com.kst.movie_ticket_reservation.feat.auth.user.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TemporaryStoreOtpPayload
{
    String otp;
    Boolean isVerified;
    Integer retryAttempt;
    // String token;
}
