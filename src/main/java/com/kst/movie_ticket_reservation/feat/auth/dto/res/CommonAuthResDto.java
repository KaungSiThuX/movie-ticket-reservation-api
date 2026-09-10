package com.kst.movie_ticket_reservation.feat.auth.dto.res;


import com.fasterxml.jackson.annotation.JsonUnwrapped;

// need to update info Data to something specific for user, admin, etc...
public record CommonAuthResDto<T>(String accessToken, String refreshToken, T infoData)
{
}
