package ai;

import org.sweep.solver.Solver;

import java.util.Arrays;
import java.util.Random;

public class LightSweeperGame {

    final int BET = 100000;
    final int BANKRUPT = -(BET/2);

    final byte[] STARTING_VISIBILITY = new byte[]{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};

    private int reward = BANKRUPT;

    private boolean finished;
    private int game;

    Random random = new Random();
    byte[] visibleBoard = STARTING_VISIBILITY.clone();



    public LightSweeperGame() {

    }

    private void start(byte index) {
        do {
            game = Solver.allPossibleGames[random.nextInt(Solver.allPossibleGames.length)];
        }while(bombAt(index));
    }

    public void click(byte index) {

        if(visibleBoard[index] == -1) {
            if(Arrays.equals(visibleBoard, STARTING_VISIBILITY) && !rollRNG(200)) {
                start(index);
            }

            if(bombAt(index)) {
                reward = BANKRUPT;
                finished = true;
            }else {
                double multiplier = rollRNG(80) ? 3 : 0.1;
                reward += BET*multiplier;
                visibleBoard[index] = hintAt(index);
            }
        }
    }

    public void cashout() {
        finished = true;
    }

    boolean bombAt(byte index) {
        return (game >> index & 1) == 1;
    }

    byte hintAt(byte index) {

        byte above = -5;
        byte below = 5;
        byte left = -1;
        byte right = 1;

        byte bombs = 0;
        //Not on top
        if(index > 4) {
            if((game >> (index+above) & 1) == 1) {
                bombs++;
            }
        }
        //Not on bottom
        if(index < 20) {
            if((game >> (index+below) & 1) == 1) {
                bombs++;
            }
        }
        //Not on the left
        if(index % 5 != 0) {
            if((game >> (index+left) & 1) == 1) {
                bombs++;
            }
        }
        //Not on right
        if((index+1) % 5 != 0) {
            if((game >> (index+right) & 1) == 1) {
                bombs++;
            }
        }
        return bombs;

    }

    public boolean isFinished() {
        return finished;
    }

    public int getReward() {
        return reward;
    }

    private boolean rollRNG(int denominator) {
        return random.nextInt(denominator) == 0;
    }
}
