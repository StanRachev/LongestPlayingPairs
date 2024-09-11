package com.academy.longestplayingpairs.api.controller;

import com.academy.longestplayingpairs.api.model.Match;
import com.academy.longestplayingpairs.api.model.Player;
import com.academy.longestplayingpairs.api.model.PlayerPair;
import com.academy.longestplayingpairs.api.model.Team;
import com.academy.longestplayingpairs.api.repository.MatchesRepository;
import com.academy.longestplayingpairs.api.repository.PlayersRepository;
import com.academy.longestplayingpairs.api.repository.TeamsRepository;
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

    private final MatchesRepository matchesRepository;
    private final TeamsRepository teamsRepository;
    private final PlayersRepository playersRepository;

    public DataController(TeamsCSVService teamsCSVService, PlayersCSVService playersCSVService,
                          MatchesCSVService matchesCSVService, RecordsCSVService recordsCSVService, PlayersService playersService,
                          MatchesRepository matchesRepository, TeamsRepository teamsRepository, PlayersRepository playersRepository) {
        this.teamsCSVService = teamsCSVService;
        this.playersCSVService = playersCSVService;
        this.matchesCSVService = matchesCSVService;
        this.recordsCSVService = recordsCSVService;
        this.playersService = playersService;
        this.matchesRepository = matchesRepository;
        this.teamsRepository = teamsRepository;
        this.playersRepository = playersRepository;
    }

    @GetMapping("/")
    public String uploadAll(@RequestParam(required = false) String dateFormat, RedirectAttributes redirectAttributes) {

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

            redirectAttributes.addFlashAttribute("warnings", warnings);

            return "redirect:/api/main";
        }
        redirectAttributes.addFlashAttribute("warning", "Files not found! Please upload them first.");

        return "redirect:/";
    }

    @GetMapping("/main")
    public String mainPage(Model model) {
        List<Match> matches = matchesRepository.findAll();
        List<Player> players = playersRepository.findAll();
        List<Team> teams = teamsRepository.findAll();

        model.addAttribute("matches", matches);
        model.addAttribute("players", players);
        model.addAttribute("teams", teams);

        if (!model.containsAttribute("warnings")) {
            model.addAttribute("warnings", new ArrayList<>());
        }

        return "index";
    }

    @GetMapping("/result")
    public String getDetails(Model model) {
        List<PlayerPair> pairs = playersService.getLongestPlayedPair();
        model.addAttribute("longestPairs", pairs);
        return "details";
    }
}
