package com.satisfactoryrandomizer.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.satisfactoryrandomizer.Console;
import com.satisfactoryrandomizer.Storage.Randomizables.Component;
import com.satisfactoryrandomizer.Storage.Randomizables.CraftStation;

public class Materials {

    // List of Components
    private List<Component> components = new ArrayList<>();
    // List of craftable items at the start of the game
    private List<CraftStation> stations = new ArrayList<>();

    public Materials() {

        // Create a temporary lists to add the prefixes to before adding to components
        List<Component> tempList = new ArrayList<>();
        List<Component> prefixedTempList;
        List<CraftStation> tempStations = new ArrayList<>();

        // Initialize the list of available stations
        tempStations.add(new CraftStation("Assembler", false, false, "Recipe_AssemblerMk1", "Build_AssemblerMk1",
                2, 1, 0, 0));
        tempStations.add(new CraftStation("Blender", false, false, "Recipe_Blender", "Build_Blender",
                2, 1, 2, 1));
        tempStations.add(new CraftStation("Constructor", true, true, "Recipe_ConstructorMk1", "Build_ConstructorMk1",
                1, 1, 0, 0));
        tempStations
                .add(new CraftStation("Manufacturer", false, false, "Recipe_ManufacturerMk1", "Build_ManufacturerMk1",
                        4, 1, 0, 0));
        tempStations.add(new CraftStation("Packager", false, false, "Recipe_Packager", "Build_Packager",
                1, 1, 1, 1));
        tempStations.add(new CraftStation("PAccel", false, false, "Recipe_HadronCollider", "Build_HadronCollider",
                2, 1, 1, 0));
        tempStations.add(new CraftStation("Refinery", false, false, "Recipe_OilRefinery", "Build_OilRefinery",
                1, 1, 1, 1));
        tempStations.add(new CraftStation("Converter", false, false, "Recipe_Converter", "Build_Converter",
                2, 1, 0, 1));
        tempStations
                .add(new CraftStation("QuantumEncoder", false, false, "Recipe_QuantumEncoder", "Build_QuantumEncoder",
                        3, 1, 1, 1));
        tempStations.add(new CraftStation("SmelterMk1", true, true, "Recipe_SmelterMk1", "Build_SmelterMk1",
                1, 1, 0, 0));
        tempStations.add(new CraftStation("FoundryMk1", false, false, "Recipe_FoundryMk1", "Build_FoundryMk1",
                2, 1, 0, 0));
        stations = AddPrefixStat(tempStations, "//Game/FactoryGame/Recipes/Buildings/");

        // Initialize the available materials
        // Starting you only have directly gatherable materials
        components.add(new Component("Desc_OreIron", true, false));
        components.add(new Component("Desc_OreCopper", true, false));
        components.add(new Component("Desc_Stone", true, false));

        // available at Onboarding, Is available but not nececessarily craftable
        // /Recipes/Constructor/
        tempList.add(new Component("Desc_IronPlate", "Recipe_IronPlate", true, false, false));
        tempList.add(new Component("Desc_IronRod", "Recipe_IronRod", true, false, false));
        tempList.add(new Component("Desc_Wire", "Recipe_Wire", true, false, false));
        tempList.add(new Component("Desc_Cable", "Recipe_Cable", true, false, false));
        tempList.add(new Component("Desc_Cement", "Recipe_Concrete", true, false, false));
        tempList.add(new Component("Desc_IronScrew", "Recipe_Screw", true, false, false));
        tempList.add(new Component("Desc_GenericBiomass", "Recipe_Biomass_Leaves", true, false, false));
        tempList.add(new Component("Desc_Leaves", "Recipe_Biomass_Wood", true, false, false));

        prefixedTempList = AddPrefix(tempList, "//Game/FactoryGame/Recipes/Constructor/");
        components.addAll(prefixedTempList);
        tempList.clear();
        prefixedTempList.clear();

        // /Recipes/Smelter/
        tempList.add(
                new Component("Desc_IronIngot", "Recipe_IngotIron", true, false, false));
        tempList.add(new Component("Desc_CopperIngot", "Recipe_IngotCopper", true, false, false));

        prefixedTempList = AddPrefix(tempList, "//Game/FactoryGame/Recipes/Smelter/");
        components.addAll(prefixedTempList);
        tempList.clear();
        prefixedTempList.clear();

        // /Recipes/Assembler/
        tempList.add(
                new Component("Desc_IronPlateReinforced", "Recipe_IronPlateReinforced", true, false, false));

        prefixedTempList = AddPrefix(tempList, "//Game/FactoryGame/Recipes/Assembler/");
        components.addAll(prefixedTempList);
        tempList.clear();
        prefixedTempList.clear();

        // Now the rest of resources.
        // Resource/RawResources/
        // components.add(new Component("", "", false, false));
    }

