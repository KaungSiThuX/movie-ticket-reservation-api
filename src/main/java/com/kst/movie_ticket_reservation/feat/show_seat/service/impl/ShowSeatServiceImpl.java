package com.kst.movie_ticket_reservation.feat.show_seat.service.impl;

import com.kst.movie_ticket_reservation.feat.seat.repository.SeatRepository;
import com.kst.movie_ticket_reservation.feat.show_date.service.ShowDateService;
import com.kst.movie_ticket_reservation.feat.show_seat.dto.req.UpdateShowSeatDto;
import com.kst.movie_ticket_reservation.feat.show_seat.dto.res.ShowSeatOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.show_seat.mapper.ShowSeatMapper;
import com.kst.movie_ticket_reservation.feat.show_time.entity.ShowTime;
import com.kst.movie_ticket_reservation.feat.show_time.repository.ShowTimeRepository;
import com.kst.movie_ticket_reservation.feat.show_time.service.ShowTimeService;
import com.kst.movie_ticket_reservation.feat.theatre.entity.Theatre;
import com.kst.movie_ticket_reservation.feat.seat.entity.Seat;
import com.kst.movie_ticket_reservation.feat.show_seat.dto.req.CreateShowSeatDto;
import com.kst.movie_ticket_reservation.feat.show_seat.dto.res.ShowSeatResDto;
import com.kst.movie_ticket_reservation.feat.show_seat.entity.ShowSeat;
import com.kst.movie_ticket_reservation.feat.show_seat.repository.ShowSeatRepository;
import com.kst.movie_ticket_reservation.feat.show_seat.service.ShowSeatService;
import com.kst.movie_ticket_reservation.feat.theatre.service.TheatreService;
import com.kst.movie_ticket_reservation.util.api_responses.OffsetPaginationApiMetaData;
import com.kst.movie_ticket_reservation.util.enums.ShowSeatStatus;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;
import com.kst.movie_ticket_reservation.util.services.pagination.OffsetPaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowSeatServiceImpl implements ShowSeatService
{
    private final ShowSeatRepository showSeatRepository;
    private final TheatreService theatreService;
    private final ShowDateService showDateService;
    private final SeatRepository seatRepository;
    private final OffsetPaginationService offsetPaginationService;
    private final ShowSeatMapper showSeatMapper;
    private final ShowTimeService showTimeService;
    private final ShowTimeRepository showTimeRepository;

    @Override
    @Transactional
    public List<ShowSeatResDto> create(CreateShowSeatDto createShowSeatDto) throws NotFoundException
    {
        //  Theatre existingTheatre = this.theatreService.findExistingTheatreById(createShowSeatDto.getTheatreId());

        //ShowDate existingShowDate = this.showDateService.findExistingShowDateById(createShowSeatDto.getShowDateId());

        ShowTime existingShowTime =
                this.showTimeRepository.findById(createShowSeatDto.getShowTimeId()).orElseThrow(() -> new NotFoundException("show time not found"));

        List<Seat> seatList =
                this.seatRepository.findAllByTheatreId(existingShowTime.getShowDate().getTheatre().getId());

        List<ShowSeat> showSeatList = seatList.stream().map(seat ->
        {
            ShowSeat showSeat = new ShowSeat();
            showSeat.setSeat(seat);
            showSeat.setShowTime(existingShowTime);
            showSeat.setShowSeatStatus(ShowSeatStatus.AVAILABLE);
            showSeat.setBasePrice(createShowSeatDto.getShowSeatPrices().stream()
                    .filter(showSeatPrice -> showSeatPrice.getSeatType() == seat.getSeatType())
                    .findFirst().orElseThrow().getPrice());

            return showSeat;
        }).toList();

        List<ShowSeat> createdShowSeatList = this.showSeatRepository.saveAll(showSeatList);

        return createdShowSeatList.stream().map(this.showSeatMapper::toShowSeatResDto).toList();
    }

//    @Override
//    public ShowSeatResDto update(Long id, UpdateShowSeatDto updateShowSeatDto) throws NotFoundException
//    {
//        ShowTime existingShowTime =
//                this.showTimeRepository.findById(updateShowSeatDto.getShowTimeId()).orElseThrow(() -> new
//                NotFoundException("show time not found"));
//
//        List<ShowSeat> showSeatList = this.showSeatRepository.findAllByShowTimeId(existingShowTime.getId());
//
//        List<ShowSeat> updateShowSeat = showSeatList.stream().map(showSeat -> {
//            showSeat.set
//        }).toList()
//    }

    private ShowSeat findExistingShowSeatById(Long id) throws NotFoundException
    {
        return this.showSeatRepository.findById(id).orElseThrow(() -> new NotFoundException("show seat not found"));
    }

    @Override
    public ShowSeatOffsetPaginationResDto findManyByOffset(int offset, int limit)
    {
        Page<ShowSeat> showSeatPage =
                this.showSeatRepository.findAll(this.offsetPaginationService.calculatePageable(offset, limit));

        OffsetPaginationApiMetaData offsetPaginationApiMetaData =
                this.offsetPaginationService.generateMetaData(showSeatPage);

        return new ShowSeatOffsetPaginationResDto(showSeatPage.getContent().stream().map(this.showSeatMapper::toShowSeatResDto).toList(), offsetPaginationApiMetaData);
    }

    @Override
    public ShowSeatOffsetPaginationResDto findManyByShowTimeIdOffset(Long showTimeId, int offset, int limit)
    {
        Page<ShowSeat> showSeatPage =
                this.showSeatRepository.findAllByShowTimeId(showTimeId,
                        this.offsetPaginationService.calculatePageable(offset, limit));

        OffsetPaginationApiMetaData offsetPaginationApiMetaData =
                this.offsetPaginationService.generateMetaData(showSeatPage);

        return new ShowSeatOffsetPaginationResDto(showSeatPage.getContent().stream().map(this.showSeatMapper::toShowSeatResDto).toList(), offsetPaginationApiMetaData);
    }
}
