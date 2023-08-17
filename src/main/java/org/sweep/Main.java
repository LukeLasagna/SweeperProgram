package org.sweep;

import org.sweep.game.SweeperGame;
import org.sweep.gui.GUI;

public class Main {


    public static void main(String[] args) {

        System.out.println(new SweeperGame(100000,new byte[]{2,2},false).toHintString());

        GUI.gui.setVisible(true);

    }
}