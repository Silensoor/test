package com.test.game.testgame;

import com.test.game.testgame.component.GameHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
public class TestGameApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestGameApplication.class, args);
    }

    @Bean
    public RouterFunction<ServerResponse> routes(GameHandler gameHandler) {
        return route(POST("/api/new"), gameHandler::startNewGame)
                .andRoute(POST("/api/turn"), gameHandler::makeMove)
                .andRoute(RequestPredicates.GET("/api/game/{id}"), gameHandler::getGameState);
    }
}
