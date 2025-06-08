package com.satisfactoryrandomizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import com.satisfactoryrandomizer.Storage.*;

public class SequenceGenerator {
    static long seed = 0;
    static Random random = new Random();

    public static void GenerateRecipes(int seed) {

        Materials materials = new Materials();

        // Generate a random seed if not provided and set it to the random generator
        if (seed == 0) {
            GenerateSeed();
        }
        System.out.println("Using seed: " + seed);
        random = new Random(seed);

    }

    private Materials GenerateMaterialIteration(Materials materials, int seed) {
        List<Component> availableComponents = materials.getAvailableButUncraftableComponents();
        Collections.shuffle(availableComponents, random);

        // Loop to generate recipes for all components available but uncraftable in this
        // iteration
        // If it is too slow, will have to do more than 1 recipe per loop iteration
        while (availableComponents.size() > 0) {

            Component comp = availableComponents.get(0);

            // Select a random crafting Station
            CraftStation station = materials.getRandomAvailableAndCraftableStation(random.nextInt());
            List<Mat> mats = new ArrayList<>();

            mats = GenerateIngredients(station, materials);

            Recipe recipe = new Recipe(
                    comp, // Component
                    mats, // Ingredients
                    random.nextInt(UiValues.getMaxCompCraft()), // Amount
                    "Recipe_" + comp.getName() + ".json", // Filename
                    station.getName() // Station
            );

            // Create Recipe JSON file
            CreateJSON.saveRecipeAsJson(recipe);

            // Mark the component as craftable and update the list of available but
            // uncraftable components
            materials.setComponentCraftable(comp.getName(), true);
            availableComponents = materials.getAvailableButUncraftableComponents();
        }
        return materials;

    }

    private List<Mat> GenerateIngredients(CraftStation station, Materials materials) {
        List<Mat> ingredients = new ArrayList<>();

        // Get slots for the station
        int liquidslots = station.getLiquidIn();
        int solidslots = station.getLiquidOut();

        // Use random resources between 1 and the max possible for the station
        int totalresources = getBiasedRandomInt(1, liquidslots + solidslots, UiValues.getInputBias());

        for (int i = 0; i < totalresources; i++) {
            Boolean selectedLiquid;
            if (liquidslots > 0 && solidslots > 0) {
                // Randomly choose between liquid and solid slot
                if (random.nextBoolean()) {
                    selectedLiquid = true;
                } else {
                    selectedLiquid = false;
                }
            } else if (liquidslots > 0) {
                selectedLiquid = true;
            } else if (solidslots > 0) {
                selectedLiquid = false;
            } else {
                System.out.println(
                        "No more slots available for ingredients, this message does not stop the program, but means there's an error somewhere.");
                break; // No more slots available, break the loop to stop the program from crashing
            }
            // Select a random component from the usable components
            List<Component> availableComponents = materials.getAvailableAndCraftableComponents(selectedLiquid);
            Component component = availableComponents.get(random.nextInt(availableComponents.size()));

            // Add the ingredient to the list and generate the amount randomly
            // Use the UiValues to get the max stack size for the component
            ingredients.add(new Mat(component.getName(), random.nextInt(UiValues.getMaxStackComp()) + 1));
        }

        return ingredients;
    }

    private int getBiasedRandomInt(int min, int max, int bias) { // In copilot we trust lmao
        if (bias <= 0)
            return min;
        if (bias >= 100)
            return max;
        if (bias == 50)
            return min + random.nextInt(max - min + 1);

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
        System.out.println("getBiasedRandomInt: min=" + (min + (int) ((max - min + 1) * r)));
        return min + (int) ((max - min + 1) * r);

    }

    public static void GenerateSeed() {
        Random random = new Random();
        while (seed < 0) {
            seed = random.nextLong();
        }
    }
}
