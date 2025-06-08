package com.satisfactoryrandomizer.Storage;

import java.util.List;
import java.util.ArrayList;

public class Materials {

    // List of Components
    private List<Component> components = new ArrayList<>();
    // List of craftable items at the start of the game
    private List<CraftStation> stations = new ArrayList<>();

    public Materials() {

        // Create a temporary lists to add the prefixes to before adding to components
        List<Component> tempList = new ArrayList<>();
        List<Component> prefixedTempList;

        // Initialize the list of available stations
        stations.add(new CraftStation("Assembler", false, "Recipe_AssemblerMk1",
                2, 1, 0, 0));
        stations.add(new CraftStation("Blender", false, "Recipe_Blender",
                2, 1, 2, 1));
        stations.add(new CraftStation("Constructor", true, "Recipe_ConstructorMk1",
                1, 1, 0, 0));
        stations.add(new CraftStation("Manufacturer", false, "Recipe_ManufacturerMk1",
                4, 1, 0, 0));
        stations.add(new CraftStation("Packager", false, "Recipe_Packager",
                1, 1, 1, 1));
        stations.add(new CraftStation("PAccel", false, "Recipe_HadronCollider",
                2, 1, 1, 0));
        stations.add(new CraftStation("Refinery", false, "Recipe_OilRefinery",
                1, 1, 1, 1));
        stations.add(new CraftStation("Converter", false, "Recipe_Converter",
                2, 1, 0, 1));
        stations.add(new CraftStation("QuantumEncoder", false, "Recipe_QuantumEncoder",
                3, 1, 1, 1));

        // Initialize the available materials
        // Starting you only have directly gatherable materials
        components.add(new Component("Desc_OreIron", true));
        components.add(new Component("Desc_OreCopper", true));
        components.add(new Component("Desc_Stone", true));

        // available at Onboarding, Is available but not nececessarily craftable
        // /Recipes/Constructor/
        tempList.add(new Component("Desc_IronPlate", "Recipe_IronPlate", true, false, false));
        tempList.add(new Component("Desc_IronRod", "Recipe_IronRod", true, false, false));
        tempList.add(new Component("Desc_Wire", "Recipe_Wire", true, false, false));
        tempList.add(new Component("Desc_Cable", "Recipe_Cable", true, false, false));
        tempList.add(new Component("Desc_Cement", "Recipe_Concrete", true, false, false));
        tempList.add(new Component("Desc_IronScrew", "Recipe_Screw", true, false, false));
        tempList.add(new Component("Desc_GenericBiomass", "Recipe_Biomass_Leaves", true, false, false));
        tempList.add(new Component("Desc_GenericBiomass", "Recipe_Biomass_Leaves", true, false, false));

        prefixedTempList = AddPrefixSuffix(tempList, "//Game/FactoryGame/Recipes/Constructor/");
        components.addAll(prefixedTempList);
        tempList.clear();
        prefixedTempList.clear();

        // /Recipes/Smelter/
        tempList.add(
                new Component("Desc_IronIngot", "Recipe_IngotIron", true, false, false));
        tempList.add(new Component("Desc_CopperIngot", "Recipe_IngotCopper", true, false, false));

        prefixedTempList = AddPrefixSuffix(tempList, "//Game/FactoryGame/Recipes/Smelter/");
        components.addAll(prefixedTempList);
        tempList.clear();
        prefixedTempList.clear();

        // /Recipes/Assembler/
        tempList.add(new Component("Desc_IronPlateReinforced", "Recipe_IronPlateReinforced", true, false, false));

        prefixedTempList = AddPrefixSuffix(tempList, "//Game/FactoryGame/Recipes/Assembler/");
        components.addAll(prefixedTempList);
        tempList.clear();
        prefixedTempList.clear();

        // Now the rest of resources.
        // Resource/RawResources/
        // components.add(new Component("", "", false, false));
    }

    private List<Component> AddPrefixSuffix(List<Component> list, String prefix) {
        List<Component> prefixedList = new ArrayList<>(list);

        for (int i = 0; i < prefixedList.size(); i++) {
            Component c = prefixedList.get(i);
            if (c.getRecipePath() != null && !c.getRecipePath().startsWith(prefix)) {
                prefixedList.get(i).setRecipePath(prefix + c.getRecipePath());
            }
        }
        System.out.println("Prefixed List with " + prefix + ":");
        for (Component comp : prefixedList) {
            System.out.println(comp.getName());
        }
        return prefixedList;
    }

    // Getters and Setters
    public void logComponents() {
        System.out.println("Listing all components:");
        for (Component component : components) {
            System.out.println(component.toString());
        }
    }

    public Component getComponentByName(String name) {
        for (Component component : components) {
            if (component.getName().equals(name)) {
                return component;
            }
        }
        System.out.println("Component not found: " + name);
        return null;
    }

    public boolean replaceComponentByName(String name, Component newComponent) {
        for (int i = 0; i < components.size(); i++) {
            if (components.get(i).getName().equals(name)) {
                components.set(i, newComponent);
                return true;
            }
        }
        System.out.println("Component to replace not found: " + name);
        return false;
    }

    public List<Component> getAvailableAndCraftableComponents(Boolean liquid) {
        List<Component> result = new ArrayList<>();
        for (Component component : components) {
            if (component.isAvailable() && component.isCraftable() && component.isLiquid() == liquid) {
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

    public List<Component> getUnavailableComponents() {
        List<Component> result = new ArrayList<>();
        for (Component component : components) {
            if (!component.isAvailable()) {
                result.add(component);
            }
        }
        return result;
    }
}