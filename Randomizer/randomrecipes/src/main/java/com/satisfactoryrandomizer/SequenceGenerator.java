package com.satisfactoryrandomizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.satisfactoryrandomizer.Storage.Data.Mat;
import com.satisfactoryrandomizer.Storage.Data.MilestoneSchematic;
import com.satisfactoryrandomizer.Storage.Data.Recipe;
import com.satisfactoryrandomizer.Storage.Materials;
import com.satisfactoryrandomizer.Storage.Randomizables.*;
import com.satisfactoryrandomizer.Storage.UiValues;

public class SequenceGenerator {
    private static long seed = UiValues.getSeed();
    private static Random random = new Random();
    private static Materials materials;
    private static Boolean liquidUnlocked = false;
    private static String lastObtainedStation = null;
    private static String firstStation = null;
    private static int nItems = 0;
    private static int mamChance = 0;
    private static Boolean mamDone = false;
    private static List<Randomizable> forcedCraftables = new ArrayList<>();

    // Delete all static values
    public static void reset() {
        seed = UiValues.getSeed();
        random = new Random();
        liquidUnlocked = false;
        lastObtainedStation = null;
        firstStation = null;
        nItems = 0;
        mamChance = 0;
        mamDone = false;
        forcedCraftables = new ArrayList<>();
    }

