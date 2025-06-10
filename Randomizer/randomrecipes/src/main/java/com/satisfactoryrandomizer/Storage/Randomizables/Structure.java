package com.satisfactoryrandomizer.Storage.Randomizables;

public class Structure extends Randomizable {
    // Basic constructor
    public Structure(String name, Boolean available, Boolean craftable, String recipepath,
            String builderpath, int solidIn, int solidOut, int liquidIn, int liquidOut) {
        super(name, recipepath + "." + recipepath + "_C", available, craftable, null);
    }
}
