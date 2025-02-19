package com.test.game.testgame.component;

import com.test.game.testgame.dto.ErrorResponse;
import com.test.game.testgame.dto.GameTurnRequest;
import com.test.game.testgame.dto.NewGameRequest;
import com.test.game.testgame.service.GameService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;


@Component
public class GameHandler {
    private final GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Mono<ServerResponse> startNewGame(ServerRequest request) {
        return request.bodyToMono(NewGameRequest.class)
                .flatMap(gameService::createNewGame)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(response))
                .onErrorResume(e -> ServerResponse.badRequest()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(new ErrorResponse(e.getMessage())));
    }

    public Mono<ServerResponse> makeMove(ServerRequest request) {
        return request.bodyToMono(GameTurnRequest.class)
                .flatMap(gameService::makeMove)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(response))
                .onErrorResume(e -> ServerResponse.badRequest()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(new ErrorResponse(e.getMessage())));
    }

    public Mono<ServerResponse> getGameState(ServerRequest request) {
        UUID gameId;
        try {
            gameId = UUID.fromString(request.pathVariable("id"));
        } catch (IllegalArgumentException e) {
            return ServerResponse.badRequest()
                    .contentType(APPLICATION_JSON)
                    .bodyValue(new ErrorResponse("Некорректный формат game_id"));
        }

        return gameService.getGameState(gameId)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(response))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
