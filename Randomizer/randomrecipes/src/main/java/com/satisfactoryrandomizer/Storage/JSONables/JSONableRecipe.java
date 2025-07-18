package com.satisfactoryrandomizer.Storage.JSONables;

import java.util.List;

import com.satisfactoryrandomizer.Storage.Data.Mat;

public class JSONableRecipe {
    private String $schema = "https://raw.githubusercontent.com/budak7273/ContentLib_Documentation/refs/heads/main/JsonSchemas/CL_Recipe.json";
    private boolean ClearIngredients = true;
    private List<Mat> Products;
    private List<Mat> Ingredients;
    private List<String> ProducedIn;
    private double ManufacturingDuration;
    private double ManualManufacturingMultiplier = 1;
    private String OverrideCategory;

    public JSONableRecipe(List<Mat> products, List<Mat> ingredients, List<String> producedIn, double manufacturingDuration, double manualDuration, String OverrideCategory) {
        this.Products = products;
        this.Ingredients = ingredients;
        this.ProducedIn = producedIn;
        this.ManufacturingDuration = manufacturingDuration;
        this.ManualManufacturingMultiplier = manualDuration;
        this.OverrideCategory = OverrideCategory;
    }
}
