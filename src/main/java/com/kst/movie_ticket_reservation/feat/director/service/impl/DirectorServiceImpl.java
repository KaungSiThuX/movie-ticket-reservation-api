package com.kst.movie_ticket_reservation.feat.director.service.impl;

import com.kst.movie_ticket_reservation.feat.director.dto.req.CreateDirectorDto;
import com.kst.movie_ticket_reservation.feat.director.dto.req.UpdateDirectorDto;
import com.kst.movie_ticket_reservation.feat.director.dto.res.DirectorOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.director.dto.res.DirectorResDto;
import com.kst.movie_ticket_reservation.feat.director.enitty.Director;
import com.kst.movie_ticket_reservation.feat.director.mapper.DirectorMapper;
import com.kst.movie_ticket_reservation.feat.director.repository.DirectorRepository;
import com.kst.movie_ticket_reservation.feat.director.service.DirectorService;
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
public class DirectorServiceImpl implements DirectorService
{
    private final DirectorRepository directorRepository;
    private final DirectorMapper directorMapper;
    private final OffsetPaginationService offsetPaginationService;

    @Override
    @Transactional
    public DirectorResDto create(CreateDirectorDto createDirectorDto) throws ConflictException
    {
        if (this.directorRepository.existsByNameIgnoreCaseOrSlugIgnoreCase(createDirectorDto.getName(),
                createDirectorDto.getSlug()))
        {
            throw new ConflictException("director with this name or slug already exist");
        }

        Director director = this.directorMapper.toDirector(createDirectorDto);

        Director createdDirector = this.directorRepository.save(director);

        return this.directorMapper.toDirectorResDto(createdDirector);
    }

    @Override
    @Transactional
    public DirectorResDto update(Long id, UpdateDirectorDto updateDirectorDto) throws NotFoundException,
            ConflictException
    {
        Director existingDirector = this.findExistingDirectorById(id);

        if (this.directorRepository.existsByNameIgnoreCaseOrSlugIgnoreCase(updateDirectorDto.getName(),
                updateDirectorDto.getSlug()))
        {
            throw new ConflictException("director with this name or slug already exist");
        }

        this.directorMapper.updateDirectorFromDto(updateDirectorDto, existingDirector);

        Director updatedDirector = this.directorRepository.save(existingDirector);

        return this.directorMapper.toDirectorResDto(updatedDirector);
    }

    @Override
    @Transactional
    public DirectorResDto delete(Long id) throws NotFoundException
    {
        Director existingDirector = this.findExistingDirectorById(id);

        this.directorRepository.deleteById(id);

        return this.directorMapper.toDirectorResDto(existingDirector);
    }

    private Director findExistingDirectorById(Long id) throws NotFoundException
    {
        return this.directorRepository.findById(id).orElseThrow(() -> new NotFoundException("director not found"));
    }

    @Override
    public DirectorOffsetPaginationResDto findMany(int offset, int limit)
    {
        Page<Director> directorPage =
                this.directorRepository.findAll(this.offsetPaginationService.calculatePageable(offset, limit));

        List<DirectorResDto> directorResDtoList =
                directorPage.getContent().stream().map(this.directorMapper::toDirectorResDto).toList();

        OffsetPaginationApiMetaData offsetPaginationApiMetaData =
                this.offsetPaginationService.generateMetaData(directorPage);

        return new DirectorOffsetPaginationResDto(directorResDtoList, offsetPaginationApiMetaData);

    }

    @Override
    public List<DirectorResDto> findAll()
    {
        List<Director> directorList = this.directorRepository.findAll();

        return directorList.stream().map(this.directorMapper::toDirectorResDto).toList();
    }
}
