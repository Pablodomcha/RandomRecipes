package com.satisfactoryrandomizer.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.satisfactoryrandomizer.Console;
import com.satisfactoryrandomizer.Storage.Randomizables.Component;
import com.satisfactoryrandomizer.Storage.Randomizables.CraftStation;
import com.satisfactoryrandomizer.Storage.Randomizables.EssentialStructure;
import com.satisfactoryrandomizer.Storage.Randomizables.Randomizable;
import com.satisfactoryrandomizer.Storage.Randomizables.Structure;

public class Materials {

    // List of Components
    private final List<Component> components = new ArrayList<>();
    // List of craftable items at the start of the game
    private List<CraftStation> stations = new ArrayList<>();
    private List<Structure> structures = new ArrayList<>(); // Generic structures that don't affect the logic
    private List<EssentialStructure> essentialStructures = new ArrayList<>(); // Structures that need to be checked by the logic

    public Materials() {

        // Create a temporary lists to add the prefixes to before adding to components
        List<Component> tempList = new ArrayList<>();
        List<Component> prefixedTempList;
        List<CraftStation> tempStations = new ArrayList<>();

        // Initialize the list of available stations
        tempStations
                .add(new CraftStation("Desc_AssemblerMk1", false, false, "Recipe_AssemblerMk1", "Build_AssemblerMk1",
                        2, 1, 0, 0));
        tempStations.add(new CraftStation("Desc_Blender", false, false, "Recipe_Blender", "Build_Blender",
                2, 1, 2, 1));
        tempStations.add(
                new CraftStation("Desc_ConstructorMk1", true, false, "Recipe_ConstructorMk1", "Build_ConstructorMk1",
                        1, 1, 0, 0));
        tempStations
                .add(new CraftStation("Desc_ManufacturerMk1", false, false, "Recipe_ManufacturerMk1",
                        "Build_ManufacturerMk1",
                        4, 1, 0, 0));
        tempStations.add(new CraftStation("Desc_Packager", false, false, "Recipe_Packager", "Build_Packager",
                1, 1, 1, 1));
        tempStations.add(
                new CraftStation("Desc_HadronCollider", false, false, "Recipe_HadronCollider", "Build_HadronCollider",
                        2, 1, 1, 0));
        tempStations.add(new CraftStation("Desc_OilRefinery", false, false, "Recipe_OilRefinery", "Build_OilRefinery",
                1, 1, 1, 1));
        tempStations.add(new CraftStation("Desc_Converter", false, false, "Recipe_Converter", "Build_Converter",
                2, 1, 0, 1));
        tempStations
                .add(new CraftStation("Desc_QuantumEncoder", false, false, "Recipe_QuantumEncoder",
                        "Build_QuantumEncoder", 3, 1, 1, 1));
        tempStations.add(new CraftStation("Desc_SmelterMk1", true, false, "Recipe_SmelterBasicMk1", "Build_SmelterMk1",
                1, 1, 0, 0));
        tempStations.add(new CraftStation("Desc_FoundryMk1", false, false, "Recipe_SmelterMk1", "Build_FoundryMk1",
                2, 1, 0, 0));
        this.stations = AddPrefixStat(tempStations, "//Game/FactoryGame/Recipes/Buildings/");

        // Initialize the available materials
        // Starting you only have directly gatherable materials
        this.components.add(new Component("Desc_OreIron", true, false));
        this.components.add(new Component("Desc_OreCopper", true, false));
        this.components.add(new Component("Desc_Stone", true, false));

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
        tempList.add(new Component("Desc_Wood", null, true, false, false));

        prefixedTempList = AddPrefix(tempList, "//Game/FactoryGame/Recipes/Constructor/");

        this.components.addAll(prefixedTempList);
        tempList.clear();
        prefixedTempList.clear();

        // /Recipes/Smelter/
        tempList.add(
                new Component("Desc_IronIngot", "Recipe_IngotIron", true, false, false));
        tempList.add(new Component("Desc_CopperIngot", "Recipe_IngotCopper", true, false, false));

        prefixedTempList = AddPrefix(tempList, "//Game/FactoryGame/Recipes/Smelter/");
        this.components.addAll(prefixedTempList);
        tempList.clear();
        prefixedTempList.clear();

        // /Recipes/Assembler/
        tempList.add(
                new Component("Desc_IronPlateReinforced", "Recipe_IronPlateReinforced", true, false, false));

        prefixedTempList = AddPrefix(tempList, "//Game/FactoryGame/Recipes/Assembler/");
        this.components.addAll(prefixedTempList);
        tempList.clear();
        prefixedTempList.clear();

        // Now the rest of resources.
        // Resource/RawResources/
        // this.components.add(new Component("", "", false, false));

        // Milestones


        // Essential structures are structures too they are just forced to be gotten early.
        this.structures.addAll(this.essentialStructures);
    }

    private List<Component> AddPrefix(List<Component> list, String prefix) {
        List<Component> prefixedList = new ArrayList<>(list);

        for (int i = 0; i < prefixedList.size(); i++) {
            Component c = prefixedList.get(i);
            if (c.getRecipePath() != null && !c.getRecipePath().startsWith(prefix)) {
                prefixedList.get(i).setRecipePath(prefix + c.getRecipePath());
            }
        }
        Console.log("Prefixed List with " + prefix);
        return prefixedList;
    }

