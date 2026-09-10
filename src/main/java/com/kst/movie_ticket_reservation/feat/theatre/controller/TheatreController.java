package com.kst.movie_ticket_reservation.feat.theatre.controller;

import com.kst.movie_ticket_reservation.feat.theatre.dto.req.CreateTheatreDto;
import com.kst.movie_ticket_reservation.feat.theatre.dto.req.UpdateTheatreDto;
import com.kst.movie_ticket_reservation.feat.theatre.dto.res.TheatreNameResDto;
import com.kst.movie_ticket_reservation.feat.theatre.dto.res.TheatreOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.theatre.dto.res.TheatreResDto;
import com.kst.movie_ticket_reservation.feat.theatre.service.TheatreService;
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
@RequestMapping("theatres")
@RequiredArgsConstructor
public class TheatreController
{
    private final TheatreService theatreService;
    private final SuccessApiResponseHandler successApiResponseHandler;

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @GetMapping
    ResponseEntity<OffsetPaginationResponse<List<TheatreResDto>>> findMany(@RequestParam(required = false,
            defaultValue = "0") int offset, @RequestParam(required = false, defaultValue = "10") int limit)
    {
        TheatreOffsetPaginationResDto theatreOffsetPaginationResDto = this.theatreService.findMany(offset, limit);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "FIND_MANY_THEATRE_SUCCESS", theatreOffsetPaginationResDto.offsetPaginationApiMetaData(),
                theatreOffsetPaginationResDto.theatreResDtoList());
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @GetMapping("all")
    ResponseEntity<SuccessApiResponse<List<TheatreNameResDto>>> findAll()
    {
        List<TheatreNameResDto> theatreNameResDtoList = this.theatreService.findAll();

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "FIND_ALL_THEATRE_SUCCESS", theatreNameResDtoList);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @GetMapping("{id}")
    ResponseEntity<SuccessApiResponse<TheatreResDto>> findById(@PathVariable Long id) throws NotFoundException
    {
        TheatreResDto theatreResDto = this.theatreService.findById(id);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(), "FIND_THEATRE_BY_ID_SUCCESS", theatreResDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PostMapping
    ResponseEntity<SuccessApiResponse<TheatreResDto>> create(@Valid @RequestBody CreateTheatreDto createTheatreDto) throws ConflictException
    {
        TheatreResDto theatreResDto = this.theatreService.create(createTheatreDto);

        return this.successApiResponseHandler.response(HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(), "CREATE_THEATRE_SUCCESS", theatreResDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PutMapping("{id}")
    ResponseEntity<SuccessApiResponse<TheatreResDto>> update(@PathVariable Long id,
                                                             @Valid @RequestBody UpdateTheatreDto updateTheatreDto)
            throws NotFoundException, ConflictException
    {
        TheatreResDto theatreResDto = this.theatreService.update(id, updateTheatreDto);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(), "UPDATE_THEATRE_SUCCESS", theatreResDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PatchMapping("{id}/hide")
    ResponseEntity<SuccessApiResponse<TheatreResDto>> hide(@PathVariable Long id) throws NotFoundException
    {
        TheatreResDto theatreResDto = this.theatreService.hide(id);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(), "HIDE_THEATRE_SUCCESS", theatreResDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PatchMapping("{id}/show")
    ResponseEntity<SuccessApiResponse<TheatreResDto>> show(@PathVariable Long id) throws NotFoundException
    {
        TheatreResDto theatreResDto = this.theatreService.show(id);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(), "SHOW_THEATRE_SUCCESS", theatreResDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @DeleteMapping("{id}")
    ResponseEntity<SuccessApiResponse<TheatreResDto>> delete(@PathVariable Long id) throws NotFoundException
    {
        TheatreResDto theatreResDto = this.theatreService.delete(id);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(), "DELETE_THEATRE_SUCCESS", theatreResDto);
    }
}