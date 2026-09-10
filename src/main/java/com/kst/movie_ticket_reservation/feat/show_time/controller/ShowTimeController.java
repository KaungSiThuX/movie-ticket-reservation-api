package com.kst.movie_ticket_reservation.feat.show_time.controller;

import com.kst.movie_ticket_reservation.feat.movie.dto.res.MovieOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.movie.dto.res.MovieResDto;
import com.kst.movie_ticket_reservation.feat.show_time.dto.req.CreateShowTimeDto;
import com.kst.movie_ticket_reservation.feat.show_time.dto.req.UpdateShowTimeDto;
import com.kst.movie_ticket_reservation.feat.show_time.dto.res.ShowTimeOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.show_time.dto.res.ShowTimeResDto;
import com.kst.movie_ticket_reservation.feat.show_time.service.ShowTimeService;
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
@RequestMapping("show-times")
@RequiredArgsConstructor
public class ShowTimeController
{
    private final ShowTimeService showTimeService;
    private final SuccessApiResponseHandler successApiResponseHandler;

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @GetMapping("/show-dates/{showDateId}")
    ResponseEntity<OffsetPaginationResponse<List<ShowTimeResDto>>> findManyByOffset(@PathVariable Long showDateId,
                                                                                    @RequestParam(name = "offset",
                                                                                            defaultValue
                                                                                                    = "0") int offset,
                                                                                    @RequestParam(name = "limit",
                                                                                            defaultValue =
                                                                                                    "10") int limit)
    {
        ShowTimeOffsetPaginationResDto showTimeOffsetPaginationResDto =
                this.showTimeService.findManyByOffset(showDateId, offset, limit);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "FIND_MANY_SHOW_TIME_BY_OFFSET_SUCCES", showTimeOffsetPaginationResDto.offsetPaginationApiMetaData(),
                showTimeOffsetPaginationResDto.showTimeResDtoList());
    }

    @PostMapping
    ResponseEntity<SuccessApiResponse<List<ShowTimeResDto>>> create(
            @Valid @RequestBody CreateShowTimeDto createShowTimeDto) throws NotFoundException
    {
        List<ShowTimeResDto> showTimeResDtoList = this.showTimeService.create(createShowTimeDto);

        return this.successApiResponseHandler.response(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(),
                "CREATE_SHOW_TIME_SUCCESS", showTimeResDtoList);
    }

    @PutMapping("{id}")
    ResponseEntity<SuccessApiResponse<ShowTimeResDto>> update(@PathVariable Long id,
                                                              @Valid @RequestBody UpdateShowTimeDto updateShowTimeDto) throws NotFoundException
    {
        ShowTimeResDto showTimeResDto = this.showTimeService.update(id, updateShowTimeDto);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "UPDATE_SHOW_TIME_SUCCESS", showTimeResDto);
    }

    @DeleteMapping("{id}")
    ResponseEntity<SuccessApiResponse<ShowTimeResDto>> delete(@PathVariable Long id) throws NotFoundException
    {
        ShowTimeResDto showTimeResDto = this.showTimeService.delete(id);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "DELETE_SHOW_TIME_SUCCESS", showTimeResDto);
    }
}
