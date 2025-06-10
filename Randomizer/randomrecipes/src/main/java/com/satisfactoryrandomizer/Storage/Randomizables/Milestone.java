package com.satisfactoryrandomizer.Storage.Randomizables;

import java.util.List;

public class Milestone extends Randomizable {

    private List<String> rewards;
    // Recipepath is a shcematic path, but W/e

    // Basic constructor
    public Milestone(String name, Boolean available, Boolean craftable, String recipepath, List<String> extraCheck) {
        super(name, recipepath + "." + recipepath + "_C", available, craftable, extraCheck);
    }
    
    public List<String> getRewards() {
        return this.rewards;
    }

    public String getrmRewards(){
        return this.rewards.get(0);
    }

    public void setRewards(List<String> rewards) {
        this.rewards = rewards;
    }
}
