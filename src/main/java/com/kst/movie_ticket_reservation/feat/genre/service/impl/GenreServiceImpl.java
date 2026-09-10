package com.kst.movie_ticket_reservation.feat.genre.service.impl;

import com.kst.movie_ticket_reservation.feat.genre.dto.req.CreateGenreDto;
import com.kst.movie_ticket_reservation.feat.genre.dto.req.UpdateGenreDto;
import com.kst.movie_ticket_reservation.feat.genre.dto.res.GenreOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.genre.dto.res.GenreResDto;
import com.kst.movie_ticket_reservation.feat.genre.entity.Genre;
import com.kst.movie_ticket_reservation.feat.genre.mapper.GenreMapper;
import com.kst.movie_ticket_reservation.feat.genre.repository.GenreRepository;
import com.kst.movie_ticket_reservation.feat.genre.service.GenreService;
import com.kst.movie_ticket_reservation.feat.movie.entity.Movie;
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
public class GenreServiceImpl implements GenreService
{
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;
    private final OffsetPaginationService offsetPaginationService;

    @Override
    //  @CacheEvict(cacheNames = "genres", allEntries = true)
    public GenreResDto create(CreateGenreDto createGenreDto) throws ConflictException
    {
        if (this.genreRepository.existsByNameIgnoreCaseOrSlugIgnoreCase(createGenreDto.getName(),
                createGenreDto.getSlug()))
        {
            throw new ConflictException("genre with this name and slug already exist");
        }

        Genre createdGenre = this.genreRepository.save(this.genreMapper.toGenre(createGenreDto));

        return this.genreMapper.toGenreResDto(createdGenre);
    }

    @Override
    //   @CacheEvict(cacheNames = "genres", allEntries = true)
    public GenreResDto update(Long id, UpdateGenreDto updateGenreDto) throws NotFoundException, ConflictException
    {
        Genre existingGenre = this.findExistingGenreById(id);

        this.genreMapper.updateGenreFromDto(updateGenreDto, existingGenre);

        Genre updatedGenre = this.genreRepository.save(existingGenre);

        return this.genreMapper.toGenreResDto(updatedGenre);
    }

    @Override
    //  @CacheEvict(cacheNames = "genres", allEntries = true)
    public GenreResDto delete(Long id) throws NotFoundException
    {
        Genre existingGenre = this.findExistingGenreById(id);

        for (Movie movie : existingGenre.getMovies())
        {
            movie.getGenres().remove(existingGenre);
        }

        existingGenre.getMovies().clear();

        this.genreRepository.deleteById(id);

        return this.genreMapper.toGenreResDto(existingGenre);
    }

    @Override
    @Transactional
    public GenreResDto hide(Long id) throws NotFoundException
    {
        Genre existingGenre = this.findExistingGenreById(id);

        existingGenre.hide();

        return this.genreMapper.toGenreResDto(existingGenre);
    }

    @Override
    @Transactional
    public GenreResDto show(Long id) throws NotFoundException
    {
        Genre existingGenre =
                this.genreRepository.findByIdAndIsDeletedTrue(id).orElseThrow(() -> new NotFoundException("genre not " +
                        "found"));

        existingGenre.show();

        return this.genreMapper.toGenreResDto(existingGenre);
    }


    @Override
    // @Cacheable(cacheNames = "genres", key = "#offset + '_' + #limit")
    public GenreOffsetPaginationResDto findMany(int offset, int limit)
    {
        Page<Genre> genrePage =
                this.genreRepository.findByIsDeletedFalseOrderByIdDesc(this.offsetPaginationService.calculatePageable(offset, limit));

        List<GenreResDto> genreResDtoList =
                genrePage.getContent().stream().map(this.genreMapper::toGenreResDto).toList();

        OffsetPaginationApiMetaData offsetPaginationApiMetaData = this.offsetPaginationService
                .generateMetaData(genrePage);

        return new GenreOffsetPaginationResDto(genreResDtoList, offsetPaginationApiMetaData);
    }

    @Override
    // @Cacheable(cacheNames = "genres", key = "#id")
    public GenreResDto findById(Long id) throws NotFoundException
    {
        Genre existingGenre = this.findExistingGenreById(id);

        return this.genreMapper.toGenreResDto(existingGenre);
    }

    @Override
    public List<GenreResDto> findAll()
    {
        List<Genre> genreList = this.genreRepository.findAll();

        return genreList.stream().map(this.genreMapper::toGenreResDto).toList();
    }

    private Genre findExistingGenreById(Long id) throws NotFoundException
    {
        return this.genreRepository.findById(id).orElseThrow(() -> new NotFoundException("genre not " +
                "found"));
    }
}
