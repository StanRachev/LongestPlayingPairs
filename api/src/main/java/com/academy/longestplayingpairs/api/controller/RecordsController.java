package com.academy.longestplayingpairs.api.controller;

import com.academy.longestplayingpairs.api.model.Record;
import com.academy.longestplayingpairs.api.repository.RecordsRepository;
import com.academy.longestplayingpairs.api.service.RecordsCSVService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/records")
public class RecordsController {

    private final RecordsRepository recordsRepository;
    private final RecordsCSVService recordsCSVService;

    public RecordsController(RecordsRepository recordsRepository, RecordsCSVService recordsCSVService) {
        this.recordsRepository = recordsRepository;
        this.recordsCSVService = recordsCSVService;
    }

    @GetMapping("/upload")
    public String csvToDB(Model model) {
        List<String> warnings = recordsCSVService.csvParse();
        model.addAttribute("warnings", warnings);
        return "records";
    }

    @GetMapping("/records")
    public String getRecords(Model model) {
        List<Record> records = recordsRepository.findAll();
        model.addAttribute("records", records);
        return "records";
    }
}
