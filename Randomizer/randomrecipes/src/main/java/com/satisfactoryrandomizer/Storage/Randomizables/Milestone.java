package com.satisfactoryrandomizer.Storage.Randomizables;

import java.util.List;

public class Milestone extends Randomizable {

    private int nRecipes;
    private String fixedUnlocks;
    private Boolean isTutorial;
    // Recipepath is a shcematic path, but W/e

    // Basic constructor
    public Milestone(String name, Boolean available, Boolean craftable, String recipepath, List<String> extraCheck) {
        super(name, recipepath, recipepath + "." + recipepath + "_C", available, craftable, extraCheck);
        this.isTutorial = false;
    }

    // Constructor for tutorials
    public Milestone(String name, Boolean available, Boolean craftable, String recipepath, List<String> extraCheck,
            Boolean isTutorial) {
        super(name, recipepath, recipepath + "." + recipepath + "_C", available, craftable, extraCheck);
        this.isTutorial = isTutorial;
    }

    public int getnRecipes() {
        return this.nRecipes;
    }

    public void setnRecipes(int nRecipes) {
        this.nRecipes = nRecipes;
    }

    public String getfixedUnlocks() {
        return this.fixedUnlocks;
    }

    public void setfixedUnlocks(String fixedUnlocks) {
        this.fixedUnlocks = fixedUnlocks;
    }

    public Boolean isTutorial() {
        return this.isTutorial;
    }
}
