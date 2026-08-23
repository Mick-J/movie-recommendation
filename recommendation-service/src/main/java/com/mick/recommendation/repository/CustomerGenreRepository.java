package com.mick.recommendation.repository;

import com.mick.recommendation.entity.CustomerGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerGenreRepository extends JpaRepository<CustomerGenre, Integer> {
}
