package com.mick.recommendation.dto;

import java.util.List;

public record MovieSummary(Integer movieId,
                           String title,
                           Integer runtime,
                           Integer voteCount,
                           String releaseDate,
                           List<String> genres,
                           String posterPath) {
}
