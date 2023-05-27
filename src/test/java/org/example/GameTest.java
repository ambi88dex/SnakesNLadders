package org.example;

import junit.framework.TestCase;

import java.util.Map;
import java.util.stream.Collectors;


public class GameTest extends TestCase {

    public void testInitializeGame() {
        Game currentGame = Game.initializeGame();
        Map<Integer, Integer> invalidLadders = currentGame.ladders.entrySet().stream()
                .filter(entry -> entry.getKey() >= entry.getValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        assertEquals(0, invalidLadders.size());

        Map<Integer, Integer> invalidSnakes = currentGame.snakes.entrySet().stream()
                .filter(entry -> entry.getKey() <= entry.getValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        assertEquals(0, invalidSnakes.size());
    }

    public void testMovementStrategySerialization() throws IllegalAccessException {
        Game currentGame = Game.initializeGame();
        assertEquals(MovementStrategy.SUM, currentGame.strategy);

    }
}