package com.academy.longestplayingpairs.api.repository;

import com.academy.longestplayingpairs.api.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamsRepository extends JpaRepository<Team, Integer> {
}