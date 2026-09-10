package com.kst.movie_ticket_reservation.util.api_responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CursorPaginationResponse<T> extends CommonApiResponse
{
    private CursorPaginationMetaData meta;
    private T data;

    public CursorPaginationResponse(int status, String message, String code,
                                    CursorPaginationMetaData cursorPaginationMetaData, T data)
    {
        super(status, message, code);
        this.meta = cursorPaginationMetaData;
        this.data = data;
    }
}
