package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.simple.parser.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class Game {

    int boardSize;

    int numberOfPlayers;

    int numberOfSnakes;
    int numberOfLadders;
    int numberOfDies;

    @JsonProperty("strategy")
    MovementStrategy strategy;

    Map<Integer, Integer> ladders;
    Map<Integer, Integer> snakes;

    int[] playerPosition;

    Map<Integer, Integer> positionToPlayerIdMap;

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getNumberOfDies() {
        return numberOfDies;
    }

    public void setNumberOfDies(int numberOfDies) {
        this.numberOfDies = numberOfDies;
    }

    public Map<Integer, Integer> getLadders() {
        return ladders;
    }

    public void setLadders(Map<Integer, Integer> ladder) {
        this.ladders = ladder;
    }

    public Map<Integer, Integer> getSnakes() {
        return snakes;
    }

    public void setSnakes(Map<Integer, Integer> snake) {
        this.snakes = snake;
    }

    public static Game initializeGame(){

//        logger.entering("Game","initialGame");
        System.out.println("--------------------------------------\n" +
                "------------------------------------------------" +
                "The game has initialized------------------------");
        JSONParser jsonParser = new JSONParser();
        Game currentGame;
        try(InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("configuration.json")){
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
            mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
            currentGame = mapper.readValue(in, Game.class);
            currentGame.playerPosition = new int[currentGame.numberOfPlayers];
            for(int i = 0 ; i < currentGame.numberOfPlayers; i++){
                currentGame.playerPosition[i]= 1;
            }
            currentGame.positionToPlayerIdMap = new HashMap<>();
            // if no key inside positionToPlayerIdMap means that the playerId is at position 1, which allows multiple positions.

            return currentGame;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void simulate() {
        int currentPlayer = 0;
        boolean winnerFound = false;
        int winnerId = -1;
        MovementStrategyGenerator dieGenerator = new MovementStrategyGenerator(strategy);
        while(!winnerFound){
            int dieOffset = dieGenerator.throwDies(numberOfDies);
            System.out.println("Player #" + currentPlayer + " has rolled the dies and has got number " + dieOffset + " to make move " + playerPosition[currentPlayer] + " -> " + (playerPosition[currentPlayer] + dieOffset));
            traverseBoard(currentPlayer, dieOffset);
            int toBeWinnerId = checkWinner();
            if( toBeWinnerId != -1){
                winnerFound = true;
                winnerId = toBeWinnerId;
            }
            currentPlayer++;
            currentPlayer %= numberOfPlayers;
        }

        System.out.println("The winner of the simulation is " + winnerId);

    }

    private void traverseBoard(int currentPlayer, int dieOffset) {
        int currentPlayerPosition = playerPosition[currentPlayer];
        int toBePosition = currentPlayerPosition + dieOffset;

        if(toBePosition > boardSize){
            return;
        }
        boolean isTraversalPending = true;

        while(isTraversalPending){
            isTraversalPending = false;
            if(ladders.containsKey(toBePosition)){
                System.out.println("Player #" + currentPlayer +
                        " caught a ladder and moved " + toBePosition + " -> " + ladders.get(toBePosition));
                toBePosition = ladders.get(toBePosition);
                isTraversalPending = true;
            }
            else if(snakes.containsKey(toBePosition)){
                System.out.println("Player #" + currentPlayer +
                        " caught a snakes and moved " + toBePosition + " -> " + snakes.get(toBePosition));
                toBePosition = snakes.get(toBePosition);
                isTraversalPending = true;
            }
        }
        resetExistingPlayerAtPosition(toBePosition);
        moveCurrentPlayerToNewPosition(currentPlayer, toBePosition);


    }

    private void moveCurrentPlayerToNewPosition(int currentPlayer, int toBePosition) {
        positionToPlayerIdMap.remove(playerPosition[currentPlayer]);

        playerPosition[currentPlayer] = toBePosition;
//        System.out.println("Moving Player #" + currentPlayer + " to position " + toBePosition);
        if(toBePosition > 1){
//            System.out.println("positionIdMap updated at index " + toBePosition +" , " + currentPlayer);
            positionToPlayerIdMap.put(toBePosition, currentPlayer);
        }
    }

    private void resetExistingPlayerAtPosition(int toBePosition) {

        if(!positionToPlayerIdMap.containsKey(toBePosition)){
            return ;
        }
        System.out.println("Player #" + positionToPlayerIdMap.get(toBePosition) +
                " is being overstepped and moved to place 1 " );
        int replacedPlayerId = positionToPlayerIdMap.get(toBePosition);
        playerPosition[replacedPlayerId] = 1;
        positionToPlayerIdMap.remove(toBePosition);
    }

    private int checkWinner() {
        for(int i = 0 ; i < numberOfPlayers; i++){
            if ( playerPosition[i] >= boardSize){
                return i;
            }
        }
        return -1;
    }
}
