package com.kst.movie_ticket_reservation.feat.seat.service.impl;

import com.kst.movie_ticket_reservation.feat.theatre.entity.Theatre;
import com.kst.movie_ticket_reservation.feat.seat.dto.req.CreateSeatDto;
import com.kst.movie_ticket_reservation.feat.seat.dto.req.UpdateSeatDto;
import com.kst.movie_ticket_reservation.feat.seat.dto.res.SeatOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.seat.dto.res.SeatResDto;
import com.kst.movie_ticket_reservation.feat.seat.entity.Seat;
import com.kst.movie_ticket_reservation.feat.seat.mapper.SeatMapper;
import com.kst.movie_ticket_reservation.feat.seat.repository.SeatRepository;
import com.kst.movie_ticket_reservation.feat.seat.service.SeatService;
import com.kst.movie_ticket_reservation.feat.theatre.service.TheatreService;
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
public class SeatServiceImpl implements SeatService
{
    private final SeatRepository seatRepository;
    private final TheatreService theatreService;
    private final SeatMapper seatMapper;
    private final OffsetPaginationService offsetPaginationService;

    @Override
    public SeatResDto create(Long theatreId, CreateSeatDto createSeatDto) throws ConflictException, NotFoundException
    {
        Theatre existingTheatre = this.theatreService.findExistingTheatreById(theatreId);

        if (this.seatRepository.existsByTheatreIdAndRowAndSeatNumber(theatreId, createSeatDto.getRow(),
                createSeatDto.getSeatNumber()))
        {
            throw new ConflictException("seat with this row and seat number already exist in this theatre");
        }

        Seat seat = this.seatMapper.toSeat(createSeatDto);
        seat.setTheatre(existingTheatre);

        Seat createdSeat = this.seatRepository.save(seat);

        return this.seatMapper.toSeatResDto(createdSeat);
    }

    @Override
    @Transactional
    public SeatResDto update(Long id, UpdateSeatDto updateSeatDto) throws NotFoundException
    {
        Seat existingSeat = this.seatRepository.findById(id).orElseThrow(() -> new NotFoundException("seat not found"));

        this.seatMapper.updateSeatFromDto(updateSeatDto, existingSeat);

        Seat updatedSeat = this.seatRepository.save(existingSeat);

        return this.seatMapper.toSeatResDto(updatedSeat);
    }

    @Override
    @Transactional
    public SeatResDto hide(Long id) throws NotFoundException
    {
        Seat existingSeat =
                this.seatRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new NotFoundException("seat not " +
                        "found"));

        existingSeat.hide();

        return this.seatMapper.toSeatResDto(existingSeat);
    }

    @Override
    @Transactional
    public SeatResDto show(Long id) throws NotFoundException
    {
        Seat existingSeat =
                this.seatRepository.findByIdAndIsDeletedTrue(id).orElseThrow(() -> new NotFoundException("seat not " +
                        "found"));

        existingSeat.show();

        return this.seatMapper.toSeatResDto(existingSeat);
    }

    @Override
    @Transactional
    public SeatResDto delete(Long id) throws NotFoundException
    {
        Seat existingSeat = this.seatRepository.findByIdAndIsDeletedTrue(id).orElseThrow(() -> new NotFoundException(
                "seat not found"));

        this.seatRepository.deleteById(id);

        return this.seatMapper.toSeatResDto(existingSeat);
    }

    @Override
    public SeatOffsetPaginationResDto findMany(int offset, int limit)
    {
        Page<Seat> seatPage = this.seatRepository.findAll(this.offsetPaginationService.calculatePageable(offset,
                limit));

        List<SeatResDto> seatResDtoList = seatPage.getContent().stream().map(this.seatMapper::toSeatResDto).toList();

        return new SeatOffsetPaginationResDto(seatResDtoList,
                this.offsetPaginationService.generateMetaData(seatPage));
    }

    @Override
    public SeatOffsetPaginationResDto findManyByTheatreId(Long theatreId, int offset, int limit)
    {
        Page<Seat> seatPage = this.seatRepository.findAllByTheatreId(theatreId,
                this.offsetPaginationService.calculatePageable(offset, limit));

        List<SeatResDto> seatResDtoList = seatPage.getContent().stream().map(this.seatMapper::toSeatResDto).toList();

        return new SeatOffsetPaginationResDto(seatResDtoList,
                this.offsetPaginationService.generateMetaData(seatPage));
    }

}

