package com.academy.longestplayingpairs.api.service;

import com.academy.longestplayingpairs.api.model.Match;
import com.academy.longestplayingpairs.api.model.Team;
import com.academy.longestplayingpairs.api.repository.MatchesRepository;
import com.academy.longestplayingpairs.api.repository.TeamsRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MatchesCSVService {

    private final MatchesRepository matchesRepository;
    private final TeamsRepository teamsRepository;
    private final DateService dateService;

    private final String PATH_MATCHES = "api/src/main/resources/upload-dir/matches.csv";

    public MatchesCSVService(MatchesRepository matchesRepository, TeamsRepository teamsRepository, DateService dateService) {
        this.matchesRepository = matchesRepository;
        this.teamsRepository = teamsRepository;
        this.dateService = dateService;
    }

    public List<String> csvParse(String dateFormatOption) {
        List<String> warnings = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_MATCHES))) {
            Pattern pattern = Pattern.compile("^(?<id>([1-9][0-9]{0,3}))\\s*[,;-]\\s*(?<ateamid>([1-9]|1[0-9]|2[0-4]))\\s*[,;-]\\s*(?<bteamid>([1-9]|1[0-9]|2[0-4]))\\s*[,;-]\\s*(?<date>.+?)\\s*[,;-]\\s*(?<score>([0-9]\\s*(\\(\\s*[0-9]\\s*\\))?\\s*[-;.\\/ ]\\s*[0-9]\\s*(\\(\\s*[0-9]\\s*\\))?))\\s*$");

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

                    if (matchesRepository.existsById(id)) {
                        warnings.add("Entity already exists in the table. Row" + lineNum);
                    } else {
                        String dateStr = matcher.group("date").trim();

                        if (!dateService.isDateCorrect(dateFormatOption, dateStr)) {
                            warnings.add("Date is not correct on line " + lineNum);
                            continue;
                        }

                        LocalDate date = dateService.convertToLocalDate(dateFormatOption, dateStr);

                        int aTeamId = Integer.parseInt(matcher.group("ateamid"));
                        int bTeamId = Integer.parseInt(matcher.group("bteamid"));

                        Team teamA = teamsRepository.findById(aTeamId).orElse(null);
                        Team teamB = teamsRepository.findById(bTeamId).orElse(null);

                        if (teamA == null || teamB == null) {
                            warnings.add("Line" + lineNum + ": Team with id " + aTeamId + " is not found. Skipping the line.");
                            continue;
                        }

                        String score = matcher.group("score");

                        Match match = new Match();

                        match.setId(id);
                        match.setDate(date);
                        match.setATeamID(teamA);
                        match.setBTeamID(teamB);
                        match.setScore(score);

                        matchesRepository.save(match);
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