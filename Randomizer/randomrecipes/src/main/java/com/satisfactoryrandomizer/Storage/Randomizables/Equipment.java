package com.satisfactoryrandomizer.Storage.Randomizables;

import java.util.List;

public class Equipment extends Randomizable {

    // Basic constructor
    // Their name is their recipe, as they can't be used as materials
    public Equipment(String name, Boolean available, Boolean craftable, String recipepath, Boolean power,
            List<String> extraCheck) {
        super(name, recipepath, recipepath + "." + recipepath + "_C", available, craftable, extraCheck);
    }
}
