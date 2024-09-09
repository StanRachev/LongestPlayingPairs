package com.academy.longestplayingpairs.api.repository;

import com.academy.longestplayingpairs.api.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayersRepository extends JpaRepository<Player, Integer> {
}