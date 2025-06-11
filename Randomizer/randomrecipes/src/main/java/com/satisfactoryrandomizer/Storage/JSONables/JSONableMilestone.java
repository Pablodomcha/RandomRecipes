package com.satisfactoryrandomizer.Storage.JSONables;

import java.util.List;

import com.satisfactoryrandomizer.Storage.Data.Mat;

public class JSONableMilestone {
    private String $schema = "https://raw.githubusercontent.com/budak7273/ContentLib_Documentation/main/JsonSchemas/CL_Schematic.json";
    private boolean ClearCost = true;
    private boolean ClearRecipes = true;
    private List<String> Recipes;
    private List<Mat> Cost;
    public int Time;

    public JSONableMilestone(List<Mat> ingredients, List<String> unlocks, int time) {
        this.Cost = ingredients;
        this.Recipes = unlocks;
        this.Time = time;
    }
}
