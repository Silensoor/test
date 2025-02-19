package com.test.game.testgame.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.game.testgame.dto.GameInfoResponse;
import com.test.game.testgame.dto.GameTurnRequest;
import com.test.game.testgame.dto.NewGameRequest;
import com.test.game.testgame.model.Game;
import com.test.game.testgame.repository.GameRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final ObjectMapper objectMapper;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
        this.objectMapper = new ObjectMapper();
    }

    public Mono<GameInfoResponse> getGameState(UUID gameId) {
        return gameRepository.findById(gameId)
                .flatMap(game -> convertToResponse(game, false));
    }

    public Mono<GameInfoResponse> createNewGame(NewGameRequest request) {
        Integer width = request.getWidth();
        Integer height = request.getHeight();
        Integer minesCount = request.getMinesCount();

        if (width > 30 || height > 30) {
            return Mono.error(new IllegalArgumentException("Размер поля не может превышать 30x30"));
        }
        if (minesCount >= width * height) {
            return Mono.error(new IllegalArgumentException("Количество мин превышает допустимый предел"));
        }

        try {
            String[][] gameField = generateEmptyField(width, height);
            placeMines(gameField, width, height, minesCount);
            calculateNumbers(gameField, width, height);

            String[][] userField = generateEmptyField(width, height);

            Game game = new Game(
                    null, width, height, minesCount, false,
                    convertFieldToJson(gameField), convertFieldToJson(userField)
            );

            return gameRepository.save(game)
                    .flatMap(entity -> convertToResponse(entity, true));
        } catch (JsonProcessingException e) {
            return Mono.error(new RuntimeException("Ошибка сериализации игрового поля", e));
        }
    }

    public Mono<GameInfoResponse> makeMove(GameTurnRequest request) {
        return gameRepository.findById(UUID.fromString(request.getGameId()))
                .flatMap(game -> {
                    if (game.isCompleted()) {
                        return Mono.error(new IllegalStateException("Игра уже завершена!"));
                    }
                    return processMove(game, request.getRow(), request.getCol());
                });
    }

    private Mono<GameInfoResponse> processMove(Game game, int row, int col) {
        try {
            String[][] gameField = convertJsonToField(game.getGameField()); // Минное поле
            String[][] userField = convertJsonToField(game.getUserField()); // Поле ходов игрока

            if (!userField[row][col].equals(" ")) {
                return Mono.error(new IllegalArgumentException("Ячейка уже открыта!"));
            }

            String value = gameField[row][col];
            userField[row][col] = value;

            if (value.equals("M")) {
                game.setCompleted(true);
                revealAllMines(userField, gameField);
            } else if (value.equals("0")) {
                openEmptyCells(userField, gameField, row, col);
            }

            if (checkVictory(userField, gameField)) {
                game.setCompleted(true);
                revealMinesOnVictory(userField, gameField);
            }

            game.setUserField(convertFieldToJson(userField));
            return gameRepository.save(game).flatMap(entity -> convertToResponse(entity, false));
        } catch (JsonProcessingException e) {
            return Mono.error(new RuntimeException("Ошибка обработки игрового поля", e));
        }
    }

    private void revealMinesOnVictory(String[][] userField, String[][] gameField) {
        for (int row = 0; row < userField.length; row++) {
            for (int col = 0; col < userField[row].length; col++) {
                if (gameField[row][col].equals("M")) {
                    userField[row][col] = "M";
                }
            }
        }
    }

    private boolean checkVictory(String[][] userField, String[][] gameField) {
        for (int row = 0; row < userField.length; row++) {
            for (int col = 0; col < userField[row].length; col++) {
                if (!gameField[row][col].equals("M") && userField[row][col].equals(" ")) {
                    return false;
                }
            }
        }
        return true;
    }


    private void openEmptyCells(String[][] userField, String[][] gameField, int startRow, int startCol) {
        int height = userField.length;
        int width = userField[0].length;

        int[] dR = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dC = {-1, 0, 1, -1, 1, -1, 0, 1};

        Queue<int[]> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.add(new int[]{startRow, startCol});


        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int row = cell[0];
            int col = cell[1];

            if (row < 0 || row >= height || col < 0 || col >= width || visited.contains(row + "," + col)) {
                continue;
            }

            visited.add(row + "," + col);

            String value = gameField[row][col];
            userField[row][col] = value;

            // Если вокруг нет мин ("0"), добавляем всех соседей в очередь
            if (value.equals("0")) {
                for (int i = 0; i < 8; i++) {
                    int newRow = row + dR[i];
                    int newCol = col + dC[i];

                    String key = newRow + "," + newCol;
                    if (newRow >= 0 && newRow < height && newCol >= 0 && newCol < width && !visited.contains(key)) {
                        queue.add(new int[]{newRow, newCol});
                    }
                }
            }
        }
    }

    private void revealAllMines(String[][] userField, String[][] gameField) {
        for (int row = 0; row < userField.length; row++) {
            for (int col = 0; col < userField[row].length; col++) {
                if (gameField[row][col].equals("M")) {
                    userField[row][col] = "X";
                }
            }
        }
    }

    private String convertFieldToJson(String[][] field) throws JsonProcessingException {
        return objectMapper.writeValueAsString(field);
    }

    private String[][] convertJsonToField(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, String[][].class);
    }

    private String[][] generateEmptyField(int width, int height) {
        String[][] field = new String[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                field[i][j] = " ";
            }
        }
        return field;
    }

    private void placeMines(String[][] field, int width, int height, int minesCount) {
        Random random = new Random();
        Set<String> mines = new HashSet<>();

        while (mines.size() < minesCount) {
            int row = random.nextInt(height);
            int col = random.nextInt(width);
            String key = row + "," + col;

            if (!mines.contains(key)) {
                mines.add(key);
                field[row][col] = "M";
            }
        }
    }

    private void calculateNumbers(String[][] field, int width, int height) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (!field[row][col].equals("M")) {
                    field[row][col] = String.valueOf(countMinesAround(field, row, col));
                }
            }
        }
    }

    private Mono<GameInfoResponse> convertToResponse(Game game, boolean isHideMines) {
        try {
            String[][] fields = convertJsonToField(game.getUserField());

            return Mono.just(new GameInfoResponse(
                    game.getId().toString(),
                    game.getWidth(),
                    game.getHeight(),
                    game.getMinesCount(),
                    game.isCompleted(),
                    isHideMines ? generateEmptyField(game.getWidth(), game.getHeight()) : fields
            ));
        } catch (JsonProcessingException e) {
            return Mono.error(new RuntimeException("Ошибка при обработке игрового поля", e));
        }
    }

    private int countMinesAround(String[][] field, int row, int col) {
        int count = 0;
        int[] dR = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dC = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int newRow = row + dR[i];
            int newCol = col + dC[i];

            if (newRow >= 0 && newRow < field.length && newCol >= 0 && newCol < field[0].length) {
                if (field[newRow][newCol].equals("M")) {
                    count++;
                }
            }
        }
        return count;
    }
}
