package com.example.maze.walker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.example.maze.walker.Application.EMPTY_TILE;
import static com.example.maze.walker.Application.WALL_TILE;

public class DfsMazeGeneratorV2 implements MazeGenerator {

    private static final Random RANDOM = new Random();
    private static final int INITIAL_MAZE_FIELD = 9;

    @Override
    public Integer[][] generate(int height, int width) {
        Integer[][] result = new Integer[height][width];
        for (Integer[] integers : result) {
            Arrays.fill(integers, INITIAL_MAZE_FIELD);
        }

        dfs(result, 0, 0);

        for (Integer[] row : result) {
            for (int i = 0; i < row.length; i++) {
                if (row[i] == INITIAL_MAZE_FIELD) {
                    row[i] = WALL_TILE;
                }
            }
        }

        return result;
    }

    private void dfs(Integer[][] array, int x, int y) {
        array[y][x] = EMPTY_TILE;
        List<Direction> possibleDirections = hasNextStep(array, x, y);
        while (!possibleDirections.isEmpty()) {

            int randomIndex = RANDOM.nextInt(0, possibleDirections.size());
            Direction randomDirection = possibleDirections.get(randomIndex);

            array[y + randomDirection.getY()][x + randomDirection.getX()] = EMPTY_TILE;
            array[y + 2 * randomDirection.getY()][x + 2 * randomDirection.getX()] = EMPTY_TILE;

            dfs(array, x + 2 * randomDirection.getX(), y + 2 * randomDirection.getY());
            possibleDirections = hasNextStep(array, x, y);
        }
    }

    private List<Direction> hasNextStep(Integer[][] array, int x, int y) {
        List<Direction> result = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            if (inScope(array, x + 2 * direction.getX(), y + 2 * direction.getY())) {
                if (array[y + direction.getY()][x + direction.getX()] == INITIAL_MAZE_FIELD &&
                        array[y + 2 * direction.getY()][x + 2 * direction.getX()] == INITIAL_MAZE_FIELD) {
                    result.add(direction);
                }
            }
        }
        return result;

        /*return Arrays.stream(Direction.values())
                .filter(direction -> inScope(array, x + 2 * direction.getX(), y + 2 * direction.getY()))
                .filter(direction -> array[y + direction.getY()][x + direction.getX()] == INITIAL_MAZE_FIELD &&
                        array[y + 2 * direction.getY()][x + 2 * direction.getX()] == INITIAL_MAZE_FIELD)
                .collect(Collectors.toList());*/
    }

    private boolean inScope(Integer[][] array, int x, int y) {
        return !(x < 0 || x >= array[0].length || y < 0 || y >= array.length);
    }
}
