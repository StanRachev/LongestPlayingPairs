package com.academy.longestplayingpairs.api.service;

import com.academy.longestplayingpairs.api.service.interfaces.CSVParser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CSVParseService {

    private CSVParser csvParser;

    public CSVParseService(CSVParser csvParser) {
        this.csvParser = csvParser;
    }

    public List<String> parse() {
        return csvParser.csvParse();
    }
}