package com.test.game.testgame.repository;

import com.test.game.testgame.model.Game;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameRepository extends ReactiveCrudRepository<Game, UUID> {
}
