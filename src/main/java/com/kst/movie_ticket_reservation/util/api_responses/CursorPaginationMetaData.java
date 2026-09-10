package com.kst.movie_ticket_reservation.util.api_responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CursorPaginationMetaData
{
    private Object nextCursor;
    // private long totalItems;
    private boolean hasNextPage;
    //  private int limit;

    public CursorPaginationMetaData(boolean hasNextPage,
                                    //  int limit,
                                    //long totalItems,
                                    Object nextCursor)
    {
        //  super(hasNextPage, hasPreviousPage, limit, totalItems, currentPage);
        this.nextCursor = nextCursor;
        this.hasNextPage = hasNextPage;
        //  this.totalItems = totalItems;
        //   this.limit = limit;
    }
}
