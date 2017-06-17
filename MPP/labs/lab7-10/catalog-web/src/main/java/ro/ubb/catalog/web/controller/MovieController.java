package ro.ubb.catalog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ubb.catalog.core.model.Movie;
import ro.ubb.catalog.core.service.MovieService;
import ro.ubb.catalog.web.converter.MovieConverter;
import ro.ubb.catalog.web.dto.EmptyJsonResponse;
import ro.ubb.catalog.web.dto.MovieDto;
import ro.ubb.catalog.web.dto.MoviesDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Nicu on 4/9/17.
 */
@RestController
public class MovieController {

    private static final Logger log = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieConverter movieConverter;

    @RequestMapping(value="/movies", method = RequestMethod.GET)
    public MoviesDto getMovies() {
        log.trace("getMovies --- method entered");

        List<Movie> movies = movieService.findAll();

        log.trace("getMovies: movies={}", movies);

        return new MoviesDto(movieConverter.convertModelsToDtos(movies));
    }

    @RequestMapping(value = "/movies/{movieId}", method = RequestMethod.PUT)
    public Map<String, MovieDto> updateMovie(
            @PathVariable final Long movieId,
            @RequestBody final Map<String, MovieDto> movieDtoMap) {

        log.trace("updateMovie: movieId={}, movieDtoMap={}", movieId, movieDtoMap);

        MovieDto movieDto = movieDtoMap.get("movie");
        Movie movie = movieService.updateMovie(movieId, movieDto.getName(), movieDto.getDirector(), movieDto.getGenre(), movieDto.getAvailableCopies(), movieDto.getClients());

        Map<String, MovieDto> result = new HashMap<>();
        result.put("movie",movieConverter.convertModelToDto(movie));

        log.trace("updateMovie: result={}",result);

        return result;
    }

    @RequestMapping(value = "/movies", method = RequestMethod.POST)
    public Map<String, MovieDto> createMovie(
            @RequestBody final Map<String, MovieDto> movieDtoMap) {
        log.trace("createMovie: movieId={}, movieDtoMap={}",movieDtoMap);

        MovieDto movieDto = movieDtoMap.get("movie");
        Movie movie = movieService.createMovie(movieDto.getName(), movieDto.getDirector(), movieDto.getGenre(), movieDto.getAvailableCopies());

        Map<String, MovieDto> result = new HashMap<>();
        result.put("movie",movieConverter.convertModelToDto(movie));

        log.trace("createMovie: result={}",result);

        return result;
    }

    @RequestMapping(value = "/movies/{movieId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteMovie(@PathVariable final Long movieId) {

        log.trace("deleteMovie: movieId={}", movieId);

        movieService.deleteMovie(movieId);

        log.trace("deleteMovie: - method end");

        return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
    }
}