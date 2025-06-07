package com.satisfactoryrandomizer.Storage;

import java.util.List;

// Stores all values needes to create a recipe in JSON format
public class Recipe {

    private Component component;
    private List<Mat> ingredients;
    private int amount;
    private String recipePath;
    private String schema;
    private String filename;
    private String station;

    public Recipe(Component component, List<Mat> ingredients, int amount, String filename, String station) {
        this.component = component;
        this.ingredients = ingredients;
        this.amount = amount;
        this.recipePath = component.getRecipePath();
        this.schema = "https://raw.githubusercontent.com/budak7273/ContentLib_Documentation/refs/heads/main/JsonSchemas/CL_Recipe.json";
        this.filename = filename;
        this.station = station;
    }

    public Component getComponent() {
        return component;
    }

    public List<Mat> getIngredients() {
        return ingredients;
    }

    public int getAmount() {
        return amount;
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
}
