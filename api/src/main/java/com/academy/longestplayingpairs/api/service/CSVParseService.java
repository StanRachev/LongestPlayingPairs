package com.academy.longestplayingpairs.api.service;

import com.academy.longestplayingpairs.api.service.interfaces.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CSVParseService {

    private final CSVParser teamsCSVService;
    private final CSVParser playersCSVService;
    private final CSVParser matchesCSVService;
    private final CSVParser recordsCSVService;

    @Autowired
    public CSVParseService(CSVParser teamsCSVService, CSVParser playersCSVService,
                           CSVParser matchesCSVService, CSVParser recordsCSVService) {
        this.teamsCSVService = teamsCSVService;
        this.playersCSVService = playersCSVService;
        this.matchesCSVService = matchesCSVService;
        this.recordsCSVService = recordsCSVService;
    }

    public List<String> parseTeamsCSV() {
        return teamsCSVService.csvParse();
    }

    public List<String> parsePlayersCSV() {
        return playersCSVService.csvParse();
    }

    public List<String> parseMatchesCSV(String dateFormat) {
        if (matchesCSVService instanceof MatchesCSVService) {
            ((MatchesCSVService) matchesCSVService).setDateFormatOption(dateFormat);
        }
        return matchesCSVService.csvParse();
    }

    public List<String> parseRecordsCSV() {
        return recordsCSVService.csvParse();
    }
}