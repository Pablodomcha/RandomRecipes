package com.satisfactoryrandomizer.Storage.Randomizables;

import java.util.List;

public class Milestone extends Randomizable {

    private List<String[]> rewards;
    // Recipepath is a shcematic path, but W/e

    // Basic constructor
    public Milestone(String name, Boolean available, Boolean craftable, String recipepath, List<String> extraCheck) {
        super(name, recipepath + "." + recipepath + "_C", available, craftable, extraCheck);
    }

    /**
     * Retrieves the list of rewards associated with this milestone.
     *
     * @return A list of rewards, where each reward is an array {rewardType, reward}.
     */

    public List<String[]> getRewards() {
        return this.rewards;
    }

    /**
     * Retrieves and removes the first reward in the list of rewards associated with this milestone.
     * 
     * @return A string array {rewardType, reward}.
     */
    public String[] getrmRewards() {
        String[] temp = this.rewards.get(0);
        this.rewards.remove(0);
        return temp;
    }

    /**
     * Sets the rewards for this milestone.
     * 
     * @param rewards A list of rewards, where each reward is an array {rewardType, reward}.
     */
    public void setRewards(List<String[]> rewards) {
        this.rewards = rewards;
    }
}
