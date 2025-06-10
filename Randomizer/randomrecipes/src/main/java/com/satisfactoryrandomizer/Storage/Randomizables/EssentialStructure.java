package com.satisfactoryrandomizer.Storage.Randomizables;

public class EssentialStructure extends Structure {

    private Boolean isTutorial = false;
    private Boolean isCheap = false;

    // Basic constructor
    public EssentialStructure(String name, Boolean available, Boolean craftable, String recipepath, Boolean isCheap,
            Boolean isTutorial) {
        super(name, available, craftable, recipepath);
        this.isCheap = isCheap;
        this.isTutorial = isTutorial;
    }

    public boolean isCheap() {
        return isCheap;
    }

    public boolean isTutorial() {
        return isTutorial;
    }

}
