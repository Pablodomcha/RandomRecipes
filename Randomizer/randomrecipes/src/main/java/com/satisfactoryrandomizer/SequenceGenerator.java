package com.satisfactoryrandomizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.satisfactoryrandomizer.Storage.Mat;
import com.satisfactoryrandomizer.Storage.Materials;
import com.satisfactoryrandomizer.Storage.Randomizables.Component;
import com.satisfactoryrandomizer.Storage.Randomizables.CraftStation;
import com.satisfactoryrandomizer.Storage.Randomizables.Randomizable;
import com.satisfactoryrandomizer.Storage.Recipe;
import com.satisfactoryrandomizer.Storage.UiValues;

public class SequenceGenerator {
    private static long seed = UiValues.getSeed();
    private static Random random = new Random();
    private static Materials materials;
    private static Boolean liquidUnlocked = false;

    public static void generateSequence() {

        // Create the materials variable to store all materials / structures
        materials = new Materials();

        // Generate the extraChecks
        try{
        materials.fillExtraChecks();
        } catch (Exception e) {
            Console.log("Exception in the extra checks: " + e.getMessage());
        }

        // Generate a random seed if not provided and set it to the random generator
        if (seed == 0) {
            generateSeed();
            Console.log("Generating seed because it wasn't provided.");
        }
        Console.log("Using seed: " + seed);
        random = new Random(seed);

        //Create the starting array of items
        List<Randomizable> randomizables = new ArrayList<>();
        randomizables.addAll(materials.getAvailableButUncraftableComponents());
        randomizables.addAll(materials.getAvailableButUncraftableStations());

        // Main loop, runs until there's nothing left to randomize
        while (!randomizables.isEmpty()) {

        }

    }

    private static void generateStructure(CraftStation station) {
        // Needed variables. Prod will always only have the station, but has to be a
        // string regardless.
        List<Mat> mats = generateIngredients(station, true);
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
        CreateJSON.saveStructureAsJson(recipe, station.getRecipePath()); // Wrong path for structures, this is the
                                                                         // path to call them for components

        // Mark the component as craftable and update the list of available but
        // uncraftable components
        materials.setComponentCraftable(station.getName(), true);

        // No need to update the list of available components as we only added buildable
        // structures.
    }

    private static void generateMaterial(Component comp) {
        // Select a random crafting Station
        CraftStation station = materials.getRandomAvailableAndCraftableStation(random.nextInt());
        List<Mat> mats = generateIngredients(station, false);
        List<Mat> prod = new ArrayList<>();
        Boolean mainliquid;

        Console.cheatsheet("Generated ingredients for " + comp.getName() + " to be made in " + station.getName());

        // Add the main product multiplying the value range by 1000 for liquids.
        // Then add the rest of the products if the option is enabled and there's space.
        if (station.getLiquidIn() + station.getSolidIn() > 0) {
            if (comp.isLiquid()) {
                prod.add(new Mat(comp.getName(), 1000 * random.nextInt(UiValues.getMaxStackComp()) + 1));
                mainliquid = true;
            } else {
                prod.add(new Mat(comp.getName(), random.nextInt(UiValues.getMaxStackComp()) + 1));
                mainliquid = false;
            }
            prod.addAll(generateProducts(station, mainliquid));
        }

        double handSpeed = random.nextDouble() * (UiValues.getHandcraftSpeed()[1] - UiValues.getHandcraftSpeed()[0])
                + UiValues.getHandcraftSpeed()[0];

        Recipe recipe = new Recipe(
                prod, // Products
                mats, // Ingredients
                "Recipe_" + comp.getName() + ".json", // Filename
                station.getBuilderPath(), // Station
                random.nextDouble() * (UiValues.getMaxTimeCraft()), // Time
                handSpeed // Handcraft speed
        );

        // Create Recipe JSON file
        CreateJSON.saveRecipeAsJson(recipe, comp.getRecipePath());

        // Mark the component as craftable and update the list of available but
        // uncraftable components
        materials.setComponentCraftable(comp.getName(), true);
    }

    /**
     * Generates a list of ingredients for a recipe given a CraftStation.
     * 
     * If it's a material recipe, the method uses up to the number of slots
     * available in the station and randomly selects resources to be used as
     * ingredients.
     * 
     * If it's a structure recipe, the method uses up to the Max item struct
     * variable different resources.
     * 
     * The resources are selected from the list of available resources. The
     * amount of each resource is randomly generated between 1 and the max
     * stack size variable.
     * 
     * @param station     The CraftStation for which to generate the ingredients
     * @param isStructure Whether the station is a structure or not
     * @return A list of ingredients with their respective amounts
     */
    private static List<Mat> generateIngredients(CraftStation station, Boolean isStructure) {
        List<Mat> ingredients = new ArrayList<>();

        // Get slots for the station
        int liquidslots = station.getLiquidIn();
        int solidslots = station.getSolidIn();

        // Use random resources between 1 and the max possible for the station for
        // production
        int totalresources;
        if (isStructure) {
            totalresources = getBiasedRandomInt(1, liquidslots + solidslots, UiValues.getInputBias());
        } else {
            totalresources = getBiasedRandomInt(1, UiValues.getMaxItemStruct(), UiValues.getInputBias());
        }

        for (int i = 0; i < totalresources && isStructure; i++) {
            Boolean selectedLiquid = false;

            // If any liquid has been unlocked we check for them, otherwise, we don't.
            // If it's a structure, we don't use liquids.
            if (liquidUnlocked && !isStructure) {
                if (liquidslots > 0 && solidslots > 0) {
                    // Randomly choose between liquid and solid slot
                    selectedLiquid = random.nextBoolean();
                } else if (liquidslots > 0) {
                    selectedLiquid = true;
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
            // Multiply by 1000 for liquids
            int amount = random.nextInt(UiValues.getMaxStackComp()) + 1;
            if (selectedLiquid) {
                amount = amount * 1000;
            }
            ingredients.add(new Mat(ensureUnused(ingredients, craftableComponents, selectedLiquid).getName(), amount));
        }
        return ingredients;
    }

    /**
     * Generates a list of products for a recipe given a CraftStation. The method
     * uses
     * the number of slots available in the station and randomly selects resources
     * to be
     * used as products. The resources are selected from the list of available
     * resources.
     * The amount of each resource is randomly generated between 1 and the max stack
     * size
     * variable. The method returns a list of products with their respective
     * amounts.
     *
     * @param station    The CraftStation for which to generate the ingredients
     * @param mainliquid Whether the main product is a liquid or not
     * @return A list of products with their respective amounts
     */
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
        int totalresources = getBiasedRandomInt(0, liquidslots + solidslots, UiValues.getInputBias());

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

    /**
     * Ensures that the component selected is not already in the list by checking
     * every component in the list. If the component is already in the list, it
     * keeps looping until it finds one that is not. If it loops more than 100
     * times, it refills the uses of all components to increase the chances of
     * finding one that is not in the list.
     *
     * @param list                The list of components to check
     * @param craftableComponents The list of available and craftable components to
     *                            select from
     * @param liquid              Whether the component should be a liquid or not
     * @return A component that is not already in the list
     */
    private static Component ensureUnused(List<Mat> list, List<Component> craftableComponents, Boolean liquid) {

        Component component = new Component("placeholder in ensureUnused in SequenceGenerator", false, false);
        Boolean success = false;
        int loops = 0;

        while (!success) {
            component = craftableComponents.get(random.nextInt(craftableComponents.size()));

            Boolean alreadyExists = false;
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
