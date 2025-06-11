package com.satisfactoryrandomizer.Storage.Randomizables;

public class CraftStation extends Randomizable {

    private final String builderpath;
    private final int solidIn;
    private final int solidOut;
    private final int liquidIn;
    private final int liquidOut;

    // Basic constructor
    public CraftStation(String name, Boolean available, Boolean craftable, String recipepath,
            String builderpath, int solidIn, int solidOut, int liquidIn, int liquidOut) {
        super(name, recipepath, recipepath + "." + recipepath + "_C", available, craftable, null);
        this.builderpath = builderpath;
        this.solidIn = solidIn;
        this.solidOut = solidOut;
        this.liquidIn = liquidIn;
        this.liquidOut = liquidOut;
    }


    public String getBuilderPath() {
        return this.builderpath;
    }

    public int getSolidIn() {
        return this.solidIn;
    }

    public int getSolidOut() {
        return this.solidOut;
    }

    public int getLiquidIn() {
        return this.liquidIn;
    }

    public int getLiquidOut() {
        return this.liquidOut;
    }

    public int getTotalIn() {
        return this.solidIn + this.liquidIn;
    }

    public int getTotalOut() {
        return this.solidOut + this.liquidOut;
    }
}
