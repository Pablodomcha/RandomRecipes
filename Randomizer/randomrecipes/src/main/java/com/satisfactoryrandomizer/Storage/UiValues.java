package com.satisfactoryrandomizer.Storage;

import com.satisfactoryrandomizer.Console;

// Stores the values from the UI inputs, to have them somewhere organized and easily accessible

public class UiValues {

    // In general, 0 = Easy, 1 = Medium, 2 = Hard, 3 = Insane. The UI displayed
    // names may not be these.
    static int oreLocation = 0;
    static int belts = 0;
    static int electricity = 0;
    static int waste = 3;
    static int stationBias = 3;
    static int forceLongGameBias = 0;

    static Boolean advLog = false;
    static Boolean startWithMiner = false;
    static Boolean progressiveBias = false;
    static Boolean capProductivityInput = false;
    static Boolean capProductivityOutput = false;

    // Numeric values here
    static long seed = 0;
    static int maxStackCraft = 2;
    static int maxProdCraft = 2;
    static int maxStackStruct = 2;
    static int maxItemStruct = 2;
    static int maxStackMile = 20;
    static int maxItemMile = 4;

    // The elevator isn't randomized, but no harm in keeping this in case it is in
    // the future.
    static int maxStackEle = 2;
    static int maxItemEle = 2;

    static int maxTimeMile = 5; // Seconds
    static double maxTimeCraft = 1; // Seconds
    static double[] handcraftSpeed = { 0.5, 1.0 };

    static int inputBias = 50; // 0 = min unputs, 100 = max inputs
    static int maxRecipesUsed = 4; // In how many recipes can a component be useds
    static int freeChance = 0; // Frequency wich a free component is given

    // Could just make them public, but nope.

    public static int addAll() {
        return 1000 * (int) (UiValues.seed % 100 + UiValues.oreLocation + UiValues.belts + UiValues.electricity
                + UiValues.waste + UiValues.stationBias + UiValues.maxStackCraft + UiValues.maxProdCraft
                + UiValues.maxStackStruct + UiValues.maxItemStruct + UiValues.maxStackMile + UiValues.maxItemMile
                + UiValues.maxStackEle + UiValues.maxItemEle + UiValues.maxTimeMile + UiValues.maxTimeCraft
                + UiValues.handcraftSpeed[1] + UiValues.handcraftSpeed[0] + UiValues.inputBias + UiValues.maxRecipesUsed
                + UiValues.freeChance + UiValues.forceLongGameBias + (UiValues.startWithMiner ? 1 : 0)
                + (UiValues.progressiveBias ? 1 : 0));
    }

    public static void logAll() {
        Console.hiddenLog("Advanced Logging: " + UiValues.advLog);
        Console.hiddenLog("Seed: " + UiValues.seed);
        Console.hiddenLog("Start with miner: " + UiValues.startWithMiner);
        Console.hiddenLog("Progressive Bias: " + UiValues.progressiveBias);
        Console.hiddenLog("Ore Location: " + UiValues.oreLocation);
        Console.hiddenLog("Belts: " + UiValues.belts);
        Console.hiddenLog("Electricity: " + UiValues.electricity);
        Console.hiddenLog("Waste: " + UiValues.waste);
        Console.hiddenLog("Station Bias: " + UiValues.stationBias);
        Console.hiddenLog("Force Long Game: " + UiValues.forceLongGameBias);
        Console.hiddenLog("Max Stack Craft: " + UiValues.maxStackCraft);
        Console.hiddenLog("Max Product Craft: " + UiValues.maxProdCraft);
        Console.hiddenLog("Max Stack Struct: " + UiValues.maxStackStruct);
        Console.hiddenLog("Max Item Struct: " + UiValues.maxItemStruct);
        Console.hiddenLog("Max Stack Mile: " + UiValues.maxStackMile);
        Console.hiddenLog("Max Item Mile: " + UiValues.maxItemMile);
        Console.hiddenLog("Max Stack Ele: " + UiValues.maxStackEle);
        Console.hiddenLog("Max Item Ele: " + UiValues.maxItemEle);
        Console.hiddenLog("Max Time Mile: " + UiValues.maxTimeMile);
        Console.hiddenLog("Max Time Craft: " + UiValues.maxTimeCraft);
        Console.hiddenLog("Handcraft Speed: " + UiValues.handcraftSpeed[0] + " - " + UiValues.handcraftSpeed[1]);
        Console.hiddenLog("Input Bias: " + UiValues.inputBias);
        Console.hiddenLog("Max Recipes Used: " + UiValues.maxRecipesUsed);
        Console.hiddenLog("Free Chance: " + UiValues.freeChance);
    }

