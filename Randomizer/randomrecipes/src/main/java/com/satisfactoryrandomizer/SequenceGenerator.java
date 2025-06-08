package com.satisfactoryrandomizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import com.satisfactoryrandomizer.Storage.*;

public class SequenceGenerator {
    private static long seed = 0;
    private static Random random = new Random();
    private static Materials materials;

    public static void generateSequence(int seed) {

        // Create the materials variable to store all materials / production buildings
        materials = new Materials();

        // Generate a random seed if not provided and set it to the random generator
        if (seed == 0) {
            GenerateSeed();
        }
        System.out.println("Using seed: " + seed);
        random = new Random(seed);

        // Do the first loop of randomization for tier 0 materials
        generateMaterialIteration();

        // Do the first loop of randomization for tier 0 Structures

    }

    private static void generateStructureIteration() {
        List<CraftStation> stations = new ArrayList<>();
    }

    private static void generateMaterialIteration() {
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
            List<Mat> prod = new ArrayList<>();
            Boolean mainliquid;

            mats = generateIngredients(station);
            prod = new ArrayList<>();

            // Add the main product multiplying the value range by 1000 for liquids.
            if (comp.isLiquid()) {
                prod.add(new Mat(comp.getName(), 1000 * random.nextInt(UiValues.getMaxStackComp()) + 1));
                mainliquid = true;
            } else {
                prod.add(new Mat(comp.getName(), random.nextInt(UiValues.getMaxStackComp()) + 1));
                mainliquid = false;
            }

            // Add main product to products.
            // Add other products if waste is not Easy
            prod.add(new Mat(comp.getName(), random.nextInt(UiValues.getMaxStackComp()) + 1));
            if (UiValues.getWaste() != 0) {
                prod.addAll(generateProducts(station, mainliquid));
            }

            Recipe recipe = new Recipe(
                    prod, // Products
                    mats, // Ingredients
                    "Recipe_" + comp.getName() + ".json", // Filename
                    station.getName() // Station
            );

            // Create Recipe JSON file
            CreateJSON.saveRecipeAsJson(recipe, comp.getRecipePath());

            // Mark the component as craftable and update the list of available but
            // uncraftable components
            materials.setComponentCraftable(comp.getName(), true);
            availableComponents = materials.getAvailableButUncraftableComponents();
        }
    }

    private static List<Mat> generateIngredients(CraftStation station) {
        List<Mat> ingredients = new ArrayList<>();

        // Get slots for the station
        int liquidslots = station.getLiquidIn();
        int solidslots = station.getSolidIn();

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

    // Generate extra materials for multi output.
    private static List<Mat> generateProducts(CraftStation station, Boolean mainliquid) {
        List<Mat> products = new ArrayList<>();

        // Get slots for the station (liquid slots are 0 if Waste is 3)
        int liquidslots = (UiValues.getWaste() == 3) ? station.getLiquidOut() : 0;
        int solidslots = station.getSolidOut();

        // Remove the slot of the main product, as it's already used
        if (mainliquid && UiValues.getWaste() == 3)
            liquidslots--;
        if (!mainliquid)
            solidslots--;

        // Ensure we didn't put a recipe in a slot it can't be
        if (liquidslots < 0 || solidslots < 0) {
            System.out.println("Slots went negative, the main ingredient can't be crafted there.");
            return products;
        }

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
            // Select a random component from the available components (craftable or not)
            List<Component> availableComponents = materials.getAvailableComponents(selectedLiquid);
            Component component = availableComponents.get(random.nextInt(availableComponents.size()));

            // Add the ingredient to the list and generate the amount randomly
            // Use the UiValues to get the max stack size for the component
            products.add(new Mat(component.getName(), random.nextInt(UiValues.getMaxStackComp()) + 1));
        }

        return products;
    }

    private static int getBiasedRandomInt(int min, int max, int bias) { // In copilot we trust lmao
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
