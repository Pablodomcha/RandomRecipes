package com.satisfactoryrandomizer.Storage.Data;

// Material for recipe, only has the material name and amount of it created.
public class Mat {

    // Capitalized for JSONification purposes
    String Item;
    int Amount;

    public Mat(String name, int amount) {
        this.Item = name;
        this.Amount = amount;
    }

    public String getName() {
        return Item;
    }

    public int getAmount() {
        return Amount;
    }
}
