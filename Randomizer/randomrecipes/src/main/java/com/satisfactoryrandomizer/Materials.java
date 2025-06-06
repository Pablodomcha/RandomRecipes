package com.satisfactoryrandomizer;

import java.util.List;
import java.util.ArrayList;

public class Materials {
    List<String> available = new ArrayList<>();
    List<String> unavailable = new ArrayList<>();

    public Materials() {
        // Initialize the available materials
        available.add("Desc_IronIngot");
        available.add("Desc_IronRod");
        available.add("Desc_IronPlate");
    }
}