package com.kst.movie_ticket_reservation.feat.movie.controller;

import com.kst.movie_ticket_reservation.feat.movie.dto.req.CreateMovieDto;
import com.kst.movie_ticket_reservation.feat.movie.dto.req.UpdateMovieFormDto;
import com.kst.movie_ticket_reservation.feat.movie.dto.req.UpdateMovieJsonDto;
import com.kst.movie_ticket_reservation.feat.movie.dto.res.MovieOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.movie.dto.res.MovieResDto;
import com.kst.movie_ticket_reservation.feat.movie.dto.res.MovieTitleResDto;
import com.kst.movie_ticket_reservation.feat.movie.service.MovieService;
import com.kst.movie_ticket_reservation.util.api_responses.OffsetPaginationResponse;
import com.kst.movie_ticket_reservation.util.api_responses.SuccessApiResponse;
import com.kst.movie_ticket_reservation.util.exceptions.ConflictException;
import com.kst.movie_ticket_reservation.util.exceptions.CustomS3Exception;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;
import com.kst.movie_ticket_reservation.util.handlers.SuccessApiResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("movies")
@RequiredArgsConstructor
public class MovieController
{
    private final MovieService movieService;
    private final SuccessApiResponseHandler successApiResponseHandler;

    @GetMapping()
    ResponseEntity<OffsetPaginationResponse<List<MovieResDto>>> findManyByOffset(@RequestParam(name = "offset",
                                                                                         defaultValue
                                                                                                 = "0") int offset,
                                                                                 @RequestParam(name = "limit",
                                                                                         defaultValue =
                                                                                                 "10") int limit)
    {
        MovieOffsetPaginationResDto movieOffsetPaginationResDto = this.movieService.findMany(offset, limit);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "FIND_MANY_MOVIE_BY_OFFSET_SUCCES", movieOffsetPaginationResDto.offsetPaginationApiMetaData(),
                movieOffsetPaginationResDto.movieResDtoList());
    }

    @GetMapping("all")
    ResponseEntity<SuccessApiResponse<List<MovieTitleResDto>>> findAll()
    {
        List<MovieTitleResDto> movieTitleResDtoList = this.movieService.findAll();

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "FIND_ALL_MOVIE_BY_TITLE_SUCCES", movieTitleResDtoList);
    }

    @GetMapping("{id}")
    ResponseEntity<SuccessApiResponse<MovieResDto>> findById(@PathVariable Long id) throws NotFoundException
    {
        MovieResDto movieResDto = this.movieService.findById(id);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(), "FIND_MOVIE_BY_ID_SUCCESS", movieResDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ResponseEntity<SuccessApiResponse<MovieResDto>> createForm(@Valid @ModelAttribute CreateMovieDto
                                                                       createMovieDto,
                                                               @RequestParam("poster") MultipartFile poster)
            throws ConflictException, NotFoundException, CustomS3Exception
    {
        MovieResDto movieResDto = this.movieService.createForm(createMovieDto, poster);

        return this.successApiResponseHandler.response(HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(), "CREATE_MOVIE_SUCCESS", movieResDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<SuccessApiResponse<MovieResDto>> createJson(@Valid @RequestBody CreateMovieDto createMovieDto) throws ConflictException, NotFoundException
    {
        MovieResDto movieResDto = this.movieService.createJson(createMovieDto);

        return this.successApiResponseHandler.response(HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(), "CREATE_MOVIE_SUCCESS", movieResDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PutMapping(value = "{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<SuccessApiResponse<MovieResDto>> updateJson(@PathVariable Long id,
                                                               @Valid @RequestBody UpdateMovieJsonDto updateMovieJsonDto)
            throws NotFoundException
    {
        MovieResDto movieResDto = this.movieService.updateJson(id, updateMovieJsonDto);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "UPDATE_MOIVE_SUCCESS", movieResDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PutMapping(value = "{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ResponseEntity<SuccessApiResponse<MovieResDto>> updateForm(@PathVariable Long id,
                                                               @Valid @ModelAttribute UpdateMovieFormDto updateMovieFormDto,
                                                               @RequestParam(value = "poster") MultipartFile poster) throws
            NotFoundException, CustomS3Exception
    {
        MovieResDto movieResDto = this.movieService.updateForm(id, updateMovieFormDto, poster);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "UPDATE_MOIVE_SUCCESS", movieResDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PatchMapping("/{id}/hide")
    ResponseEntity<SuccessApiResponse<MovieResDto>> hide(@PathVariable Long id) throws NotFoundException
    {
        MovieResDto movieResDto = this.movieService.hide(id);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "HIDE_MOVIE_SUCCESS", movieResDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PatchMapping("/{id}/show")
    ResponseEntity<SuccessApiResponse<MovieResDto>> show(@PathVariable Long id) throws NotFoundException
    {
        MovieResDto movieResDto = this.movieService.show(id);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "SHOW_MOVIE_SUCCESS", movieResDto);
    }

    @DeleteMapping("{id}")
    ResponseEntity<SuccessApiResponse<MovieResDto>> delete(@PathVariable Long id) throws NotFoundException
    {
        MovieResDto movieResDto = this.movieService.delete(id);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "DELETE_MOIVE_SUCCESS", movieResDto);
    }
}
