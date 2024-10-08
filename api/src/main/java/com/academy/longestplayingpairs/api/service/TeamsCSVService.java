package com.academy.longestplayingpairs.api.service;

import com.academy.longestplayingpairs.api.model.Team;
import com.academy.longestplayingpairs.api.model.enums.Group;
import com.academy.longestplayingpairs.api.repository.TeamsRepository;
import com.academy.longestplayingpairs.api.service.interfaces.CSVParser;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Service for parsing CSV files to the DB

@Service
public class TeamsCSVService implements CSVParser {

    TeamsRepository teamsRepository;

//    private final String PATH_TEAMS = "api/src/main/resources/upload-dir/teams.csv";
    private final String PATH_TEAMS = "api/src/main/resources/test/teams.csv";

    public TeamsCSVService(TeamsRepository teamsRepository) {
        this.teamsRepository = teamsRepository;
    }

    // Uses pattern to validate the rows from the file
    // Returns a list with warnings if a row is unsuccessfully parsed

    @Override
    public List<String> csvParse() {
        List<String> warnings = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_TEAMS))) {
            Pattern pattern = Pattern.compile("(?<id>\\d+)\\s*[,;-]\\s*(?<name>([A-Z][a-z]+\\s+){0,2}[A-Z][a-z]+)\\s*[,;-]\\s*(?<manager>(([\\p{L}]+\\s+){0,5}[\\p{L}]+|[a-z]+)(?: \\([a-z]+\\))?)\\s*[,;-]\\s*(?<group>[A-Za-z])");

            int lineNum = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                ++lineNum;

                // Skips first row of the file
                if (lineNum == 1) {
                    continue;
                }

                Matcher matcher = pattern.matcher(line);
                boolean matchFound = matcher.find();

                if (matchFound) {
                    int id = Integer.parseInt(matcher.group("id"));

                    if (teamsRepository.existsById(id)) {
                        warnings.add("Team already exists in the table. Row" + lineNum);
                    } else {

                        String name = matcher.group("name");
                        String manager = matcher.group("manager");
                        String group = matcher.group("group").toUpperCase();

                        Team team = new Team();
                        team.setName(name);
                        team.setManagerFullName(manager);
                        team.setGroupF(Group.valueOf(group));

                        teamsRepository.save(team);
                    }
                } else {
                    warnings.add("Line " + lineNum + " doesn't match in Teams.");
                }
            }
        } catch (IOException e) {
            warnings.add("File teams.csv not found. Please upload it first!");
        }
        return warnings;
    }
}