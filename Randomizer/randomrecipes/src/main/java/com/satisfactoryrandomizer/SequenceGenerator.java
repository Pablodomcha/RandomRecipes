package com.satisfactoryrandomizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import java.util.Collections;

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

    public static void generateSequence() throws Exception {
        // Log the selected values
        Console.hiddenLog("\nSelected values:");
        UiValues.logAll();
        Console.hiddenLog("\n");

        // Create the materials variable to store all materials / structures
        SequenceGenerator.materials = new Materials();
        materials.prepare(random.nextInt(10000));

        // Set the number of items for percentage related things
        SequenceGenerator.nItems = materials.getAllRandomizables().size(); // Removing iron since it starts unlocked.

        logAvailability("Status at start:");

        // Generate the extraChecks
        materials.fillExtraChecks();

        // Generate a random seed if not provided and set it to the random generator
        if (SequenceGenerator.seed == 0) {
            generateSeed();
            Console.log("Generating seed because it wasn't provided.");
        }
        Console.log("Using seed: " + SequenceGenerator.seed);
        SequenceGenerator.random = new Random(SequenceGenerator.seed);
        Console.cheatsheet(
                "\nItems in order of generation (only ensured way to reach a material is to have all the previous):");

        // Create the starting array of items
        List<Randomizable> randomizables = materials.getAvailableButUncraftableRandomizables();

        // Set a random Crafting Station as the first one
        SequenceGenerator.firstStation = materials.getStations().get(random.nextInt(materials.getGenerators().size()))
                .getName();
        Console.log("First station: " + SequenceGenerator.firstStation);
        SequenceGenerator.lastObtainedStation = SequenceGenerator.firstStation;
        materials.setStructureAvailable(SequenceGenerator.firstStation, true);
        materials.doExtraChecks("power", Arrays.asList(SequenceGenerator.firstStation));
        materials.doExtraChecks("cable", Arrays.asList(SequenceGenerator.firstStation));
        materials.doExtraChecks("pole", Arrays.asList(SequenceGenerator.firstStation));
        generateStructure(materials.getStationByName(SequenceGenerator.firstStation), "structure");

        // Spread the Randomizables evenly across the milestones
        int milestonesAvailable = materials.getAllMilestones().size();
        int nDistributableRandomizables = materials.getAllNonMilestonedRandomizables();
        int distributed = 0;

        for (Milestone mile : materials.getAllMilestones()) {
            int nUnlocksMilestone = (nDistributableRandomizables - distributed) / milestonesAvailable;
            int distributing = (nUnlocksMilestone + random.nextInt(2));
            mile.setnRecipes(distributing);
            Console.hiddenLog("Recipes in " + mile.getName() + ": " + mile.getnRecipes());
            distributed += distributing;
            milestonesAvailable--;
        }
        Console.log("Distributed " + (nDistributableRandomizables) + " randomizables across "
                + materials.getAllMilestones().size() + " milestones.");

        for (Randomizable r : materials.getCraftableRandomizables()) {
            Console.log("Craftable from the start: " + r.getName());
        }

        // Main loop, runs until there's nothing left to randomize
        int cap = 2000;
        int iteration = 0;
        while (!randomizables.isEmpty() && ++iteration < cap) {

            // Pick a random item to randomize
            int size = randomizables.size();
            Randomizable randomizable = randomizables.get(random.nextInt(size));

            // Starting value will include all ores.
            Console.log("Creating one of the remaining " + size + " available items. Done "
                    + materials.getCraftableRandomizables().size() + "/" + SequenceGenerator.nItems + " items.");

            if (randomizable instanceof Component) {
                if (randomizable instanceof ElevatorPart) {
                    if (((ElevatorPart) randomizable).addWhen() > materials.getPhase()) {
                        Console.hiddenLog("Avoided " + randomizable.getName() + " in phase " + materials.getPhase()
                                + " since addWhen is " + ((ElevatorPart) randomizable).addWhen()
                                + " and material phase is " + materials.getPhase());
                        continue;
                    } else {
                        materials.setPhase();
                    }
                }
                generateComp((Component) randomizable, materials.getCraftableRandomizables().size(), "component");
            } else if (randomizable instanceof CraftStation) {
                generateStructure((CraftStation) randomizable, "structure");
            } else if (randomizable instanceof Milestone) {

                // Chance to skip the milestone to make them appear later
                Boolean skipMilestone = false;
                for (int i = 0; i < UiValues.getForceLongGameBias(); i++) {
                    skipMilestone = random.nextBoolean();
                }
                if (skipMilestone) {
                    break;
                }

                if (materials.getAllMilestones().size() - materials.getCraftableMilestones().size() <= 1) {
                    ((Milestone) randomizable).setnRecipes(2 * ((Milestone) randomizable).getnRecipes());
                    Console.log("Increasing milestone " + randomizable.getName() + " recipes to "
                            + ((Milestone) randomizable).getnRecipes() + " to ensure everything is craftable.");
                }
                generateMilestone((Milestone) randomizable, "milestone");
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

            // Update the list (the item is set craftable by the methods above)
            randomizables.clear();
            randomizables = materials.getAvailableButUncraftableRandomizables();
        }

        Console.log("Remaining " + randomizables.size() + " items. Done "
                + materials.getCraftableRandomizables().size() + "/" + SequenceGenerator.nItems + " items.");

        if (iteration >= cap) {
            Console.importantLog("Randomization failed, loop cap reached.");
        } else {
            Console.importantLog("Randomization completed. Remaining loops to cap: " + (cap - iteration));
        }

        if (!materials.getUncraftableRandomizables().isEmpty()) {
            Console.importantLog("Missing Unlocks (should be playable, but some items will have vanilla values): ");
            for (Randomizable elem : materials.getUncraftableRandomizables()) {
                Console.log(elem.getName());
            }
            Console.importantLog(
                    "These items didn't get randomized properly, they may be missing from the game or have their default recipe."
                            + "\nEither way, as long as none of these are Space Elevator parts, the game is completable without them.");
        }

        Console.importantLog("Checksum = " + random.nextInt(UiValues.addAll())
                + " | If you're playing multiplayer and you are all randomizing separately, this should be the same for all players.");
    }

    private static void generateMilestone(Milestone milestone, String type) {

        List<Mat> mats = generateIngredients(type);
        List<String> checkAlso = new ArrayList<>();

        for (Object mile : milestone.getCheckAlso()) {
            checkAlso.add((String) mile);
        }

        // Force Tutorials to have at least some of the to ensure they are
        // craftable by the time they are needed for Tutorial_6
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

        // Add the first crafting station in Tutorial_6 if it wasn't added earlier
        if (milestone.getName().equals("Tutorial_6")) {
            milestone.addFixedUnlock(firstStation);
        }

        logAvailability("Status before unlock generation:");

        List<String> unlocks = generateUnlocks(milestone.getnRecipes(), milestone.getFixedUnlocks(),
                milestone.getPhase());

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

        logAvailability("Status after creating milestone: " + milestone.getName());
    }

    private static void generateStructure(Randomizable structure, String type) {
        // Needed variables. Prod will always only have the structure, but has to be a
        // string regardless.
        if (structure instanceof EssentialStructure) {
            if (((EssentialStructure) structure).isCheap()) {
                type = "cheap";
            }
        }

        List<Mat> mats = generateIngredients(type);

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

        Console.cheatsheet(structure.getName() + " can be made");

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
        while (!station.getName().equals(SequenceGenerator.lastObtainedStation) && count-- > 0) {
            station = materials.getRandomAvailableAndCraftableStation(random.nextInt());
        }
        // Ensure it has output for the liquid component.
        while (station.getLiquidOut() < 1 && comp.isLiquid() && count++ < 100) {
            station = materials.getRandomAvailableAndCraftableStation(random.nextInt());
        }

        // If no station with liquid output is available for liquid creation, return
        // without creating the recipe.
        if (count >= 100) {
            Console.hiddenLog("No station with liquid output is available for: " + comp.getName());
            return;
        }

        List<Mat> mats = generateIngredientsComp(station);
        List<Mat> prod = new ArrayList<>();

        // Add the main product multiplying the value range by 1000 for liquids.
        // Then add the rest of the products if the option is enabled and there's space.
        // Don't add main product if there isn't one (alternate recipes).
        if (station.getLiquidIn() + station.getSolidIn() > 0 && comp.getName() != null) {
            if (comp.isLiquid()) {
                prod.add(new Mat(comp.getName(), (random.nextInt(UiValues.getMaxStackCraft()) + 1) * 1000));
                prod.addAll(generateProducts(station, true));
            } else {
                prod.add(new Mat(comp.getName(), random.nextInt(UiValues.getMaxStackCraft()) + 1));
                prod.addAll(generateProducts(station, false));
            }
        } else {
            generateProducts(station, null);
        }

        double handSpeed = random.nextDouble() * (UiValues.getHandcraftSpeed()[1] - UiValues.getHandcraftSpeed()[0])
                + UiValues.getHandcraftSpeed()[0];
        double time = random.nextDouble() * (UiValues.getMaxTimeCraft());

        Recipe recipe;

        // Give the recipe the name of the main component if not alternate and a generic
        // name otherwise
        recipe = new Recipe(
                prod, // Products
                mats, // Ingredients
                "Recipe_" + comp.getName() + ".json", // Filename
                station.getBuilderPath(), // Station
                time, // Time
                handSpeed // Handcraft speed
        );

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

            CreateJSON.saveRecipeVNAsJson(recipe, comp.getRecipePath(), SequenceGenerator.firstStation,
                    energy);
        } else {
            CreateJSON.saveRecipeAsJson(recipe, comp.getRecipePath(), SequenceGenerator.firstStation);
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

        Console.cheatsheet(comp.getName() + " is made in " + station.getName()); // name will be null for alternate
    }

    private static void logAvailability(String title) {
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
        Console.advLog("\n");
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
        int totalresources = getBiasedRandomInt(1, liquidslots + solidslots, UiValues.getInputBias());

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
            List<Component> craftableComponents = materials.getAvailableAndCraftableComponents(selectedLiquid);

            Component comp = ensureUnused(ingredients, craftableComponents, selectedLiquid);

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
        for (Mat m : ingredients) {
            Console.cheatsheet("Ingredient: " + m.getName() + " | Amount: " + m.getAmount());
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
        } else if (type.equals("cheap")) {
            totalresources = 1;
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
                amount = random.nextInt(UiValues.getMaxStackStruct()) + 1;
            } else if (type.equals("milestone")) {
                amount = random.nextInt(UiValues.getMaxStackMile()) + 1;
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
    private static List<String> generateUnlocks(int numberOfUnlocks, List<String> fixedUnlocks, int phase) {
        List<String> unlocks = new ArrayList<>();
        List<String> fixedlocks = new ArrayList<>();

        // Add extra unlocks if it's not null
        if (fixedUnlocks != null) {
            fixedlocks.addAll(fixedUnlocks);

            // Removing the tutorials (they may be in fixed unlocks and can't be generated
            // here) and changing from the name to the recipe (it's what the milestone uses)
            List<Randomizable> fixUnl = new ArrayList<>();
            for (String s : fixedUnlocks) {
                fixUnl.add(materials.getRandomizableByName(s));
            }
            fixUnl.removeIf(s -> s.getName().contains("Tutorial_"));
            Collections.shuffle(fixUnl);

            for (Randomizable unlock : fixUnl) {
                Console.hiddenLog("Forcing unlock: " + unlock);
                materials.setRandomizableAvailable(unlock.getName(), true);
                if (unlock instanceof EssentialStructure) {
                    generateStructure((EssentialStructure) unlock, "structure");
                    unlocks.add(unlock.getPath());
                } else if (unlock instanceof Structure) {
                    generateStructure((EssentialStructure) unlock, "structure");
                    unlocks.add(unlock.getPath());
                } else {
                    Console.log(unlock + " is not one of the allowed categories.");
                }
                if (!unlock.getCheckAlso().isEmpty())
                    materials.doExtraChecks(unlock.getName(), unlock.getCheckAlso());
            }
        }

        // Make a list with all possible randomizables
        List<Randomizable> itemList = materials.getUnavailableAndUncraftableRandomizables();
        itemList.removeAll(materials.getAllMilestones());

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
        int liquidslots;
        int solidslots;
        if (mainliquid != null) {
            liquidslots = (UiValues.getWaste() == 3) ? station.getLiquidOut() : 0;
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
            if (mainliquid && UiValues.getWaste() == 3)
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
        int totalresources = getBiasedRandomInt(0, liquidslots + solidslots, UiValues.getInputBias());
        if(mainliquid == null && totalresources == 0){
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
            // Select a random component from the available components (craftable or not)
            List<Component> availableComponents = materials.getAllComponents(selectedLiquid);

            Component component = availableComponents.get(random.nextInt(availableComponents.size()));

            // Add the ingredient to the list and generate the amount randomly
            // Use the UiValues to get the max stack size for the component
            int amount = random.nextInt(UiValues.getMaxProdCraft()) + 1;

            if (selectedLiquid) {
                amount = amount * 1000;
            }
            products.add(new Mat(component.getName(), amount));

            if (selectedLiquid) {
                liquidslots--;
            } else {
                solidslots--;
            }
        }

        for (Mat m : products) {
            Console.cheatsheet("Product: " + m.getName() + " | Amount: " + m.getAmount());
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

        Component component = new Component("placeholder in ensureUnused in SequenceGenerator", null, false, false,
                false);
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