    public static void generateSequence() throws Exception {
        // Log the selected values
        Console.hiddenLog("\nSelected values:");
        UiValues.logAll();

        SequenceGenerator.seed = UiValues.getSeed();
        // Generate a random seed if not provided and set it to the random generator
        if (SequenceGenerator.seed == 0) {
            generateSeed();
            Console.log("\nGenerating seed because it wasn't provided.");
        }
        Console.importantLog("Using seed: " + SequenceGenerator.seed);
        SequenceGenerator.random = new Random(SequenceGenerator.seed);
        Console.hiddenLog("");

        // Delete RecipePatches folder before starting to avoid having extra alternates
        CreateJSON.deleteFiles();

        // Create the materials variable to store all materials / structures
        SequenceGenerator.materials = new Materials();
        materials.prepare(random.nextInt(1000000000));

        // Set the starting recipes
        List<String> startRecipes = new ArrayList<>(); // Desc_IronIngot

        startRecipes.addAll(Arrays.asList("Desc_IronIngot", "Desc_IronRod", "Desc_IronPlate"));
        for (String s : startRecipes) {
            setForceCraftable(materials.getRandomizableByName(s));
            materials.getRandomizableByName(s).setAvailable(true);

        }

        if (UiValues.getStartWithMiner()) {
            startRecipes.add("BP_ItemDescriptorPortableMiner");
            setForceCraftable(materials.getRandomizableByName("BP_ItemDescriptorPortableMiner"));
            materials.getRandomizableByName("BP_ItemDescriptorPortableMiner").setAvailable(true);
        }
        CreateJSON.saveStartingRecipes(startRecipes);

        // Set the number of items for percentage related things
        SequenceGenerator.nItems = materials.getAllRandomizables().size();

        // Create the starting array of items
        List<Randomizable> randomizables = materials.getAvailableButUncraftableRandomizables();

        // Set a random Crafting Station as the first one
        SequenceGenerator.firstStation = materials.getStations().get(random.nextInt(materials.getGenerators().size()))
                .getName();
        Console.log("\nFirst station: " + SequenceGenerator.firstStation);
        SequenceGenerator.lastObtainedStation = SequenceGenerator.firstStation;
        materials.doExtraChecks("power", Arrays.asList(SequenceGenerator.firstStation));
        materials.doExtraChecks("cable", Arrays.asList(SequenceGenerator.firstStation));
        materials.doExtraChecks("pole", Arrays.asList(SequenceGenerator.firstStation));
        materials.doExtraChecks("Tutorial_6", Arrays.asList(SequenceGenerator.firstStation));
        materials.getMilestoneByName("Tutorial_6").addExtraCheck(SequenceGenerator.firstStation);
        Console.hiddenLog("");

        // Add all extrachecks of Tutorial_6 to forcedunlocks, since they all need to be
        // craftable as soon as they're unlocked.
        for (String s : materials.getMilestoneByName("Tutorial_6").getExtraCheck()) {
            setForceCraftable(materials.getRandomizableByName(s));
        }

        // Generate the extraChecks after they have been set
        materials.fillExtraChecks();

        // Fixed Unlocks
        Console.hiddenLog("\nDoing fixed unlocks:");

        // Spread Tutorial_6 extrachecks among the other tutorials.
        for (String s : materials.getMilestoneByName("Tutorial_6").getExtraCheck()) {
            if (s.contains("Tutorial_")) {
                continue;
            }
            int n = random.nextInt(5) + 1;
            String milestone = "Tutorial_" + n;
            materials.getMilestoneByName(milestone).addFixedUnlock(s);
        }
        String st = "Tutorial_";
        for (int i = 1; i <= 6; i++) {
            String str = st + i;
            Console.hiddenLog("Fixed unlocks for " + str + ": " + materials.getMilestoneByName(str).getFixedUnlocks());
        }

        // Take extrachecks for milestones and add them as fixedunlocks to previous
        // milestones
        for (int i = 1; i <= 5; i++) {
            List<Milestone> milestonesChecks = materials.getMilestonesInPhase(i + 1);
            List<Milestone> milestones = materials.getMilestonesInPhase(i);
            Set<Randomizable> items = new HashSet<>();

            for (Milestone m : milestonesChecks) {
                for (String s : m.getExtraCheck()) {
                    Randomizable item = materials.getRandomizableByName(s);
                    if (item != null || !(item instanceof Milestone)) {
                        items.add(item);
                    }
                }
            }

            if (milestonesChecks.isEmpty()) {
                Console.hiddenLog("No milestones in phase " + (i + 1) + " (expected for phase 6)" +
                        "\nAdding elevator parts for that phase instead.");
                for (Component e : materials.getElevatorPartsInPhase(i)) {
                    items.add(e);
                }
            }

            for (Randomizable item : items) {
                Milestone milestone = milestones.get(random.nextInt(milestones.size()));
                milestone.addFixedUnlock(item.getName());
                Console.hiddenLog("Added " + item.getName() + " to " + milestone.getName());
                materials.setfixedRandomizable(item);
                setForceCraftable(item);
                if (milestones.size() > items.size()) {
                    milestones.remove(milestone);
                }
            }

        }

        // Spread the Randomizables evenly across the milestones
        int milestonesAvailable = materials.getAllMilestones().size();
        int nDistributableRandomizables = materials.getAllNonMilestonedRandomizables();
        int distributed = 0;

        Console.hiddenLog("\nSetting up randomizables per milestone:");
        for (Milestone mile : materials.getAllMilestones()) {
            int nUnlocksMilestone = (nDistributableRandomizables - distributed) / milestonesAvailable;
            int distributing = (nUnlocksMilestone - 1 + random.nextInt(3));
            mile.setnRecipes(distributing);
            Console.hiddenLog("    Recipes in " + mile.getName() + ": " + mile.getnRecipes());
            distributed += distributing;
            milestonesAvailable--;
        }

        Console.log("Distributed " + (nDistributableRandomizables) + " randomizables across "
                + materials.getAllMilestones().size() + " milestones.");

        logAvailability("Status at start:");

        // Main loop, runs until there's nothing left to randomize
        Console.cheatsheet("\nItems in order of generation (ingredients precede the recipe):");
        int cap = 3000;
        int iteration = 0;
        Boolean finishedProgression = false;
        while (!randomizables.isEmpty() && ++iteration < cap) {

            // Pick a random item to randomize
            int size = randomizables.size();
            Randomizable randomizable = randomizables.get(random.nextInt(size));

            // Starting value will include all ores.
            Console.log("Creating one of the remaining " + size + " available items. Done "
                    + materials.getCraftableRandomizables().size() + "/" + SequenceGenerator.nItems + " items.");

            // If there are items that need to be made craftable, give them priority
            randomizable = forceCraftable(randomizable);

            if (randomizable instanceof Component) {
                generateComp((Component) randomizable, materials.getCraftableRandomizables().size(), "component");
            } else if (randomizable instanceof CraftStation) {
                generateStructure((CraftStation) randomizable, "structure");
            } else if (randomizable instanceof Milestone) {

                // Chance to skip the milestone to make them appear later
                Boolean skipMilestone = false;
                for (int i = 0; i < UiValues.getForceLongGameBias(); i++) {
                    skipMilestone = random.nextBoolean();
                    if (skipMilestone || size > 1) {
                        break;
                    }
                }

                if (!finishedProgression
                        && materials.getAllMilestones().size() - materials.getCraftableMilestones().size() <= 1) {
                    ((Milestone) randomizable).setnRecipes(2 * (((Milestone) randomizable).getnRecipes() + 10));
                    Console.log("Increasing milestone " + randomizable.getName() + " recipes to "
                            + ((Milestone) randomizable).getnRecipes() + " to ensure everything is craftable.");
                }
                generateMilestone((Milestone) randomizable, materials.getCraftableRandomizables().size(), "milestone");
            } else if (randomizable instanceof Structure) {
                generateStructure((Structure) randomizable, "structure");
            } else {
                Console.log("Unknown item type: " + randomizable);
                Console.log("Exiting the randomize loop.");
                break;
            }

            // Remove the name of the created randomizable from any randomizables for which
            // it is an estraCheck
            if (!randomizable.getCheckAlso().isEmpty())
                materials.doExtraChecks(randomizable.getName(), randomizable.getCheckAlso());

            // Update the list (the item is set craftable by the methods above).
            randomizables.clear();
            randomizables = materials.getAvailableButUncraftableRandomizables();

            // Add recipes without conditions
            randomizables.removeAll(materials.getHD());
            if (randomizables.size() <= 0) {
                finishedProgression = true;
                randomizables.addAll(materials.getUncraftableHD());
                randomizables.addAll(materials.getUncraftableDepotMilestones());
            }

            if ((randomizable instanceof Milestone)) {
                logAvailability("Status after unlock generation:");
            }
        }
        // End of main loop

        Console.log("Remaining " + randomizables.size() + " items. Done "
                + materials.getCraftableRandomizables().size() + "/" + SequenceGenerator.nItems + " items.");

        if (iteration >= cap) {
            Console.importantLog("Randomization failed, loop cap reached.");
        } else {
            Console.importantLog("Randomization completed. Remaining loops to cap: " + (cap - iteration));
        }
        // Generate Depot Milestone recipes getDepotMilestones

        if (!materials.getUncraftableRandomizables().isEmpty()) {
            Console.importantLog("Missing Unlocks (should be playable, but some items will have vanilla values): ");
            for (Randomizable elem : materials.getUncraftableRandomizables()) {
                if (elem.getName() != null) {
                    Console.log(elem.getName());
                } else {
                    Console.log(elem.getPath());
                }
            }
            Console.importantLog(
                    "These items didn't get randomized properly, they may be missing from the game or have their default recipe."
                            + "\nEither way, as long as none of these are Space Elevator parts, the game is completable without them.");
        }

        Console.importantLog("Used Seed: " + SequenceGenerator.seed + " Checksum = " + random.nextInt(UiValues.addAll())
                + " | If you're playing multiplayer and you are all randomizing separately, these should be the same for all players.");
    }

