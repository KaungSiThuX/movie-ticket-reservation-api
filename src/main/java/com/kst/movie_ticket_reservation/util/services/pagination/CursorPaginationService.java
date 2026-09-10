package com.kst.movie_ticket_reservation.util.services.pagination;

import com.kst.movie_ticket_reservation.util.api_responses.CursorPaginationMetaData;
import org.springframework.stereotype.Component;

@Component
public class CursorPaginationService
{
    public CursorPaginationMetaData generateMetaData(boolean hasNextPage, Object nextCursor)
    {
        return new CursorPaginationMetaData(hasNextPage, nextCursor);
    }
}
