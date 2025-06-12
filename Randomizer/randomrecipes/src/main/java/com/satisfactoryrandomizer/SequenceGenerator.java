package com.satisfactoryrandomizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.satisfactoryrandomizer.Storage.Data.Mat;
import com.satisfactoryrandomizer.Storage.Data.MilestoneSchematic;
import com.satisfactoryrandomizer.Storage.Data.Recipe;
import com.satisfactoryrandomizer.Storage.Materials;
import com.satisfactoryrandomizer.Storage.Randomizables.Component;
import com.satisfactoryrandomizer.Storage.Randomizables.CraftStation;
import com.satisfactoryrandomizer.Storage.Randomizables.Milestone;
import com.satisfactoryrandomizer.Storage.Randomizables.Randomizable;
import com.satisfactoryrandomizer.Storage.UiValues;

public class SequenceGenerator {
    private static long seed = UiValues.getSeed();
    private static Random random = new Random();
    private static Materials materials;
    private static Boolean liquidUnlocked = false;
    private static String lastObtainedStation = null;
    private static String firstStation = null;
    private static int nItems = 0;

    public static void generateSequence() {

        // Create the materials variable to store all materials / structures
        SequenceGenerator.materials = new Materials();

        // Set the number of items for percentage related things
        SequenceGenerator.nItems = materials.getAllRandomizables().size(); // Removing iron since it starts unlocked.

        materials.testSetup();

        // Generate the extraChecks
        try {
            materials.fillExtraChecks();
        } catch (Exception e) {
            Console.log("Exception in the extra checks: " + e.getMessage());
        }

        // Generate a random seed if not provided and set it to the random generator
        if (SequenceGenerator.seed == 0) {
            generateSeed();
            Console.log("Generating seed because it wasn't provided.");
        }
        Console.log("Using seed: " + SequenceGenerator.seed);
        SequenceGenerator.random = new Random(SequenceGenerator.seed);
        Console.cheatsheet("Seed: " + SequenceGenerator.seed
                + "\nItems in order of generation (only ensured way to reach a material is to have all the previous):");

        // Create the starting array of items
        List<Randomizable> randomizables = materials.getAvailableButUncraftableRandomizables();

        // Temporarily set a CraftingStation craftable to be able to run the main loop
        Console.test(""); // This code below is for tests
        materials.setStructureCraftable("Desc_HadronCollider", true);
        materials.setStructureAvailable("Desc_HadronCollider", true);
        SequenceGenerator.firstStation = "Desc_HadronCollider";

        // Spread the Randomizables evenly across the milestones
        int nDistributableRandomizables = materials.getAllNonMilestonedRandomizables();

        for (Milestone mile : materials.getAllMilestones()) {
            mile.setnRecipes((nDistributableRandomizables / materials.getAllMilestones().size()));
        }
        Console.log("Distributed " + (nDistributableRandomizables)
                + " randomizables across "
                + materials.getAllMilestones().size()
                + " milestones. Total randomizables per milestone: "
                + materials.getMilestoneByName("Tutorial_1").getnRecipes());

        // Main loop, runs until there's nothing left to randomize
        int cap = 10000;
        int iteration = 0;
        while (!randomizables.isEmpty() && iteration++ < cap) {

            // Pick a random item to randomize
            int size = randomizables.size();
            Randomizable randomizable = randomizables.get(random.nextInt(size));

            // Starting value will include all ores.
            Console.log("Creating one of the remaining " + size + " available items. Done "
                    + materials.getCraftableRandomizables().size() + "/" + SequenceGenerator.nItems + " items.");

            if (randomizable instanceof Component) {
                Component comp = (Component) randomizable;
                if (comp.isLiquid() && !SequenceGenerator.liquidUnlocked) { // If liquids aren't unlocked, skip them
                    continue;
                } else {
                    generateComp(comp, materials.getCraftableRandomizables().size(), "component");
                    if (comp.isLiquid()) {
                        SequenceGenerator.liquidUnlocked = true;
                    }
                }
            } else if (randomizable instanceof CraftStation) {
                generateStructure((CraftStation) randomizable, "structure");
            } else if (randomizable instanceof Milestone) {
                generateMilestone((Milestone) randomizable, "milestone");
            } else {
                Console.log("Unknown item type: " + randomizable);
                Console.log("Exiting the randomize loop.");
                break;
            }

            // Remove the name of the created randomizable from any randomizables for which
            // it is an estraCheck
            if (!randomizable.getCheckAlso().isEmpty())
                materials.doExtraChecks(randomizable.getName(), randomizable.getCheckAlso());

            // Update the list (the item is set craftable by the methods above)
            randomizables.clear();
            randomizables = materials.getAvailableButUncraftableRandomizables();
        }

        Console.log("Remaining " + randomizables.size() + " items. Done "
                + materials.getCraftableRandomizables().size() + "/" + SequenceGenerator.nItems + " items.");

        Console.log("Randomization completed. Remaining loops to cap: " + (cap - iteration));

        if (iteration >= cap) {
            Console.log("Randomization failed, loop cap reached.");
        }

        if (!materials.getUncraftableRandomizables().isEmpty()) {
            Console.log("\nMissing Unlocks: ");
            for (Randomizable elem : materials.getUncraftableRandomizables()) {
                Console.log(elem.getName());
            }
            Console.log(
                    "These items didn't get randomized properly, they may be missing from the game or have their default recipe."
                            + "\nEither way, as long as none of these are Space Elevator parts, the game is completable without them.");
        }
    }

