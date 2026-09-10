package com.kst.movie_ticket_reservation.util.handlers;


import com.kst.movie_ticket_reservation.util.api_responses.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class SuccessApiResponseHandler
{
    public <T> ResponseEntity<SuccessApiResponse<T>> response(int status, String message, String code,
                                                              T data)
    {
        SuccessApiResponse<T> successApiResponse = new SuccessApiResponse<>(status, message, code, data);

        return new ResponseEntity<>(successApiResponse, new HttpHeaders(),
                successApiResponse.getStatus());
    }

    public <T> ResponseEntity<OffsetPaginationResponse<T>> response(int status, String message, String code,
                                                                    OffsetPaginationApiMetaData offsetPaginationApiMetaData, T data)
    {
        OffsetPaginationResponse<T> offsetPaginationResponse = new OffsetPaginationResponse<>(status, message, code,
                offsetPaginationApiMetaData, data);

        return new ResponseEntity<>(offsetPaginationResponse, new HttpHeaders(),
                offsetPaginationResponse.getStatus());
    }

    public <T> ResponseEntity<CursorPaginationResponse<T>> response(int status, String message, String code,
                                                                    CursorPaginationMetaData cursorPaginationMetaData
            , T data)
    {
        CursorPaginationResponse<T> cursorPaginationResponse = new CursorPaginationResponse<>(status, message, code,
                cursorPaginationMetaData, data);

        return new ResponseEntity<>(cursorPaginationResponse, new HttpHeaders(),
                cursorPaginationResponse.getStatus());
    }
}
