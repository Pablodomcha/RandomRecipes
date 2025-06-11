package com.satisfactoryrandomizer.Storage.Randomizables;

public class Structure extends Randomizable {
    // Basic constructor
    public Structure(String name, Boolean available, Boolean craftable, String recipepath) {
        super(name, recipepath, recipepath + "." + recipepath + "_C", available, craftable, null);
    }
}
