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

    public Recipe(List<Mat> products, List<Mat> ingredients, String filename, String station) {
        this.products = products;
        this.ingredients = ingredients;
        this.schema = "https://raw.githubusercontent.com/budak7273/ContentLib_Documentation/refs/heads/main/JsonSchemas/CL_Recipe.json";
        this.filename = filename;
        this.station = station;
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
}