    /**
     * Retrieves the current ore location setting.
     * 
     * @return 1. Ore location:
     *         Easy: You don't need ores you can't scan to unlock the ability to
     *         scan them. Uranium will only be needed if you can have radiation
     *         protection.
     *         Medium: Any ore can be part of any recipe. Uranium will only be
     *         needed if you can have radiation protection.
     *         Radiation Party: Uranium could be needed for recipes unlocked before
     *         you can have radiation protection.
     */
    public static int getOreLocation() {
        return UiValues.oreLocation;
    }

    public static void setOreLocation(int oreLocation) {
        UiValues.oreLocation = oreLocation;
    }

    /**
     * Retrieves the current belt setting.
     * 
     * @return 2. Belts:
     *         Easy: Only use raw ore, ensuring they can always be made (though you
     *         may need to search for the ore without scan).
     *         Medium: Tier 1 belt and pole only uses raw ore. Other belts can use
     *         anything (guarantees at least access to basic belts).
     *         Hard: Uses anything, you could end up with no access to belts for
     *         part/most of your playthrough.
     */
    public static int getBelts() {
        return UiValues.belts;
    }

    public static void setBelts(int belts) {
        UiValues.belts = belts;
    }

    /**
     * Retrieves the current electricity setting.
     * 
     * @return 3. Electricity:
     *         Easy: Basic electric poles and biomass burners use only raw ore and
     *         are cheap.
     *         Medium: Basic electric poles use only raw ore and are cheap.
     *         Hard: Have fun with your handcrafting (poles and biomass burners will
     *         still be buildable from the start, but they might be really
     *         expensive).
     */
    public static int getElectricity() {
        return UiValues.electricity;
    }

    public static void setElectricity(int electricity) {
        UiValues.electricity = electricity;
    }

    /**
     * Retrieves the current waste setting.
     * 
     * @return 4. Waste (the "desired" output is the one for which the recipe is
     *         generated, even if you like more the other output):
     *         Easy: Recipes only have 1 output, so you don't need to handle a
     *         second resource you may not need.
     *         Medium: Recipes can have solid outputs other than the desired, but
     *         the Awesome sink recipe will be available from the start and only use
     *         raw ore.
     *         Hard: Recipes can have solid outputs other than the desired and the
     *         Awesome sink may not be available or have really hard crafting
     *         recipe.
     *         Liquids too: Recipes can have solid/liquid outputs other than the
     *         desired
     *         and the sink may not be available or have really hard crafting
     *         recipe. Packaging to get rid of liquids may or may not be easy, you
     *         may need tons deposits and manual flushing.
     *         Double party: Always get an extra output for muliple output stations.
     */
    public static int getWaste() {
        return UiValues.waste;
    }

    public static void setWaste(int waste) {
        UiValues.waste = waste;
    }

    /**
     * Retrieves the current station bias setting.
     * 
     * @return 6. Station bias:
     *         None: The stations are selected completely random, this makes the
     *         earlier unlocked stations have way more recipes than the later ones.
     *         Slight bias: Increases the chance of using stations unlocked later
     *         for later recipes, increaseing (hopefully) station variability
     *         leaning slightly on newer unlocked stations.
     *         Medium Bias: Increases the chance of using stations unlocked later
     *         for later recipes, increaseing (hopefully) station variability
     *         leaning on newer unlocked stations.
     *         Heavy Bias: Increases the chance of using stations unlocked later for
     *         later recipes, increaseing (hopefully) station variability leaning
     *         heavily on newer unlocked stations.
     */
    public static int getStationBias() {
        return UiValues.stationBias;
    }

