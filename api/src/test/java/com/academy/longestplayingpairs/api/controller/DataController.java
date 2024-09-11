package com.academy.longestplayingpairs.api.controller;

import com.academy.longestplayingpairs.api.model.PlayerPair;
import com.academy.longestplayingpairs.api.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api")
public class DataController {

    private final TeamsCSVService teamsCSVService;
    private final PlayersCSVService playersCSVService;
    private final MatchesCSVService matchesCSVService;
    private final RecordsCSVService recordsCSVService;

    private final PlayersService playersService;

    public DataController(TeamsCSVService teamsCSVService, PlayersCSVService playersCSVService,
                          MatchesCSVService matchesCSVService, RecordsCSVService recordsCSVService, PlayersService playersService) {
        this.teamsCSVService = teamsCSVService;
        this.playersCSVService = playersCSVService;
        this.matchesCSVService = matchesCSVService;
        this.recordsCSVService = recordsCSVService;
        this.playersService = playersService;
    }

    @GetMapping("/uploadAll")
    public String uploadAll(@RequestParam("dateFormat") String dateFormat, Model model, RedirectAttributes redirectAttributes) {

        File fileMatch = new File("api/src/main/resources/test/matches.csv");
        File filePlayer = new File("api/src/main/resources/test/players.csv");
        File fileTeams = new File("api/src/main/resources/test/teams.csv");
        File fileRecords = new File("api/src/main/resources/test/records.csv");

        if (fileMatch.exists() && filePlayer.exists() && fileTeams.exists() && fileRecords.exists()) {
            List<String> teamsWarnings = teamsCSVService.csvParse();
            List<String> playersWarnings = playersCSVService.csvParse();
            List<String> matchesWarnings = matchesCSVService.csvParse(dateFormat);
            List<String> recordsWarnings = recordsCSVService.csvParse();

            List<String> warnings = new ArrayList<>();
            warnings.addAll(teamsWarnings);
            warnings.addAll(playersWarnings);
            warnings.addAll(matchesWarnings);
            warnings.addAll(recordsWarnings);

            model.addAttribute("warnings", warnings);

            return "index";
        }

        redirectAttributes.addFlashAttribute("warning", "Files not found! Please upload them first.");

        return "redirect:/";
    }

    @GetMapping("/result")
    public String getDetails(Model model) {
        List<PlayerPair> pairs = playersService.getLongestPlayedPair();

        model.addAttribute("longestPairs", pairs);
        return "details";
    }
}
