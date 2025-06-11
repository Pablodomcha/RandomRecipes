package com.satisfactoryrandomizer.Storage.Randomizables;

public class EssentialStructure extends Structure {

    private int addWhen = 0;
    private Boolean isCheap = false;

    // Basic constructor
    public EssentialStructure(String name, Boolean available, Boolean craftable, String recipepath, Boolean isCheap,
            int isTutorial) {
        super(name, available, craftable, recipepath);
        this.isCheap = isCheap;
        this.addWhen = isTutorial;
    }

    public boolean isCheap() {
        return this.isCheap;
    }

    public int addWhen() {
        return this.addWhen;
    }

}
