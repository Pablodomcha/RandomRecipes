package com.satisfactoryrandomizer.Storage.Randomizables;

import java.util.List;

public class Randomizable {

    private final String name;
    private String recipepath;
    private Boolean available;
    private Boolean craftable;
    private List<String> extraCheck = null; // Items that need to be craftable before this component can be crafted

    public Randomizable(String name, String recipepath, Boolean available, Boolean craftable,
            List<String> extraCheck) {
        this.name = name;
        this.recipepath = recipepath;
        this.available = available;
        this.craftable = craftable;
        this.extraCheck = extraCheck;
    }

    public String getName() {
        return name;
    }

    public String getRecipePath() {
        return recipepath;
    }

    public void setRecipePath(String recipepath) {
        this.recipepath = recipepath;
    }

    public Boolean isAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Boolean isCraftable() {
        return craftable;
    }

    public void setCraftable(Boolean craftable) {
        this.craftable = craftable;
    }

    public List<String> getExtraCheck() {
        return extraCheck;
    }

    public void setExtraCheck(List<String> extraCheck) {
        this.extraCheck = extraCheck;
    }
}
