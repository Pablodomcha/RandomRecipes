package com.satisfactoryrandomizer.Storage;

import java.util.List;

public class JSONableRecipe {
    private String $schema = "https://raw.githubusercontent.com/budak7273/ContentLib_Documentation/refs/heads/main/JsonSchemas/CL_Recipe.json";
    private boolean ClearIngredients = true;
    private List<Mat> Products;
    private List<Mat> Ingredients;
    private List<String> ProducedIn;
    private double ManufacturingDuration;
    private double ManualManufacturingMultiplier = 1;

    public JSONableRecipe(List<Mat> products, List<Mat> ingredients, List<String> producedIn, double manufacturingDuration, double manualDuration) {
        this.Products = products;
        this.Ingredients = ingredients;
        this.ProducedIn = producedIn;
        this.ManufacturingDuration = manufacturingDuration;
        this.ManualManufacturingMultiplier = manualDuration;
    }
}
