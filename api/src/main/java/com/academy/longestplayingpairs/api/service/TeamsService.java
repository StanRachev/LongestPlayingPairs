package com.academy.longestplayingpairs.api.service;

import com.academy.longestplayingpairs.api.model.Team;
import com.academy.longestplayingpairs.api.model.TeamValidator;
import com.academy.longestplayingpairs.api.repository.TeamsRepository;
import com.academy.longestplayingpairs.api.service.interfaces.TeamServiceI;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TeamsService implements TeamServiceI {

    private final TeamsRepository teamsRepository;

    public TeamsService(TeamsRepository teamsRepository) {
        this.teamsRepository = teamsRepository;
    }

    public String createTeam(Team team) {
        String notification;

        if(TeamValidator.isValid(team)) {
            notification = "Team successfully created!";
            teamsRepository.save(team);
        } else {
            notification = "Team is not created. Team or manager name not correct.";
        }

        return notification;
    }

    public String editTeam(int id, Team team) {
        Team newTeam = teamsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Team profile not found"));

        String notification;
        if(TeamValidator.isValid(team)) {

            newTeam.setName(team.getName());
            newTeam.setManagerFullName(team.getManagerFullName());
            newTeam.setGroupF(team.getGroupF());

            notification = "Team successfully edited!";
            teamsRepository.save(newTeam);
        } else {
            notification = "Team is not edited. Team or manager name is not correct.";
        }

        return notification;
    }

    public void removeTeam(int id) {
        teamsRepository.deleteById(id);
    }

    public Team findById(int id) {
        return teamsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Team profile not found"));
    }
}