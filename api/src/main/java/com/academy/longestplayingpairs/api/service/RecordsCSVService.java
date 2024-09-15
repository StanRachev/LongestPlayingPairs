package com.academy.longestplayingpairs.api.service;

import com.academy.longestplayingpairs.api.model.Match;
import com.academy.longestplayingpairs.api.model.Player;
import com.academy.longestplayingpairs.api.model.Record;
import com.academy.longestplayingpairs.api.repository.MatchesRepository;
import com.academy.longestplayingpairs.api.repository.PlayersRepository;
import com.academy.longestplayingpairs.api.repository.RecordsRepository;
import com.academy.longestplayingpairs.api.service.interfaces.CSVParser;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Service for parsing CSV files to the DB

@Service
public class RecordsCSVService implements CSVParser {

    RecordsRepository recordsRepository;
    PlayersRepository playersRepository;
    MatchesRepository matchesRepository;

//    private final String PATH_RECORDS = "api/src/main/resources/upload-dir/records.csv";
    private final String PATH_RECORDS = "api/src/main/resources/test/records.csv";

    public RecordsCSVService(RecordsRepository recordsRepository, PlayersRepository playersRepository, MatchesRepository matchesRepository) {
        this.recordsRepository = recordsRepository;
        this.playersRepository = playersRepository;
        this.matchesRepository = matchesRepository;
    }

    // Uses pattern to validate the rows from the file
    // Returns a list with warnings if a row is unsuccessfully parsed

    @Override
    public List<String> csvParse() {
        List<String> warnings = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_RECORDS))) {
            Pattern pattern = Pattern.compile("^(?<id>([1-9][0-9]{0,3}))\\s*[,;-]\\s*(?<playerid>([1-9][0-9]{0,3}))\\s*[,;-]\\s*(?<matchid>([1-9][0-9]{0,1}))\\s*[,;-]\\s*(?<frommin>([0-9]|[1-8][0-9]|90))\\s*[,;-]\\s*(?<tomin>([1-9]|[1-8][0-9]|90|(?i)null))\\s*$");

            int lineNum = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                ++lineNum;

                // Skips first row
                if (lineNum == 1) {
                    continue;
                }

                Matcher matcher = pattern.matcher(line);
                boolean matchFound = matcher.find();

                if (matchFound) {
                    int id = Integer.parseInt(matcher.group("id"));

                    if (recordsRepository.existsById(id)) {
                        warnings.add("Record already exists in the table. Row" + lineNum);
                    } else {
                        int fromMin = Integer.parseInt(matcher.group("frommin"));
                        int toMin;
                        if (matcher.group("tomin").equalsIgnoreCase("null")) {
                            toMin = 90;
                        } else {
                            toMin = Integer.parseInt(matcher.group("tomin"));
                        }

                        if (fromMin > toMin) {
                            warnings.add("FromMinute cannot be bigger than ToMinute on line " + lineNum + " in Records");
                            continue;
                        }

                        int playerId = Integer.parseInt(matcher.group("playerid"));
                        Player player = playersRepository.findById(playerId).orElse(null);

                        int matchId = Integer.parseInt(matcher.group("matchid"));
                        Match match = matchesRepository.findById(matchId).orElse(null);

                        Record record = new Record();

                        record.setFromMinute(fromMin);
                        record.setToMinute(toMin);

                        if (player != null && match != null) {
                            record.setPlayer(player);
                            record.setMatch(match);
                        }
                        recordsRepository.save(record);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            warnings.add("File records.csv isn't found. Please upload it first!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return warnings;
    }
}
