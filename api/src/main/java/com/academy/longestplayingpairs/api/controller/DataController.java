package com.academy.longestplayingpairs.api.controller;

import com.academy.longestplayingpairs.api.model.Match;
import com.academy.longestplayingpairs.api.model.Player;
import com.academy.longestplayingpairs.api.model.PlayerPair;
import com.academy.longestplayingpairs.api.model.Team;
import com.academy.longestplayingpairs.api.repository.MatchesRepository;
import com.academy.longestplayingpairs.api.repository.PlayersRepository;
import com.academy.longestplayingpairs.api.repository.TeamsRepository;
import com.academy.longestplayingpairs.api.service.CSVParseService;
import com.academy.longestplayingpairs.api.service.PlayersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// Processes CSV files and stores them into the database.
// Displays the main page of the website with result from the algorithm
// and tables with teams, players and matches.

@Controller
@RequestMapping("/api")
public class DataController {

    private static final String FILE_PATH = "api/src/main/resources/test/";

    private final CSVParseService csvParseService;
    private final PlayersService playersService;
    private final MatchesRepository matchesRepository;
    private final TeamsRepository teamsRepository;
    private final PlayersRepository playersRepository;

    public DataController(CSVParseService csvParseService, PlayersService playersService, MatchesRepository matchesRepository,
                          TeamsRepository teamsRepository, PlayersRepository playersRepository) {
        this.csvParseService = csvParseService;
        this.playersService = playersService;
        this.matchesRepository = matchesRepository;
        this.teamsRepository = teamsRepository;
        this.playersRepository = playersRepository;
    }

    // Checks if the required files are uploaded
    // Assigns them to their appropriate service classes
    // Redirects to /api/main with warnings, if any
    @GetMapping("/")
    public String uploadAll(@RequestParam(required = false) String dateFormat, RedirectAttributes redirectAttributes) {

        File fileMatches = new File(FILE_PATH + "matches.csv");
        File filePlayers = new File(FILE_PATH + "players.csv");
        File fileTeams = new File(FILE_PATH + "teams.csv");
        File fileRecords = new File(FILE_PATH + "records.csv");

        if (fileMatches.exists() && filePlayers.exists() && fileTeams.exists() && fileRecords.exists()) {
            List<String> warnings = csvParseService.parseAllCSVs(dateFormat);

            redirectAttributes.addFlashAttribute("warnings", warnings);

            return "redirect:/api/main";
        }
        redirectAttributes.addFlashAttribute("warning", "Files not found! Please upload them first.");

        return "redirect:/";
    }

    // Finds the teams, players and matches from the DB and adds them to the model
    // Returns the main page - index.html
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

    // Finds the longest pairs that played together and adds them to the model
    // Returns details.html with the displayed results
    @GetMapping("/result")
    public String getDetails(Model model) {
        List<PlayerPair> pairs = playersService.getLongestPlayedPair();
        model.addAttribute("longestPairs", pairs);
        return "details";
    }
}