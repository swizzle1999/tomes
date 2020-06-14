package com.swizzle.tomes.Utils;

import java.util.Random;

public class RandomNumberBetweenBounds {
    private final static Random random = new Random();

    public static int getRandomInt(int lowerBound, int upperBound){
        if (lowerBound != upperBound && lowerBound < upperBound) {
            return random.nextInt(upperBound - lowerBound) + lowerBound;
        } else if (lowerBound > upperBound) {
            System.out.println("ERROR: lower bound is greater than upper bound. Returning lower bound instead");
            return lowerBound;
        } else {
            return lowerBound;
        }
    }
}
