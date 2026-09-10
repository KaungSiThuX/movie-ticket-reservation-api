package com.kst.movie_ticket_reservation.feat.cast.service.impl;

import com.kst.movie_ticket_reservation.feat.cast.dto.req.CreateCastDto;
import com.kst.movie_ticket_reservation.feat.cast.dto.req.UpdateCastDto;
import com.kst.movie_ticket_reservation.feat.cast.dto.res.CastOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.cast.dto.res.CastResDto;
import com.kst.movie_ticket_reservation.feat.cast.entity.Cast;
import com.kst.movie_ticket_reservation.feat.cast.mapper.CastMapper;
import com.kst.movie_ticket_reservation.feat.cast.repository.CastRepository;
import com.kst.movie_ticket_reservation.feat.cast.service.CastService;
import com.kst.movie_ticket_reservation.util.api_responses.OffsetPaginationApiMetaData;
import com.kst.movie_ticket_reservation.util.exceptions.ConflictException;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;
import com.kst.movie_ticket_reservation.util.services.pagination.OffsetPaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CastServiceImpl implements CastService
{
    private final CastRepository castRepository;
    private final CastMapper castMapper;
    private final OffsetPaginationService offsetPaginationService;

    @Override
    @Transactional
    public CastResDto create(CreateCastDto createCastDto) throws ConflictException
    {
        Optional<Cast> existingCast =
                this.castRepository.findByNameIgnoreCaseAndSlugIgnoreCaseAndCastType(createCastDto.getName(),
                        createCastDto.getSlug(), createCastDto.getCastType());

        if (existingCast.isPresent())
        {
            throw new ConflictException("director with this name or slug already exist");
        }

        Cast cast = this.castMapper.toCast(createCastDto);

        Cast createdCast = this.castRepository.save(cast);

        return this.castMapper.toCastResDto(createdCast);
    }

    @Override
    @Transactional
    public CastResDto update(Long id, UpdateCastDto updateCastDto) throws NotFoundException,
            ConflictException
    {
        Cast existingCast = this.findExistingCastById(id);

        if (this.castRepository.findByNameIgnoreCaseAndSlugIgnoreCaseAndCastType(updateCastDto.getName(),
                updateCastDto.getSlug(), updateCastDto.getCastType()).isPresent())
        {
            throw new ConflictException("cast with this name or slug or type already exist");
        }

        this.castMapper.updateCastFromDto(updateCastDto, existingCast);

        Cast updatedCast = this.castRepository.save(existingCast);

        return this.castMapper.toCastResDto(updatedCast);
    }

    @Override
    @Transactional
    public CastResDto delete(Long id) throws NotFoundException
    {
        Cast existingCast = this.findExistingCastById(id);

        this.castRepository.deleteById(id);

        return this.castMapper.toCastResDto(existingCast);
    }

    private Cast findExistingCastById(Long id) throws NotFoundException
    {
        return this.castRepository.findById(id).orElseThrow(() -> new NotFoundException("cast not found"));
    }

    @Override
    public CastOffsetPaginationResDto findMany(int offset, int limit)
    {
        Page<Cast> castPage =
                this.castRepository.findAll(this.offsetPaginationService.calculatePageable(offset, limit));

        List<CastResDto> castResDtoList =
                castPage.getContent().stream().map(this.castMapper::toCastResDto).toList();

        OffsetPaginationApiMetaData offsetPaginationApiMetaData =
                this.offsetPaginationService.generateMetaData(castPage);

        return new CastOffsetPaginationResDto(castResDtoList, offsetPaginationApiMetaData);

    }

    @Override
    public List<CastResDto> findAll()
    {
        List<Cast> castList = this.castRepository.findAll();

        return castList.stream().map(this.castMapper::toCastResDto).toList();
    }
}
