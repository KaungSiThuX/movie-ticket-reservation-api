package com.kst.movie_ticket_reservation.feat.theatre.service.impl;

import com.kst.movie_ticket_reservation.feat.theatre.dto.req.CreateTheatreDto;
import com.kst.movie_ticket_reservation.feat.theatre.dto.req.UpdateTheatreDto;
import com.kst.movie_ticket_reservation.feat.theatre.dto.res.TheatreNameResDto;
import com.kst.movie_ticket_reservation.feat.theatre.dto.res.TheatreOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.theatre.dto.res.TheatreResDto;
import com.kst.movie_ticket_reservation.feat.theatre.entity.Theatre;
import com.kst.movie_ticket_reservation.feat.theatre.mapper.TheatreMapper;
import com.kst.movie_ticket_reservation.feat.theatre.repository.TheatreRepository;
import com.kst.movie_ticket_reservation.feat.theatre.service.TheatreService;
import com.kst.movie_ticket_reservation.util.api_responses.OffsetPaginationApiMetaData;
import com.kst.movie_ticket_reservation.util.exceptions.ConflictException;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;
import com.kst.movie_ticket_reservation.util.services.pagination.OffsetPaginationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TheatreServiceImpl implements TheatreService
{
    private final TheatreRepository theatreRepository;
    private final TheatreMapper theatreMapper;
    private final OffsetPaginationService offsetPaginationService;

    @Override
    @Transactional
    public TheatreResDto create(CreateTheatreDto createTheatreDto) throws ConflictException
    {
        this.validateExistingTheatreByTitleOrSlug(createTheatreDto.getName(), createTheatreDto.getSlug());

        Theatre createdTheatre = this.theatreRepository.save(this.theatreMapper.toTheatre(createTheatreDto));

        return this.theatreMapper.toTheatreResDto(createdTheatre);
    }

    @Override
    @Transactional
    public TheatreResDto update(Long id, UpdateTheatreDto updateTheatreDto) throws NotFoundException, ConflictException
    {
        Theatre existingTheatre = this.findExistingTheatreById(id);

        this.theatreMapper.updateTheatreFromDto(updateTheatreDto, existingTheatre);

        Theatre updatedTheatre = this.theatreRepository.save(existingTheatre);

        return this.theatreMapper.toTheatreResDto(updatedTheatre);
    }

    private void validateExistingTheatreByTitleOrSlug(String name, String slug) throws ConflictException
    {
        if (this.theatreRepository.existsByNameIgnoreCaseOrSlugIgnoreCase(name, slug))
        {
            throw new ConflictException("movie with that name or slug already exist");
        }
    }

    @Override
    @Transactional
    public TheatreResDto hide(Long id) throws NotFoundException
    {
        Theatre existingTheatre = this.findExistingTheatreById(id);

        existingTheatre.hide();

        return this.theatreMapper.toTheatreResDto(existingTheatre);
    }

    @Override
    @Transactional
    public TheatreResDto show(Long id) throws NotFoundException
    {
        Theatre existingTheatre =
                this.theatreRepository.findByIdAndIsDeletedTrue(id).orElseThrow(() -> new NotFoundException("theatre " +
                        "not found"));

        existingTheatre.show();

        return this.theatreMapper.toTheatreResDto(existingTheatre);
    }

    @Override
    @Transactional
    public TheatreResDto delete(Long id) throws NotFoundException
    {
        Theatre existingTheatre = this.findExistingTheatreById(id);

        this.theatreRepository.deleteById(id);

        return this.theatreMapper.toTheatreResDto(existingTheatre);
    }


    @Override
    public TheatreOffsetPaginationResDto findMany(int offset, int limit)
    {
        Page<Theatre> theatrePage =
                this.theatreRepository.findByIsDeletedFalse(this.offsetPaginationService.calculatePageable(offset,
                        limit));

        List<TheatreResDto> theatreResDtoList =
                theatrePage.getContent().stream().map(this.theatreMapper::toTheatreResDto).toList();

        OffsetPaginationApiMetaData offsetPaginationApiMetaData = this.offsetPaginationService
                .generateMetaData(theatrePage);

        return new TheatreOffsetPaginationResDto(theatreResDtoList, offsetPaginationApiMetaData);
    }

    @Override
    public TheatreResDto findById(Long id) throws NotFoundException
    {
        Theatre existingTheatre = this.findExistingTheatreById(id);

        return this.theatreMapper.toTheatreResDto(existingTheatre);
    }

    @Override
    public Theatre findExistingTheatreById(Long id) throws NotFoundException
    {
        return this.theatreRepository.findById(id).orElseThrow(() -> new NotFoundException("theatre " +
                "not" +
                " " +
                "found"));
    }

    @Override
    public List<TheatreNameResDto> findAll()
    {
        List<Theatre> theatreList = this.theatreRepository.findAll();

        return theatreList.stream().map(this.theatreMapper::toTheatreNameResDto).toList();
    }
}
