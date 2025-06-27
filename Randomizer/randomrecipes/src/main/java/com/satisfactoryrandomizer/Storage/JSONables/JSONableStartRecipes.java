package com.satisfactoryrandomizer.Storage.JSONables;

import java.util.Arrays;
import java.util.List;

import com.satisfactoryrandomizer.Storage.Data.Mat;

public class JSONableStartRecipes {
    private String $schema = "https://raw.githubusercontent.com/budak7273/ContentLib_Documentation/main/JsonSchemas/CL_Schematic.json";
    private List<String> Recipes;

    public JSONableStartRecipes(List<String> recipes) {
        this.Recipes = recipes;
    }
}