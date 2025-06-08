package com.satisfactoryrandomizer;

import java.util.Random;

public class Tests {
    static Random random = new Random();
    // Simply to try out things, just run this in App.java to check what the content does.
    public static void test(){

        int iterations = 10000;
        double average = 0;
        for (int i = 0; i < iterations; i++) {
            average += getBiasedRandomInt(1, 1, 1);
        }
        System.out.println("Average: " + (average / (double) iterations));

    }

    private static int getBiasedRandomInt(int min, int max, int bias) { // In copilot we trust lmao
    if (bias <= 0) return min;
    if (bias >= 100) return max;
    if (bias == 50) return min + random.nextInt(max - min + 1);

    double r = random.nextDouble(); // uniform [0,1)
    // Map bias 0..50 to favor min, 50..100 to favor max
    if (bias < 50) {
        // Skew towards min
        double power = 1 + (49.0 - bias) / 49.0 * 9.0; // power: 10 at bias=0, 1 at bias=50
        r = Math.pow(r, power);
    } else {
        // Skew towards max
        double power = 1 + (bias - 51.0) / 49.0 * 9.0; // power: 1 at bias=50, 10 at bias=100
        r = 1 - Math.pow(1 - r, power);
    }
    return min + (int) ((max - min + 1) * r);

}

}
