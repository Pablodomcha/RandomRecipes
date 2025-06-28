package com.satisfactoryrandomizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.satisfactoryrandomizer.Storage.Materials;
import com.satisfactoryrandomizer.Storage.Data.Mat;
import com.satisfactoryrandomizer.Storage.Data.Recipe;
import com.satisfactoryrandomizer.Storage.Randomizables.CraftStation;

public class Tests {
    static Materials materials = new Materials();
    static Random random = new Random();

    // Simply to try out things, just run this in App.java to check what the content
    // does.
    public static void test() {

        for (int bias = 1; bias <= 100; bias += 5) {
            double r = 0.5; // uniform [0,1)
            // Map bias 0..50 to favor min, 50..100 to favor max
            if (bias < 50) {
                // Skew towards min
                double power = 1 + (49.0 - bias) / 49.0 * 2; // power: 10 at bias=0, 1 at bias=50
                r = Math.pow(r, power);
            } else {
                // Skew towards max
                double power = 1 + (bias - 51.0) / 49.0 * 2; // power: 1 at bias=50, 10 at bias=100
                r = 1 - Math.pow(1 - r, power);
            }

            System.out.println("Bias: " + bias + " r: " +  String.valueOf(r));
        }
    }
}
