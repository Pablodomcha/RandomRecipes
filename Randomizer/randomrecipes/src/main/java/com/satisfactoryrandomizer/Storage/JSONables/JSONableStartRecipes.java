package com.satisfactoryrandomizer.Storage.JSONables;

import java.util.List;

public class JSONableStartRecipes {
    private String $schema = "https://raw.githubusercontent.com/budak7273/ContentLib_Documentation/main/JsonSchemas/CL_Schematic.json";
    private List<String> Recipes;

    public JSONableStartRecipes(List<String> recipes) {
        this.Recipes = recipes;
    }
}