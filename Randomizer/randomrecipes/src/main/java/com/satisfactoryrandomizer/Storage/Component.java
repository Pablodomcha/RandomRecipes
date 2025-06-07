package com.satisfactoryrandomizer.Storage;
public class Component {

    private String name;
    private String recipepath = null;
    private Boolean available;
    private Boolean craftable;
    private String extraCheck = null;

    // Basic constructor
    public Component(String name, Boolean available) {
        this.name = name;
        this.available = available;
        this.craftable = available;
    }

    // Full constructor
    public Component(String name, String recipepath, Boolean available, Boolean craftable) {
        this.name = name;
        this.recipepath = recipepath + "." + recipepath + "_C";
        this.available = available;
        this.craftable = (available && craftable);
    }

    // Constructor for components without a recipe (Raw materials)
    public Component(String name, Boolean available, Boolean craftable) {
        this.name = name;
        this.available = available;
        this.craftable = (available && craftable);
    }

    // Full constructor for components that enable other components
    // (like uranium needs to check for several items)
    public Component(String name, String recipepath, Boolean available, Boolean craftable, String extraCheck) {
        this.name = name;
        this.recipepath = recipepath + "." + recipepath + "_C";
        this.available = available;
        this.craftable = (available && craftable);
        this.extraCheck = extraCheck;
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

    public String getExtraCheck() {
        return extraCheck;
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

    public void setExtraCheck(String extraCheck) {
        this.extraCheck = extraCheck;
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