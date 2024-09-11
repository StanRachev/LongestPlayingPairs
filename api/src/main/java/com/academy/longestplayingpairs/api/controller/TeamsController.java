package com.academy.longestplayingpairs.api.controller;

import com.academy.longestplayingpairs.api.model.Team;
import com.academy.longestplayingpairs.api.model.enums.Group;
import com.academy.longestplayingpairs.api.repository.TeamsRepository;
import com.academy.longestplayingpairs.api.service.TeamsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/teams")
public class TeamsController {

    private final TeamsService teamsService;

    public TeamsController(TeamsRepository teamsRepository, TeamsService teamsService) {
        this.teamsService = teamsService;
    }

    @GetMapping("/addTeams")
    public String getTeamForm(Model model) {
        model.addAttribute("team", new Team());
        model.addAttribute("groups", Group.values());
        return "addTeam";
    }

    @PostMapping("/add")
    public String createTeam(@ModelAttribute Team team, RedirectAttributes redirectAttributes) {
        String notification = teamsService.createTeam(team);

        redirectAttributes.addFlashAttribute("warnings", notification);

        return "redirect:/api/main";
    }

    @GetMapping("/remove/{id}")
    public String removeTeam(@PathVariable("id") int id) {
        teamsService.removeTeam(id);
        return "redirect:/api/main";
    }

    @PostMapping("/edit/{id}")
    public String editTeam(@PathVariable("id") int id, @ModelAttribute Team team, RedirectAttributes redirectAttributes) {
        String notification = teamsService.editTeam(id, team);

        redirectAttributes.addFlashAttribute("warnings", notification);

        return "redirect:/api/main";
    }

    @GetMapping("/edit/{id}")
    public String editTeamForm(@PathVariable("id") int id, Model model) {
        Team team = teamsService.findById(id); // Add this method in your service to fetch team by ID
        model.addAttribute("team", team);
        model.addAttribute("groups", Group.values());
        return "editTeam";
    }
}