    private static void generateMilestone(Milestone milestone, int addedItems, String type) {
        List<Mat> mats;
        if (type.equals("depot")) {
            mats = generateIngredients("milestone", addedItems);
        } else if (milestone.getName().contains("Tutorial_")) {
            mats = generateIngredients("tutorial", addedItems);
        } else {
            mats = generateIngredients(type, addedItems);
        }
        List<String> checkAlso = new ArrayList<>();

        for (Mat m : mats) {
            Console.cheatsheet("    Ingredient: " + m.getName() + " | Amount: " + m.getAmount());
        }

        // Increase the chance of the milestone having the MAM as fixed unlock
        // (increased independently as both MAM and Store use it)
        SequenceGenerator.mamChance += 2;

        if (!mamDone) {
            // Add MAM as fixed unlock if the random number is below the chance, ensuring
            // it's added at all
            if (SequenceGenerator.mamChance > random.nextInt(100)) {
                milestone.addFixedUnlock("Desc_Mam");
                mamDone = true;
            }
        }

        for (Object mile : milestone.getCheckAlso()) {
            checkAlso.add((String) mile);
        }

        // Force Tutorials to have at least some of the tutorial requirements to ensure
        // they are craftable by the time they are needed for Tutorial_6
        if (milestone.getName().contains("Tutorial_")) {
            Milestone tut6 = materials.getMilestoneByName("Tutorial_6");

            // Adds up to 2 fixed unlocks per tutorial and 10 to Tutorial_5 (increase the
            // chance extrachecks are done)
            int cont = 2;
            if (milestone.getName().equals("Tutorial_5")) {
                cont = 10;
            }
            List<String> extraCheck;

            while (tut6.getExtraCheck().size() > 0 && cont-- > 0) {
                // Remove the extra check belonging to a milestone and get an extra check at
                // random
                extraCheck = new ArrayList<>(tut6.getExtraCheck());
                extraCheck.removeIf(s -> s.contains("Tutorial_"));

                if (!extraCheck.isEmpty()) {
                    String s = extraCheck.get(random.nextInt(extraCheck.size()));
                    milestone.addFixedUnlock(s);
                }
            }

        }

        List<String> unlocks;
        if (type.equals("depot")) {
            unlocks = generateUnlocks(milestone.getnRecipes(), milestone.getFixedUnlocks(),
                    milestone.getPhase(), false);
        } else {
            unlocks = generateUnlocks(milestone.getnRecipes(), milestone.getFixedUnlocks(),
                    milestone.getPhase(), false);
        }

        MilestoneSchematic recipe = new MilestoneSchematic(
                unlocks, // Unlocks
                mats, // Ingredients
                "Schematic_" + milestone.getName() + ".json", // Filename
                random.nextInt(UiValues.getMaxTimeMile())// Time
        );

        // Create Recipe JSON file
        CreateJSON.saveMilestoneAsJson(recipe, milestone.getRecipePath());

        Console.cheatsheet("    " + milestone.getName() + " can be made.");

        // Mark the component as craftable
        materials.setMilestoneCraftable(milestone.getName(), true);
    }

