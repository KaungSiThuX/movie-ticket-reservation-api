package com.kst.movie_ticket_reservation.feat.public_api.service.impl;

import com.kst.movie_ticket_reservation.feat.movie.entity.Movie;
import com.kst.movie_ticket_reservation.feat.movie.repository.MovieRepository;
import com.kst.movie_ticket_reservation.feat.public_api.dto.res.*;
import com.kst.movie_ticket_reservation.feat.public_api.mapper.PublicApiMapper;
import com.kst.movie_ticket_reservation.feat.public_api.service.PublicApiService;

import com.kst.movie_ticket_reservation.feat.seat_lock.service.SeatLockService;
import com.kst.movie_ticket_reservation.feat.show_date.entity.ShowDate;
import com.kst.movie_ticket_reservation.feat.show_date.repository.ShowDateRepository;
import com.kst.movie_ticket_reservation.feat.show_seat.dto.res.ShowSeatResDto;
import com.kst.movie_ticket_reservation.feat.show_seat.entity.ShowSeat;
import com.kst.movie_ticket_reservation.feat.show_seat.repository.ShowSeatRepository;
import com.kst.movie_ticket_reservation.feat.show_time.entity.ShowTime;
import com.kst.movie_ticket_reservation.feat.show_time.repository.ShowTimeRepository;
import com.kst.movie_ticket_reservation.util.enums.ShowSeatStatus;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;
import com.kst.movie_ticket_reservation.util.services.pagination.CursorPaginationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicApiServiceImpl implements PublicApiService
{
    private final ShowSeatRepository showSeatRepository;
    private final SeatLockService seatLockService;
    private final ShowDateRepository showDateRepository;
    private final ShowTimeRepository showTimeRepository;
    private final PublicApiMapper publicApiMapper;
    private final MovieRepository movieRepository;
    private final CursorPaginationService cursorPaginationService;

    @Override
    public List<ShowSeatResDto> findManyShowSeat(String showTimePublicId) throws NotFoundException
    {
        ShowTime existingShowTime =
                this.showTimeRepository.findByPublicId(showTimePublicId)
                        .orElseThrow(() -> new NotFoundException("show time not found"));

        List<ShowSeat> showSeatList =
                this.showSeatRepository.findAllByShowTimeId(existingShowTime.getId());

        Set<Long> lockedShowSeatIdSet = this.seatLockService.getLockedSeatIdsForShowTime(existingShowTime.getId());

        log.info("seat lock set length " + lockedShowSeatIdSet.size());
        for (Long id : lockedShowSeatIdSet)
        {
            log.info("locked seat id in public api " + id);
        }

        return showSeatList.stream().map(showSeat ->
        {
            ShowSeatStatus showSeatStatus = lockedShowSeatIdSet.contains(showSeat.getId()) ? ShowSeatStatus.LOCK :
                    showSeat.getShowSeatStatus();

            return new ShowSeatResDto(showSeat.getId(),
                    showSeat.getSeat().getRow(),
                    showSeat.getSeat().getSeatNumber(),
                    showSeat.getSeat().getSeatType(),
                    showSeat.getBasePrice(),
                    showSeatStatus
            );
        }).toList();
    }

    @Override
    public List<PublicApiShowDateResDto> findManyShowDate(Long movieId)
    {
        List<ShowDate> showDateList = this.showDateRepository.findByMovieId(movieId);

        return showDateList.stream().map(this.publicApiMapper::toPublicApiShowDateResDto).toList();
    }

    @Override
    public PublicApiMoviesCursorPaginationResDto findManyMoviesByCursor(Long cursor, int limit)
    {
        Slice<Movie> movieSlice = this.movieRepository.findAllByCursor(cursor, PageRequest.of(0, limit));

        List<PublicApiMovieResDto> movieList =
                movieSlice.getContent().stream().map(this.publicApiMapper::toPublicApiMovieResDto).toList();
        boolean hasNextPage = movieSlice.hasNext();

        String nextCursor = (!movieList.isEmpty() && hasNextPage) ?
                String.valueOf(movieList.get(movieList.size() - 1).id()) : null;

        return new PublicApiMoviesCursorPaginationResDto(movieList,
                this.cursorPaginationService.generateMetaData(hasNextPage, nextCursor));
    }

    @Override
    public PublicApiMovieDetailsResDto findMovieDetails(Long movieId) throws NotFoundException
    {
        Movie movie = this.movieRepository.findById(movieId).orElseThrow(() -> new NotFoundException("movie not " +
                "found"));

        return this.publicApiMapper.toPublicApiMovieDetailsResDto(movie);
    }

    @Override
    public List<PublicApiShowSeatResDto> findAllShowSeatsByShowTimePublicId(String showTimePublicId) throws NotFoundException
    {
        ShowTime showTime =
                this.showTimeRepository.findByPublicId(showTimePublicId).orElseThrow(() -> new NotFoundException(
                        "show time not found"));

        List<ShowSeat> showSeatList = this.showSeatRepository.findAllByShowTimeId(showTime.getId());

        return showSeatList.stream().map(this.publicApiMapper::toPublicApiShowSeatResDto).toList();
    }
}
