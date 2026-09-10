package com.kst.movie_ticket_reservation.util.api_responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OffsetPaginationApiMetaData
{
    private int offset;
    private Integer nextPage;        // if not need, remove
    private Integer previousPage;   // if not need remove
    private int totalPages;

    private boolean hasNextPage;
    private boolean hasPreviousPage;
    private int limit;
    private long totalItems;
    private int currentPage;

    public OffsetPaginationApiMetaData(boolean hasNextPage,
                                       boolean hasPreviousPage,
                                       int limit,
                                       long totalItems,
                                       int currentPage,
                                       int totalPages,
                                       Integer nextPage,
                                       Integer previousPage,
                                       int offset)
    {
        // super(hasNextPage, hasPreviousPage, limit, totalItems, currentPage);
        this.offset = offset;
        this.nextPage = nextPage;
        this.previousPage = previousPage;
        this.totalPages = totalPages;
        this.hasNextPage = hasNextPage;
        this.hasPreviousPage = hasPreviousPage;
        this.limit = limit;
        this.totalItems = totalItems;
        this.currentPage = currentPage;
    }
}