    private static void generateMilestone(Milestone milestone, String type) {
        // Needed variables. Prod will always only have the station, but has to be a
        // string regardless.

        List<Mat> mats = generateIngredients(type);
        List<String> checkAlso = new ArrayList<>();

        for (Object mile : milestone.getCheckAlso()) {
            checkAlso.add((String) mile);
        }

        List<String> unlocks = generateUnlocks(milestone.getnRecipes(), milestone.getfixedUnlocks(), checkAlso);

        MilestoneSchematic recipe = new MilestoneSchematic(
                unlocks, // Unlocks
                mats, // Ingredients
                "Schematic_" + milestone.getName() + ".json", // Filename
                random.nextInt(UiValues.getMaxTimeMile())// Time
        );

        // Create Recipe JSON file
        CreateJSON.saveMilestoneAsJson(recipe, milestone.getRecipePath());

        Console.cheatsheet(milestone.getName() + " can be made");

        // Mark the component as craftable
        materials.setMilestoneCraftable(milestone.getName(), true);
    }

    private static void generateStructure(CraftStation station, String type) {
        // Needed variables. Prod will always only have the station, but has to be a
        // string regardless.
        List<Mat> mats = generateIngredients(type);

        Recipe recipe = new Recipe(
                mats, // Doesn't apply
                mats, // Ingredients
                "Recipe_" + station.getName() + ".json", // Filename
                "", // Doesn't apply
                1, // Doesn't apply
                1.0 // Doesn't apply
        );

        // Create Recipe JSON file
        CreateJSON.saveStructureAsJson(recipe, station.getRecipePath());

        Console.cheatsheet(station.getName() + " can be made");

        // Mark the component as craftable
        materials.setStructureCraftable(station.getName(), true);

        // Update the last obtained station
        SequenceGenerator.lastObtainedStation = station.getName();
    }

