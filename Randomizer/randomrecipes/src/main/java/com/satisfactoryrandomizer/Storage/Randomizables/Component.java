package com.satisfactoryrandomizer.Storage.Randomizables;

import java.util.List;

import com.satisfactoryrandomizer.Storage.UiValues;

public class Component extends Randomizable {
    private List<String> extraCheck = null; // Items that need to be craftable before this component can be crafted
    private Boolean liquid;
    private int remainingUses = UiValues.getMaxRecipesUsed();

    // Constructor for raw materials, set as craftable as soon as available so that
    // no recipe is created for them.
    public Component(String name, Boolean available, Boolean liquid) {
        super(name, null, available, true, null);
        this.liquid = liquid;
    }

    // Full constructor
    public Component(String name, String recipepath, Boolean available, Boolean craftable, Boolean liquid) {
        super(name, recipepath + "." + recipepath + "_C", available, craftable, null);
        this.liquid = liquid;
    }

    // Full constructor for components that enable other components
    // (like uranium needs to check for several items)
    public Component(String name, String recipepath, Boolean available, Boolean craftable, Boolean liquid,
            List<String> extraCheck) {
        super(name, recipepath + "." + recipepath + "_C", available, craftable, null);
        this.liquid = liquid;
    }

    public List<String> getExtraCheck() {
        return extraCheck;
    }

    public Boolean isLiquid() {
        return liquid;
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