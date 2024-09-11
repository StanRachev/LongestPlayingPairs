package com.academy.longestplayingpairs.api.service.interfaces;

import com.academy.longestplayingpairs.api.model.Team;

public interface TeamServiceI {

    public String createTeam(Team team);
    public String editTeam(int id, Team team);
}
