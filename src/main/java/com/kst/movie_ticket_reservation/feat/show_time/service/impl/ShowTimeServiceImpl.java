package com.kst.movie_ticket_reservation.feat.show_time.service.impl;

import com.kst.movie_ticket_reservation.feat.show_date.entity.ShowDate;
import com.kst.movie_ticket_reservation.feat.show_date.service.ShowDateService;
import com.kst.movie_ticket_reservation.feat.show_time.dto.req.CreateShowTimeDto;
import com.kst.movie_ticket_reservation.feat.show_time.dto.req.UpdateShowTimeDto;
import com.kst.movie_ticket_reservation.feat.show_time.dto.res.ShowTimeOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.show_time.dto.res.ShowTimeResDto;
import com.kst.movie_ticket_reservation.feat.show_time.entity.ShowTime;
import com.kst.movie_ticket_reservation.feat.show_time.mapper.ShowTimeMapper;
import com.kst.movie_ticket_reservation.feat.show_time.repository.ShowTimeRepository;
import com.kst.movie_ticket_reservation.feat.show_time.service.ShowTimeService;
import com.kst.movie_ticket_reservation.util.api_responses.OffsetPaginationApiMetaData;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;
import com.kst.movie_ticket_reservation.util.services.pagination.OffsetPaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowTimeServiceImpl implements ShowTimeService
{
    private final ShowTimeRepository showTimeRepository;
    private final ShowDateService showDateService;
    private final OffsetPaginationService offsetPaginationService;
    private final ShowTimeMapper showTimeMapper;

    @Override
    @Transactional
    public List<ShowTimeResDto> create(CreateShowTimeDto createShowTimeDto) throws NotFoundException
    {
        ShowDate existingShowDate = this.showDateService.findExistingShowDateById(createShowTimeDto.getShowDateId());

        List<ShowTime> showTimeList = createShowTimeDto.getShowDisplayTimes().stream().map(showTimeStr ->
        {
            ShowTime showTime = new ShowTime();
            showTime.setShowDisplayTime(showTimeStr);
            showTime.setShowDate(existingShowDate);
            return showTime;
        }).toList();

        List<ShowTime> createdShowTimeList = this.showTimeRepository.saveAll(showTimeList);

        return createdShowTimeList.stream().map(this.showTimeMapper::toShowTimeResDto).toList();
    }

    @Override
    public ShowTimeResDto update(Long id, UpdateShowTimeDto updateShowTimeDto) throws NotFoundException
    {
        ShowTime existingShowTime = this.findExistingShowTimeById(id);

        ShowDate existingShowDate =
                this.showDateService.findExistingShowDateById(existingShowTime.getShowDate().getId());

        existingShowTime.setShowDate(existingShowDate);
        // this.showTimeMapper.updateShowTimeFromDto(updateShowTimeDto, existingShowTime);
        existingShowTime.setShowDisplayTime(updateShowTimeDto.getShowDisplayTime());
        ShowTime updatedShowTime = this.showTimeRepository.save(existingShowTime);

        return this.showTimeMapper.toShowTimeResDto(updatedShowTime);
    }

    @Override
    public ShowTimeResDto delete(Long id) throws NotFoundException
    {
        ShowTime existingShowTime = this.findExistingShowTimeById(id);

        this.showTimeRepository.deleteById(id);

        return this.showTimeMapper.toShowTimeResDto(existingShowTime);
    }

    private ShowTime findExistingShowTimeById(Long id) throws NotFoundException
    {
        return this.showTimeRepository.findById(id).orElseThrow(() -> new NotFoundException(
                "show time not found"));
    }

    @Override
    public ShowTimeOffsetPaginationResDto findManyByOffset(Long showDateId, int offset, int limit)
    {
        Page<ShowTime> showTimePage = this.showTimeRepository.findByShowDateId(showDateId,
                this.offsetPaginationService.calculatePageable(offset, limit));

        OffsetPaginationApiMetaData offsetPaginationApiMetaData =
                this.offsetPaginationService.generateMetaData(showTimePage);

        return new ShowTimeOffsetPaginationResDto(showTimePage.getContent().stream().map(this.showTimeMapper::toShowTimeResDto).toList(),
                offsetPaginationApiMetaData);
    }
}
