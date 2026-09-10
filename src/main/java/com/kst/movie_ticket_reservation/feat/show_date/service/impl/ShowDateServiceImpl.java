package com.kst.movie_ticket_reservation.feat.show_date.service.impl;

import com.kst.movie_ticket_reservation.feat.movie.service.MovieService;
import com.kst.movie_ticket_reservation.feat.show_date.dto.res.ShowDateOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.show_date.mapper.ShowDateMapper;
import com.kst.movie_ticket_reservation.feat.theatre.entity.Theatre;
import com.kst.movie_ticket_reservation.feat.movie.entity.Movie;
import com.kst.movie_ticket_reservation.feat.show_date.dto.req.CreateShowDateDto;
import com.kst.movie_ticket_reservation.feat.show_date.dto.req.UpdateShowDateDto;
import com.kst.movie_ticket_reservation.feat.show_date.dto.res.ShowDateResDto;
import com.kst.movie_ticket_reservation.feat.show_date.entity.ShowDate;
import com.kst.movie_ticket_reservation.feat.show_date.repository.ShowDateRepository;
import com.kst.movie_ticket_reservation.feat.show_date.service.ShowDateService;
import com.kst.movie_ticket_reservation.feat.theatre.service.TheatreService;
import com.kst.movie_ticket_reservation.util.api_responses.OffsetPaginationApiMetaData;
import com.kst.movie_ticket_reservation.util.exceptions.ConflictException;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;
import com.kst.movie_ticket_reservation.util.services.pagination.OffsetPaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowDateServiceImpl implements ShowDateService
{
    private final ShowDateRepository showDateRepository;
    private final ShowDateMapper showDateMapper;
    private final OffsetPaginationService offsetPaginationService;
    private final TheatreService theatreService;
    private final MovieService movieService;

    @Override
    @Transactional
    public ShowDateResDto create(CreateShowDateDto createShowDateDto) throws ConflictException,
            NotFoundException
    {
        if (this.showDateRepository.existsByShowDisplayDateAndTheatreIdAndMovieId(createShowDateDto.getShowDisplayDate(), createShowDateDto.getTheatreId(), createShowDateDto.getMovieId()))
        {
            throw new ConflictException("show date for this movie and theater already exist");
        }

        Theatre existingTheatre = this.theatreService.findExistingTheatreById(createShowDateDto.getTheatreId());

        Movie exisitngMovie = this.movieService.findExistingMovieById(createShowDateDto.getMovieId());

        ShowDate showDate = this.showDateMapper.toShowDate(createShowDateDto);
        showDate.setTheatre(existingTheatre);
        showDate.setMovie(exisitngMovie);

        ShowDate createdShowDate = this.showDateRepository.save(showDate);

        return this.showDateMapper.toShowDateResDto(createdShowDate);
    }

    @Override
    @Transactional
    public ShowDateResDto update(Long id, UpdateShowDateDto updateShowDateDto) throws NotFoundException,
            ConflictException
    {
        ShowDate existingShowDate = this.findExistingShowDateById(id);

        Theatre existingTheatre = this.theatreService.findExistingTheatreById(updateShowDateDto.getTheatreId());

        Movie exisitngMovie = this.movieService.findExistingMovieById(updateShowDateDto.getMovieId());

        if (this.showDateRepository.existsByShowDisplayDateAndTheatreIdAndMovieIdAndIsDeletedFalse(updateShowDateDto.getShowDisplayDate(), updateShowDateDto.getTheatreId(), updateShowDateDto.getMovieId()))
        {
            throw new ConflictException("show date for that movie and theatre already exist");
        }

        existingShowDate.setTheatre(existingTheatre);
        existingShowDate.setMovie(exisitngMovie);
        this.showDateMapper.updateShowDateFromDto(updateShowDateDto, existingShowDate);

        ShowDate updatedShowDate = this.showDateRepository.save(existingShowDate);

        return this.showDateMapper.toShowDateResDto(updatedShowDate);
    }

    @Override
    @Transactional
    public ShowDateResDto delete(Long id) throws NotFoundException
    {
        ShowDate existingShowDate = this.findExistingShowDateById(id);

        this.showDateRepository.deleteById(id);

        return this.showDateMapper.toShowDateResDto(existingShowDate);
    }

    @Override
    public ShowDateOffsetPaginationResDto findManyByOffset(int offset, int limit)
    {
        Page<ShowDate> showDatePage =
                this.showDateRepository.findAll(this.offsetPaginationService.calculatePageable(offset, limit));

        List<ShowDateResDto> showDateResDtoList =
                showDatePage.getContent().stream().map(this.showDateMapper::toShowDateResDto).toList();

        OffsetPaginationApiMetaData offsetPaginationApiMetaData =
                this.offsetPaginationService.generateMetaData(showDatePage);

        return new ShowDateOffsetPaginationResDto(showDateResDtoList, offsetPaginationApiMetaData);
    }

    @Override
    public ShowDate findExistingShowDateById(Long id) throws NotFoundException
    {
        return this.showDateRepository.findById(id).orElseThrow(() -> new NotFoundException("show date not found"));
    }

}
