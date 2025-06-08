package com.satisfactoryrandomizer;

import java.util.Random;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import com.satisfactoryrandomizer.Storage.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Tests {
    static Random random = new Random();
    // Simply to try out things, just run this in App.java to check what the content does.
    public static void test(){

        //ensure that we're going into this function at all.
        System.out.println("Starting Test...");

        List<Mat> products = new ArrayList<>();
        List<Mat> ingredients = new ArrayList<>();
        String producedIn = "Build_ManufacturerMk1";
        String filename = "Recipe_test.json";
        String filepath = "//Game/FactoryGame/Recipes/Constructor/Recipe_IronPlate.Recipe_IronPlate_C";

        products.add(new Mat("Desc_IronPlate", 10));
        products.add(new Mat("Desc_LiquidOil", 5000));
        ingredients.add(new Mat("Desc_OreIron", 20));
        ingredients.add(new Mat("Desc_CopperWire", 10));


        Recipe rec = new Recipe(products, ingredients, filename, producedIn);

        CreateJSON.saveRecipeAsJson(rec, filepath);
    }

    private static int getBiasedRandomInt(int min, int max, int bias) { // In copilot we trust lmao
    if (bias <= 0) return min;
    if (bias >= 100) return max;
    if (bias == 50) return min + random.nextInt(max - min + 1);

    double r = random.nextDouble(); // uniform [0,1)
    // Map bias 0..50 to favor min, 50..100 to favor max
    if (bias < 50) {
        // Skew towards min
        double power = 1 + (49.0 - bias) / 49.0 * 9.0; // power: 10 at bias=0, 1 at bias=50
        r = Math.pow(r, power);
    } else {
        // Skew towards max
        double power = 1 + (bias - 51.0) / 49.0 * 9.0; // power: 1 at bias=50, 10 at bias=100
        r = 1 - Math.pow(1 - r, power);
    }
    return min + (int) ((max - min + 1) * r);

}

}
