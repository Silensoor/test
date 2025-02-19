package com.test.game.testgame.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GameInfoResponse {
    @JsonProperty("game_id")
    private String gameId;
    private int width;
    private int height;
    @JsonProperty("mines_count")
    private int minesCount;
    private boolean completed;
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private String[][] field;
}
