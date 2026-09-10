package com.kst.movie_ticket_reservation.feat.show_date.controller;

import com.kst.movie_ticket_reservation.feat.movie.dto.res.MovieOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.movie.dto.res.MovieResDto;
import com.kst.movie_ticket_reservation.feat.show_date.dto.req.CreateShowDateDto;
import com.kst.movie_ticket_reservation.feat.show_date.dto.req.UpdateShowDateDto;
import com.kst.movie_ticket_reservation.feat.show_date.dto.res.ShowDateOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.show_date.dto.res.ShowDateResDto;
import com.kst.movie_ticket_reservation.feat.show_date.service.ShowDateService;
import com.kst.movie_ticket_reservation.util.api_responses.OffsetPaginationResponse;
import com.kst.movie_ticket_reservation.util.api_responses.SuccessApiResponse;
import com.kst.movie_ticket_reservation.util.exceptions.ConflictException;
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
@RequestMapping("show-dates")
@RequiredArgsConstructor
public class ShowDateController
{
    private final ShowDateService showDateService;
    private final SuccessApiResponseHandler successApiResponseHandler;

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @GetMapping()
    ResponseEntity<OffsetPaginationResponse<List<ShowDateResDto>>> findManyByOffset(@RequestParam(name = "offset",
                                                                                            defaultValue
                                                                                                    = "0") int offset,
                                                                                    @RequestParam(name = "limit",
                                                                                            defaultValue =
                                                                                                    "10") int limit)
    {
        ShowDateOffsetPaginationResDto showDateOffsetPaginationResDto = this.showDateService.findManyByOffset(offset,
                limit);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "FIND_MANY_SHOW_DATE_BY_OFFSET_SUCCES", showDateOffsetPaginationResDto.offsetPaginationApiMetaData(),
                showDateOffsetPaginationResDto.showDateResDtoList());
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PostMapping
    ResponseEntity<SuccessApiResponse<ShowDateResDto>> create(@Valid @RequestBody CreateShowDateDto createShowDateDto) throws ConflictException, NotFoundException
    {
        ShowDateResDto showDateResDto = this.showDateService.create(createShowDateDto);

        return this.successApiResponseHandler.response(HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(), "CREATE_SHOW_DATE_SUCCESS", showDateResDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PutMapping("{id}")
    ResponseEntity<SuccessApiResponse<ShowDateResDto>> update(@PathVariable Long id,
                                                              @Valid @RequestBody UpdateShowDateDto
                                                                      updateShowDateDto) throws NotFoundException,
            ConflictException
    {
        ShowDateResDto showDateResDto = this.showDateService.update(id, updateShowDateDto);

        return this.successApiResponseHandler.response(HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(), "UPDATE_SHOW_DATE_SUCCESS", showDateResDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @DeleteMapping("{id}")
    ResponseEntity<SuccessApiResponse<ShowDateResDto>> update(@PathVariable Long id) throws NotFoundException
    {
        ShowDateResDto showDateResDto = this.showDateService.delete(id);

        return this.successApiResponseHandler.response(HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(), "DELETE_SHOW_DATE_SUCCESS", showDateResDto);
    }
}
