package com.satisfactoryrandomizer.Storage;

import java.util.List;

public class Component {

    private String name;
    private String recipepath = null;
    private Boolean available;
    private Boolean craftable;
    private List<String> extraCheck = null; // Used for components that require additional checks
    private Boolean liquid = false;
    private Boolean raw = false; // Indicates if the component is a raw material (no recipe)

    // Basic constructor
    public Component(String name, Boolean available) {
        this.name = name;
        this.available = available;
        this.craftable = available;
        this.raw = true; // Default to raw material if no recipe is provided
    }

    // Full constructor
    public Component(String name, String recipepath, Boolean available, Boolean craftable, Boolean liquid) {
        this.name = name;
        this.recipepath = recipepath + "." + recipepath + "_C";
        this.available = available;
        this.craftable = (available && craftable);
        this.liquid = liquid;
    }

    // Constructor for components without a recipe (Raw materials)
    public Component(String name, Boolean available, Boolean craftable, Boolean liquid) {
        this.name = name;
        this.available = available;
        this.craftable = (available && craftable);
        this.liquid = liquid;
    }

    // Full constructor for components that enable other components
    // (like uranium needs to check for several items)
    public Component(String name, String recipepath, Boolean available, Boolean craftable, Boolean liquid,
            List<String> extraCheck) {
        this.name = name;
        this.recipepath = recipepath + "." + recipepath + "_C";
        this.available = available;
        this.craftable = (available && craftable);
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
        return available;
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

    public void setCraftable(Boolean craftable, Boolean checkAvailable) {
        this.craftable = craftable;
        if (checkAvailable) { // If checkAvailable is true, ensure craftable is only true if available also is
            this.craftable = this.available && craftable;
        }
        if (!available && craftable) { // Warn if craftable is set to true while available is false
            System.out.println("Warning: Component '" + this.name + "' is set to craftable while not available.");
        }
    }

    public void setExtraCheck(List<String> extraCheck) {
        this.extraCheck = extraCheck;
    }

    public void setLiquid(Boolean liquid) {
        this.liquid = liquid;
    }

    public String toString() {
        return "Component{" +
                "name='" + name + '\'' +
                ", recipepath='" + recipepath + '\'' +
                ", available=" + available +
                ", craftable=" + craftable +
                ", extraCheck='" + extraCheck + '\'' +
                '}';
    }
}