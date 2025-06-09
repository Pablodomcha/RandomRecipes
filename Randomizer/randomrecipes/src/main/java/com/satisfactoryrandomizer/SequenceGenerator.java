package com.satisfactoryrandomizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.satisfactoryrandomizer.Storage.Component;
import com.satisfactoryrandomizer.Storage.CraftStation;
import com.satisfactoryrandomizer.Storage.Mat;
import com.satisfactoryrandomizer.Storage.Materials;
import com.satisfactoryrandomizer.Storage.Recipe;
import com.satisfactoryrandomizer.Storage.UiValues;

public class SequenceGenerator {
    private static long seed = 0;
    private static Random random = new Random();
    private static Materials materials;
    private static boolean liquidUnlocked = false;

    public static void generateSequence() {

        // Create the materials variable to store all materials / structures
        materials = new Materials();

        // Generate a random seed if not provided and set it to the random generator
        if (seed == 0) {
            generateSeed();
            Console.log("Generating seed because it wasn't provided.");
        }
        Console.log("Using seed: " + seed);
        random = new Random(seed);

        // Do the first loop of randomization for tier 0 materials
        generateMaterialIteration();

        // Do the first loop of randomization for tier 0 Structures
        generateProductionIteration();

    }

    private static void generateProductionIteration() {

        // Get the station list and components available to make them
        List<CraftStation> stations = materials.getAvailableButUncraftableStations();
        //stations.add materials.getStation("");
        while (!stations.isEmpty()) {
            // Needed variables. Prod will always only have the station, but has to be a
            // string regardless.
            CraftStation station = stations.get(random.nextInt(stations.size()));
            List<Mat> mats = generateStructureIngredients();
            List<Mat> prod = new ArrayList<>();

            // Only produces one of the structure
            prod.add(new Mat(station.getName(), 1));

            Recipe recipe = new Recipe(
                    prod, // Products
                    mats, // Ingredients
                    "Recipe_" + station.getName() + ".json", // Filename
                    "", // Doesn't apply
                    1, // Doesn't apply
                    1.0 // Doesn't apply
            );

            // Create Recipe JSON file
            CreateJSON.saveStructureAsJson(recipe, station.getRecipePath()); // Wrong path for structures, this is the path to call them for components

            // Mark the component as craftable and update the list of available but
            // uncraftable components
            materials.setComponentCraftable(station.getName(), true);

            // No need to update the list of available components as we only added buildable
            // structures.
        }
    }

