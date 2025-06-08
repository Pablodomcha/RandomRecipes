package com.satisfactoryrandomizer.Storage;

import java.util.List;

public class JSONableRecipe {
    private String $schema = "https://raw.githubusercontent.com/budak7273/ContentLib_Documentation/refs/heads/main/JsonSchemas/CL_Recipe.json";
    private boolean ClearIngredients = true;
    private List<Mat> Products;
    private List<Mat> Ingredients;
    private List<String> ProducedIn;
    private int ManufacturingDuration;

    public JSONableRecipe(List<Mat> products, List<Mat> ingredients, List<String> producedIn, int manufacturingDuration) {
        this.Products = products;
        this.Ingredients = ingredients;
        this.ProducedIn = producedIn;
        this.ManufacturingDuration = manufacturingDuration;
    }
}
