package com.satisfactoryrandomizer.Storage;

import java.math.BigDecimal;
import java.util.List;

// Stores all values needes to create a recipe in JSON format
public class Recipe {

    private List<Mat> products;
    private List<Mat> ingredients;
    private String recipePath;
    private String schema;
    private String filename;
    private String station;
    private double time;
    private double handcraftingSpeed;
    private String path = "ContentLib/RecipePatches/";

    public Recipe(List<Mat> products, List<Mat> ingredients, String filename, String station, double time,
            double handSpeed) {
        this.products = products;
        this.ingredients = ingredients;
        this.schema = "https://raw.githubusercontent.com/budak7273/ContentLib_Documentation/refs/heads/main/JsonSchemas/CL_Recipe.json";
        this.filename = this.path + filename;
        this.station = station;

        BigDecimal bigTime = new BigDecimal(time);
        bigTime = bigTime.setScale(2, BigDecimal.ROUND_UP);
        double roundedTime = bigTime.doubleValue();
        this.time = roundedTime;

        BigDecimal ManTime = new BigDecimal(time);
        ManTime = ManTime.setScale(2, BigDecimal.ROUND_UP);
        double roundedMan = ManTime.doubleValue();
        this.handcraftingSpeed = roundedMan;
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

    public double getTime() {
        return time;
    }

    public double getHandcraftSpeed() {
        return handcraftingSpeed;
    }
}
