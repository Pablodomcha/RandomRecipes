package com.satisfactoryrandomizer.Storage.Randomizables;

import java.util.ArrayList;
import java.util.List;

public class Randomizable {

    private final String name;
    private final String path;
    private String recipepath;
    private Boolean available;
    private Boolean craftable;
    private final List<String> extraCheck = new ArrayList<>(); // Items that need to be craftable before this component can be
                                                         // crafted
    private final List<String> checkAlso = new ArrayList<>(); // Items that this component is an extraCheck for

    // Extra checks can be in form of generic "station with liquid output" form or
    // specific items.

    public Randomizable(String name, String path, String recipepath, Boolean available, Boolean craftable,
            List<String> extraCheck) {
        this.name = name;
        this.path = path;
        this.recipepath = recipepath;
        this.available = available;
        this.craftable = craftable;

        if (extraCheck != null)
            this.extraCheck.addAll(extraCheck);
    }

    public String getName() {
        return this.name;
    }

    public String getRecipePath() {
        return this.recipepath;
    }

    public String getPath() {
        return this.path;
    }

    public void setRecipePath(String recipepath) {
        this.recipepath = recipepath;
    }

    public Boolean isAvailable() {
        return this.available && extraCheck.isEmpty();
    }

    public Boolean trueAvailable(){
        return this.available;
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

    public void addExtraCheck(String extraCheck) {
        this.extraCheck.add(extraCheck);
    }

    public void removeExtraCheck(String extraCheck) {
        for (int i = 0; i < this.extraCheck.size(); i++) {
            if (this.extraCheck.get(i).equals(extraCheck)) {
                this.extraCheck.remove(i);
            }
        }
    }

    public List<String> getCheckAlso() {
        return this.checkAlso;
    }

    public void addCheckAlso(String checkAlso) {
        this.checkAlso.add(checkAlso);
    }
}
