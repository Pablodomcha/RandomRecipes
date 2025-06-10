package com.satisfactoryrandomizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.satisfactoryrandomizer.Storage.Mat;
import com.satisfactoryrandomizer.Storage.Materials;
import com.satisfactoryrandomizer.Storage.Randomizables.CraftStation;
import com.satisfactoryrandomizer.Storage.Recipe;

public class Tests {
    static Materials materials = new Materials();
    static Random random = new Random();

    // Simply to try out things, just run this in App.java to check what the content
    // does.
    public static void test() {

        for (CraftStation station : materials.getCraftStations()) {
            Console.log("Generating Structure for " + station.getName());
            generateStructure(station);
        }

    }

    private static void generateStructure(CraftStation station) {
        // Needed variables. Prod will always only have the station, but has to be a
        // string regardless.
        List<Mat> mats = new ArrayList<>();
        List<Mat> prod = new ArrayList<>();

        // Only produces one of the structure
        prod.add(new Mat(station.getName(), 1));
        mats.add(new Mat("Desc_OreIron", random.nextInt(100) + 1));

        Recipe recipe = new Recipe(
                prod, // Products
                mats, // Ingredients
                "Recipe_" + station.getName() + ".json", // Filename
                "", // Doesn't apply
                1, // Doesn't apply
                1.0 // Doesn't apply
        );

        // Create Recipe JSON file
        CreateJSON.saveStructureAsJson(recipe, station.getRecipePath());
    }

}
