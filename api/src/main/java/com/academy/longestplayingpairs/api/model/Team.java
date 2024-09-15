package com.academy.longestplayingpairs.api.model;

import com.academy.longestplayingpairs.api.model.enums.Group;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "teams", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id", "name", "manager_full_name"})
})
@Getter @Setter @NoArgsConstructor
public class Team {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @Column(name = "name")
    private String name;
    @NotNull
    @Column(name = "manager_full_name")
    private String managerFullName;
    @NotNull
    @Column(name = "group_f")
    private Group groupF;
    
    @OneToMany(mappedBy = "team")
    private List<Player> players;

    public Team(int id, String name, String managerFullName, Group groupF, List<Player> players) {
        this.id = id;
        this.name = name;
        this.managerFullName = managerFullName;
        this.groupF = groupF;
        this.players = players;
    }
}