package com.satisfactoryrandomizer.Storage;

import java.util.List;

public class JSONableStructure {
    private String $schema = "https://raw.githubusercontent.com/budak7273/ContentLib_Documentation/refs/heads/main/JsonSchemas/CL_Recipe.json";
    private boolean ClearIngredients = true;
    private List<Mat> Products;
    private List<Mat> Ingredients;

    public JSONableStructure(List<Mat> products, List<Mat> ingredients) {
        this.Products = products;
        this.Ingredients = ingredients;
    }
}
