package com.academy.longestplayingpairs.api.service;

import com.academy.longestplayingpairs.api.service.interfaces.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
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

    @Transactional
    public List<String> parseAllCSVs(String dateFormat) {
        List<String> warnings = new ArrayList<>();

        warnings.addAll(parseTeamsCSV());
        warnings.addAll(parsePlayersCSV());
        warnings.addAll(parseMatchesCSV(dateFormat));
        warnings.addAll(parseRecordsCSV());

        if (!warnings.isEmpty()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return warnings;
    }

    public List<String> parseTeamsCSV() {
        return teamsCSVService.csvParse();
    }

    public List<String> parsePlayersCSV() {
        return playersCSVService.csvParse();
    }

    public List<String> parseMatchesCSV(String dateFormat) {
        if (matchesCSVService instanceof MatchesCSVService) {
            matchesCSVService.setDateFormatOption(dateFormat);
        }
        return matchesCSVService.csvParse();
    }

    public List<String> parseRecordsCSV() {
        return recordsCSVService.csvParse();
    }
}