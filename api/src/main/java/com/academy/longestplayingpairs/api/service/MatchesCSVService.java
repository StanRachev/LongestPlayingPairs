package com.academy.longestplayingpairs.api.service;

import com.academy.longestplayingpairs.api.model.Match;
import com.academy.longestplayingpairs.api.model.Team;
import com.academy.longestplayingpairs.api.repository.MatchesRepository;
import com.academy.longestplayingpairs.api.repository.TeamsRepository;
import com.academy.longestplayingpairs.api.service.interfaces.CSVParser;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Service for parsing CSV files to the DB

@Service
public class MatchesCSVService implements CSVParser {

    private final MatchesRepository matchesRepository;
    private final TeamsRepository teamsRepository;
    private final DateService dateService;

//    private final String PATH_MATCHES = "api/src/main/resources/upload-dir/matches.csv";
    private final String PATH_MATCHES = "api/src/main/resources/test/matches.csv";

    private String dateFormatOption;

    public MatchesCSVService(MatchesRepository matchesRepository, TeamsRepository teamsRepository, DateService dateService) {
        this.matchesRepository = matchesRepository;
        this.teamsRepository = teamsRepository;
        this.dateService = dateService;
    }

    // CSV parsing to DB
    // Uses pattern to check if the rows are correctly formated
    // Parses the date to LocalDate object
    // Returns list with warnings if a row is unsuccessfully parsed

    @Override
    public void setDateFormatOption(String dateFormatOption) {
        this.dateFormatOption = dateFormatOption;
    }

    @Override
    public List<String> csvParse() {
        List<String> warnings = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_MATCHES))) {
            Pattern pattern = Pattern.compile("^(?<id>([1-9][0-9]{0,3}))\\s*[,;-]\\s*(?<ateamid>([1-9]|1[0-9]|2[0-4]))\\s*[,;-]\\s*(?<bteamid>([1-9]|1[0-9]|2[0-4]))\\s*[,;-]\\s*(?<date>.+?)\\s*[,;-]\\s*(?<score>([0-9]\\s*(\\(\\s*[0-9]\\s*\\))?\\s*[-;.\\/ ]\\s*[0-9]\\s*(\\(\\s*[0-9]\\s*\\))?))\\s*$");

            int lineNum = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                ++lineNum;

                // Skips first row with headers
                if (lineNum == 1) {
                    continue;
                }

                Matcher matcher = pattern.matcher(line);
                boolean matchFound = matcher.find();

                if (matchFound) {
                    int id = Integer.parseInt(matcher.group("id"));

                    if (matchesRepository.existsById(id)) {
                        warnings.add("Match already exists in the table. Row" + lineNum);
                    } else {
                        String dateStr = matcher.group("date").trim();

                        if (!dateService.isValidDateFormat(dateFormatOption, dateStr)) {
                            warnings.add("Date is not correct on line " + lineNum);
                            continue;
                        }

                        // Converts the date String to a LocalDate object
                        LocalDate date = dateService.convertToLocalDate(dateFormatOption, dateStr);

                        int aTeamId = Integer.parseInt(matcher.group("ateamid"));
                        int bTeamId = Integer.parseInt(matcher.group("bteamid"));

                        Team teamA = teamsRepository.findById(aTeamId).orElse(null);
                        Team teamB = teamsRepository.findById(bTeamId).orElse(null);

                        if (teamA == null || teamB == null) {

                            int teamId;
                            if (teamA == null) {
                                teamId = aTeamId;
                            } else {
                                teamId = bTeamId;
                            }
                            warnings.add("Line" + lineNum + ": Team with id " + teamId + " is not found in Matches.");
                            continue;
                        }

                        String score = matcher.group("score");

                        Match match = new Match();

                        match.setDate(date);
                        match.setATeamID(teamA);
                        match.setBTeamID(teamB);
                        match.setScore(score);

                        matchesRepository.save(match);
                    }
                } else {
                    warnings.add("Line " + lineNum + " in Matches isn't correct.");
                }
            }
        } catch (IOException e) {
            warnings.add("File matches.csv isn't found. Please upload it first!");
        }
        return warnings;
    }
}