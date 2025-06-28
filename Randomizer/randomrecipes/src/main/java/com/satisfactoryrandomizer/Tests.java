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

        double min = 0.0;
        double max = 1000.0;
        double targetBias = 100;

        int numGenerations = 10;
        long sum = 0;

        System.out.println(
                "Generating " + numGenerations + " numbers with Gaussian bias " + targetBias);
        for (int i = 0; i < numGenerations; i++) {
            double num = generateGaussianBiasedNumber(min, max, targetBias);
            sum += num;
            System.out.println("Generated number " + (i + 1) + ": " + num);
        }

        double average = (double) sum / numGenerations;
        System.out.println("Average of generated numbers: " + average);
        System.out.println("Expected average (bias): " + targetBias + "%");
        System.out.println("std dev: " + Math.min(max - targetBias, targetBias - min));
    }

    public static int generateGaussianBiasedNumber(double min, double max, double bias) {
        if (bias < (-1) || bias > 100) {
            throw new IllegalArgumentException("Bias must be within the range [" + (-1) + ", " + 100 + "]");
        }
        if(bias == (-1)){
            return ((int) (random.nextDouble() * (max-min) + min));
        }

        double usedBias = (max - min) * bias / 100;
        double spread = bias - min;
        double gaussianValue;
        int result;
        Boolean cap = false;

        do {
            gaussianValue = (random.nextGaussian() * spread) + usedBias;
            result = (int) Math.round(gaussianValue);

            // If the value is too far above the bias, put it above the max to reroll it
            // capped above the bias.
            if (result > (bias + (bias - min)) && !cap) {
                result = (int) max + 10;
            }
            // If the value is too far below the bias, put it below the min to reroll it
            // capped below the bias.
            if (result < (bias - (max - bias)) && !cap) {
                result = (int) min - 10;
            }
            // if the value is below the min, reroll it but keep it below the bias.
            if (result < 0 && !cap) {
                max = bias;
                cap = true;
            }
            // if the value is avobe the max, reroll it but keep it above the bias.
            if (result > max && !cap) {
                min = bias;
                cap = true;
            }
            // System.out.println("Value: " + result);
        } while (result < min || result > max); // Re-roll if outside the desired range

        return result;
    }
}
