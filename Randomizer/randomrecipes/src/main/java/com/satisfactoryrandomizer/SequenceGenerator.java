package com.satisfactoryrandomizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import com.satisfactoryrandomizer.Storage.*;

public class SequenceGenerator {
    static int seed = 0;

    public static void GenerateRecipes(int seed) {

        Materials materials = new Materials();

        // Generate a random seed if not provided
        if (seed == 0) {
            GenerateSeed();
        }
        System.out.println("Using seed: " + seed);

        // Shuffle the list of available components
        List<Component> availableComponents = materials.getAvailableButUncraftableComponents();
        Collections.shuffle(availableComponents, new Random(seed));

        // Loop to generate recipes for all components
        while (materials.getAvailableButUncraftableComponents().size() > 0) {

        }

    }

    public static void RandomNumber() {
        Random random = new Random(seed);
        int randomNumber = random.nextInt(100);
        System.out.println("Random number: " + randomNumber);
    }

    public static void GenerateSeed() {
        Random random = new Random();
        seed = random.nextInt(2000000000);
    }
}
