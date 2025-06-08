package com.satisfactoryrandomizer;

import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.satisfactoryrandomizer.Storage.*;

import java.util.List;
import java.util.ArrayList;

public class CreateJSON {

    public static void saveRecipeAsJson(Recipe recipe, String recipePath) {

        // Convert the station to list and add manual if it's from constructor or smelter.
        List<String> stations = new ArrayList<>();
        stations.add(recipe.getStation());
        if(recipe.getStation().equals("Build_ConstructorMk1") || recipe.getStation().equals("Build_SmelterMk1")){
            stations.add("manual");
        }
        JSONableRecipe jsonRecipe = new JSONableRecipe(recipe.getProducts(), recipe.getIngredients(), stations);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(jsonRecipe);
        json = json.replace("\n", "\r\n"); // Make the breaks CRLF
        json = recipePath + "\n" + json;

        System.out.println(json);

        try (FileWriter writer = new FileWriter(recipe.getFilename())) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
