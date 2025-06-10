package com.satisfactoryrandomizer.Storage.Randomizables;

import java.util.List;

public class Structure extends Randomizable {

    private String rewards;
    // Recipepath is a shcematic path, but W/e

    // Basic constructor
    public Structure(String name, Boolean available, Boolean craftable, String recipepath, List<String> extraCheck) {
        super(name, recipepath + "." + recipepath + "_C", available, craftable, extraCheck);
    }

    public String getRewards() {
        return rewards;
    }

    public void setRewards(String rewards) {
        this.rewards = rewards;
    }
}