    private static void generateMaterialIteration() {
        List<Component> availableComponents = materials.getAvailableButUncraftableComponents();
        Collections.shuffle(availableComponents, random);

        // Loop to generate recipes for all components available but uncraftable in this
        // iteration
        // If it is too slow, will have to do more than 1 recipe per loop iteration
        while (!availableComponents.isEmpty()) {

            Component comp = availableComponents.get(random.nextInt(availableComponents.size()));

            // Select a random crafting Station
            CraftStation station = materials.getRandomAvailableAndCraftableStation(random.nextInt());
            List<Mat> mats;
            List<Mat> prod = new ArrayList<>();
            Boolean mainliquid;

            mats = generateIngredients(station);

            Console.cheatsheet("Generated ingredients for " + comp.getName() + " to be made in " + station.getName());

            // Add the main product multiplying the value range by 1000 for liquids.
            if (comp.isLiquid()) {
                prod.add(new Mat(comp.getName(), 1000 * random.nextInt(UiValues.getMaxStackComp()) + 1));
                mainliquid = true;
            } else {
                prod.add(new Mat(comp.getName(), random.nextInt(UiValues.getMaxStackComp()) + 1));
                mainliquid = false;
            }

            double handSpeed = random.nextDouble() * (UiValues.getHandcraftSpeed()[1] - UiValues.getHandcraftSpeed()[0])
                    + UiValues.getHandcraftSpeed()[0];

            Recipe recipe = new Recipe(
                    prod, // Products
                    mats, // Ingredients
                    "Recipe_" + comp.getName() + ".json", // Filename
                    station.getBuilderPath(), // Station
                    random.nextInt(UiValues.getMaxTimeCraft()), // Time
                    handSpeed // Handcraft speed
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
            Boolean selectedLiquid = false;

            // If any liquid has been unlocked we check for them, otherwise, we don't.
            if (liquidUnlocked) {
                if (liquidslots > 0 && solidslots > 0) {
                    // Randomly choose between liquid and solid slot
                    selectedLiquid = random.nextBoolean();
                } else if (liquidslots > 0) {
                    selectedLiquid = true;
                } else if (solidslots > 0) {
                    selectedLiquid = false;
                } else {
                    Console.log(
                            "No more slots available for ingredients, this message does not stop the program, but means there's an error somewhere.");
                    break; // No more slots available, break the loop to stop the program from crashing
                }
            }
            // Select a random component from the usable components
            List<Component> craftableComponents = materials.getAvailableAndCraftableComponents(selectedLiquid);

            ensureUnused(ingredients, craftableComponents, selectedLiquid);

            // Add the ingredient to the list and generate the amount randomly
            // Use the UiValues to get the max stack size for the component
            ingredients.add(new Mat(ensureUnused(ingredients, craftableComponents, selectedLiquid).getName(),
                    random.nextInt(UiValues.getMaxStackComp()) + 1));
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
            Console.log("Slots went negative, the main ingredient can't be crafted there.");
            return products;
        }

        // Use random resources between 1 and the max possible for the station
        int totalresources = getBiasedRandomInt(1, liquidslots + solidslots, UiValues.getInputBias());

        for (int i = 0; i < totalresources; i++) {
            Boolean selectedLiquid;
            if (liquidslots > 0 && solidslots > 0) {
                // Randomly choose between liquid and solid slot
                selectedLiquid = random.nextBoolean();
            } else if (liquidslots > 0) {
                selectedLiquid = true;
            } else if (solidslots > 0) {
                selectedLiquid = false;
            } else {
                Console.log(
                        "No more slots available for ingredients, this message does not stop the program, but means there's an error somewhere.");
                break; // No more slots available, break the loop to stop the program from crashing
            }
            // Select a random component from the available components (craftable or not)
            List<Component> availableComponents = materials.getAvailableComponents(selectedLiquid);
            Component component = availableComponents.get(random.nextInt(availableComponents.size()));

            // Add the ingredient to the list and generate the amount randomly
            // Use the UiValues to get the max stack size for the component
            products.add(new Mat(component.getName(), random.nextInt(UiValues.getMaxCompCraft()) + 1));
        }

        return products;
    }

    private static List<Mat> generateStructureIngredients() {

        List<Component> craftableComponents = materials.getAvailableAndCraftableComponents(false); // cannot use liquids
        int diffResources = getBiasedRandomInt(1, Math.min(craftableComponents.size(), UiValues.getMaxItemStruct()),
                UiValues.getInputBias());
        List<Mat> ingredients = new ArrayList<>();

        for (int i = 0; i < diffResources; i++) {
            ingredients.add(new Mat(ensureUnused(ingredients, craftableComponents, false).getName(),
                    random.nextInt(UiValues.getMaxStackComp()) + 1));
        }

        return ingredients;

    }

    private static Component ensureUnused(List<Mat> list, List<Component> craftableComponents, boolean liquid) {

        Component component = new Component("placeholder in ensureUnused in SequenceGenerator", false);
        Boolean success = false;
        int loops = 0;

        while (!success) {
            component = craftableComponents.get(random.nextInt(craftableComponents.size()));

            boolean alreadyExists = false;
            for (Mat ing : list) {
                if (ing.getName().equals(component.getName())) {
                    alreadyExists = true;
                }
            }
            if (!alreadyExists) {
                success = true;
            }
            loops++;
            if (loops > 100) {
                Console.log("Looping through the available materials for the " + loops
                        + "th time, \"Max recipes used\" is too low. The uses of all components will be increased by 1 to procceed");
                materials.refillComponents();
                craftableComponents = materials.getAvailableAndCraftableComponents(liquid);
            }
        }

        if (component.isAvailable()) {
            materials.useComponent(component.getName());
        } else {
            Console.log(
                    "For some reason this recipe is trying to use an unavailable component: " + component.getName());
        }

        return component;
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
        Console.log("getBiasedRandomInt: min=" + (min + (int) ((max - min + 1) * r)));
        return min + (int) ((max - min + 1) * r);

    }

    public static void generateSeed() {
        while (seed <= 0) {
            seed = random.nextLong();
        }
    }
}
