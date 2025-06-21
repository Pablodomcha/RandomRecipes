package com.satisfactoryrandomizer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.satisfactoryrandomizer.Storage.Data.MilestoneSchematic;
import com.satisfactoryrandomizer.Storage.Data.Recipe;
import com.satisfactoryrandomizer.Storage.JSONables.JSONableMilestone;
import com.satisfactoryrandomizer.Storage.JSONables.JSONableRecipe;
import com.satisfactoryrandomizer.Storage.JSONables.JSONableRecipeVN;
import com.satisfactoryrandomizer.Storage.JSONables.JSONableStructure;

public class CreateJSON {

    public static void saveRecipeAsJson(Recipe recipe, String recipePath, String firstStation) {

        // Convert the station to list and add manual if it's from constructor or
        // smelter.
        // Also prefix it with "Build_" to make it the correct name.
        List<String> stations = new ArrayList<>();
        stations.add(recipe.getStation());

        if (recipe.getStation().equals(firstStation.replace("Desc","Build"))) {
            stations.add("manual");
        }

        JSONableRecipe jsonRecipe = new JSONableRecipe(recipe.getProducts(), recipe.getIngredients(), stations,
                recipe.getTime(), recipe.getHandcraftSpeed());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(jsonRecipe);
        json = json.replace("\n", "\r\n"); // Make the line breaks CRLF

        if (recipePath == null) {
            Console.hiddenLog(recipe.getProducts().get(0).getName() + " has a null path, adding as new recipe.");
            try (FileWriter writer = new FileWriter(
                    "ContentLib/Recipes/" + recipe.getProducts().get(0).getName() + ".json")) {
                writer.write(json);
                return;
            } catch (IOException e) {
                Console.log("Error writing JSON file: " + e.getMessage());
            }
        } else {
            json = recipePath + "\r\n" + json;
        }

        // Create the directory and file if they don't exist.
        File file = new File(recipe.getFilename());
        file.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(recipe.getFilename())) {
            writer.write(json);
        } catch (IOException e) {
            Console.log("Error writing JSON file: " + e.getMessage());
        }
    }

    public static void saveRecipeVNAsJson(Recipe recipe, String recipePath, String firstStation, int energy) {

        // Convert the station to list and add manual if it's from constructor or
        // smelter.
        List<String> stations = new ArrayList<>();
        stations.add(recipe.getStation());

        if (recipe.getStation().equals(firstStation.replace("Desc","Build"))) {
            stations.add("manual");
        }

        JSONableRecipeVN jsonVNRecipe = new JSONableRecipeVN(recipe.getProducts(), recipe.getIngredients(), stations,
                recipe.getTime(), recipe.getHandcraftSpeed(), energy);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(jsonVNRecipe);
        json = json.replace("\n", "\r\n"); // Make the line breaks CRLF

        if (recipePath.contains("null")) {
            Console.log(recipe.getProducts().get(0).getName() + " has a null path, adding as new recipe.");
            try (FileWriter writer = new FileWriter(
                    "ContentLib/Recipes/" + recipe.getProducts().get(0).getName() + ".json")) {
                writer.write(json);
                return;
            } catch (IOException e) {
                Console.log("Error writing JSON file: " + e.getMessage());
            }
        } else {
            json = recipePath + "\r\n" + json;
        }

        // Create the directory and file if they don't exist.
        File file = new File(recipe.getFilename());
        file.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(recipe.getFilename())) {
            writer.write(json);
        } catch (IOException e) {
            Console.log("Error writing JSON file: " + e.getMessage());
        }
    }

    public static void saveStructureAsJson(Recipe recipe, String recipePath) {

        // Convert the station to list and add manual if it's from constructor or
        // smelter.
        JSONableStructure jsonRecipe = new JSONableStructure(recipe.getIngredients());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(jsonRecipe);
        json = json.replace("\n", "\r\n"); // Make the breaks CRLF
        json = recipePath + "\r\n" + json;

        // Create the directory and file if they don't exist.
        File file = new File(recipe.getFilename());
        file.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(recipe.getFilename())) {
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            Console.log("Error writing JSON file: " + e.getMessage());
        }
    }

    public static void saveMilestoneAsJson(MilestoneSchematic milestone, String recipePath) {

        // Convert the station to list and add manual if it's from constructor or
        // smelter.
        JSONableMilestone jsonRecipe = new JSONableMilestone(milestone.getIngredients(), milestone.getUnlocks(),
                milestone.getTime());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(jsonRecipe);
        json = json.replace("\n", "\r\n"); // Make the breaks CRLF
        json = recipePath + "\r\n" + json;

        // Create the directory and file if they don't exist.
        File file = new File(milestone.getFilename());
        file.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(milestone.getFilename())) {
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            Console.log("Error writing JSON file: " + e.getMessage());
        }
    }

    /**
     * Creates the directories of ContentLib in case they don't exist.
     */

    public static void createDirectories() {
        new File("ContentLib/CDOs").mkdirs();
        new File("ContentLib/Icons").mkdirs();
        new File("ContentLib/ItemPatches").mkdirs();
        new File("ContentLib/Items").mkdirs();
        new File("ContentLib/RecipePatches").mkdirs();
        new File("ContentLib/Recipes").mkdirs();
        new File("ContentLib/SchematicPatches").mkdirs();
        new File("ContentLib/Schematics").mkdirs();
        new File("ContentLib/VisualKits").mkdirs();
    }
}