    private static void generateStructure(Randomizable structure, String type) {
        // Needed variables. Prod will always only have the structure, but has to be a
        // string regardless.
        if (structure instanceof EssentialStructure) {
            if (((EssentialStructure) structure).isCheap()) {
                type = "cheap";
            }
        }

        List<Mat> mats = generateIngredients(type, 0);

        for (Mat m : mats) {
            Console.cheatsheet("    Ingredient: " + m.getName() + " | Amount: " + m.getAmount());
        }

        Recipe recipe = new Recipe(
                mats, // Doesn't apply
                mats, // Ingredients
                "Recipe_" + structure.getName() + ".json", // Filename
                "", // Doesn't apply
                1, // Doesn't apply
                1.0 // Doesn't apply
        );

        // Create Recipe JSON file
        CreateJSON.saveStructureAsJson(recipe, structure.getRecipePath());

        Console.cheatsheet(structure.getName() + " can be made.");

        // Mark the component as craftable
        materials.setRandomizableCraftable(structure.getName(), true);

        // Update the last obtained station
        if (structure instanceof CraftStation) {
            SequenceGenerator.lastObtainedStation = structure.getName();
        }
    }

    private static void generateComp(Component comp, int addedItems, String type) {
        // Select a random crafting Station
        CraftStation station = materials.getRandomAvailableAndCraftableStation(random.nextInt());
        if (station == null) {
            Console.hiddenLog("Station is: " + station + " it will be changed to: " + firstStation);
            station = materials.getStationByName(firstStation);
        }

        // If the station is not the last one obtained, reroll to increase station
        // variability.
        int count = UiValues.getStationBias();

        // Check if there's even a last obtained station before checking it's name.
        while (!station.getName().equals(SequenceGenerator.lastObtainedStation)
                && !station.getName().equals(firstStation) && count-- > 0) {
            station = materials.getRandomAvailableAndCraftableStation(random.nextInt());
        }
        // Ensure it has output for the liquid component.
        if (comp.isLiquid() && !station.getName().equals(firstStation)) {
            while (station.getLiquidOut() < 1 && count++ < 100) {
                station = materials.getRandomAvailableAndCraftableStation(random.nextInt());
            }
        } else if (comp.isLiquid()) {
            count = 100;
        }

        // If no station with liquid output is available for liquid creation, return
        // without creating the recipe.
        if (count >= 100) {
            Console.hiddenLog("No station with liquid output is available for: " + comp.getName());
            return;
        }

        // Generate ingredients for alternate or base recipe.
        Boolean alternate = false;
        if (comp.getName() == null) {
            alternate = true;
        }

        List<Mat> mats = generateIngredientsComp(station, alternate);

        for (Mat m : mats) {
            Console.cheatsheet("    Ingredient: " + m.getName() + " | Amount: " + m.getAmount());
        }

        List<Mat> prod = new ArrayList<>();

        // Add the main product multiplying the value range by 1000 for liquids.
        // Then add the rest of the products if the option is enabled and there's space.
        // Don't add main product if there isn't one (alternate recipes).
        if (station.getLiquidIn() + station.getSolidIn() > 0 && comp.getName() != null) {
            if (comp.isLiquid()) {
                prod.add(new Mat(comp.getName(), (random.nextInt(Math.min(comp.getStack(), UiValues.getMaxProdCraft())) + 1) * 1000));
                prod.addAll(generateProducts(station, true));
            } else {
                prod.add(new Mat(comp.getName(), random.nextInt(Math.min(comp.getStack(), UiValues.getMaxProdCraft())) + 1));
                prod.addAll(generateProducts(station, false));
            }
        } else {
            prod.addAll(generateProducts(station, null));
        }

        for (Mat m : prod) {
            Console.cheatsheet("    Product: " + m.getName() + " | Amount: " + m.getAmount());
        }

        double handSpeed = random.nextDouble() * (UiValues.getHandcraftSpeed()[1] - UiValues.getHandcraftSpeed()[0])
                + UiValues.getHandcraftSpeed()[0];
        double time = random.nextDouble() * (UiValues.getMaxTimeCraft());

        Recipe recipe;

        // Give the recipe the name of the main component if not alternate and a generic
        // name otherwise
        String filename = "Recipe_" + comp.getName() + ".json";
        if (comp.getName() == null) {
            filename = "Recipe_Alternate" + addedItems + ".json";
        }

        recipe = new Recipe(
                prod, // Products
                mats, // Ingredients
                filename, // Filename
                station.getBuilderPath(), // Station
                time, // Time
                handSpeed // Handcraft speed
        );

        String category = "Category" + (random.nextInt(10) + 1);

        // Create Recipe JSON file (RecipeVN if it goes into a machine with variable
        // consumption)
        if (station.getBuilderPath().equals("Build_HadronCollider")
                || station.getBuilderPath().equals("Build_Converter")
                || station.getBuilderPath().equals("Build_QuantumEncoder")) {

            // Amount of energy used by the recipe, the value is randomized with min and max
            // from 10 to 750 based on the percentage of recipes unlocked
            // the rounding to int has losses, but we don't need such precise values.

            double max = (double) addedItems * 740 / SequenceGenerator.nItems;
            int energy = (int) (random.nextDouble() * max + 10);

            CreateJSON.saveRecipeVNAsJson(recipe, comp.getRecipePath(), SequenceGenerator.firstStation, category,
                    energy);
        } else {
            CreateJSON.saveRecipeAsJson(recipe, comp.getRecipePath(), SequenceGenerator.firstStation, category);
        }

        // Mark the component as craftable and update the list of available but
        // uncraftable components
        if (comp.getName() != null) {
            materials.setRandomizableCraftable(comp.getName(), true);
        } else {
            materials.setRandomizableCraftablePath(comp.getPath(), true);
        }
        if (comp.isLiquid()) {
            SequenceGenerator.liquidUnlocked = true;
        }

        if (comp.getName() != null) {
            Console.cheatsheet(comp.getName() + " is made in " + station.getName()); // name will be null for alternate
        } else {
            Console.cheatsheet(comp.getPath() + " is made in " + station.getName());
        }
    }

