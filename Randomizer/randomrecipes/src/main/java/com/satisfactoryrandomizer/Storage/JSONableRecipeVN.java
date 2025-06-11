package com.satisfactoryrandomizer.Storage;

import java.util.List;

public class JSONableRecipeVN extends JSONableRecipe {
    private int VariablePowerConsumptionConstant = 1;
    private int VariablePowerConsumptionFactor = 1;


    public JSONableRecipeVN(List<Mat> products, List<Mat> ingredients, List<String> producedIn, double manufacturingDuration, double manualDuration, int minenerg) {
        super(products, ingredients, producedIn, manufacturingDuration, manualDuration);
        this.VariablePowerConsumptionConstant = minenerg;
        this.VariablePowerConsumptionFactor = minenerg * 2;
    }
}
