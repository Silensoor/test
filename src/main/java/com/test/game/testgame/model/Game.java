package com.test.game.testgame.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("games")
@AllArgsConstructor()
@Getter
@Setter
public class Game {
    @Id
    private UUID id;
    private int width;
    private int height;
    @Column("mines_count")
    private int minesCount;
    private boolean completed;
    @Column("game_field")
    private String gameField;
    @Column("user_field")
    private String userField;
}
