package org.sweep.game;

import org.sweep.MathUtils;
import org.sweep.solver.PlayerTile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SweeperGame {



    public final static byte BOARD_WIDTH = 5;

    public final static byte BOARD_HEIGHT = 5;


    Tile[][] board;

    byte[] startingPosition;

    int bet;



    final Random random = new Random();



    public SweeperGame(int bet, byte[] startingPosition, boolean blank) {
        this.bet = bet;
        this.startingPosition = startingPosition;
        board = new Tile[BOARD_WIDTH][BOARD_HEIGHT];

        if(!blank) {
            if(!MathUtils.performRandomCheck(1,200)) {
                for (int x = 0; x < BOARD_WIDTH; x++) {
                    for (int y = 0; y < BOARD_HEIGHT; y++) {
                        board[x][y] = new Tile();
                    }
                }


                byte BOMB_COUNT = MathUtils.randomNumberInRange(4,9);
                for (int i = 0; i < BOMB_COUNT; i++) {
                    Tile tile = getRandomTile();
                    while(tile.isBomb()) {
                        tile = getRandomTile();
                    }
                    tile.setBomb();
                }
            }else {
                for (int x = 0; x < BOARD_WIDTH; x++) {
                    for (int y = 0; y < BOARD_HEIGHT; y++) {
                        board[x][y] = new Tile(Tile.MONEY_BAG);
                    }
                }
            }
        }

    }

    public static SweeperGame fromByteArray(byte[] bytes) {
        SweeperGame sweeperGame = new SweeperGame(100000,new byte[2],true);

        for (int x = 0; x < BOARD_WIDTH; x++) {
            for (int y = 0; y < BOARD_HEIGHT; y++) {
                sweeperGame.board[x][y] = new Tile(Tile.MONEY_BAG);
            }
        }

        return null;

    }

    public byte[] getPlayerTileCoordinates() {
        byte[] bytes = new byte[25];
        int numberOfTiles = 0;

        for (Tile[] tiles:getBoard()) {
            for (Tile tile:tiles) {
                if(tile instanceof PlayerTile) {
                    byte[] cords = getCoordinatesOfTile(tile);
                    bytes[numberOfTiles] = (byte) (cords[0]+cords[1]*5);
                    numberOfTiles++;
                }
            }
        }

        return bytes;
    }

    public List<PlayerTile> getPlayerTiles() {

        List<PlayerTile> playerTiles = new ArrayList<>();

        for (Tile[] tiles:getBoard()) {
            for (Tile tile:tiles) {
                if(tile instanceof PlayerTile playerTile) {
                    playerTiles.add(playerTile);
                }
            }
        }

        return playerTiles;
    }


    public Tile[][] getBoard() {
        return board;
    }

    public void setBoard(Tile[][] board) {
        this.board = board;
    }

    public byte[] getStartingPosition() {
        return startingPosition;
    }

    public void setStartingPosition(byte[] startingPosition) {
        this.startingPosition = startingPosition;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public Random getRandom() {
        return random;
    }

    public Tile getTileAt(byte[] coordinate) {
        return board[coordinate[0]][coordinate[1]];
    }
    public Tile getTileAt(byte x, byte y) {
        if((x >= BOARD_WIDTH || x < 0) || (y >= BOARD_HEIGHT || y < 0)) {
            return null;
        }
        return board[x][y];
    }

    public Tile getRandomTile() {
        byte[] randomCoord = new byte[]{MathUtils.randomNumberInRange(0, 4), MathUtils.randomNumberInRange(0, 4)};
        while (randomCoord[0] == startingPosition[0] && randomCoord[1] == startingPosition[1]) {
            randomCoord = new byte[]{MathUtils.randomNumberInRange(0, 4), MathUtils.randomNumberInRange(0, 4)};
        }
        return board[randomCoord[0]][randomCoord[1]];
    }

    public byte[] boardToStackedArray() {
        byte[] singleArrayBoard = new byte[BOARD_WIDTH*BOARD_HEIGHT];

        int x = 0;
        int y = 0;
        for (int i = 0; i < singleArrayBoard.length; i++) {
            singleArrayBoard[i] = board[x][y].value;
            x++;
            if(x > 4) {
                x = 0;
                y++;
            }
        }
        return singleArrayBoard;

    }

    public int getTileHint(byte x, byte y) {
        int hint = 0;
        Tile up =       getTileAt(x, (byte) (y-1));
        Tile down =     getTileAt(x, (byte) (y+1));
        Tile left =     getTileAt((byte) (x-1),y);
        Tile right =    getTileAt((byte) (x+1),y);

        if(up != null && up.isBomb()) {
            hint++;
        }
        if(down != null && down.isBomb()) {
            hint++;
        }
        if(left != null && left.isBomb()) {
            hint++;
        }
        if(right != null && right.isBomb()) {
            hint++;
        }

        return hint;
    }



    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();

        for (byte x = 0; x < BOARD_WIDTH; x++) {
            for (byte y = 0; y < BOARD_HEIGHT; y++) {
                string.append(getTileAt(x,y)).append(" ");
            }
            string.append("\n");
        }
        return string.toString();
    }

    public String toHintString() {
        StringBuilder string = new StringBuilder();

        for (byte x = 0; x < BOARD_WIDTH; x++) {
            for (byte y = 0; y < BOARD_HEIGHT; y++) {
                if(getTileAt(x,y).isBomb()) {
                    string.append("!").append(" ");
                }else {
                    string.append(getTileHint(x,y)).append(" ");
                }
            }
            string.append("\n");
        }
        return string.toString();

    }

    public List<Tile> getSurroundingTiles(Tile tile) {
        byte[] coord = getCoordinatesOfTile(tile);
        byte x = coord[0];
        byte y = coord[1];

        Tile up =       getTileAt(x, (byte) (y-1));
        Tile down =     getTileAt(x, (byte) (y+1));
        Tile left =     getTileAt((byte) (x-1),y);
        Tile right =    getTileAt((byte) (x+1),y);

        List<Tile> tiles = new ArrayList<>();
        if(up != null) {
            tiles.add(up);
        }
        if(down != null) {
            tiles.add(down);
        }
        if(left != null) {
            tiles.add(left);
        }
        if(right != null) {
            tiles.add(right);
        }

        return tiles;

    }

    public List<Tile> getSurroundingUndiscoveredTiles(Tile tile) {
        return getSurroundingTiles(tile).stream().filter(surroundingTile -> !surroundingTile.isRevealed()).collect(Collectors.toList());
    }

    public int getKnownBombs(Tile tile) {
        return Math.toIntExact(getSurroundingTiles(tile).stream().filter(surroundingTile -> surroundingTile.isRevealed() && surroundingTile.isBomb()).count());

    }



    public int getKnownBombTiles(Tile tile) {
        return Math.toIntExact(getSurroundingTiles(tile).stream().filter(surroundingTile -> surroundingTile.isRevealed() && surroundingTile.isBomb()).count());
    }
    public int getKnownBombTiles() {
        int knownTiles = 0;
        for (byte x = 0; x < BOARD_WIDTH; x++) {
            for (byte y = 0; y < BOARD_HEIGHT; y++) {
                if(getTileAt(x,y)!= null && getTileAt(x,y).isRevealed() && getTileAt(x,y).isBomb()) {
                    knownTiles++;
                }
            }
        }
        return knownTiles;
    }

    public int getKnownSafeTiles() {
        int knownTiles = 0;
        for (byte x = 0; x < BOARD_WIDTH; x++) {
            for (byte y = 0; y < BOARD_HEIGHT; y++) {
                if(getTileAt(x,y)!= null && getTileAt(x,y).isRevealed() && !getTileAt(x,y).isBomb()) {
                    knownTiles++;
                }
            }
        }
        return knownTiles;

    }



    public byte[] getCoordinatesOfTile(Tile tile) {
        for (byte x = 0; x < BOARD_WIDTH; x++) {
            for (byte y = 0; y < BOARD_HEIGHT; y++) {
                if(tile == board[x][y]) {
                    return new byte[]{x,y};
                }
            }
        }
        return new byte[0];
    }



    public void setTile(byte[] cooord, Tile tile) {
        board[cooord[0]][cooord[1]] = tile;
    }
}

