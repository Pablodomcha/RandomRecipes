package com.satisfactoryrandomizer.Storage.Data;

import java.util.List;

// Stores all values needes to create a recipe in JSON format
public class MilestoneSchematic {

    private final List<String> unlocks;
    private final List<Mat> ingredients;
    private String recipePath;
    private final String filename;
    private final int time;
    private final String path = "ContentLib/SchematicPatches/";

    public MilestoneSchematic(List<String> unlocks, List<Mat> ingredients, String filename, int time) {
        this.unlocks = unlocks;
        this.ingredients = ingredients;
        this.filename = this.path + filename;
        this.time = time;
    }

    public List<String> getUnlocks() {
        return this.unlocks;
    }

    public List<Mat> getIngredients() {
        return this.ingredients;
    }

    public String getRecipePath() {
        return this.recipePath;
    }

    public String getFilename() {
        return this.filename;
    }

    public int getTime() {
        return this.time;
    }
}
