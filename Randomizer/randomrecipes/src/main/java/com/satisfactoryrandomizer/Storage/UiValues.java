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

    // Inventory Slots Weirdo
    static int inventorySlots = 0; // 0 = fixed, 1 = random
    static int inventorySlotsMin = 1;
    static int inventorySlotsMax = 3;

    // Numeric values here
    static int seed = 0;
    static int maxStackComp = 1;
    static int maxCompCraft = 1;
    static int maxStackStruct = 1;
    static int maxItemStruct = 1;
    static int maxStackMile = 1;
    static int maxItemMile = 1;
    static int maxStackEle = 1;
    static int maxItemEle = 1;

    static int maxTimeMile = 100; // Seconds
    static int maxTimeCraft = 100; // Seconds
    static double handcraftSpeed = 1.0;

    static int inputBias = 100; // 0 = min unputs, 100 = max inputs
    static int maxRecipesUsed = 5; // In how many recipes can a component be used
    static int freeRecipeChance = 0; // 0 = no free recipes, 100 = all recipes are free

    // Could just make them public, but nope.

    
    public static int getOreLocation() {
        return oreLocation;
    }

    public static void setOreLocation(int oreLocation) {
        UiValues.oreLocation = oreLocation;
    }

    public static int getBelts() {
        return belts;
    }

    public static void setBelts(int belts) {
        UiValues.belts = belts;
    }

    public static int getElectricity() {
        return electricity;
    }

    public static void setElectricity(int electricity) {
        UiValues.electricity = electricity;
    }

    public static int getWaste() {
        return waste;
    }

    public static void setWaste(int waste) {
        UiValues.waste = waste;
    }

    public static int getLiquids() {
        return liquids;
    }

    public static void setLiquids(int liquids) {
        UiValues.liquids = liquids;
    }

    public static int getSeed() {
        return seed;
    }

    public static void setSeed(int seed) {
        UiValues.seed = seed;
    }

    public static int getMaxStackComp() {
        return maxStackComp;
    }

    public static void setMaxStackComp(int maxStackComp) {
        UiValues.maxStackComp = maxStackComp;
    }

    public static int getMaxCompCraft() {
        return maxCompCraft;
    }

    public static void setMaxCompCraft(int maxCompCraft) {
        UiValues.maxCompCraft = maxCompCraft;
    }

    public static int getMaxStackStruct() {
        return maxStackStruct;
    }

    public static void setMaxStackStruct(int maxStackStruct) {
        UiValues.maxStackStruct = maxStackStruct;
    }

    public static int getMaxItemStruct() {
        return maxItemStruct;
    }

    public static void setMaxItemStruct(int maxItemStruct) {
        UiValues.maxItemStruct = maxItemStruct;
    }

    public static int getMaxStackMile() {
        return maxStackMile;
    }

    public static void setMaxStackMile(int maxStackMile) {
        UiValues.maxStackMile = maxStackMile;
    }

    public static int getMaxItemMile() {
        return maxItemMile;
    }

    public static void setMaxItemMile(int maxItemMile) {
        UiValues.maxItemMile = maxItemMile;
    }

    public static int getMaxStackEle() {
        return maxStackEle;
    }

    public static void setMaxStackEle(int maxStackEle) {
        UiValues.maxStackEle = maxStackEle;
    }

    public static int getMaxItemEle() {
        return maxItemEle;
    }

    public static void setMaxItemEle(int maxItemEle) {
        UiValues.maxItemEle = maxItemEle;
    }

    public static int getMaxTimeMile() {
        return maxTimeMile;
    }

    public static void setMaxTimeMile(int maxTimeMile) {
        UiValues.maxTimeMile = maxTimeMile;
    }

    public static int getMaxTimeCraft() {
        return maxTimeCraft;
    }

    public static void setMaxTimeCraft(int maxTimeCraft) {
        UiValues.maxTimeCraft = maxTimeCraft;
    }

    public static double getHandcraftSpeed() {
        return handcraftSpeed;
    }

    public static void setHandcraftSpeed(double handcraftSpeed) {
        UiValues.handcraftSpeed = handcraftSpeed;
    }

    public static int getInputBias() {
        return inputBias;
    }

    public static void setInputBias(int inputBias) {
        UiValues.inputBias = inputBias;
    }

    public static int getMaxRecipesUsed() {
        return maxRecipesUsed;
    }

    public static void setMaxRecipesUsed(int maxRecipesUsed) {
        UiValues.maxRecipesUsed = maxRecipesUsed;
    }

    public static int getFreeRecipeChance() {
        return freeRecipeChance;
    }

    public static void setFreeRecipeChance(int freeRecipeChance) {
        UiValues.freeRecipeChance = freeRecipeChance;
    }
}
