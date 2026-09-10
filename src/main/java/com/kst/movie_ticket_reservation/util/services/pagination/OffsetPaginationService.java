package com.kst.movie_ticket_reservation.util.services.pagination;

import com.kst.movie_ticket_reservation.util.api_responses.OffsetPaginationApiMetaData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class OffsetPaginationService
{
    public OffsetPaginationApiMetaData generateMetaData(Page<?> page)
    {
        // Spring Data page numbers are 0-indexed
        int currentPage = page.getNumber() + 1;

        Integer previousPage = page.hasPrevious() ? currentPage - 1 : null;
        Integer nextPage = page.hasNext() ? currentPage + 1 : null;

        return new OffsetPaginationApiMetaData(
                page.hasNext(),
                page.hasPrevious(),
                page.getSize(),
                page.getTotalElements(),
                currentPage,
                page.getTotalPages(),
                nextPage,
                previousPage,
                (int) page.getPageable().getOffset()
        );
    }


    public Pageable calculatePageable(int offset, int limit)
    {
        return PageRequest.of(offset / limit, limit, Sort.Direction.DESC, "id");
    }

}
