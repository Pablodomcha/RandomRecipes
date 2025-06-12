package com.satisfactoryrandomizer.Storage.Randomizables;

public class OtherUnlocks extends Randomizable {

    private String type;
    private int quantity;

    // Basic constructor
    public OtherUnlocks(String name, Boolean available, Boolean craftable, String recipepath, String type,
            int quantity) {
        super(name, recipepath, recipepath + "." + recipepath + "_C", available, craftable, null);
        this.type = type;
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

}
