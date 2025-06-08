package com.satisfactoryrandomizer.Storage;

// Stores the values from the UI inputs, to have them somewhere organized and easily accessible

public class UiValues {

    // In general, 0 = Easy, 1 = Medium, 2 = Hard, 3 = Insane. The UI displayed
    // names may not be these.
    static int oreLocation = 0;
    static int belts = 0;
    static int electricity = 0;
    static int waste = 0;
    static int liquids = 0;
    static int power = 0;

    // Inventory Slots Weirdo
    static int inventorySlots = 0; // 0 = fixed, 1 = random
    static int[] inventoryRange = { 1, 3 };

    // Numeric values here
    static int seed = 0;
    static int maxStackComp = 2;
    static int maxCompCraft = 2;
    static int maxStackStruct = 2;
    static int maxItemStruct = 2;
    static int maxStackMile = 2;
    static int maxItemMile = 2;
    static int maxStackEle = 2;
    static int maxItemEle = 2;

    static int maxTimeMile = 100; // Seconds
    static int maxTimeCraft = 100; // Seconds
    static double[] handcraftSpeed = { 0.5, 1.0 };

    static int inputBias = 50; // 0 = min unputs, 100 = max inputs
    static int maxRecipesUsed = 10; // In how many recipes can a component be used

    // Could just make them public, but nope.

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
        return oreLocation;
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
        return belts;
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
        return electricity;
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
     *         No U: Recipes can have solid/liquid outputs other than the desired
     *         and the sink may not be available or have really hard crafting
     *         recipe. Packaging to get rid of liquids may or may not be easy, you
     *         may need tons deposits and manual flushing.
     */
    public static int getWaste() {
        return waste;
    }

    public static void setWaste(int waste) {
        UiValues.waste = waste;
    }

    /**
     * Retrieves the current liquid setting.
     * 
     * @return 5. Liquids:
     *         Only liquid: Liquids can only be used in their liquid form for
     *         recipes (this is the case for the base game).
     *         Packaged and Liquid: Packaged liquids can be part of recipes.
     *         Packaging could be really expensive (or not).
     */
    public static int getLiquids() {
        return liquids;
    }

    public static void setLiquids(int liquids) {
        UiValues.liquids = liquids;
    }

    /**
     * Retrieves the current power setting.
     * 
     * @return 6. Power:
     *         Vanilla: Structures use vanilla power. It's fun until you have to run
     *         Particle Accelerators on Biomass Burners.
     *         Balanced: Structures obtained earlier consume less energy. If your
     *         Particle Accelerator is Tier 2, it will consume on average what a
     *         foundry does.
     *         Random: I felt like adding this. The idea is really funny, the
     *         gameplay isn't (the min value is 10MW and the max is 2GW)
     */
    public static int getPower() {
        return power;
    }

    public static void setPower(int power) {
        UiValues.power = power;
    }

    /**
     * Retrieves the current inventory slots setting.
     * 
     * @return 7. Inventory slots: (The number of upgrades that give slots is not
     *         randomized, only the amount of slots they give and the milestones/MAM
     *         research they appear in).
     *         Fixed: The amount of slots is fixed, uses Max value (with this option
     *         Min values is ignored). MAM researches that give these give double.
     *         Random: The amount of slots is random between the values provided.
     *         MAM researches double the randomized value (yes, this means MAM
     *         always gives an even number of slots).
     *         Use getInvenotryRange() to get the range.
     *         Max: Number (min 1).
     *         Min: Number (min 1).
     */
    public static int getInventorySlots() {
        return inventorySlots;
    }

    public static void setInventorySlots(int inventorySlots) {
        UiValues.inventorySlots = inventorySlots;
    }

    /**
     * Retrieves the current inventory slots range.
     * 
     * @return a 2-element array, the first element is the min and the second is the
     *         max value.
     */
    public static int[] getInventoryRange() {
        return inventoryRange;
    }

    public static void setInventoryRange(int[] inventoryRange) {
        UiValues.inventoryRange = inventoryRange;
    }

    public static int getSeed() {
        return seed;
    }

    public static void setSeed(int seed) {
        UiValues.seed = seed;
    }

    /**
     * @return 1. Max stack per component: Maximum number of each component needed
     *         for a craft. Over 50 will crash.
     */
    public static int getMaxStackComp() {
        return maxStackComp;
    }

    public static void setMaxStackComp(int maxStackComp) {
        UiValues.maxStackComp = maxStackComp;
    }

    /**
     * @return 2. Max component per craft: Maximum number of each component produced
     *         by a craft. Over 50 will crash.
     */

    public static int getMaxCompCraft() {
        return maxCompCraft;
    }

    public static void setMaxCompCraft(int maxCompCraft) {
        UiValues.maxCompCraft = maxCompCraft;
    }

    /**
     * @return 3. Max stack per structure: Maximum number of each component needed
     *         to build with the builder tool.
     */
    public static int getMaxStackStruct() {
        return maxStackStruct;
    }

    public static void setMaxStackStruct(int maxStackStruct) {
        UiValues.maxStackStruct = maxStackStruct;
    }

    /**
     * @return 4. Max items per structure: Maximum number of different items to
     *         build with the builder tool.
     */
    public static int getMaxItemStruct() {
        return maxItemStruct;
    }

    public static void setMaxItemStruct(int maxItemStruct) {
        UiValues.maxItemStruct = maxItemStruct;
    }

    /**
     * @return 5. Max stack per milestone: Maximum number of each component needed
     *         to complete a milestone.
     */
    public static int getMaxStackMile() {
        return maxStackMile;
    }

    public static void setMaxStackMile(int maxStackMile) {
        UiValues.maxStackMile = maxStackMile;
    }

    /**
     * @return 6. Max items per milestone: Maximum number of different items to
     *         complete a milestone.
     */
    public static int getMaxItemMile() {
        return maxItemMile;
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
        return maxTimeMile;
    }

    public static void setMaxTimeMile(int maxTimeMile) {
        UiValues.maxTimeMile = maxTimeMile;
    }

    /**
     * @return 8. Max stack per elevator part: Maximum number of each component
     *         needed to complete a Project Assembly stage.
     */

    public static int getMaxStackEle() {
        return maxStackEle;
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
        return maxItemEle;
    }

    public static void setMaxItemEle(int maxItemEle) {
        UiValues.maxItemEle = maxItemEle;
    }

    /**
     * Retrieves the maximum time needed for a craft.
     * 
     * @return 10. Max time to craft.
     */
    public static int getMaxTimeCraft() {
        return maxTimeCraft;
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
        return handcraftSpeed;
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
        return inputBias;
    }

    public static void setInputBias(int inputBias) {
        UiValues.inputBias = inputBias;
    }

    /**
     * Maximum number of recipes that can use a materials. The randomizer will
     * increase this value if it can't complete a randomization.
     * 
     * @return 13. Max recipes used.
     */
    public static int getMaxRecipesUsed() {
        return maxRecipesUsed;
    }

    public static void setMaxRecipesUsed(int maxRecipesUsed) {
        UiValues.maxRecipesUsed = maxRecipesUsed;
    }

}
