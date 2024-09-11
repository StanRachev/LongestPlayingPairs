package com.academy.longestplayingpairs.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter @Setter @NoArgsConstructor
public class PlayerPair {

    private Player playerOne;
    private Player playerTwo;
    private int totalTime;
    private Map<Match, Integer> matchesTotalTime;

    public PlayerPair(Player playerOne, Player playerTwo, int totalTime, Map<Match, Integer> matchesTotalTime) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.totalTime = totalTime;
        this.matchesTotalTime = matchesTotalTime;
    }
}
