package org.sweep;

import org.sweep.game.SweeperGame;
import org.sweep.gui.GUI;
import org.sweep.solver.Solver;

public class Main {


    public static void main(String[] args) {

        System.out.println(new SweeperGame(100000,new byte[]{2,2},false).toHintString());

        Solver.findAllPossibleSweeperGames();
        GUI.gui.setVisible(true);

    }
}