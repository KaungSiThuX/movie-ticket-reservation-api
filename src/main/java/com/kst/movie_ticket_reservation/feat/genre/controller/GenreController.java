package com.kst.movie_ticket_reservation.feat.genre.controller;

import com.kst.movie_ticket_reservation.feat.genre.dto.req.CreateGenreDto;
import com.kst.movie_ticket_reservation.feat.genre.dto.req.UpdateGenreDto;
import com.kst.movie_ticket_reservation.feat.genre.dto.res.GenreOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.genre.dto.res.GenreResDto;
import com.kst.movie_ticket_reservation.feat.genre.service.GenreService;
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
@RequestMapping("genres")
@RequiredArgsConstructor
public class GenreController
{
    private final GenreService genreService;
    private final SuccessApiResponseHandler successApiResponseHandler;

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @GetMapping
    ResponseEntity<OffsetPaginationResponse<List<GenreResDto>>> findMany(@RequestParam(required = false,
            defaultValue = "0") int offset, @RequestParam(required = false, defaultValue = "20") int limit)
    {
        GenreOffsetPaginationResDto genreOffsetPaginationResDto = this.genreService.findMany(offset, limit);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "FIND_MANY_GENRE_SUCCESS", genreOffsetPaginationResDto.offsetPaginationApiMetaData(),
                genreOffsetPaginationResDto.genreResDtoList());
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @GetMapping("/all")
    ResponseEntity<SuccessApiResponse<List<GenreResDto>>> findAll()
    {
        List<GenreResDto> genreResDtoList = this.genreService.findAll();

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "FIND_ALL_GENRE_SUCCESS", genreResDtoList);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @GetMapping("{id}")
    ResponseEntity<SuccessApiResponse<GenreResDto>> findById(@PathVariable Long id) throws NotFoundException
    {
        GenreResDto genreResDto = this.genreService.findById(id);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(), "FIND_GENRE_BY_ID_SUCCESS", genreResDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PostMapping
    ResponseEntity<SuccessApiResponse<GenreResDto>> create(@Valid @RequestBody CreateGenreDto createGenreDto) throws ConflictException
    {
        GenreResDto genreResDto = this.genreService.create(createGenreDto);

        return this.successApiResponseHandler.response(HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(), "CREATE_GENRE_SUCCESS", genreResDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PutMapping("{id}")
    ResponseEntity<SuccessApiResponse<GenreResDto>> update(@PathVariable Long id,
                                                           @Valid @RequestBody UpdateGenreDto updateGenreDto)
            throws NotFoundException, ConflictException
    {
        GenreResDto genreResDto = this.genreService.update(id, updateGenreDto);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(), "UPDATE_GENRE_SUCCESS", genreResDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PatchMapping("{id}/hide")
    ResponseEntity<SuccessApiResponse<GenreResDto>> hide(@PathVariable Long id) throws NotFoundException
    {
        GenreResDto genreResDto = this.genreService.hide(id);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(), "SOFT_DELETE_GENRE_SUCCESS", genreResDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PatchMapping("{id}/show")
    ResponseEntity<SuccessApiResponse<GenreResDto>> show(@PathVariable Long id) throws NotFoundException
    {
        GenreResDto genreResDto = this.genreService.show(id);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(), "RESTORE_GENRE_SUCCESS", genreResDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @DeleteMapping("{id}")
    ResponseEntity<SuccessApiResponse<GenreResDto>> delete(@PathVariable Long id) throws NotFoundException
    {
        GenreResDto genreResDto = this.genreService.delete(id);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(), "DELETE_GENRE_SUCCESS", genreResDto);
    }
}
