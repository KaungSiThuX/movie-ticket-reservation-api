package com.kst.movie_ticket_reservation.feat.public_api.controller;

import com.kst.movie_ticket_reservation.feat.public_api.dto.res.*;
import com.kst.movie_ticket_reservation.feat.public_api.service.PublicApiService;
import com.kst.movie_ticket_reservation.feat.show_seat.dto.res.ShowSeatResDto;
import com.kst.movie_ticket_reservation.util.api_responses.CursorPaginationResponse;
import com.kst.movie_ticket_reservation.util.api_responses.SuccessApiResponse;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;
import com.kst.movie_ticket_reservation.util.handlers.SuccessApiResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("public-api")
@RequiredArgsConstructor
public class PublicApiController
{
    private final PublicApiService publicApiService;
    private final SuccessApiResponseHandler successApiResponseHandler;

    @GetMapping("/movies")
    ResponseEntity<CursorPaginationResponse<List<PublicApiMovieResDto>>> findManyMoviesByCursor(@RequestParam(value =
            "cursor", required = false) Long cursor)
    {
        PublicApiMoviesCursorPaginationResDto publicApiMoviesCursorPaginationResDto =
                this.publicApiService.findManyMoviesByCursor(cursor, 10);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "FIND_MANY_MOVIES_PUBLIC_API_SUCCESS", publicApiMoviesCursorPaginationResDto.cursorPaginationMetaData(),
                publicApiMoviesCursorPaginationResDto.movieList());
    }

    @GetMapping("/movies/{movieId}")
    ResponseEntity<SuccessApiResponse<PublicApiMovieDetailsResDto>> findMovieDetailsById(@PathVariable Long movieId) throws NotFoundException
    {
        PublicApiMovieDetailsResDto publicApiMovieDetailsResDto =
                this.publicApiService.findMovieDetails(movieId);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "FIND_MOVIE_BY_ID_PUBLIC_API_SUCCESS", publicApiMovieDetailsResDto);
    }

//    @GetMapping("/show-seats/show-times/{showTimePublicId}")
//    ResponseEntity<SuccessApiResponse<List<ShowSeatResDto>>> findManyShowSeat(@PathVariable String
//    showTimePublicId) throws NotFoundException
//    {
//        List<ShowSeatResDto> showSeatResDtoList = this.publicApiService.findManyShowSeat(showTimePublicId);
//
//        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
//                "FIND_MANY_SHOW_SEAT_PUBLIC_API_SUCCESS", showSeatResDtoList);
//    }

    @GetMapping("/show-seats/show-times/{showTimePublicId}")
    ResponseEntity<SuccessApiResponse<List<PublicApiShowSeatResDto>>> findAllShowSeatsByShowTimePublicId(@PathVariable String showTimePublicId) throws NotFoundException
    {
        List<PublicApiShowSeatResDto> showSeatResDtoList =
                this.publicApiService.findAllShowSeatsByShowTimePublicId(showTimePublicId);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "FIND_ALL_SHOW_SEATS_PUBLIC_API_SUCCESS", showSeatResDtoList);
    }

}
