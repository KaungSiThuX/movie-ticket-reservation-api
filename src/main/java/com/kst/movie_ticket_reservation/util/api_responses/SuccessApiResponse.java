package com.kst.movie_ticket_reservation.util.api_responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessApiResponse<T> extends CommonApiResponse
{
    private T data;

    public SuccessApiResponse(int status, String message, String code, T data)
    {
        super(status, message, code);
        this.data = data;
    }
}
