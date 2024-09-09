package com.academy.longestplayingpairs.api.repository;

import com.academy.longestplayingpairs.api.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchesRepository extends JpaRepository<Match, Integer> {
}