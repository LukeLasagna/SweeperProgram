package org.sweep.solver;

import org.sweep.Main;
import org.sweep.game.SweeperGame;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntPredicate;

public class Solver {
    public static int[] allPossibleGames;


    public static void findAllPossibleSweeperGames() {
        int possible5x5Boards = (int) Math.pow(2,26)-1;

        int validBoardIndex = 0;
        int[] boards = new int[7696260];

        for (int i = 0; i < possible5x5Boards; i++) {
            int bombs = 0;
            boolean valid = true;
            for (int index = 0; index < 25; index++) {
                if(((i >> index) & 1) == 1) {
                    bombs++;
                    if(bombs>9) {
                        valid = false;
                        break;
                    }
                }
            }
            if(valid && bombs >= 4) {
                boards[validBoardIndex] = i;

                validBoardIndex++;
            }
        }

        System.out.println(validBoardIndex);

        allPossibleGames = boards;



    }

    public static double[][] getProbabilityMatrix(SweeperGame sweeperGame) {
        double[][] probabilityMatrix = new double[5][5];

        byte[] playerTileIndexes = sweeperGame.getPlayerTileCoordinates();

        byte above = -5;
        byte below = 5;
        byte left = -1;
        byte right = 1;

        int[] possibleBoards = Arrays.stream(allPossibleGames).filter(board -> {
            int i = -1;
            for (PlayerTile playerTile: sweeperGame.getPlayerTiles()) {
                i++;
                int index = playerTileIndexes[i];
                if((board >> index & 1) == 0) {
                    int bombs = 0;
                    //Not on top
                    if(index > 4) {
                        if((board >> (index+above) & 1) == 1) {
                            bombs++;
                        }
                    }
                    //Not on bottom
                    if(index < 20) {
                        if((board >> (index+below) & 1) == 1) {
                            bombs++;
                        }
                    }
                    //Not on the left
                    if(index % 5 != 0) {
                        if((board >> (index+left) & 1) == 1) {
                            bombs++;
                        }
                    }
                    //Not on right
                    if((index+1) % 5 != 0) {
                        if((board >> (index+right) & 1) == 1) {
                            bombs++;
                        }
                    }
                    if(playerTile.getHint() != bombs) {
                        return false;
                    }
                }else {
                    return false;
                }
            }
            return true;
        }).toArray();


        for (int x = 0; x < SweeperGame.BOARD_WIDTH; x++) {
            for (int y = 0; y < SweeperGame.BOARD_HEIGHT; y++) {
                int index = x+(y*5);
                int boardWhereBomb = 0;
                for (int board:possibleBoards) {
                    if((board >> index & 1) == 1) {
                        boardWhereBomb++;
                    }
                }
                probabilityMatrix[x][y] = (double) (boardWhereBomb) /possibleBoards.length;
            }
        }


        

        return probabilityMatrix;
    }
}
