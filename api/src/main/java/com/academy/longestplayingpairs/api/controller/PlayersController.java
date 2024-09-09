package com.academy.longestplayingpairs.api.controller;

import com.academy.longestplayingpairs.api.model.Player;
import com.academy.longestplayingpairs.api.repository.PlayersRepository;
import com.academy.longestplayingpairs.api.service.PlayersCSVService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/players")
public class PlayersController {

    private final PlayersRepository playersRepository;
    private final PlayersCSVService playersCSVService;

    public PlayersController(PlayersRepository playersRepository, PlayersCSVService playersCSVService) {
        this.playersRepository = playersRepository;
        this.playersCSVService = playersCSVService;
    }

    @GetMapping("/upload")
    public String csvToDB(Model model) {
        List<String> warnings = playersCSVService.csvParse();
        model.addAttribute("warnings", warnings);
        return "players";
    }

    @GetMapping("/players")
    public String getTeams(Model model) {
        List<Player> players = playersRepository.findAll();
        model.addAttribute("players", players);
        return "players";
    }
}
