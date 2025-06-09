package com.satisfactoryrandomizer.Storage;

public class CraftStation {

    private final String name;
    private String recipepath;
    private final String builderpath;
    private final Boolean available;
    private final Boolean craftable;
    private final int solidIn;
    private final int solidOut;
    private final int liquidIn;
    private final int liquidOut;

    // Basic constructor
    public CraftStation(String name, Boolean available, Boolean craftable, String recipepath,
            String builderpath, int solidIn, int solidOut, int liquidIn, int liquidOut) {
        this.name = name;
        this.available = available;
        this.craftable = craftable;
        this.recipepath = recipepath + "." + recipepath + "_C";
        this.builderpath = builderpath;
        this.solidIn = solidIn;
        this.solidOut = solidOut;
        this.liquidIn = liquidIn;
        this.liquidOut = liquidOut;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getRecipePath() {
        return recipepath;
    }

    public String getBuilderPath() {
        return builderpath;
    }

    public Boolean isAvailable() {
        return available;
    }

    public Boolean isCraftable() {
        return craftable;
    }

    public int getSolidIn() {
        return solidIn;
    }

    public int getSolidOut() {
        return solidOut;
    }

    public int getLiquidIn() {
        return liquidIn;
    }

    public int getLiquidOut() {
        return liquidOut;
    }

    public int getTotalIn() {
        return solidIn + liquidIn;
    }

    public int getTotalOut() {
        return solidOut + liquidOut;
    }

    public void setRecipePath(String recipepath) {
        this.recipepath = recipepath;
    }
}
