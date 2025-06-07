package com.satisfactoryrandomizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import com.satisfactoryrandomizer.Storage.*;

import com.satisfactoryrandomizer.Storage.Materials;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    public static void main(String[] args) {

        Materials materials = new Materials();

        List<Mat> mats = new ArrayList<>();
        mats.add(new Mat("Desc_OreIron", 1));
        mats.add(new Mat("Desc_OreCopper", 2));
        mats.add(new Mat("Desc_Stone", 3));
        Recipe recipe = new Recipe(
                materials.getComponentByName("Desc_IronRod"), // Component
                mats, // Ingredients
                3, // Amount
                "Recipe_test.json", // Filename
                "Build_ManufacturerMk1" // Station
        );
        CreateJSON.saveObjectAsJson(recipe);
    }
}
