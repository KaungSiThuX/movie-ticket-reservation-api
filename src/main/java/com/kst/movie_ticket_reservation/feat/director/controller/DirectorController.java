package com.kst.movie_ticket_reservation.feat.director.controller;

import com.kst.movie_ticket_reservation.feat.director.dto.req.CreateDirectorDto;
import com.kst.movie_ticket_reservation.feat.director.dto.req.UpdateDirectorDto;
import com.kst.movie_ticket_reservation.feat.director.dto.res.DirectorOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.director.dto.res.DirectorResDto;
import com.kst.movie_ticket_reservation.feat.director.service.DirectorService;
import com.kst.movie_ticket_reservation.feat.genre.dto.req.CreateGenreDto;
import com.kst.movie_ticket_reservation.feat.genre.dto.req.UpdateGenreDto;
import com.kst.movie_ticket_reservation.feat.genre.dto.res.GenreOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.genre.dto.res.GenreResDto;
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
@RequestMapping("directors")
@RequiredArgsConstructor
public class DirectorController
{
    private final DirectorService directorService;
    private final SuccessApiResponseHandler successApiResponseHandler;

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @GetMapping
    ResponseEntity<OffsetPaginationResponse<List<DirectorResDto>>> findMany(@RequestParam(required = false,
            defaultValue = "0") int offset, @RequestParam(required = false, defaultValue = "10") int limit)
    {
        DirectorOffsetPaginationResDto directorOffsetPaginationResDto = this.directorService.findMany(offset, limit);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "FIND_MANY_DIRECTOR_SUCCESS", directorOffsetPaginationResDto.offsetPaginationApiMetaData(),
                directorOffsetPaginationResDto.directorResDtoList());
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @GetMapping("/all")
    ResponseEntity<SuccessApiResponse<List<DirectorResDto>>> findAll()
    {
        List<DirectorResDto> directorResDtoList = this.directorService.findAll();

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "FIND_ALL_DIRECTOR_SUCCESS", directorResDtoList);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PostMapping
    ResponseEntity<SuccessApiResponse<DirectorResDto>> create(@Valid @RequestBody CreateDirectorDto createDirectorDto) throws ConflictException
    {
        DirectorResDto directorResDto = this.directorService.create(createDirectorDto);

        return this.successApiResponseHandler.response(HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(), "CREATE_DIRECTOR_SUCCESS", directorResDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PutMapping("{id}")
    ResponseEntity<SuccessApiResponse<DirectorResDto>> update(@PathVariable Long id,
                                                              @Valid @RequestBody UpdateDirectorDto updateDirectorDto)
            throws NotFoundException, ConflictException
    {
        DirectorResDto directorResDto = this.directorService.update(id, updateDirectorDto);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(), "UPDATE_DIRECTOR_SUCCESS", directorResDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @DeleteMapping("{id}")
    ResponseEntity<SuccessApiResponse<DirectorResDto>> delete(@PathVariable Long id) throws NotFoundException
    {
        DirectorResDto directorResDto = this.directorService.delete(id);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(), "DELETE_DIRECTOR_SUCCESS", directorResDto);
    }
}
