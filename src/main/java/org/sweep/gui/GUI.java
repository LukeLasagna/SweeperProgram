package org.sweep.gui;

import org.sweep.game.SweeperGame;
import org.sweep.game.Tile;
import org.sweep.solver.PlayerTile;
import org.sweep.solver.VarTile;

import javax.swing.*;
import java.awt.*;

import static org.sweep.game.SweeperGame.BOARD_HEIGHT;
import static org.sweep.game.SweeperGame.BOARD_WIDTH;

public class GUI extends JFrame {

    public static final GUI gui = new GUI();

    SweeperGame sweeperGame;

    GUITile[][] guiTiles;
    public static GUITile startingTile;


    public GUI() {
        super("GUI");
        setResizable(false);
        setSize(500,610);




        GridLayout gridLayout = new GridLayout(5,5,10,10);
        JPanel gamePanel = new JPanel(gridLayout);
        gamePanel.setSize(500,500);

        guiTiles = new GUITile[BOARD_WIDTH][BOARD_HEIGHT];

        for (int x = 0; x < BOARD_WIDTH; x++) {
            for (int y = 0; y < BOARD_HEIGHT; y++) {
                GUITile guiTile = new GUITile();
                guiTiles[x][y] = guiTile;

                gamePanel.add(guiTile);
            }
        }

        this.getContentPane().add(gamePanel);







    }


    public void recalculate(GUITile guiTile) {
        byte[] cooord = new byte[2];
        for (byte x = 0; x < BOARD_WIDTH; x++) {
            for (byte y = 0; y < BOARD_HEIGHT; y++) {
                if(guiTiles[x][y] == guiTile) {
                    cooord = new byte[]{x,y};
                }
            }
        }
        if(sweeperGame == null) {

            startingTile = guiTile;
            sweeperGame = new SweeperGame(100000,cooord,true);
        }
        if(guiTile.value != null) {
            sweeperGame.setTile(cooord, new PlayerTile(guiTile.value));
        }else {
            sweeperGame.setTile(cooord, null);
        }

        refreshBoard();
        calculateOdds();
        setBoard();
        reRender();




    }


    public void refreshBoard() {
        for (byte x = 0; x < BOARD_WIDTH; x++) {
            for (byte y = 0; y < BOARD_HEIGHT; y++) {


                if(!(sweeperGame.getTileAt(x, y) instanceof PlayerTile)) {

                    sweeperGame.setTile(new byte[]{x,y},new VarTile(Tile.BOMB, -100));


                }
            }
        }
    }

    public void calculateOdds() {

        boolean needsToRecalculate;

        do {
            needsToRecalculate = false;
            for (byte x = 0; x < BOARD_WIDTH; x++) {
                for (byte y = 0; y < BOARD_HEIGHT; y++) {
                    if(sweeperGame.getTileAt(x, y) instanceof PlayerTile playerTile) {
                        for (Tile tile:sweeperGame.getSurroundingUndiscoveredTiles(playerTile)) {
                            if(tile instanceof VarTile varTile) {
                                int knownBombs = sweeperGame.getKnownBombs(playerTile);
                                int hint = playerTile.getHint();
                                int numberOfSurroundingUnknownTiles = Math.toIntExact(sweeperGame.getSurroundingUndiscoveredTiles(playerTile).stream().count());

                                double certainty = (double) (hint - knownBombs) / numberOfSurroundingUnknownTiles;
                                varTile.setCertainty(certainty);

                                //System.out.println(String.format("At %s, %s, there are %s known bombs and the tile says there is %s bombs around. Since there are only %s unknown tiles, the odds of this tile being a bomb is %s",sweeperGame.getCoordinatesOfTile(tile)[0],sweeperGame.getCoordinatesOfTile(tile)[1],knownBombs,hint,numberOfSurroundingUnknownTiles,certainty));

                                if(varTile.getCertainty() == 0) {
                                    varTile.setValue(Tile.MONEY_BAG);
                                    varTile.setRevealed(true);
                                    needsToRecalculate = true;
                                }

                                if(varTile.getCertainty() == 1) {
                                    varTile.setValue(Tile.BOMB);
                                    varTile.setRevealed(true);
                                    needsToRecalculate = true;
                                }




                            }
                        }

                    }
                }
            }
        }while (needsToRecalculate);
    }

    public void setBoard() {
        int knownTiles = sweeperGame.getKnownSafeTiles();
        int knownBombs = sweeperGame.getKnownBombTiles();
        int totalTiles = 25;

        double bestCaseCertainty = (double) (Math.min(4, knownBombs)-knownBombs) /(totalTiles-knownTiles);
        double worstCaseCertainty = (double) (Math.max(knownBombs,9)-knownBombs)/(totalTiles-knownTiles);

        double totalCertainty = (bestCaseCertainty+worstCaseCertainty)/2;
        for (byte x = 0; x < BOARD_WIDTH; x++) {
            for (byte y = 0; y < BOARD_HEIGHT; y++) {


                if(sweeperGame.getTileAt(x,y) instanceof VarTile varTile && varTile.getCertainty() == -100) {

                    varTile.setCertainty(totalCertainty);


                }
            }
        }
    }

    private void reRender() {
        for (byte x = 0; x < BOARD_WIDTH; x++) {
            for (byte y = 0; y < BOARD_HEIGHT; y++) {
                GUITile guiTile = guiTiles[x][y];
                Tile tile = sweeperGame.getTileAt(x,y);

                if(tile instanceof PlayerTile playerTile) {
                    guiTile.setText(String.valueOf(playerTile.getHint()));
                    guiTile.setBackground(Color.GREEN);
                }
                if(tile instanceof VarTile varTile) {
                    guiTile.setText(varTile.getCertainty()*100 + "%");
                    Color color = new Color((int) (255*varTile.getCertainty()), (int) (255-(255*varTile.getCertainty())),0);
                    guiTile.setBackground(color);
                }
            }
        }

    }

    public static GUITile getStartingTile() {
        return startingTile;
    }

}
