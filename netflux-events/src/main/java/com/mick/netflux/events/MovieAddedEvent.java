package com.mick.netflux.events;

import java.time.Instant;
import java.util.List;

public record MovieAddedEvent(Integer movieId,
                              String title,
                              Integer runtime,
                              Integer voteCount,
                              String releaseDate,
                              List<String> genres,
                              String posterPath,
                              Instant addedAt) {
}