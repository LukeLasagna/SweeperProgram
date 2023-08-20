package org.sweep.gui;

import org.sweep.game.SweeperGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static org.sweep.game.SweeperGame.BOARD_HEIGHT;
import static org.sweep.game.SweeperGame.BOARD_WIDTH;

public class GUITile extends JButton {
    Byte value;

    public GUITile() {
        setSize(80,80);
        setVisible(true);

        GUITile guiTile = this;
        this.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(value == null) {
                    value = 0;
                    setText(String.valueOf(value));

                }
                else if(value > 3) {
                    if(GUI.getStartingTile() == guiTile) {
                        value = 0;
                        setText(String.valueOf(value));
                    }else {
                        value = null;
                        setText("");
                    }

                }else {
                    value++;
                    setText(String.valueOf(value));
                }
                GUI.gui.recalculate(guiTile);

            }
        });
    }

    public Byte getValue() {
        return value;
    }

    public void setValue(Byte value) {
        this.value = value;
    }
}
