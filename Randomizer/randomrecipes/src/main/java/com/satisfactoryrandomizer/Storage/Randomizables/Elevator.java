package com.satisfactoryrandomizer.Storage.Randomizables;

import java.util.List;

public class Elevator extends Randomizable {

    // Basic constructor
    public Elevator(String name, List<String> extraCheck) {
        super(name, null, null, true, true, extraCheck);
    }
}
