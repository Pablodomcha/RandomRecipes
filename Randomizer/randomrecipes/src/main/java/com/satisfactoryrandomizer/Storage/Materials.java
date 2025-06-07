package com.satisfactoryrandomizer.Storage;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class Materials {

    // List of Components
    private List<Component> components = new ArrayList<>();
    // List of craftable items at the start of the game
    private Map<String, Boolean> stations = new HashMap<>();

    public Materials() {

        // Create a temporary lists to add the prefixes to before adding to components
        List<Component> tempList = new ArrayList<>();
        List<Component> prefixedTempList;

        // Initialize the list of available stations
        stations.put("Assembler", false);
        stations.put("Constructor", false);
        stations.put("Blender", false);
        stations.put("Manufacturer", false);
        stations.put("Refinery", false);
        stations.put("Smelter", false);
        stations.put("Foundry", false);
        stations.put("PartAccel", false);

        // Initialize the available materials
        // Starting you only have directly gatherable materials
        components.add(new Component("Desc_OreIron", true));
        components.add(new Component("Desc_OreCopper", true));
        components.add(new Component("Desc_Stone", true));

        // available at Onboarding, Is available but not nececessarily craftable
        // /Recipes/Constructor/
        tempList.add(new Component("Desc_IronPlate", "Recipe_IronPlate", true, false));
        tempList.add(new Component("Desc_IronRod", "Recipe_IronRod", true, false));
        tempList.add(new Component("Desc_Wire", "Recipe_Wire", true, false));
        tempList.add(new Component("Desc_Cable", "Recipe_Cable", true, false));
        tempList.add(new Component("Desc_Cement", "Recipe_Concrete", true, false));
        tempList.add(new Component("Desc_IronScrew", "Recipe_Screw", true, false));
        tempList.add(new Component("Desc_GenericBiomass", "Recipe_Biomass_Leaves", true, false));
        tempList.add(new Component("Desc_GenericBiomass", "Recipe_Biomass_Leaves", true, false));

        prefixedTempList = AddPrefixSuffix(tempList, "//Game/FactoryGame/Recipes/Constructor/");
        components.addAll(prefixedTempList);
        tempList.clear();
        prefixedTempList.clear();

        // /Recipes/Smelter/
        tempList.add(
                new Component("Desc_IronIngot", "Recipe_IngotIron", true, false));
        tempList.add(new Component("Desc_CopperIngot", "Recipe_IngotCopper", true, false));

        prefixedTempList = AddPrefixSuffix(tempList, "//Game/FactoryGame/Recipes/Smelter/");
        components.addAll(prefixedTempList);
        tempList.clear();
        prefixedTempList.clear();

        // /Recipes/Assembler/
        tempList.add(new Component("Desc_IronPlateReinforced","Recipe_IronPlateReinforced", true, false));

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

    public List<Component> getAvailableAndCraftableComponents() {
        List<Component> result = new ArrayList<>();
        for (Component component : components) {
            if (component.isAvailable() && component.isCraftable()) {
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