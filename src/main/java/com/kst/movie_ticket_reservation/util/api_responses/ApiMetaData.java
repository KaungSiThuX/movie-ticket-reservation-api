package com.kst.movie_ticket_reservation.util.api_responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiMetaData
{
    private boolean hasNextPage;
    private boolean hasPreviousPage;
    private int limit;
    private long totalItems;
    private int currentPage;
}
