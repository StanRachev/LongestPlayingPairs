package com.academy.longestplayingpairs.api.service;

import com.academy.longestplayingpairs.api.model.Match;
import com.academy.longestplayingpairs.api.model.Player;
import com.academy.longestplayingpairs.api.model.PlayerPair;
import com.academy.longestplayingpairs.api.model.Record;
import com.academy.longestplayingpairs.api.repository.PlayersRepository;
import com.academy.longestplayingpairs.api.repository.RecordsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// In this class contains the algorithm for finding the longest playing pair/s
// Algorithm steps:

// 1. Entry point: getLongestPlayedPair().
// 2. Fetching all the players and records from the repository.
// 3. Comparing each pair from same teams.
// 4. Finding the records for each player.
// 5. Get the total playing time for the pair and the matches they've participated in
// and adding them to PlayerPair object with totalTimePlayedWithMatches().
// 6. Identifying pairs with the longest time with findLongestPlayingPairs().

@Service
public class PlayersService {

    private final PlayersRepository playersRepository;
    private final RecordsRepository recordsRepository;

    public PlayersService(PlayersRepository playersRepository, RecordsRepository recordsRepository) {
        this.playersRepository = playersRepository;
        this.recordsRepository = recordsRepository;
    }

    // This method finds all the same team pairs, which played together
    // Returns a list with the longest playing pairs in case there is more than one pair
    public List<PlayerPair> getLongestPlayedPair() {
        List<Player> players = playersRepository.findAll();

        List<Record> recordsPlayerOne;
        List<Record> recordsPlayerTwo;

        List<PlayerPair> playerPairsTimes = new ArrayList<>();

        Player playerOne;
        Player playerTwo;

        for (int i = 0; i < players.size(); i++) {
            playerOne = players.get(i);
            for (int j = i + 1; j < players.size(); j++) {
                playerTwo = players.get(j);

                // players from the same team only
                if (playerOne.getTeam().getId() != playerTwo.getTeam().getId()) {
                    continue;
                }

                recordsPlayerOne = playerOne.getRecords();
                recordsPlayerTwo = playerTwo.getRecords();

                // get total time for the pair + all the matches they participated in together and their times
                PlayerPair pair = totalTimePlayedWithMatches(playerOne, playerTwo, recordsPlayerOne, recordsPlayerTwo);
                playerPairsTimes.add(pair);
            }
        }

        return findLongestPlayingPairs(playerPairsTimes);
    }

    // Finds the longest playing pairs out of all the pairs which played together
    private List<PlayerPair> findLongestPlayingPairs(List<PlayerPair> pairs) {

        List<PlayerPair> playersLongestTime = new ArrayList<>();

        int maxTime = 0;
        for (PlayerPair pair : pairs) {
            int totalTimePair = pair.getTotalTime();
            if (maxTime < totalTimePair) {
                maxTime = totalTimePair;
            }
        }

        for (PlayerPair pair : pairs) {
            if (pair.getTotalTime() == maxTime) {
                playersLongestTime.add(pair);
            }
        }

        return playersLongestTime;
    }

    // Creates a Pair object with Player 1, Player 2, the total time they've played together
    // and all the matches they played with the total time they've been of the field for each of them
    private PlayerPair totalTimePlayedWithMatches(Player playerOne, Player playerTwo, List<Record> playerOneRec, List<Record> playerTwoRec) {

        int playerOneFrom;
        int playerOneTo;
        int playerTwoFrom;
        int playerTwoTo;

        int togetherFrom;
        int togetherTo;

        Map<Match, Integer> matches = new HashMap<>();

        int total = 0;
        for (Record recordOne : playerOneRec) {
            for (Record recordTwo : playerTwoRec) {

                // For each match where both players have records
                if (recordOne.getMatch().getId() == recordTwo.getMatch().getId()) {
                    playerOneFrom = recordOne.getFromMinute();
                    playerOneTo = recordOne.getToMinute();
                    playerTwoFrom = recordTwo.getFromMinute();
                    playerTwoTo = recordTwo.getToMinute();

                    togetherFrom = Math.max(playerOneFrom, playerTwoFrom);
                    togetherTo = Math.min(playerOneTo, playerTwoTo);

                    // Calculate the overlapping time
                    if (togetherFrom < togetherTo) {
                        int totalCurrMatch = togetherTo - togetherFrom;
                        total += totalCurrMatch;

                        matches.put(recordOne.getMatch(), totalCurrMatch);
                    }
                }
            }
        }
        return new PlayerPair(playerOne, playerTwo, total, matches);
    }
}