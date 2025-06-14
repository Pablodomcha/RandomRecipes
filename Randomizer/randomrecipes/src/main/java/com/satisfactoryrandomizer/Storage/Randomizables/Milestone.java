package com.satisfactoryrandomizer.Storage.Randomizables;

import java.util.List;

public class Milestone extends Randomizable {

    private int nRecipes;
    private List<String> fixedUnlocks;
    private int phase;
    // Recipepath is a shcematic path, but W/e

    // Basic constructor
    public Milestone(String name, Boolean available, Boolean craftable, String recipepath, List<String> extraCheck, int phase) {
        super(name, recipepath, recipepath + "." + recipepath + "_C", available, craftable, extraCheck);
        this.phase = phase;
    }

    public int getnRecipes() {
        return this.nRecipes;
    }

    public void setnRecipes(int nRecipes) {
        this.nRecipes = nRecipes;
    }

    public List<String> getFixedUnlocks() {
        return this.fixedUnlocks;
    }

    public void addFixedUnlock(String fixedUnlock) {
        this.fixedUnlocks.add(fixedUnlock);
    }

    public int getPhase() {
        return this.phase;
    }
}
