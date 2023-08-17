package org.sweep.game;

import org.sweep.MathUtils;
import org.sweep.solver.PlayerTile;

public class Tile {


    public static final byte BOMB = -1;
    public static final byte MONEY_BAG = 0;
    public final byte DIAMOND = 1;

    byte value;
    boolean revealed;

    public Tile() {
        value = MathUtils.performRandomCheck(1, 80) ? DIAMOND : MONEY_BAG;
        revealed = false;
    }

    public Tile(byte value) {
        this.value = value;
        revealed = false;
    }

    protected void setBomb() {
        value = BOMB;
    }

    public boolean isBomb() {
        return value == BOMB;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    @Override
    public String toString() {
        switch (value) {
            case BOMB -> {
                return "!";
            }
            case MONEY_BAG -> {
                return "$";
            }
            case DIAMOND -> {
                return "◆";
            }
        }
        return "?";
    }

}
