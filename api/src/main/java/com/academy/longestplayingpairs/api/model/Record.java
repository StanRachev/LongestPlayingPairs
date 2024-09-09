package com.academy.longestplayingpairs.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "records", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id"})
})
@Getter @Setter @NoArgsConstructor
public class Record {

    @Id
    @NotNull
    private int id;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;
    @NotNull
    private int fromMinute;
    private int toMinute;

    public Record(int id, Player player, Match match, int fromMinute, int toMinute) {
        this.id = id;
        this.player = player;
        this.match = match;
        this.fromMinute = fromMinute;
        this.toMinute = toMinute;
    }
}
