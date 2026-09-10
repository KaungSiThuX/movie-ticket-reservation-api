package com.kst.movie_ticket_reservation.util.api_responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OffsetPaginationResponse<T> extends CommonApiResponse
{
    private OffsetPaginationApiMetaData meta;
    private T data;

    public OffsetPaginationResponse(int status, String message, String code,
                                    OffsetPaginationApiMetaData offsetPaginationApiMetaData,
                                    T data)
    {
        super(status, message, code);
        this.meta = offsetPaginationApiMetaData;
        this.data = data;
    }

}
