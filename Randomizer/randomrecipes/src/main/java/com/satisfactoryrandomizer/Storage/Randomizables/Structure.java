package com.satisfactoryrandomizer.Storage.Randomizables;

public class Structure extends Randomizable {

    private Boolean power;

    // Basic constructor
    public Structure(String name, Boolean available, Boolean craftable, String recipepath, Boolean power) {
        super(name, recipepath, recipepath + "." + recipepath + "_C", available, craftable, null);
        this.power = power;
    }
}
