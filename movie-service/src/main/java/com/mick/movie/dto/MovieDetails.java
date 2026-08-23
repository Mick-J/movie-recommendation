package com.mick.movie.dto;

import java.util.List;

public record MovieDetails(Integer id,
                            String title,
                            Double voteAverage,
                            Integer voteCount,
                            String releaseDate,
                            Long revenue,
                            Integer runtime,
                            String backdropPath,
                            Long budget,
                            String homepage,
                            String overview,
                            Double popularity,
                            String posterPath,
                            List<String> genres) {
}
