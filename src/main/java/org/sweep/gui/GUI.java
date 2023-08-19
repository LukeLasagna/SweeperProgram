package org.sweep.gui;

import org.sweep.game.SweeperGame;
import org.sweep.game.Tile;
import org.sweep.solver.PlayerTile;
import org.sweep.solver.Solver;
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
        reRender();




    }


    public void refreshBoard() {

        double[][] probablities = Solver.getProbabilityMatrix(sweeperGame);
        for (byte x = 0; x < BOARD_WIDTH; x++) {
            for (byte y = 0; y < BOARD_HEIGHT; y++) {



                if(!(sweeperGame.getTileAt(x, y) instanceof PlayerTile)) {

                    sweeperGame.setTile(new byte[]{x,y},new VarTile(Tile.BOMB,probablities[x][y]));


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
                    guiTile.setText(Math.round(varTile.getCertainty()*100) + "%");
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
