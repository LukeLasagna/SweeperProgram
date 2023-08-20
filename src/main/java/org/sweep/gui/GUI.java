package org.sweep.gui;

import org.sweep.game.SweeperGame;
import org.sweep.game.Tile;
import org.sweep.solver.PlayerTile;
import org.sweep.solver.Solver;
import org.sweep.solver.VarTile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import static org.sweep.game.SweeperGame.BOARD_HEIGHT;
import static org.sweep.game.SweeperGame.BOARD_WIDTH;

public class GUI extends JFrame {

    public static GUI gui = new GUI();

    SweeperGame sweeperGame;

    GUITile[][] guiTiles;
    public static GUITile startingTile;


    public GUI() {
        super("GUI");
        setResizable(false);
        setSize(500,600);

        this.setDefaultCloseOperation(HIDE_ON_CLOSE);

        this.setLayout(new GridLayout(2,1));

        GridLayout gridLayout = new GridLayout(5,5,10,10);
        JPanel gamePanel = new JPanel(gridLayout);
        gamePanel.setSize(500,500);
        gamePanel.setMaximumSize(new Dimension(500,500));
        gamePanel.setMinimumSize(new Dimension(500,500));


        guiTiles = new GUITile[BOARD_WIDTH][BOARD_HEIGHT];

        for (int x = 0; x < BOARD_WIDTH; x++) {
            for (int y = 0; y < BOARD_HEIGHT; y++) {
                GUITile guiTile = new GUITile();
                guiTiles[x][y] = guiTile;

                gamePanel.add(guiTile);
            }
        }

        gamePanel.setMaximumSize(new Dimension(500,500));


        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                GUI.gui = new GUI();
                gui.setVisible(true);
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

        JButton jButton = new JButton("submit");
        jButton.setSize(new Dimension(500,100));
        jButton.setMaximumSize(new Dimension(500,100));
        jButton.setMinimumSize(new Dimension(500,100));



        jButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                refreshBoard();
                reRender();
            }
        });



        this.getContentPane().add(gamePanel);
        this.getContentPane().add(jButton);







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
                    guiTile.setBackground(Color.YELLOW);
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
