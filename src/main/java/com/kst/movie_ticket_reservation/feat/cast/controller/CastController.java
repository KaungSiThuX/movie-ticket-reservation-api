package com.kst.movie_ticket_reservation.feat.cast.controller;

import com.kst.movie_ticket_reservation.feat.cast.dto.req.CreateCastDto;
import com.kst.movie_ticket_reservation.feat.cast.dto.req.UpdateCastDto;
import com.kst.movie_ticket_reservation.feat.cast.dto.res.CastOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.cast.dto.res.CastResDto;
import com.kst.movie_ticket_reservation.feat.cast.service.CastService;
import com.kst.movie_ticket_reservation.feat.director.dto.req.CreateDirectorDto;
import com.kst.movie_ticket_reservation.feat.director.dto.req.UpdateDirectorDto;
import com.kst.movie_ticket_reservation.feat.director.dto.res.DirectorOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.director.dto.res.DirectorResDto;
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
@RequestMapping("casts")
@RequiredArgsConstructor
public class CastController
{
    private final CastService castService;
    private final SuccessApiResponseHandler successApiResponseHandler;

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @GetMapping
    ResponseEntity<OffsetPaginationResponse<List<CastResDto>>> findMany(@RequestParam(required = false,
            defaultValue = "0") int offset, @RequestParam(required = false, defaultValue = "10") int limit)
    {
        CastOffsetPaginationResDto castOffsetPaginationResDto = this.castService.findMany(offset, limit);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "FIND_MANY_CAST_SUCCESS", castOffsetPaginationResDto.offsetPaginationApiMetaData(),
                castOffsetPaginationResDto.castResDtoList());
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @GetMapping("/all")
    ResponseEntity<SuccessApiResponse<List<CastResDto>>> findAll()
    {
        List<CastResDto> castResDtoList = this.castService.findAll();

        return this.successApiResponseHandler.response(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "FIND_ALL_CAST_SUCCESS", castResDtoList);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PostMapping
    ResponseEntity<SuccessApiResponse<CastResDto>> create(@Valid @RequestBody CreateCastDto createCastDto) throws ConflictException
    {
        CastResDto castResDto = this.castService.create(createCastDto);

        return this.successApiResponseHandler.response(HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(), "CREATE_CAST_SUCCESS", castResDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PutMapping("{id}")
    ResponseEntity<SuccessApiResponse<CastResDto>> update(@PathVariable Long id,
                                                          @Valid @RequestBody UpdateCastDto updateCastDto)
            throws NotFoundException, ConflictException
    {
        CastResDto castResDto = this.castService.update(id, updateCastDto);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(), "UPDATE_CAST_SUCCESS", castResDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @DeleteMapping("{id}")
    ResponseEntity<SuccessApiResponse<CastResDto>> delete(@PathVariable Long id) throws NotFoundException
    {
        CastResDto castResDto = this.castService.delete(id);

        return this.successApiResponseHandler.response(HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(), "DELETE_CAST_SUCCESS", castResDto);
    }
}
