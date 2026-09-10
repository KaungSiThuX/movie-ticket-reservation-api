package com.kst.movie_ticket_reservation.feat.movie.service.impl;

import com.kst.movie_ticket_reservation.feat.cast.entity.Cast;
import com.kst.movie_ticket_reservation.feat.cast.repository.CastRepository;
import com.kst.movie_ticket_reservation.feat.director.enitty.Director;
import com.kst.movie_ticket_reservation.feat.director.repository.DirectorRepository;
import com.kst.movie_ticket_reservation.feat.genre.entity.Genre;
import com.kst.movie_ticket_reservation.feat.genre.repository.GenreRepository;
import com.kst.movie_ticket_reservation.feat.movie.dto.req.CreateMovieDto;
import com.kst.movie_ticket_reservation.feat.movie.dto.req.UpdateMovieFormDto;
import com.kst.movie_ticket_reservation.feat.movie.dto.req.UpdateMovieJsonDto;
import com.kst.movie_ticket_reservation.feat.movie.dto.res.MovieOffsetPaginationResDto;
import com.kst.movie_ticket_reservation.feat.movie.dto.res.MovieResDto;
import com.kst.movie_ticket_reservation.feat.movie.dto.res.MovieTitleResDto;
import com.kst.movie_ticket_reservation.feat.movie.entity.Movie;
import com.kst.movie_ticket_reservation.feat.movie.mapper.MovieMapper;
import com.kst.movie_ticket_reservation.feat.movie.repository.MovieRepository;
import com.kst.movie_ticket_reservation.feat.movie.service.MovieService;
import com.kst.movie_ticket_reservation.util.api_responses.OffsetPaginationApiMetaData;
import com.kst.movie_ticket_reservation.util.events.ImageDeleteEvent;
import com.kst.movie_ticket_reservation.util.exceptions.ConflictException;
import com.kst.movie_ticket_reservation.util.exceptions.CustomS3Exception;
import com.kst.movie_ticket_reservation.util.exceptions.NotFoundException;
import com.kst.movie_ticket_reservation.util.job_payloads.ImageDeletePayload;
import com.kst.movie_ticket_reservation.util.services.object_storage.ObjectStorageService;
import com.kst.movie_ticket_reservation.util.services.pagination.OffsetPaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService
{
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final GenreRepository genreRepository;
    private final ObjectStorageService objectStorageService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final OffsetPaginationService offsetPaginationService;
    private final DirectorRepository directorRepository;
    private final CastRepository castRepository;

    @Override
    @Transactional
    public MovieResDto createJson(CreateMovieDto createMovieDto) throws ConflictException,
            NotFoundException
    {
        this.validateExistingMovieByTitleOrSlug(createMovieDto.getTitle(), createMovieDto.getSlug());

        List<Genre> genreList = this.findExistingGenresByIds(createMovieDto.getGenreIds());

        List<Director> directorList = this.findExistingDirectorsByIds(createMovieDto.getDirectorIds());

        List<Cast> castList = this.findExistingCastsByIds(createMovieDto.getCastIds());

        Movie newMovie = this.movieMapper.toMovie(createMovieDto);
        newMovie.setGenres(new HashSet<>(genreList));
        newMovie.setDirectors(new HashSet<>(directorList));
        newMovie.setCasts(new HashSet<>(castList));

        Movie createdMovie = this.movieRepository.save(newMovie);

        return this.movieMapper.toMovieResDto(createdMovie);
    }

    @Override
    @Transactional
    public MovieResDto createForm(CreateMovieDto createMovieDto, MultipartFile poster) throws ConflictException,
            NotFoundException, CustomS3Exception
    {
        this.validateExistingMovieByTitleOrSlug(createMovieDto.getTitle(), createMovieDto.getSlug());

        List<Genre> genreList = this.findExistingGenresByIds(createMovieDto.getGenreIds());

        List<Director> directorList = this.findExistingDirectorsByIds(createMovieDto.getDirectorIds());

        List<Cast> castList = this.findExistingCastsByIds(createMovieDto.getCastIds());

        String posterUrl = this.objectStorageService.upload("movies", poster);

        Movie newMovie = this.movieMapper.toMovie(createMovieDto);
        newMovie.setGenres(new HashSet<>(genreList));
        newMovie.setDirectors(new HashSet<>(directorList));
        newMovie.setCasts(new HashSet<>(castList));
        newMovie.setPoster(posterUrl);

        Movie createdMovie = this.movieRepository.save(newMovie);

        return this.movieMapper.toMovieResDto(createdMovie);
    }

    private void validateExistingMovieByTitleOrSlug(String title, String slug) throws ConflictException
    {
        if (this.movieRepository.existsByTitleIgnoreCaseOrSlugIgnoreCase(title, slug))
        {
            throw new ConflictException("movie with that name or slug already exist");
        }
    }

    @Override
    public MovieResDto updateJson(Long id, UpdateMovieJsonDto updateMovieJsonDto) throws NotFoundException
    {
        Movie existingMovie = this.findExistingMovieById(id);

        List<Genre> genreList = this.findExistingGenresByIds(updateMovieJsonDto.getGenreIds());

        List<Director> directorList = this.findExistingDirectorsByIds(updateMovieJsonDto.getDirectorIds());

        List<Cast> castList = this.findExistingCastsByIds(updateMovieJsonDto.getCastIds());

        existingMovie.setGenres(new HashSet<>(genreList));
        existingMovie.setDirectors(new HashSet<>(directorList));
        existingMovie.setCasts(new HashSet<>(castList));
        this.movieMapper.updateMovieFromDto(updateMovieJsonDto, existingMovie);

        Movie updatedMovie = this.movieRepository.save(existingMovie);

        return this.movieMapper.toMovieResDto(updatedMovie);
    }

    @Override
    @Transactional
    public MovieResDto updateForm(Long id, UpdateMovieFormDto updateMovieFormDto, MultipartFile poster) throws NotFoundException, CustomS3Exception
    {
        Movie existingMovie = this.findExistingMovieById(id);

        if (existingMovie.getPoster() != null && !existingMovie.getPoster().isEmpty())
        {
            ImageDeletePayload imageDeletePayload = new ImageDeletePayload(existingMovie.getPoster());

            this.applicationEventPublisher.publishEvent(new ImageDeleteEvent(imageDeletePayload));
        }


        List<Genre> genreList = this.findExistingGenresByIds(updateMovieFormDto.getGenreIds());

        List<Director> directorList = this.findExistingDirectorsByIds(updateMovieFormDto.getDirectorIds());

        List<Cast> castList = this.findExistingCastsByIds(updateMovieFormDto.getCastIds());

        String posterUrl = this.objectStorageService.upload("movies", poster);

        existingMovie.setGenres(new HashSet<>(genreList));
        existingMovie.setDirectors(new HashSet<>(directorList));
        existingMovie.setCasts(new HashSet<>(castList));
        existingMovie.setPoster(posterUrl);

        this.movieMapper.updateMovieFromDto(updateMovieFormDto, existingMovie);

        Movie updatedMovie = this.movieRepository.save(existingMovie);

        return this.movieMapper.toMovieResDto(updatedMovie);
    }

    private List<Genre> findExistingGenresByIds(Set<Long> genreIds) throws NotFoundException
    {
        List<Genre> genreList = this.genreRepository.findAllById(genreIds);

        if (genreList.size() != genreIds.size())
        {
            throw new NotFoundException("one or more genre not exist");
        }

        return genreList;
    }

    private List<Director> findExistingDirectorsByIds(Set<Long> directorIds) throws NotFoundException
    {
        List<Director> directorList = this.directorRepository.findAllById(directorIds);

        if (directorList.size() != directorIds.size())
        {
            throw new NotFoundException("one or more director not exist");
        }

        return directorList;
    }

    private List<Cast> findExistingCastsByIds(Set<Long> castIds) throws NotFoundException
    {
        List<Cast> castList = this.castRepository.findAllById(castIds);

        if (castList.size() != castIds.size())
        {
            throw new NotFoundException("one or more cast not exist");
        }

        return castList;
    }

    @Override
    @Transactional
    public MovieResDto hide(Long id) throws NotFoundException
    {
        Movie existingMovie = this.findExistingMovieById(id);

        existingMovie.hide();

        return this.movieMapper.toMovieResDto(existingMovie);
    }

    @Override
    @Transactional
    public MovieResDto show(Long id) throws NotFoundException
    {
        Movie existingMovie =
                this.movieRepository.findByIdAndIsDeletedTrue(id).orElseThrow(() -> new NotFoundException("movie not " +
                        "found"));

        existingMovie.show();

        return this.movieMapper.toMovieResDto(existingMovie);
    }

    @Override
    public MovieResDto delete(Long id) throws NotFoundException
    {
        Movie existingMovie = this.findExistingMovieById(id);

        ImageDeletePayload imageDeletePayload = new ImageDeletePayload(existingMovie.getPoster());
        this.applicationEventPublisher.publishEvent(new ImageDeleteEvent(imageDeletePayload));

        this.movieRepository.deleteById(id);

        return this.movieMapper.toMovieResDto(existingMovie);
    }


    @Override
    public MovieOffsetPaginationResDto findMany(int offset, int limit)
    {
        Page<Movie> moviePage =
                this.movieRepository.findByIsDeletedFalse(this.offsetPaginationService.calculatePageable(offset,
                        limit));

        List<MovieResDto> movieResDtoList =
                moviePage.getContent().stream().map(this.movieMapper::toMovieResDto).toList();

        OffsetPaginationApiMetaData offsetPaginationApiMetaData =
                this.offsetPaginationService.generateMetaData(moviePage);

        return new MovieOffsetPaginationResDto(movieResDtoList, offsetPaginationApiMetaData);
    }

    @Override
    public MovieResDto findById(Long id) throws NotFoundException
    {
        Movie existingMovie = this.findExistingMovieById(id);

        return this.movieMapper.toMovieResDto(existingMovie);
    }

    @Override
    public Movie findExistingMovieById(Long id) throws NotFoundException
    {
        return this.movieRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new NotFoundException("movie not " +
                "found"));
    }

    @Override
    public List<MovieTitleResDto> findAll()
    {
        List<Movie> movieList = this.movieRepository.findAll();

        return movieList.stream().map(this.movieMapper::toMovieTitleResDto).toList();
    }
}