    public static void setStationBias(int stationBias) {
        UiValues.stationBias = stationBias;
    }

    /**
     * Retrieves the setting for the long game bias.
     * 
     * @return 7. The force long game bias value:
     *         Higher values increase the likelihood of needing more milestones.
     */

    public static int getForceLongGameBias() {
        return UiValues.forceLongGameBias;
    }

    public static void setForceLongGameBias(int forceLongGameBias) {
        UiValues.forceLongGameBias = forceLongGameBias;
    }

    /**
     * Retrieves the advanced logging setting. Logs many things helpful for
     * troubleshooting, but is way slower. Use only for debugging.
     * 
     * @return 8. Advanced Logging.
     */
    public static Boolean getAdvLog() {
        return UiValues.advLog;
    }

    public static void setAdvLog(Boolean advLog) {
        UiValues.advLog = advLog;
    }

    public static long getSeed() {
        return UiValues.seed;
    }

    public static void setSeed(long seed) {
        UiValues.seed = seed;
    }

    /**
     * @return 1. Max stack per component: Maximum number of each component produced
     *         by a craft. Over 50 will crash.
     */
    public static int getMaxProdCraft() {
        return UiValues.maxProdCraft;
    }

    public static void setMaxProdCraft(int maxProdCraft) {
        UiValues.maxProdCraft = maxProdCraft;
    }

    /**
     * @return 2. Max component per craft: Maximum number of each component needed
     *         for a craft. Over 50 will crash.
     */

    public static int getMaxStackCraft() {
        return UiValues.maxStackCraft;
    }

    public static void setMaxStackCraft(int maxStackCraft) {
        UiValues.maxStackCraft = maxStackCraft;
    }

    /**
     * @return 3. Max stack per structure: Maximum number of each component needed
     *         to build with the builder tool.
     */
    public static int getMaxStackStruct() {
        return UiValues.maxStackStruct;
    }

    public static void setMaxStackStruct(int maxStackStruct) {
        UiValues.maxStackStruct = maxStackStruct;
    }

    /**
     * @return 4. Max items per structure: Maximum number of different items to
     *         build with the builder tool.
     */
    public static int getMaxItemStruct() {
        return UiValues.maxItemStruct;
    }

    public static void setMaxItemStruct(int maxItemStruct) {
        UiValues.maxItemStruct = maxItemStruct;
    }

    /**
     * @return 5. Max stack per milestone: Maximum number of each component needed
     *         to complete a milestone.
     */
    public static int getMaxStackMile() {
        return UiValues.maxStackMile;
    }

    public static void setMaxStackMile(int maxStackMile) {
        UiValues.maxStackMile = maxStackMile;
    }

    /**
     * @return 6. Max items per milestone: Maximum number of different items to
     *         complete a milestone.
     */
    public static int getMaxItemMile() {
        return UiValues.maxItemMile;
    }

    public static void setMaxItemMile(int maxItemMile) {
        UiValues.maxItemMile = maxItemMile;
    }

    /**
     * Retrieves the maximum time needed for a milestone after completing one or
     * time to complete a MAM research.
     * 
     * @return 7. Max time per milestone.
     */
    public static int getMaxTimeMile() {
        return UiValues.maxTimeMile;
    }

    public static void setMaxTimeMile(int maxTimeMile) {
        UiValues.maxTimeMile = maxTimeMile;
    }

    /**
     * @return 8. Max stack per elevator part: Maximum number of each component
     *         needed to complete a Project Assembly stage.
     */

    public static int getMaxStackEle() {
        return UiValues.maxStackEle;
    }

    public static void setMaxStackEle(int maxStackEle) {
        UiValues.maxStackEle = maxStackEle;
    }

