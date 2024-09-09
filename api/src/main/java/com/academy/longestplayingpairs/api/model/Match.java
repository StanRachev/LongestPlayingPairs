package com.academy.longestplayingpairs.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "matches", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id"})
})
@Getter @Setter @NoArgsConstructor
public class Match {

    @Id
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "a_team_id")
    private Team ATeamID;

    @ManyToOne
    @JoinColumn(name = "b_team_id")
    private Team BTeamID;
    @NotNull
    private LocalDate date;
    @NotNull
    private String score;

    @OneToMany(mappedBy = "match")
    private List<Record> records;

    public Match(int id, Team ATeamID, Team BTeamID, LocalDate date, String score, List<Record> records) {
        this.id = id;
        this.ATeamID = ATeamID;
        this.BTeamID = BTeamID;
        this.date = date;
        this.score = score;
        this.records = records;
    }
}
