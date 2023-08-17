package org.sweep.solver;

import org.sweep.game.Tile;

public class VarTile extends Tile {
    double certainty;

    public VarTile(byte type, double certainty) {
        super();
        this.setValue(type);
        this.certainty = certainty;
    }

    public double getCertainty() {
        return certainty;
    }

    public void setCertainty(double certainty) {
        this.certainty = certainty;
    }
}
