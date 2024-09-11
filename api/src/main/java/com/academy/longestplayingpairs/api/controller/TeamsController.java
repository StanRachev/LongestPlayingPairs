package com.academy.longestplayingpairs.api.controller;

import com.academy.longestplayingpairs.api.model.Team;
import com.academy.longestplayingpairs.api.model.enums.Group;
import com.academy.longestplayingpairs.api.repository.TeamsRepository;
import com.academy.longestplayingpairs.api.service.TeamsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Controller to add and edit team records in DB

@Controller
@RequestMapping("/teams")
public class TeamsController {

    private final TeamsService teamsService;

    public TeamsController(TeamsRepository teamsRepository, TeamsService teamsService) {
        this.teamsService = teamsService;
    }

    // Sends an empty Team object and the values for the Group for a dropdown menu
    @GetMapping("/addTeams")
    public String getTeamForm(Model model) {
        model.addAttribute("team", new Team());
        model.addAttribute("groups", Group.values());
        return "addTeam";
    }

    // Validates the team and if correct, saves it to DB.
    // Returns a notification
    @PostMapping("/add")
    public String createTeam(@ModelAttribute Team team, RedirectAttributes redirectAttributes) {
        String notification = teamsService.createTeam(team);

        redirectAttributes.addFlashAttribute("warnings", notification);

        return "redirect:/api/main";
    }

    // Fetches the record from DB and populates the fields, which user will edit
    @GetMapping("/edit/{id}")
    public String editTeamForm(@PathVariable("id") int id, Model model) {
        Team team = teamsService.findById(id);
        model.addAttribute("team", team);
        model.addAttribute("groups", Group.values());
        return "editTeam";
    }

    // The edited team is sent to the teamService for validation and to be saved if correct
    // Returns a notification
    @PostMapping("/edit/{id}")
    public String editTeam(@PathVariable("id") int id, @ModelAttribute Team team, RedirectAttributes redirectAttributes) {
        String notification = teamsService.editTeam(id, team);

        redirectAttributes.addFlashAttribute("warnings", notification);

        return "redirect:/api/main";
    }
}