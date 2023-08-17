package org.sweep.solver;

import org.sweep.game.Tile;

public class PlayerTile extends Tile {
    byte hint;

    public PlayerTile(byte hint) {
        super();
        this.setRevealed(true);
        this.hint = hint;
    }

    public byte getHint() {
        return hint;
    }

    public void setHint(byte hint) {
        this.hint = hint;
    }
}
