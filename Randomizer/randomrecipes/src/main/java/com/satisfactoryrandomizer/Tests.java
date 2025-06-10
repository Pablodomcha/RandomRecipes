package com.satisfactoryrandomizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.satisfactoryrandomizer.Storage.Mat;
import com.satisfactoryrandomizer.Storage.Recipe;

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
        int time = 2;

        products.add(new Mat("Desc_IronPlate", 10));
        products.add(new Mat("Desc_LiquidOil", 5000));
        //ingredients.add(new Mat("Desc_OreIron", 20));
        //ingredients.add(new Mat("Desc_CopperWire", 10));


        Recipe rec = new Recipe(products, ingredients, filename, producedIn, time, 1.0);

        //CreateJSON.saveRecipeAsJson(rec, filepath);
    }

}
