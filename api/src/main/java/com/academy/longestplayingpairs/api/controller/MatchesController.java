package com.academy.longestplayingpairs.api.controller;

import com.academy.longestplayingpairs.api.model.Match;
import com.academy.longestplayingpairs.api.repository.MatchesRepository;
import com.academy.longestplayingpairs.api.service.MatchesCSVService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/matches")
public class MatchesController {

    private final MatchesRepository matchesRepository;
    private final MatchesCSVService matchesCSVService;

    public MatchesController(MatchesRepository matchesRepository, MatchesCSVService matchesCSVService) {
        this.matchesRepository = matchesRepository;
        this.matchesCSVService = matchesCSVService;
    }

    @GetMapping("/selectDateFormat")
    public String showDateFormatSelection() {
        return "selectDateFormat"; // The name of your HTML file without the .html extension
    }

    @GetMapping("/upload")
    public String csvToDB(@RequestParam("date") String date, Model model) {
        List<String> warnings = matchesCSVService.csvParse(date);
        model.addAttribute("warnings", warnings);
        return "matches";
    }

    @GetMapping("/matches")
    public String getMatches(Model model) {
        List<Match> matches = matchesRepository.findAll();
        model.addAttribute("matches", matches);
        return "matches";
    }
}
