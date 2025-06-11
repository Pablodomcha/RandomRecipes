package com.satisfactoryrandomizer.Storage.Randomizables;

import java.util.List;

public class Milestone extends Randomizable {

    private int nRecipes;
    private String fixedUnlocks;
    // Recipepath is a shcematic path, but W/e

    // Basic constructor
    public Milestone(String name, Boolean available, Boolean craftable, String recipepath, List<String> extraCheck, int nRecipes) {
        super(name, recipepath, recipepath + "." + recipepath + "_C", available, craftable, extraCheck);
        this.nRecipes = nRecipes;
    }

    public int getnRecipes() {
        return this.nRecipes;
    }

    public String getfixedUnlocks() {
        return this.fixedUnlocks;
    }

    public void setfixedUnlocks(String hubUnlocks) {
        this.fixedUnlocks = hubUnlocks;
    }
}