    private List<CraftStation> AddPrefixStat(List<CraftStation> list, String prefixRecipe) {
        List<CraftStation> prefixedList = new ArrayList<>(list);

        for (int i = 0; i < prefixedList.size(); i++) {
            CraftStation c = prefixedList.get(i);
            if (c.getRecipePath() != null && !c.getRecipePath().startsWith(prefixRecipe)) {
                prefixedList.get(i).setRecipePath(prefixRecipe + c.getRecipePath());
            }
        }
        Console.log("Prefixed CraftStation List with " + prefixRecipe);
        return prefixedList;
    }

    // Getters and Setters

    public Component getComponentByName(String name) {
        for (Component component : this.components) {
            if (component.getName().equals(name)) {
                return component;
            }
        }
        Console.log("Component not found: " + name);
        return null;
    }

    public CraftStation getStationByName(String name) {
        for (CraftStation station : this.stations) {
            if (station.getName().equals(name)) {
                return station;
            }
        }
        Console.log("Station not found: " + name);
        return null;
    }

    public Boolean replaceComponentByName(String name, Component newComponent) {
        for (int i = 0; i < this.components.size(); i++) {
            if (this.components.get(i).getName().equals(name)) {
                this.components.set(i, newComponent);
                return true;
            }
        }
        Console.log("Component to replace not found: " + name);
        return false;
    }

    public List<Component> getAvailableAndCraftableComponents(Boolean liquid) {
        List<Component> result = new ArrayList<>();
        for (Component component : this.components) {
            if (component.isAvailable() && component.isCraftable() && component.isLiquid().equals(liquid)) {
                result.add(component);
            }
        }
        return result;
    }

    public List<Component> getAvailableButUncraftableComponents() {
        List<Component> result = new ArrayList<>();
        for (Component component : this.components) {
            if (component.isAvailable() && !component.isCraftable()) {
                result.add(component);
            }
        }
        return result;
    }

    public List<Component> getAvailableComponents(Boolean liquid) {
        List<Component> result = new ArrayList<>();
        for (Component component : this.components) {
            if (component.isAvailable() && component.isLiquid().equals(liquid)) {
                result.add(component);
            }
        }
        return result;
    }

    public List<Component> getUnavailableComponents() {
        List<Component> result = new ArrayList<>();
        for (Component component : this.components) {
            if (!component.isAvailable()) {
                result.add(component);
            }
        }
        return result;
    }

    public CraftStation getRandomAvailableAndCraftableStation(long seed) {
        List<CraftStation> availableStations = new ArrayList<>();
        for (CraftStation station : this.stations) {
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
        for (CraftStation station : this.stations) {
            if (station.isAvailable() && !station.isCraftable()) {
                result.add(station);
            }
        }
        return result;
    }

    public List<Randomizable> getUnavailableAndUncraftableRandomizables() {
        List<Randomizable> result = new ArrayList<>();
        for (CraftStation station : this.stations) {
            if (!station.isAvailable() && !station.isCraftable()) {
                result.add(station);
            }
        }
        for (Component component : this.components) {
            if (!component.isAvailable() && !component.isCraftable()) {
                result.add(component);
            }
        }
        for (Structure structure : this.structures) {
            if (!structure.isAvailable() && !structure.isCraftable()) {
                result.add(structure);
            }
        }
        return result;
    }

    public Boolean setComponentAvailable(String name, Boolean available) {
        for (Component component : this.components) {
            if (component.getName().equals(name)) {
                component.setAvailable(true);
                return true;
            }
        }
        Console.log("Could not set Available, Component not found: " + name);
        return false;
    }

    public Boolean setComponentCraftable(String name, Boolean craftable) {
        for (Component component : this.components) {
            if (component.getName().equals(name)) {
                component.setCraftable(craftable);
                return true;
            }
        }
        Console.log("Could not set Craftable, component not found: " + name);
        return false;
    }

    public void setStructureAvailable(String name, Boolean available) {
        for (CraftStation station : this.stations) {
            if (station.getName().equals(name)) {
                station.setAvailable(available);
                return;
            }
        }
        Console.log("Could not set Craftable, station not found: " + name);
    }

    public void setStructureCraftable(String name, Boolean craftable) {
        for (CraftStation station : this.stations) {
            if (station.getName().equals(name)) {
                station.setCraftable(craftable);
                return;
            }
        }
        Console.log("Could not set Craftable, station not found: " + name);
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
        for (Component component : this.components) {
            component.refill();
        }
    }

    public void fillExtraChecks() throws Exception {
        // Get the Randomizables enabled by this component
        for (Component c : this.components) {
            for (String extra : c.getExtraCheck()) {
                // If the Randomizable is not of a certain type, check the next one, search it
                // as a Station
                if (getComponentByName(extra) == null) {
                    if (getStationByName(extra) == null) {
                        // Add here any other type of Randomizable created that influences this part of
                        // the logic
                        throw new Exception("Randomizable not found: " + extra);
                    }
                    getStationByName(extra).addCheckAlso(c.getName());
                }
                getComponentByName(extra).addCheckAlso(c.getName());
            }
        }

        // Get the Randomizables enabled by this station
        for (CraftStation s : this.stations) {
            for (String extra : s.getExtraCheck()) {
                // If the Randomizable is not of a certain type, check the next one, search it
                // as a Station
                if (getComponentByName(extra) == null) {
                    if (getStationByName(extra) == null) {
                        // Add here any other type of Randomizable created that influences this part of
                        // the logic
                        throw new Exception("Randomizable not found: " + extra);
                    }
                    getStationByName(extra).addCheckAlso(s.getName());
                }
                getComponentByName(extra).addCheckAlso(s.getName());
            }
        }
    }

    // Only for debugging
    public void testSetup() {
        Console.test();
        for (CraftStation stat : this.stations) {
            getStationByName(stat.getName()).setAvailable(true);
        }
    }

    // Test only

}