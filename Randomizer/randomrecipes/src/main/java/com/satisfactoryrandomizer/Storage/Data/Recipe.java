package com.satisfactoryrandomizer.Storage.Data;

import java.math.BigDecimal;
import java.util.List;

// Stores all values needes to create a recipe in JSON format
public class Recipe {

    private final List<Mat> products;
    private final List<Mat> ingredients;
    private String recipePath;
    private final String filename;
    private final String station;
    private final double time;
    private final double handcraftingSpeed;
    private final String path = "ContentLib/RecipePatches/";

    public Recipe(List<Mat> products, List<Mat> ingredients, String filename, String station, double time,
            double handSpeed) {
        this.products = products;
        this.ingredients = ingredients;
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
