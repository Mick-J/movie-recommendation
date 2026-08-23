package com.mick.movie.mapper;

import com.mick.movie.dto.MovieDetails;
import com.mick.movie.entity.Movie;
import com.mick.netflux.events.MovieAddedEvent;

import java.time.Instant;

// prefer MapStruct
public class MovieMapper {

    public static MovieDetails toMovieDetails(Movie movie) {
        return new MovieDetails(
                movie.getId(),
                movie.getTitle(),
                movie.getVoteAverage(),
                movie.getVoteCount(),
                movie.getReleaseDate(),
                movie.getRevenue(),
                movie.getRuntime(),
                movie.getBackdropPath(),
                movie.getBudget(),
                movie.getHomepage(),
                movie.getOverview(),
                movie.getPopularity(),
                movie.getPosterPath(),
                movie.getGenres()
        );
    }

    public static Movie toMovie(MovieDetails movieDetails) {
        var entity = new Movie();
        entity.setId(movieDetails.id());
        entity.setTitle(movieDetails.title());
        entity.setVoteAverage(movieDetails.voteAverage());
        entity.setVoteCount(movieDetails.voteCount());
        entity.setReleaseDate(movieDetails.releaseDate());
        entity.setRevenue(movieDetails.revenue());
        entity.setRuntime(movieDetails.runtime());
        entity.setBackdropPath(movieDetails.backdropPath());
        entity.setBudget(movieDetails.budget());
        entity.setHomepage(movieDetails.homepage());
        entity.setOverview(movieDetails.overview());
        entity.setPopularity(movieDetails.popularity());
        entity.setPosterPath(movieDetails.posterPath());
        entity.setGenres(movieDetails.genres());
        return entity;
    }

    public static MovieAddedEvent toMovieAddedEvent(Movie movie) {
        return new MovieAddedEvent(
                movie.getId(),
                movie.getTitle(),
                movie.getRuntime(),
                movie.getVoteCount(),
                movie.getReleaseDate(),
                movie.getGenres(),
                movie.getPosterPath(),
                Instant.now()
        );
    }

}