    /**
     * Retrieves the maximum number of different items needed to complete a Project
     * Assembly stage.
     * 
     * @return 9. Max items per elevator part.
     */

    public static int getMaxItemEle() {
        return UiValues.maxItemEle;
    }

    public static void setMaxItemEle(int maxItemEle) {
        UiValues.maxItemEle = maxItemEle;
    }

    /**
     * Retrieves the maximum time needed for a craft.
     * 
     * @return 10. Max time to craft.
     */
    public static double getMaxTimeCraft() {
        return UiValues.maxTimeCraft;
    }

    public static void setMaxTimeCraft(int maxTimeCraft) {
        UiValues.maxTimeCraft = maxTimeCraft;
    }

    /**
     * Retrieves the multiplier for handcrafting speed. Lower is faster (the base
     * game value is usually between 0.5 and 1 depending on recipe).
     * 
     * @return 11. Handcrafting speed array {min,max}.
     */
    public static double[] getHandcraftSpeed() {
        return UiValues.handcraftSpeed;
    }

    public static void setHandcraftSpeed(double[] handcraftSpeed) {
        UiValues.handcraftSpeed = handcraftSpeed;
    }

    /**
     * Retrieves the bias for input slots. Higher numbers mean that the
     * randomizer will try to use more of the slots available in the production
     * station. 100 means always will use all slots, 0 means all recipes use 1
     * input/output slot, 50 means there's no bias. This also affects the amount of
     * resources used for structures.
     * 
     * @return 12. Input bias.
     */
    public static int getInputBias() {
        return UiValues.inputBias;
    }

    public static void setInputBias(int inputBias) {
        UiValues.inputBias = inputBias;
    }

    /**
     * Maximum number of recipes that can use a material. The randomizer will
     * increase this value if it can't complete a randomization.
     * 
     * @return 13. Max recipes used.
     */
    public static int getMaxRecipesUsed() {
        return UiValues.maxRecipesUsed;
    }

    public static void setMaxRecipesUsed(int maxRecipesUsed) {
        UiValues.maxRecipesUsed = maxRecipesUsed;
    }

    /**
     * Retrieves the chance for a material to be free (like Excited Photonic Matter,
     * except that one may no longer be free).
     * 
     * @return 14. Free Chance.
     */
    public static int getFreeChance() {
        return UiValues.freeChance;
    }

    public static void setFreeChance(int free) {
        UiValues.freeChance = free;
    }

    /**
     * Retrieves the flag for starting the game with the portable miner.
     * 
     * @return Whether the game should start with the portable miner or not.
     */
    public static Boolean getStartWithMiner() {
        return UiValues.startWithMiner;
    }

    public static void setStartWithMiner(Boolean startWithMiner) {
        UiValues.startWithMiner = startWithMiner;
    }

    /**
     * Retrieves the flag for whether the randomizer should try to make the game
     * progressively harder or not. When enabled, the randomizer will try to
     * randomize higher amounts of ingredients as the game progresses, making the
     * game progressively harder to play. When disabled, the randomizer will
     * randomize components randomly, regardless of the game progress.
     * 
     * @return Whether the randomizer should try to make the game progressively
     *         harder or not.
     */
    public static Boolean getProgressiveBias() {
        return UiValues.progressiveBias;
    }

    public static void setProgressiveBias(Boolean progBias) {
        UiValues.progressiveBias = progBias;
    }

    /**
     * Retrieves the productivity settings for the input and output.
     * 
     * @return An array of Booleans where the first element indicates whether the
     *         input productivity is capped, and the second element indicates 
     *         whether the output productivity is capped.
     */

    public static Boolean[] getProductivity() {
        return new Boolean[] { UiValues.capProductivityInput, UiValues.capProductivityOutput };
    }

    public static void setProductivity(Boolean[] productivity) {
        UiValues.capProductivityInput = productivity[0];
        UiValues.capProductivityOutput = productivity[1];
    }

}
