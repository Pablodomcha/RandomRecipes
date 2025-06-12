package com.satisfactoryrandomizer.Storage.Randomizables;

import java.util.List;

import com.satisfactoryrandomizer.Storage.UiValues;

public class Component extends Randomizable {
    private Boolean liquid;
    private int remainingUses = UiValues.getMaxRecipesUsed();
    private Boolean raw = false;

    // Constructor for raw materials, set as craftable as soon as available so that
    // no recipe is created for them.
    public Component(String name, String recipepath, Boolean available, Boolean liquid, List<String> extraCheck) {
        super(name, recipepath, null, available, true, extraCheck);
        this.liquid = liquid;
        this.raw = true;
    }

    // Full constructor
    public Component(String name, String recipepath, Boolean available, Boolean craftable, Boolean liquid) {
        super(name, recipepath, recipepath + "." + recipepath + "_C", available, craftable, null);
        this.liquid = liquid;
    }

    // Full constructor for components that enable other components
    // (like uranium needs to check for several items)
    public Component(String name, String recipepath, Boolean available, Boolean craftable, Boolean liquid,
            List<String> extraCheck) {
        super(name, recipepath, recipepath + "." + recipepath + "_C", available, craftable, null);
        this.liquid = liquid;
    }

    public Boolean isLiquid() {
        return this.liquid;
    }

    public void setLiquid(Boolean liquid) {
        this.liquid = liquid;
    }

    public Boolean isRaw() {
        return this.raw;
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
}