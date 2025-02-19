package com.test.game.testgame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewGameRequest {
    @NotNull
    @Min(1)
    @Max(30)
    private Integer width;

    @NotNull
    @Min(1)
    @Max(30)
    private Integer height;

    @NotNull
    @Min(1)
    @JsonProperty("mines_count")
    private Integer minesCount;

}
