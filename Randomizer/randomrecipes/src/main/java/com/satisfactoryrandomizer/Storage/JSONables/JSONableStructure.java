package com.satisfactoryrandomizer.Storage.JSONables;

import java.util.List;

import com.satisfactoryrandomizer.Storage.Data.Mat;

public class JSONableStructure {
    private String $schema = "https://raw.githubusercontent.com/budak7273/ContentLib_Documentation/refs/heads/main/JsonSchemas/CL_Recipe.json";
    private boolean ClearIngredients = true;
    private List<Mat> Ingredients;

    public JSONableStructure(List<Mat> ingredients) {
        this.Ingredients = ingredients;
    }
}
