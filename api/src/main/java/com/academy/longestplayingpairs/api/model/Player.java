package com.academy.longestplayingpairs.api.model;

import com.academy.longestplayingpairs.api.model.enums.PlayerPosition;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "players", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id", "full_name"})
})
@Getter @Setter @NoArgsConstructor
public class Player {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @Column(name = "team_number")
    private int teamNumber;
    @NotNull
    @Column(name = "position")
    private PlayerPosition position;
    @NotNull
    @Column(name = "full_name")
    String fullName;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "player")
    private List<Record> records;

    public Player(int id, int teamNumber, PlayerPosition position, String fullName, Team team) {
        this.id = id;
        this.teamNumber = teamNumber;
        this.position = position;
        this.fullName = fullName;
        this.team = team;
    }
}
