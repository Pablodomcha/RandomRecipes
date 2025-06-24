package com.satisfactoryrandomizer.Storage.Randomizables;

import java.util.List;

import com.satisfactoryrandomizer.Storage.UiValues;

public class Component extends Randomizable {
    private Boolean liquid;
    private int remainingUses = UiValues.getMaxRecipesUsed();
    private int maxstack = 50;

    // Constructor for raw materials, set as craftable as soon as available so that
    // no recipe is created for them.
    public Component(String name, Boolean liquid, List<String> extraCheck) {
        super(name, null, null, true, true, extraCheck);
        this.liquid = liquid;
    }

    // Full constructor
    public Component(String name, String recipepath, Boolean available, Boolean craftable, Boolean liquid) {
        super(name, recipepath, recipepath + "." + recipepath + "_C", available, craftable, null);
        this.liquid = liquid;
    }

    // Full constructor for components that need other checks
    // (like uranium needs to check for several items)
    public Component(String name, String recipepath, Boolean available, Boolean craftable, Boolean liquid, int maxstack,
            List<String> extraCheck) {
        super(name, recipepath, recipepath + "." + recipepath + "_C", available, craftable, extraCheck);
        this.liquid = liquid;
        this.maxstack = maxstack;
    }

    public Boolean isLiquid() {
        return this.liquid;
    }

    public void setLiquid(Boolean liquid) {
        this.liquid = liquid;
    }

    public int getStack() {
        return this.maxstack;
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

    /**
     * Check if the component is available, i.e. if it has remaining uses and is
     * generally available.
     * 
     * @return True if the component is available, false if not.
     */
    @Override
    public Boolean isAvailable() {
        return super.isAvailable() && this.remainingUses > 0;
    }

    public Boolean isAvailableIgnoreUses(){
        return super.isAvailable();
    }
}