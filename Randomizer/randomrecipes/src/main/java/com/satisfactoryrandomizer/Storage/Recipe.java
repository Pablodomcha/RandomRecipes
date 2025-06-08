package com.satisfactoryrandomizer.Storage;

import java.util.List;

// Stores all values needes to create a recipe in JSON format
public class Recipe {

    private List<Mat> products;
    private List<Mat> ingredients;
    private String recipePath;
    private String schema;
    private String filename;
    private String station;
    private int time;
    private double handcraftingSpeed;
    private String path = "ContentLib/RecipePatches/";

    public Recipe(List<Mat> products, List<Mat> ingredients, String filename, String station, int time, double handSpeed) {
        this.products = products;
        this.ingredients = ingredients;
        this.schema = "https://raw.githubusercontent.com/budak7273/ContentLib_Documentation/refs/heads/main/JsonSchemas/CL_Recipe.json";
        this.filename = path + filename;
        this.station = station;
        this.time = time;
        this.handcraftingSpeed = handSpeed;
    }

    public List<Mat> getProducts() {
        return products;
    }

    public List<Mat> getIngredients() {
        return ingredients;
    }

    public String getRecipePath() {
        return recipePath;
    }

    public String getSchema() {
        return schema;
    }

    public String getFilename() {
        return filename;
    }

    public String getStation() {
        return station;
    }

    public int getTime() {
        return time;
    }

    public double getHandcraftSpeed() {
        return handcraftingSpeed;
    }
}