    private List<Component> AddPrefix(List<Component> list, String prefix) {
        List<Component> prefixedList = new ArrayList<>(list);

        for (int i = 0; i < prefixedList.size(); i++) {
            Component c = prefixedList.get(i);
            if (c.getRecipePath() != null && !c.getRecipePath().startsWith(prefix)) {
                prefixedList.get(i).setRecipePath(prefix + c.getRecipePath());
            }
        }
        Console.log("Prefixed List with " + prefix + ":");
        return prefixedList;
    }

    private List<CraftStation> AddPrefixStat(List<CraftStation> list, String prefix) {
        List<CraftStation> prefixedList = new ArrayList<>(list);

        for (int i = 0; i < prefixedList.size(); i++) {
            CraftStation c = prefixedList.get(i);
            if (c.getRecipePath() != null && !c.getRecipePath().startsWith(prefix)) {
                prefixedList.get(i).setRecipePath(prefix + c.getRecipePath());
            }
        }
        Console.log("Prefixed List with " + prefix + ":");
        return prefixedList;
    }

    // Getters and Setters

    public Component getComponentByName(String name) {
        for (Component component : components) {
            if (component.getName().equals(name)) {
                return component;
            }
        }
        Console.log("Component not found: " + name);
        return null;
    }

    public Boolean replaceComponentByName(String name, Component newComponent) {
        for (int i = 0; i < components.size(); i++) {
            if (components.get(i).getName().equals(name)) {
                components.set(i, newComponent);
                return true;
            }
        }
        Console.log("Component to replace not found: " + name);
        return false;
    }

    public List<Component> getAvailableAndCraftableComponents(Boolean liquid) {
        List<Component> result = new ArrayList<>();
        for (Component component : components) {
            if (component.isAvailable() && component.isCraftable() && component.isLiquid().equals(liquid)) {
                result.add(component);
            }
        }
        return result;
    }

    public List<Component> getAvailableButUncraftableComponents() {
        List<Component> result = new ArrayList<>();
        for (Component component : components) {
            if (component.isAvailable() && !component.isCraftable()) {
                result.add(component);
            }
        }
        return result;
    }

    public List<Component> getAvailableComponents(Boolean liquid) {
        List<Component> result = new ArrayList<>();
        for (Component component : components) {
            if (component.isAvailable() && component.isLiquid().equals(liquid)) {
                result.add(component);
            }
        }
        return result;
    }

    public List<Component> getUnavailableComponents() {
        List<Component> result = new ArrayList<>();
        for (Component component : components) {
            if (!component.isAvailable()) {
                result.add(component);
            }
        }
        return result;
    }

    public CraftStation getRandomAvailableAndCraftableStation(int seed) {
        List<CraftStation> availableStations = new ArrayList<>();
        for (CraftStation station : stations) {
            if (station.isAvailable() && station.isCraftable()) {
                availableStations.add(station);
            }
        }
        if (availableStations.isEmpty()) {
            return null;
        }
        Random random = new Random(seed);
        int index = random.nextInt(availableStations.size());
        return availableStations.get(index);
    }

    public List<CraftStation> getAvailableButUncraftableStations() {
        List<CraftStation> result = new ArrayList<>();
        for (CraftStation station : stations) {
            if (station.isAvailable() && !station.isCraftable()) {
                result.add(station);
            }
        }
        return result;
    }

    public Boolean setComponentAvailable(String name, Boolean available) {
        for (Component component : components) {
            if (component.getName().equals(name)) {
                component.setAvailable(true);
                return true;
            }
        }
        Console.log("Could not set Available, Component not found: " + name);
        return false;
    }

    public Boolean setComponentCraftable(String name, Boolean craftable) {
        for (Component component : components) {
            if (component.getName().equals(name)) {
                component.setCraftable(craftable);
                return true;
            }
        }
        Console.log("Could not set Craftable, component not found: " + name);
        return false;
    }

    /**
     * Use one of the material, reducing the amount of remaining uses by 1.
     * 
     * @param name The name of the material to use.
     * @return The new amount of remaining uses, or 0 if the material was not found.
     */
    public int useComponent(String name) {
        for (Component component : components) {
            if (component.getName().equals(name)) {
                return component.use();
            }
        }
        Console.log("Could not use component, component not found: " + name);
        return 0;
    }

    /**
     * Increase the number of uses for al materials by 1. Used to avoid crashing due
     * to the lack of material uses.
     */
    public void refillComponents() {
        for (Component component : components) {
            component.refill();
        }
    }

}