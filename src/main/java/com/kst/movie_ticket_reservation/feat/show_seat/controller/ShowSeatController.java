package com.kst.movie_ticket_reservation.feat.show_seat.controller;

import com.kst.movie_ticket_reservation.feat.show_seat.dto.req.CreateShowSeatDto;
import com.kst.movie_ticket_reservation.feat.show_seat.dto.res.ShowSeatOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.show_seat.dto.res.ShowSeatResDto;
import com.kst.movie_ticket_reservation.feat.show_seat.service.ShowSeatService;
import com.kst.movie_ticket_reservation.feat.show_time.dto.res.ShowTimeOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.show_time.dto.res.ShowTimeResDto;
import com.kst.movie_ticket_reservation.util.api_responses.OffsetPaginationResponse;
import com.kst.movie_ticket_reservation.util.api_responses.SuccessApiResponse;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;
import com.kst.movie_ticket_reservation.util.handlers.SuccessApiResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("show-seats")
@RequiredArgsConstructor
public class ShowSeatController
{
    private final ShowSeatService showSeatService;
    private final SuccessApiResponseHandler successApiResponseHandler;

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @GetMapping()
    ResponseEntity<OffsetPaginationResponse<List<ShowSeatResDto>>> findManyByOffset(@RequestParam(name = "offset",
                                                                                            defaultValue
                                                                                                    = "0") int offset,
                                                                                    @RequestParam(name = "limit",
                                                                                            defaultValue =
                                                                                                    "10") int limit)
    {
        ShowSeatOffsetPaginationResDto showSeatOffsetPaginationResDto =
                this.showSeatService.findManyByOffset(offset, limit);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "FIND_MANY_SHOW_SEAT_BY_OFFSET_SUCCES", showSeatOffsetPaginationResDto.offsetPaginationApiMetaData(),
                showSeatOffsetPaginationResDto.seatResDtoList());
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @GetMapping("/show-times/{showTimeId}")
    ResponseEntity<OffsetPaginationResponse<List<ShowSeatResDto>>> findManyByShowTimeIdOffset(@PathVariable Long showTimeId, @RequestParam(name = "offset",
                                                                                                      defaultValue
                                                                                                              = "0") int offset,
                                                                                              @RequestParam(name =
                                                                                                      "limit",
                                                                                                      defaultValue =
                                                                                                              "10") int limit)
    {
        ShowSeatOffsetPaginationResDto showSeatOffsetPaginationResDto =
                this.showSeatService.findManyByShowTimeIdOffset(showTimeId, offset, limit);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "FIND_MANY_SHOW_SEAT_BY_SHOW_TIME_ID_OFFSET_SUCCES",
                showSeatOffsetPaginationResDto.offsetPaginationApiMetaData(),
                showSeatOffsetPaginationResDto.seatResDtoList());
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PostMapping
    ResponseEntity<SuccessApiResponse<List<ShowSeatResDto>>> create(@Valid @RequestBody CreateShowSeatDto createShowSeatDto) throws NotFoundException
    {
        List<ShowSeatResDto> showSeatResDtoList = this.showSeatService.create(createShowSeatDto);

        return this.successApiResponseHandler.response(HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(), "CREATE_SHOW_TIME_SEAT_SUCCESS", showSeatResDtoList);
    }
}
