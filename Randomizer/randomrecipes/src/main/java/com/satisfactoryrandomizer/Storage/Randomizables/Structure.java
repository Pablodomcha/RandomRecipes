package com.satisfactoryrandomizer.Storage.Randomizables;

import java.util.List;

public class Structure extends Randomizable {

    // Basic constructor
    public Structure(String name, Boolean available, Boolean craftable, String recipepath, Boolean power) {
        super(name, recipepath, recipepath + "." + recipepath + "_C", available, craftable, null);
    }

    public Structure(String name, Boolean available, Boolean craftable, String recipepath,
            List<String> extraCheck) {
        super(name, recipepath, recipepath + "." + recipepath + "_C", available, craftable, extraCheck);
    }

    // Relic of the old days when the power variable existed (still used for any structure that used it)
    public Structure(String name, Boolean available, Boolean craftable, String recipepath, Boolean power,
            List<String> extraCheck) {
        super(name, recipepath, recipepath + "." + recipepath + "_C", available, craftable, extraCheck);
    }
}
