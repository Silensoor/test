package com.test.game.testgame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameTurnRequest {
    @NotBlank
    @JsonProperty("game_id")
    private String gameId;

    @NotNull
    @Min(0)
    private Integer row;

    @NotNull
    @Min(0)
    private Integer col;
}
