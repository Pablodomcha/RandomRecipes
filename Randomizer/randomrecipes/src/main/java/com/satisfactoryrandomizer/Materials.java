package com.satisfactoryrandomizer;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class Materials {
    Set<String> available = new HashSet<>();
    List<String[]> craftStart = new ArrayList<>();
    List<String[]> equipStart = new ArrayList<>();
    List<String[]> buildingStart = new ArrayList<>();
    List<String[]> unavailableRaw = new ArrayList<>();
    List<String[]> unavailableCraft = new ArrayList<>();

    public Materials() {
        // Initialize the available materials
        // Starting you only have directly gatherable materials
        available.add("Desc_OreIron");
        available.add("Desc_OreCopper");
        available.add("Desc_Stone");

        // available at Onboarding, needs to be randomized at start.
        // /Recipes/Constructor/
        craftStart.add(new String[] { "Desc_IronPlate", "Recipe_IronPlate" });
        craftStart.add(new String[] { "Desc_IronRod", "Recipe_IronRod" });
        craftStart.add(new String[] { "Desc_Wire", "Recipe_Wire" });
        craftStart.add(new String[] { "Desc_Cable", "Recipe_Cable" });
        craftStart.add(new String[] { "Desc_Cement", "Recipe_Concrete" });
        craftStart.add(new String[] { "Desc_IronScrew", "Recipe_Screw" });
        craftStart.add(new String[] { "Desc_GenericBiomass", "Recipe_Biomass_Leaves" });
        craftStart.add(new String[] { "Desc_GenericBiomass", "Recipe_Biomass_Leaves" });

        craftStart = AddPrefix(craftStart, "//Game/FactoryGame/Recipes/Constructor/");

        // /Recipes/Smelter/
        craftStart.add(new String[] { "Desc_IronIngot", "//Game/FactoryGame/Recipes/Smelter/Recipe_IngotIron" });
        craftStart.add(new String[] { "Desc_CopperIngot", "//Game/FactoryGame/Recipes/Smelter/Recipe_IngotCopper" });

        // /Recipes/Assembler/
        craftStart.add(new String[] { "Desc_IronPlateReinforced",
                "//Game/FactoryGame/Recipes/Assembler/Recipe_IronPlateReinforced" });

        // Now the rest of resources.
        // Resource/RawResources/
        unavailableRaw.add(new String[] { "", "" });
    }

    private List<String[]> AddPrefix(List<String[]> list, String prefix) {
        List<String[]> prefixedList = new ArrayList<>(list);

        for (String[] item : prefixedList) {
            item[1] = prefix + item[1];
        }

        return prefixedList;

    }
}