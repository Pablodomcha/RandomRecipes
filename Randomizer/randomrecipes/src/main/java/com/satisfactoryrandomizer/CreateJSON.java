package com.satisfactoryrandomizer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.satisfactoryrandomizer.Storage.JSONableRecipe;
import com.satisfactoryrandomizer.Storage.JSONableStructure;
import com.satisfactoryrandomizer.Storage.Recipe;

public class CreateJSON {

    public static void saveRecipeAsJson(Recipe recipe, String recipePath, String firstStation) {

        // Convert the station to list and add manual if it's from constructor or
        // smelter.
        // Also prefix it with "Build_" to make it the correct name.
        List<String> stations = new ArrayList<>();
        stations.add(recipe.getStation());
        if (recipe.getStation().equals(firstStation)) {
            stations.add("manual");
        }
        JSONableRecipe jsonRecipe = new JSONableRecipe(recipe.getProducts(), recipe.getIngredients(), stations,
                recipe.getTime(), recipe.getHandcraftSpeed());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(jsonRecipe);
        json = json.replace("\n", "\r\n"); // Make the line breaks CRLF
        json = recipePath + "\r\n" + json;

        try (FileWriter writer = new FileWriter(recipe.getFilename())) {
            writer.write(json);
        } catch (IOException e) {
            Console.log("Error writing JSON file: " + e.getMessage());
        }
    }

    public static void saveStructureAsJson(Recipe recipe, String recipePath) {

        // Convert the station to list and add manual if it's from constructor or
        // smelter.
        JSONableStructure jsonRecipe = new JSONableStructure(recipe.getProducts(), recipe.getIngredients());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(jsonRecipe);
        json = json.replace("\n", "\r\n"); // Make the breaks CRLF
        json = recipePath + "\n" + json;

        try (FileWriter writer = new FileWriter(recipe.getFilename())) {
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            Console.log("Error writing JSON file: " + e.getMessage());
        }
    }
}
