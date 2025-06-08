package com.satisfactoryrandomizer;

import java.io.FileWriter;
import java.io.IOException;
import com.satisfactoryrandomizer.Storage.Component;
import com.satisfactoryrandomizer.Storage.Mat;
import com.satisfactoryrandomizer.Storage.Recipe;

import java.util.List;
import java.util.ArrayList; //mats will be an Arraylist, so it's actually used 

public class CreateJSON {

    public static void saveRecipeAsJson(Recipe recipe) {
        String json = recipeToJson(recipe.getComponent(), recipe.getAmount(), recipe.getIngredients(),
                recipe.getSchema(), recipe.getStation());
        try (FileWriter writer = new FileWriter(recipe.getFilename())) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String recipeToJson(Component comp, int amount, List<Mat> mats, String schema, String station) {
        String file = comp.getRecipePath() + "\r\n" +
                "{\r\n" +
                "  \"$schema\": \"" + schema + "\",\r\n" +
                "  \"ClearIngredients\": true,\r\n" +
                "  \"Products\": [\r\n    {" + "\r\n" +
                "      \"Item\": \"" + comp.getName() + "\",\r\n" +
                "      \"Amount\": " + amount + "\r\n" +
                "    }\r\n  ],\r\n";

        // We don't want to add "Ingredients" if there are none
        if (mats.size() != 0) {
            file += "  \"Ingredients\": [" + "\r\n";
        }

        for (int i = 0; i < mats.size(); i++) {
            file += "    {\r\n" +
                    "      \"Item\": \"" + mats.get(i).getName() + "\",\r\n" +
                    "      \"Amount\": " + mats.get(i).getAmount() + "\r\n" +
                    "    }";
            if (i < mats.size() - 1) {
                file += ",";
            }
            file += "\r\n";
        }
        // Don't close and array that's not started
        if (mats.size() != 0) {
            file += "  ],\r\n";
        }

        file += "  \"ProducedIn\": [\"" + station + "\"]\r\n}";

        return file;
    }
}
