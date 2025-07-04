package com.satisfactoryrandomizer.Storage.JSONables;

import java.util.List;

import com.satisfactoryrandomizer.Storage.Data.Mat;

public class JSONableRecipeVN extends JSONableRecipe {
    private int VariablePowerConsumptionConstant = 1;
    private int VariablePowerConsumptionFactor = 1;


    public JSONableRecipeVN(List<Mat> products, List<Mat> ingredients, List<String> producedIn, double manufacturingDuration, double manualDuration,String override , int minenerg) {
        super(products, ingredients, producedIn, manufacturingDuration, manualDuration, override);
        this.VariablePowerConsumptionConstant = minenerg;
        this.VariablePowerConsumptionFactor = minenerg * 2;
    }
}
