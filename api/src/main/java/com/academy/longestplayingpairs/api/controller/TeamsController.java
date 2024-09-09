package com.academy.longestplayingpairs.api.controller;

import com.academy.longestplayingpairs.api.model.Team;
import com.academy.longestplayingpairs.api.repository.TeamsRepository;
import com.academy.longestplayingpairs.api.service.TeamsCSVService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/teams")
public class TeamsController {

    private final TeamsRepository teamsRepository;
    private final TeamsCSVService teamsCSVService;

    public TeamsController(TeamsRepository teamsRepository, TeamsCSVService teamsCSVService) {
        this.teamsRepository = teamsRepository;
        this.teamsCSVService = teamsCSVService;
    }

    @GetMapping("/upload")
    public String csvToDB(Model model) {
        List<String> warnings = teamsCSVService.csvParse();
        model.addAttribute("warnings", warnings);
        return "teams";
    }

    @GetMapping("/teams")
    public String getTeams(Model model) {
        List<Team> teams = teamsRepository.findAll();
        model.addAttribute("teams", teams);
        return "teams";
    }
}
