package com.satisfactoryrandomizer.Storage;

public class CraftStation {

    private String name;
    private String recipepath = null;
    private Boolean available;
    private Boolean craftable;
    private int solidIn;
    private int solidOut;
    private int liquidIn;
    private int liquidOut;

    // Basic constructor
    public CraftStation(String name, Boolean available, String recipepath,
            int solidIn, int solidOut, int liquidIn, int liquidOut) {
        this.name = name;
        this.available = available;
        this.recipepath = recipepath + "." + recipepath + "_C";
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
