package com.satisfactoryrandomizer.Storage.Randomizables;

public class EssentialStructure extends Structure {

    private int addWhen = 0; // Tier by which it has to be added, 0 = tutorial.
    private Boolean isCheap = false;

    // Basic constructor
    public EssentialStructure(String name, Boolean available, Boolean craftable, String recipepath, Boolean isCheap,
            int addWhen) {
        super(name, available, craftable, recipepath);
        this.isCheap = isCheap;
        this.addWhen = addWhen;
    }

    public boolean isCheap() {
        return this.isCheap;
    }

    public int addWhen() {
        return this.addWhen;
    }

}