    private static void generateComp(Component comp, int addedItems, String type) {
        // Select a random crafting Station
        CraftStation station = materials.getRandomAvailableAndCraftableStation(random.nextInt());

        // If the station is not the last one obtained, reroll to increase station
        // variability.
        int count = UiValues.getStationBias();
        while (!station.getName().equals(SequenceGenerator.lastObtainedStation) && count-- > 0
                && SequenceGenerator.lastObtainedStation != null) {
            station = materials.getRandomAvailableAndCraftableStation(random.nextInt());
        }

        List<Mat> mats = generateIngredientsComp(station);
        List<Mat> prod = new ArrayList<>();
        Boolean mainliquid;

        Console.cheatsheet(comp.getName() + " is made in " + station.getName());

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
        double time = random.nextDouble() * (UiValues.getMaxTimeCraft());

        Recipe recipe = new Recipe(
                prod, // Products
                mats, // Ingredients
                "Recipe_" + comp.getName() + ".json", // Filename
                station.getBuilderPath(), // Station
                time, // Time
                handSpeed // Handcraft speed
        );
        Console.test("Time to craft: " + recipe.getTime());
        Console.test("Handcraft speed: " + recipe.getHandcraftSpeed());

        // Create Recipe JSON file (RecipeVN if it goes into a machine with variable
        // consumption)
        if (station.getBuilderPath().equals("Build_HadronCollider")
                || station.getBuilderPath().equals("Build_Converter")) {

            // Amount of energy used by the recipe, the value is randomized with min and max
            // from 10 to 750 based on the percentage of recipes unlocked
            // the rounding to int has losses, but we don't need such precise values.

            double max = (double) addedItems * 740 / SequenceGenerator.nItems;
            int energy = (int) (random.nextDouble() * max + 10);

            CreateJSON.saveRecipeVNAsJson(recipe, comp.getRecipePath(), SequenceGenerator.firstStation,
                    energy);

        } else {
            CreateJSON.saveRecipeAsJson(recipe, comp.getRecipePath(), SequenceGenerator.firstStation);
        }

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
    private static List<Mat> generateIngredientsComp(CraftStation station) {
        List<Mat> ingredients = new ArrayList<>();

        // If it's free, we don't need no ingredients.
        if (random.nextInt(100) < UiValues.getFreeChance()) {
            return ingredients;
        }

        // Get slots for the station
        int liquidslots = station.getLiquidIn();
        int solidslots = station.getSolidIn();

        // Use random resources between 1 and the max possible for the station for
        // production.
        int totalresources = getBiasedRandomInt(1, UiValues.getMaxItemStruct(), UiValues.getInputBias());

        for (int i = 0; i < totalresources; i++) {
            Boolean selectedLiquid = false;

            // If any liquid has been unlocked we check for them, otherwise, we don't.
            // If it's a structure, we don't use liquids.
            if (SequenceGenerator.liquidUnlocked) {
                if (liquidslots > 0 && solidslots > 0) {
                    // Randomly choose between liquid and solid slot
                    selectedLiquid = random.nextBoolean();
                } else if (liquidslots > 0) {
                    selectedLiquid = true;
                } else {
                    Console.log(
                            "No more slots available for ingredients. This shouldn't happen.");
                }
            }
            // Select a random component from the usable components
            List<Component> craftableComponents = materials.getAvailableAndCraftableComponents(selectedLiquid);

            Component comp = ensureUnused(ingredients, craftableComponents, selectedLiquid);

            if (comp == null) {
                return ingredients;
            }

            // Add the ingredient to the list and generate the amount randomly
            // Use the UiValues to get the max stack size for the component
            // Multiply by 1000 for liquids
            int amount = random.nextInt(Math.min(50, liquidslots + solidslots));

            if (selectedLiquid) {
                amount = amount * 1000;
            }
            ingredients.add(new Mat(comp.getName(), amount));
        }
        return ingredients;
    }

    private static List<Mat> generateIngredients(String type) {
        List<Mat> ingredients = new ArrayList<>();

        // If it's free, we don't need no ingredients.
        if (random.nextInt(100) < UiValues.getFreeChance()) {
            return ingredients;
        }

        // Use random resources between 1 and the max selected value.
        int totalresources;
        if (type.equals("structure")) {
            totalresources = getBiasedRandomInt(1, UiValues.getMaxItemStruct(), UiValues.getInputBias());
        } else if (type.equals("milestone")) {
            totalresources = getBiasedRandomInt(1, UiValues.getMaxItemMile(), UiValues.getInputBias());
        } else {
            Console.log("Invalid recipe type: " + type);
            return ingredients;
        }

        for (int i = 0; i < totalresources; i++) {

            // Select a random component from the usable components
            List<Component> craftableComponents = materials.getAvailableAndCraftableComponents(false);

            Component comp = ensureUnused(ingredients, craftableComponents, false);

            if (comp == null) {
                return ingredients;
            }

            // Add the ingredient to the list and generate the amount randomly
            // Use the UiValues to get the max stack size for the component

            int amount;
            if (type.equals("structure")) {
                amount = random.nextInt(UiValues.getMaxItemStruct()) + 1;
            } else if (type.equals("milestone")) {
                amount = random.nextInt(UiValues.getMaxItemMile()) + 1;
            } else {
                Console.log("Invalid recipe type: " + type + ". This shouldn't happen.");
                return ingredients;
            }

            ingredients.add(new Mat(comp.getName(), amount));
        }
        return ingredients;
    }

    private static List<String> generateUnlocks(int numberOfUnlocks, String extraUnlocks, List<String> checkAlso) {
        List<String> unlocks = new ArrayList<>();

        // Add extra unlocks if it's not null
        if (extraUnlocks != null) {
            unlocks.add(extraUnlocks);
        }

        // Make a list with all possible randomizables
        List<Randomizable> itemList = materials.getUnavailableAndUncraftableRandomizables();
        itemList.removeAll(materials.getAllMilestones());

        for (String s : checkAlso) {
            itemList.remove(materials.getRandomizableByName(s));
        }

        // Add extra unlocks
        for (int i = 0; i < numberOfUnlocks; i++) {
            if (itemList.isEmpty()) {
                Console.log(
                        "No more unlocks available. This may happen once (in the last milestone), but if it happens more, it is a bug.");
                return unlocks;
            }
            Randomizable r = itemList.get(random.nextInt(itemList.size()));
            itemList.remove(r);
            unlocks.add(r.getName());
            materials.setRandomizableAvailable(r.getName(), true);
        }

        return unlocks;
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

        // Get slots for the station:
        // Cannot have liquid slots if waste is not 3
        // If waste is 0, the only product is the already added main product
        int liquidslots = (UiValues.getWaste() == 3) ? station.getLiquidOut() : 0;
        if (UiValues.getWaste() == 0) {
            return products;
        }
        int solidslots = station.getSolidOut();

        // Remove the slot of the main product, as it's already used
        if (mainliquid && UiValues.getWaste() == 3)
            liquidslots--;
        if (!mainliquid)
            solidslots--;

        // Use random resources between 1 and the max possible for the station
        int totalresources = getBiasedRandomInt(0, Math.min(0, liquidslots + solidslots), UiValues.getInputBias());

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
                        "Tried to add ingredients without slots. This should never happen.");
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

        Component component = new Component("placeholder in ensureUnused in SequenceGenerator", false, false, null);
        Boolean success = false;
        int loops = 0;

        while (!success) {

            if (loops > 200) {
                Console.log("Looping through the available materials for the " + loops
                        + "th time, trying to create a recipe with more materials than available. Happens with some seeds for early structures when \"Max items per structure\" is high. This shouldn't stop the randomizer from working.");
                return null;
            } else if (loops++ == 100 || craftableComponents.size() <= 0) {
                Console.log("Looping through the available materials for the " + loops
                        + "th time, \"Max recipes used\" is too low. The uses of all components will be increased by 1 to procceed");
                materials.refillComponents();
                craftableComponents = materials.getAvailableAndCraftableComponents(liquid);
            }

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
        return min + (int) ((max - min + 1) * r);
    }

    public static void generateSeed() {
        while (SequenceGenerator.seed <= 0) {
            SequenceGenerator.seed = random.nextLong();
        }
    }
}
