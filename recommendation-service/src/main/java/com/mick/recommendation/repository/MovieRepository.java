package com.mick.recommendation.repository;

import com.mick.recommendation.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    // newly added recommendations
    List<Movie> findTop10ByOrderByCreatedAtDesc();

    // personalized
    @Query(value = """
            SELECT m.* FROM movie m
            JOIN customer_genre cg ON cg.customer_id = :customerId
            WHERE ARRAY_CONTAINS(m.genres, cg.favorite_genre)
            ORDER BY m.vote_count DESC
            LIMIT 10
            """, nativeQuery = true)
    List<Movie> findPersonalized(Integer customerId);

}
