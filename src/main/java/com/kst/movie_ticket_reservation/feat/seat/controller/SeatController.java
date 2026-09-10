package com.kst.movie_ticket_reservation.feat.seat.controller;

import com.kst.movie_ticket_reservation.feat.seat.dto.req.CreateSeatDto;
import com.kst.movie_ticket_reservation.feat.seat.dto.req.UpdateSeatDto;
import com.kst.movie_ticket_reservation.feat.seat.dto.res.SeatOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.seat.dto.res.SeatResDto;
import com.kst.movie_ticket_reservation.feat.seat.service.SeatService;
import com.kst.movie_ticket_reservation.util.api_responses.OffsetPaginationResponse;
import com.kst.movie_ticket_reservation.util.api_responses.SuccessApiResponse;
import com.kst.movie_ticket_reservation.util.exceptions.ConflictException;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;
import com.kst.movie_ticket_reservation.util.handlers.SuccessApiResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("seats")
@RequiredArgsConstructor
public class SeatController
{
    private final SeatService seatService;
    private final SuccessApiResponseHandler successApiResponseHandler;

    @GetMapping
    ResponseEntity<OffsetPaginationResponse<List<SeatResDto>>> findMany(@RequestParam(required = false,
            defaultValue = "0") int offset, @RequestParam(required = false, defaultValue = "10") int limit)
    {
        SeatOffsetPaginationResDto seatOffsetPaginationResDto = this.seatService.findMany(offset, limit);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "FIND_MANY_SEAT_SUCCESS", seatOffsetPaginationResDto.offsetPaginationApiMetaData(),
                seatOffsetPaginationResDto.seatResDtoList());
    }

    @GetMapping("/theatres/{theatreId}")
    ResponseEntity<OffsetPaginationResponse<List<SeatResDto>>> findManyByTheatreId(@PathVariable Long theatreId,
                                                                                   @RequestParam(required = false,
                                                                                           defaultValue = "0") int offset,
                                                                                   @RequestParam(required = false,
                                                                                           defaultValue = "10") int limit)
    {
        SeatOffsetPaginationResDto seatOffsetPaginationResDto = this.seatService.findManyByTheatreId(theatreId,
                offset, limit);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "FIND_MANY_SEAT_SUCCESS", seatOffsetPaginationResDto.offsetPaginationApiMetaData(),
                seatOffsetPaginationResDto.seatResDtoList());
    }

    @PostMapping("/theatres/{theatreId}")
    ResponseEntity<SuccessApiResponse<SeatResDto>> create(@PathVariable Long theatreId,
                                                          @Valid @RequestBody CreateSeatDto createSeatDto) throws ConflictException, NotFoundException
    {
        SeatResDto seatResDto = this.seatService.create(theatreId, createSeatDto);

        return this.successApiResponseHandler.response(HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(), "SEAT_CREATE_SUCCESS", seatResDto);
    }

    @PutMapping("{id}")
    ResponseEntity<SuccessApiResponse<SeatResDto>> update(@PathVariable Long id,
                                                          @Valid @RequestBody UpdateSeatDto updateSeatDto) throws
            NotFoundException
    {
        SeatResDto seatResDto = this.seatService.update(id, updateSeatDto);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(), "SEAT_UPDATE_SUCCESS", seatResDto);
    }

    @PatchMapping("/{id}/hide")
    ResponseEntity<SuccessApiResponse<SeatResDto>> hide(@PathVariable Long id) throws
            NotFoundException
    {
        SeatResDto seatResDto = this.seatService.hide(id);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(), "SEAT_HIDE_SUCCESS", seatResDto);
    }

    @PatchMapping("/{id}/show")
    ResponseEntity<SuccessApiResponse<SeatResDto>> show(@PathVariable Long id) throws
            NotFoundException
    {
        SeatResDto seatResDto = this.seatService.show(id);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(), "SEAT_SHOW_SUCCESS", seatResDto);
    }

    @DeleteMapping("{id}")
    ResponseEntity<SuccessApiResponse<SeatResDto>> delete(@PathVariable Long id) throws NotFoundException
    {
        SeatResDto seatResDto = this.seatService.delete(id);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(), "SEAT_DELETE_SUCCESS", seatResDto);
    }
}
