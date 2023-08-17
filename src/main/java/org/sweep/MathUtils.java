package org.sweep;

import java.util.Random;

public interface MathUtils {

    Random random = new Random();

    static byte randomNumberInRange(int min, int max) {
        return (byte) (random.nextInt((max-min)+1)+min);
    }

    static byte[] randomCoordinate() {
        return new byte[]{randomNumberInRange(0, 5), randomNumberInRange(0, 5)};
    }

    static boolean performRandomCheck(int chances, int possibilities) {
        return random.nextInt(possibilities) < chances;

    }
}