    private static void logAvailability(String title) {
        if (!UiValues.getAdvLog()) {
            return;
        }

        Console.advLog("\n" + title);
        for (Randomizable r : materials.getAllRandomizables()) {
            if (r.getName() == null) {
                continue;
            }

            // Align:
            String name = r.getName();
            int size = name.length();
            while (size++ < 50) {
                name = name + " ";
            }
            String avail;
            String craftab;
            if (r.trueAvailable()) {
                avail = "Available    ";
            } else {
                avail = "Not Available";
            }
            if (r.isCraftable()) {
                craftab = "Craftable    ";
            } else {
                craftab = "Not Craftable";
            }
            String log = name + " | " + avail + " | " + craftab;

            if (!r.getExtraCheck().isEmpty()) {
                log += " | extraChecks: " + r.getExtraCheck();
            }
            if (!r.getCheckAlso().isEmpty()) {
                log += " | checkAlso: " + r.getCheckAlso();
            }
            Console.advLog(log);
        }
        for (Randomizable r : forcedCraftables) {
            String name = r.getName();
            int size = name.length();
            while (size++ < 50) {
                name = name + " ";
            }
            Console.advLog("Randomizable to be forced: " + name + " | Availability: " + r.isAvailable());
        }
        Console.advLog("");
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
    private static List<Mat> generateIngredientsComp(CraftStation station, Boolean alternate) {
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
        int totalresources = generateGaussianBiasedNumber(1, liquidslots + solidslots, UiValues.getInputBias());

        for (int i = 0; i < totalresources; i++) {
            Boolean selectedLiquid = false;

            // If any liquid has been unlocked we check for them, otherwise, we don't.
            if (SequenceGenerator.liquidUnlocked) {
                if (liquidslots > 0 && solidslots > 0) {
                    // Randomly choose between liquid and solid slot
                    selectedLiquid = random.nextBoolean();
                } else if (liquidslots > 0) {
                    selectedLiquid = true;
                } else if (solidslots > 0) {
                    selectedLiquid = false;
                } else {
                    Console.log(
                            "No more slots available for ingredients. This shouldn't happen.");
                }
            }

            // Select a random component from the usable components
            // The first attempt in alternate will use all ingredients (available or not),
            // from there, it will roll only craftable
            List<Component> craftableComponents;
            if (alternate) {
                craftableComponents = materials.getAllComponents(selectedLiquid);
                if (!selectedLiquid) {
                    craftableComponents.addAll(materials.getAvailableAnimal());
                    craftableComponents.addAll(materials.getAvailableLimited());
                }
            } else {
                craftableComponents = materials.getAvailableAndCraftableComponents(selectedLiquid);
            }

            Component comp = ensureUnused(ingredients, craftableComponents, selectedLiquid, alternate);

            // Remove the used slot
            if (selectedLiquid) {
                liquidslots--;
            } else {
                solidslots--;
            }

            if (comp == null) {
                return ingredients;
            }

            // Add the ingredient to the list and generate the amount randomly
            // Use the UiValues to get the max stack size for the component
            // Multiply by 1000 for liquids
            int amount = random.nextInt(Math.min(comp.getStack(), UiValues.getMaxStackCraft())) + 1;

            if (selectedLiquid) {
                amount = amount * 1000;
            }
            ingredients.add(new Mat(comp.getName(), amount));
        }

        return ingredients;
    }

    private static List<Mat> generateIngredients(String type, int addedItems) {
        List<Mat> ingredients = new ArrayList<>();

        // If it's free, we don't need no ingredients.
        if (random.nextInt(100) < UiValues.getFreeChance()) {
            return ingredients;
        }

        // Use random resources between 1 and the max selected value.
        int totalresources;
        if (type.equals("structure")) {
            totalresources = random.nextInt(UiValues.getMaxItemStruct());
        } else if (type.equals("milestone") || type.equals("tutorial")) {
            totalresources = random.nextInt(UiValues.getMaxItemMile());
        } else if (type.equals("cheap")) {
            totalresources = 1;
        } else {
            Console.log("Invalid recipe type: " + type);
            return ingredients;
        }

        for (int i = 0; i < totalresources; i++) {

            // Select a random component from the usable components
            List<Component> craftableComponents = materials.getAvailableAndCraftableComponents(false);

            // Add animal parts for milestones (but not for those among the first 100
            // recipes)
            if (type.equals("milestone") && materials.getCraftableRandomizables().size() > 300) {
                craftableComponents.addAll(materials.getAvailableAnimal());
            }

            Component comp = ensureUnused(ingredients, craftableComponents, false, false);

            if (comp == null) {
                return ingredients;
            }

            // Add the ingredient to the list and generate the amount randomly
            // Use the UiValues to get the max stack size for the component
            int amount;
            if (type.equals("structure")) {
                amount = random.nextInt(UiValues.getMaxStackStruct()) + 1;
            } else if (type.equals("milestone") || type.equals("tutorial")) {
                int bias = addedItems * 100 / SequenceGenerator.nItems;
                int max = UiValues.getMaxStackMile();
                for (Component c : materials.getAvailableAnimal()) {
                    if (c.equals(comp)) {
                        max = (int) Math.sqrt(max); // reduce the amount of animal parts
                    }
                }
                amount = generateGaussianBiasedNumber(1, max, UiValues.getProgressiveBias() ? bias : -1);
            } else if (type.equals("cheap")) {
                amount = random.nextInt(2) + 1;
            } else {
                Console.log("Invalid recipe type: " + type + ". This shouldn't happen.");
                return ingredients;
            }

            ingredients.add(new Mat(comp.getName(), amount));
        }
        return ingredients;
    }

    /**
     * Generates a list of random unlocks for a milestone.
     * 
     * @param numberOfUnlocks The number of random unlocks to generate.
     * @param extraUnlocks    An extra unlock to add to the list if it's not null.
     * @return A list of random unlocks.
     */
    private static List<String> generateUnlocks(int numberOfUnlocks, List<String> fixedUnlocks, int phase,
            Boolean depot) {
        List<String> unlocks = new ArrayList<>();

        // Add extra unlocks if it's not null
        if (fixedUnlocks != null) {

            // Removing the tutorials (they may be in fixed unlocks and can't be generated
            // here) and changing from the name to the recipe (it's what the milestone uses)
            List<String> fixUnl = fixedUnlocks;
            fixUnl.removeIf(s -> s.contains("Tutorial_"));
            List<Randomizable> fixUnlock = new ArrayList<>();

            for (String s : fixUnl) {
                fixUnlock.add(materials.getRandomizableByName(s));
            }

            for (Randomizable unlock : fixUnlock) {
                materials.setRandomizableAvailable(unlock.getName(), true);
                unlocks.add(unlock.getPath());
            }
        }

        // Make a list with all possible randomizables
        List<Randomizable> itemList = materials.getUnavailableAndUncraftableRandomizables();

        // Add unlocks
        for (int i = 0; i < numberOfUnlocks; i++) {
            if (itemList.isEmpty()) {
                Console.log(
                        "No more unlocks available. This can safely be ignored if it appears below the message saying a milestone had it's recipes increased.");
                return unlocks;
            }
            Randomizable r = itemList.get(random.nextInt(itemList.size()));
            itemList.remove(r);
            unlocks.add(r.getPath());
            if (r.getName() == null) {
                materials.setRandomizableAvailablePath(r.getPath(), true);
            } else {
                materials.setRandomizableAvailable(r.getName(), true);
            }

            if (r.getName() == null) {
                Console.hiddenLog("    Unlocked: " + r.getPath());
            } else {
                Console.hiddenLog("    Unlocked: " + r.getName());
            }

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
        // Cannot have liquid slots if waste is not 3 or more
        // If waste is 0, the only product is the already added main product
        int liquidslots;
        int solidslots;
        if (mainliquid != null) {
            liquidslots = (UiValues.getWaste() >= 3) ? station.getLiquidOut() : 0;
            if (UiValues.getWaste() == 0) {
                return products;
            }
            solidslots = station.getSolidOut();
        } else {
            liquidslots = station.getLiquidOut();
            solidslots = station.getSolidOut();
        }

        // Remove the slot of the main product, as it's already used
        // Don't remove slots for alternate recipes, as they don't have main product
        if (mainliquid != null) {
            if (mainliquid && UiValues.getWaste() >= 3)
                liquidslots--;
            if (!mainliquid)
                solidslots--;
        }

        // Remove the liquid slots if liquids haven't been unlocked (aka, has at least
        // pipes)
        if (!liquidUnlocked) {
            liquidslots = 0;
        }

        // Use random resources between 1 and the max possible for the station
        int totalresources;
        if(UiValues.getWaste() >= 4){
            totalresources = liquidslots + solidslots;
        } else {
            totalresources = random.nextInt(liquidslots + solidslots + 1);
        }
        if (mainliquid == null && totalresources == 0) {
            totalresources++;
        }

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
            // Select a random component from the components
            List<Component> availableComponents = materials.getAllComponents(selectedLiquid);
            availableComponents.addAll(materials.getAvailableAnimal());
            availableComponents.addAll(materials.getAvailableLimited());

            Component component = availableComponents.get(random.nextInt(availableComponents.size()));

            // Add the ingredient to the list and generate the amount randomly.
            // Use the UiValues to get the max stack size for the product.
            int amount = random.nextInt(Math.min(component.getStack(), UiValues.getMaxProdCraft())) + 1;

            if (component.isLiquid()) {
                amount = amount * 1000;
            }
            products.add(new Mat(component.getName(), amount));

            if (component.isLiquid()) {
                liquidslots--;
            } else {
                solidslots--;
            }
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
    private static Component ensureUnused(List<Mat> list, List<Component> craftableComponents, Boolean liquid,
            Boolean alternate) {

        Component component = new Component("placeholder in ensureUnused in SequenceGenerator", null, false, false,
                false);
        Boolean success = false;
        int loops = 0;

        while (!success) {

            if (loops >= 200) {
                Console.log("Looping through the available materials for the " + loops
                        + "th time, trying to create a recipe with more materials than available. Happens with some seeds for early structures when \"Max number of different items for building\" is high. This shouldn't stop the randomizer from working.");
                return null;
            } else if (++loops == 100 || craftableComponents.size() <= 0) {
                Console.log("Looping through the available materials for the " + loops
                        + "th time, \"Max recipes used\" is too low. The uses of all available components will be increased by 1 to procceed");
                materials.refillComponents();
                craftableComponents = materials.getAvailableAndCraftableComponents(liquid);
                if (craftableComponents.size() < list.size()) {
                    Console.hiddenLog("All available materials used, can't add more.");
                }
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

        if (component.isAvailable() || alternate) {
            materials.useComponent(component.getName());
        } else {
            Console.log(
                    "For some reason this non alternate recipe is trying to use an unavailable component: "
                            + component.getName());
        }

        return component;
    }

    public static int generateGaussianBiasedNumber(double min, double max, double bias) {
        if(max == min){
            return (int) min;
        }
        if (bias < (-1) || bias > 100) {
            throw new IllegalArgumentException("Bias must be within the range [" + (-1) + ", " + 100 + "]");
        }
        if (bias == (-1)) {
            return ((int) (random.nextDouble() * (max - min) + min));
        }

        double usedBias = (max - min) * bias / 100;
        double spread = usedBias - min;
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

    public static void generateSeed() {
        while (SequenceGenerator.seed <= 0) {
            SequenceGenerator.seed = random.nextLong();
        }
    }

    /**
     * Adds a randomizable to the list of forced craftables.
     * These randomizables will be made craftable ASAP.
     * 
     * @param randomizables The randomizable to add to the list.
     */
    private static void setForceCraftable(Randomizable randomizable) {
        SequenceGenerator.forcedCraftables.add(randomizable);
        Console.hiddenLog(randomizable.getName() + " added to the list of forced craftables.");
    }

    /**
     * Ensures that important randomizables are set craftable ASAP.
     *
     * @param randomizable The randomizable currently selected.
     * @return A randomizable item that needs to be craftable ASAP, or the original
     *         if none needs it.
     */

    private static Randomizable forceCraftable(Randomizable randomizable) {
        for (Randomizable r : SequenceGenerator.forcedCraftables) {
            if (r.isAvailable()) {
                SequenceGenerator.forcedCraftables.remove(r);
                Console.hiddenLog(r.getName() + " forced to be craftable.");
                return r;
            }
        }
        return randomizable;
    }

}
