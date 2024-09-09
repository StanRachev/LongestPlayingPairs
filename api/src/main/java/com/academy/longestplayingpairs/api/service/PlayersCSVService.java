package com.academy.longestplayingpairs.api.service;

import com.academy.longestplayingpairs.api.model.Player;
import com.academy.longestplayingpairs.api.model.Team;
import com.academy.longestplayingpairs.api.model.enums.PlayerPosition;
import com.academy.longestplayingpairs.api.repository.PlayersRepository;
import com.academy.longestplayingpairs.api.repository.TeamsRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PlayersCSVService {

    PlayersRepository playersRepository;
    TeamsRepository teamsRepository;

    private final String PATH_PLAYERS = "api/src/main/resources/upload-dir/players.csv";

    public PlayersCSVService(PlayersRepository playersRepository, TeamsRepository teamsRepository) {
        this.playersRepository = playersRepository;
        this.teamsRepository = teamsRepository;
    }

    public List<String> csvParse() {
        List<String> warnings = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_PLAYERS))) {
            Pattern pattern = Pattern.compile("^(?<id>([1-9][0-9]{0,3}))\\s*[,;-]\\s*(?<number>([1-9]|[1-9][0-9]))\\s*[,;-]\\s*(?<position>[A-Z][A-Z])\\s*[,;-]\\s*(?<name>(([\\p{L}\\s'-]+)(\\p{Zs}\\([a-z]+\\))?))\\s*[,;-]\\s*(?<teamid>([1-9]|[1-9][0-9]))\\s*$");

            int lineNum = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                ++lineNum;

                if (lineNum == 1) {
                    continue;
                }

                Matcher matcher = pattern.matcher(line);
                boolean matchFound = matcher.find();

                if (matchFound) {
                    int id = Integer.parseInt(matcher.group("id"));

                    if (playersRepository.existsById(id)) {
                        warnings.add("Entity already exists in the table. Row" + lineNum);
                    } else {
                        int number = Integer.parseInt(matcher.group("number"));
                        String position = matcher.group("position");
                        String name = matcher.group("name");
                        int teamId = Integer.parseInt(matcher.group("teamid"));

                        Player player = new Player();
                        player.setId(id);
                        player.setTeamNumber(number);
                        player.setPosition(PlayerPosition.valueOf(position));
                        player.setFullName(name);

                        Team team = teamsRepository.findById(teamId).orElse(null);

                        if (team != null) {
                            player.setTeam(team);
                            playersRepository.save(player);
                        } else {
                            warnings.add("Line" + lineNum + ": Team with id " + teamId + " is not found. Skipping the line.");
                        }
                    }
                } else {
                    warnings.add("Line" + lineNum + "doesn't match. Skipping the line.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return warnings;
    }
}
