package com.satisfactoryrandomizer.Storage.Randomizables;

public class Milestone extends Randomizable {
    // Basic constructor
    public Milestone(String name, Boolean available, Boolean craftable, String recipepath, String extraCheck) {
        super(name, recipepath + "." + recipepath + "_C", available, craftable, null);
    }
}
