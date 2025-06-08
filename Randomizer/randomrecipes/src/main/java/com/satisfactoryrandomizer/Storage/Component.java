package com.satisfactoryrandomizer.Storage;

import java.util.List;

public class Component {

    private String name;
    private String recipepath = null;
    private Boolean available;
    private Boolean craftable;
    private List<String> extraCheck = null; // Items that need to be craftable before this component can be crafted
    private Boolean liquid = false;
    private int remainingUses = UiValues.getMaxRecipesUsed();

    // Basic constructor
    public Component(String name, Boolean available) {
        this.name = name;
        this.available = available;
        this.craftable = available;
    }

    // Full constructor
    public Component(String name, String recipepath, Boolean available, Boolean craftable, Boolean liquid) {
        this.name = name;
        this.recipepath = recipepath + "." + recipepath + "_C";
        this.available = available;
        this.craftable = craftable;
        this.liquid = liquid;
    }

    // Constructor for components without a recipe (Raw materials)
    public Component(String name, Boolean available, Boolean craftable, Boolean liquid) {
        this.name = name;
        this.available = available;
        this.craftable = craftable;
        this.liquid = liquid;
    }

    // Full constructor for components that enable other components
    // (like uranium needs to check for several items)
    public Component(String name, String recipepath, Boolean available, Boolean craftable, Boolean liquid,
            List<String> extraCheck) {
        this.name = name;
        this.recipepath = recipepath + "." + recipepath + "_C";
        this.available = available;
        this.craftable = craftable;
        this.extraCheck = extraCheck;
        this.liquid = liquid;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getRecipePath() {
        return recipepath;
    }

    public Boolean isAvailable() {
        return available && remainingUses > 0;
    }

    public Boolean isCraftable() {
        return craftable;
    }

    public List<String> getExtraCheck() {
        return extraCheck;
    }

    public Boolean isLiquid() {
        return liquid;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setRecipePath(String recipepath) {
        this.recipepath = recipepath;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public void setCraftable(Boolean craftable) {
        this.craftable = craftable;
    }

    public void setExtraCheck(List<String> extraCheck) {
        this.extraCheck = extraCheck;
    }

    public void setLiquid(Boolean liquid) {
        this.liquid = liquid;
    }

    /**
     * Use one of the material, reducing the amount of remaining uses by 1.
     * 
     * @return The new amount of remaining uses.
     */
    public int use() {
        return --this.remainingUses;
    }

    /**
     * Refill the material, increasing the amount of remaining uses by 1.
     * 
     * @return The new amount of remaining uses.
     */
    public int refill() {
        return ++this.remainingUses;
    }
}