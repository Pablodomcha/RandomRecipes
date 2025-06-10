package com.satisfactoryrandomizer.Storage.Randomizables;

import java.util.ArrayList;
import java.util.List;

public class Randomizable {

    private final String name;
    private String recipepath;
    private Boolean available;
    private Boolean craftable;
    private final List<String> extraCheck; // Items that need to be craftable before this component can be crafted
    private final List<String> checkAlso = new ArrayList<>(); // Items that this component is an extraCheck for

    // Extra checks can be in form of generic "station with liquid output" form or
    // specific items.

    public Randomizable(String name, String recipepath, Boolean available, Boolean craftable,
            List<String> extraCheck) {
        this.name = name;
        this.recipepath = recipepath;
        this.available = available;
        this.craftable = craftable;

        if (extraCheck == null)
            this.extraCheck = new ArrayList<>();
        else
            this.extraCheck = extraCheck;
    }

    public String getName() {
        return this.name;
    }

    public String getRecipePath() {
        return this.recipepath;
    }

    public void setRecipePath(String recipepath) {
        this.recipepath = recipepath;
    }

    public Boolean isAvailable() {
        return this.available && extraCheck.isEmpty();
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Boolean isCraftable() {
        return this.craftable;
    }

    public void setCraftable(Boolean craftable) {
        this.craftable = craftable;
    }

    public List<String> getExtraCheck() {
        return this.extraCheck;
    }

    public void removeExtraCheck(String extraCheck) {
        this.extraCheck.remove(extraCheck);
    }

    public List<String> getCheckAlso() {
        return this.checkAlso;
    }

    public void addCheckAlso(String checkAlso) {
        this.checkAlso.add(checkAlso);
    }
}
