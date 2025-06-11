package com.satisfactoryrandomizer.Storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.satisfactoryrandomizer.Console;
import com.satisfactoryrandomizer.Storage.Randomizables.Component;
import com.satisfactoryrandomizer.Storage.Randomizables.CraftStation;
import com.satisfactoryrandomizer.Storage.Randomizables.EssentialStructure;
import com.satisfactoryrandomizer.Storage.Randomizables.Milestone;
import com.satisfactoryrandomizer.Storage.Randomizables.Randomizable;
import com.satisfactoryrandomizer.Storage.Randomizables.Structure;

public class Materials {

    private final List<Component> components = new ArrayList<>();// Components
    private List<CraftStation> stations = new ArrayList<>(); // Crafting Stations
    private List<Structure> structures = new ArrayList<>(); // Generic structures that don't affect the logic
    private List<EssentialStructure> essentialStructures = new ArrayList<>(); // Structures that need to be checked by
                                                                              // the logic
    private List<Milestone> milestones = new ArrayList<>(); // Milestones

    public Materials() {

        // Create a temporary lists to add the prefixes to before adding to components
        List<Component> tempComp = new ArrayList<>();
        List<CraftStation> tempStations = new ArrayList<>();
        List<Milestone> tempMilestones = new ArrayList<>();

        // Crafting Stations
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
        tempStations.clear();

        // Components

        // Raw materials
        this.components.add(new Component("Desc_OreIron", true, false, null));
        this.components.add(new Component("Desc_OreCopper", true, false, null));
        this.components.add(new Component("Desc_Stone", true, false, null));

        // /Recipes/Constructor/
        tempComp.add(new Component("Desc_IronPlate", "Recipe_IronPlate", true, false, false));
        tempComp.add(new Component("Desc_IronRod", "Recipe_IronRod", true, false, false));
        tempComp.add(new Component("Desc_Wire", "Recipe_Wire", true, false, false));
        tempComp.add(new Component("Desc_Cable", "Recipe_Cable", true, false, false));
        tempComp.add(new Component("Desc_Cement", "Recipe_Concrete", true, false, false));
        tempComp.add(new Component("Desc_IronScrew", "Recipe_Screw", true, false, false));
        tempComp.add(new Component("Desc_GenericBiomass", "Recipe_Biomass_Leaves", true, false, false));

        this.components.addAll((List<Component>) AddPrefixComp(tempComp, "//Game/FactoryGame/Recipes/Constructor/"));
        tempComp.clear();

        // /Recipes/Smelter/
        tempComp.add(
                new Component("Desc_IronIngot", "Recipe_IngotIron", true, false, false));
        tempComp.add(new Component("Desc_CopperIngot", "Recipe_IngotCopper", true, false, false));

        this.components.addAll(AddPrefixComp(tempComp, "//Game/FactoryGame/Recipes/Constructor/"));
        tempComp.clear();

        // /Recipes/Assembler/
        tempComp.add(new Component("Desc_IronPlateReinforced", "Recipe_IronPlateReinforced", true, false, false));

        this.components.addAll(AddPrefixComp(tempComp, "//Game/FactoryGame/Recipes/Constructor/"));
        tempComp.clear();

        // Structures

        // Milestones
        // //Game/FactoryGame/Schematics/Tutorial/
        tempMilestones.add(new Milestone("Tutorial_1", true, false, "Schematic_Tutorial1", null));
        tempMilestones.add(new Milestone("Tutorial_2", false, false, "Schematic_Tutorial1_5", Arrays.asList("Tutorial_1")));
        tempMilestones.add(new Milestone("Tutorial_3", false, false, "Schematic_Tutorial2", Arrays.asList("Tutorial_2")));
        tempMilestones.add(new Milestone("Tutorial_4", false, false, "Schematic_Tutorial3", Arrays.asList("Tutorial_3")));
        tempMilestones.add(new Milestone("Tutorial_5", false, false, "Schematic_Tutorial4", Arrays.asList("Tutorial_4")));
        tempMilestones.add(new Milestone("Tutorial_6", false, false, "Schematic_Tutorial5", Arrays.asList("Tutorial_5")));

        this.milestones.addAll(AddPrefixMilestone(tempMilestones, "//Game/FactoryGame/Schematics/Tutorial/"));
        tempMilestones.clear();

        // Essential structures are structures too they are just forced to be craftable
        // early.
        this.structures.addAll(this.essentialStructures);
    }

    public void generateLimitedMats() {
        // Materials without associated recipe and can't be crafted.
        // They will be added at the end for alternate recipes.
        this.components.add(new Component("Desc_Wood", null, true, false, false));
    }

    private static List<Component> AddPrefixComp(List<Component> list, String prefix) {
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

    private static List<Milestone> AddPrefixMile(List<Milestone> list, String prefix) {
        List<Milestone> prefixedList = new ArrayList<>(list);

        for (int i = 0; i < prefixedList.size(); i++) {
            Milestone c = prefixedList.get(i);
            if (c.getRecipePath() != null && !c.getRecipePath().startsWith(prefix)) {
                prefixedList.get(i).setRecipePath(prefix + c.getRecipePath());
            }
        }
        Console.log("Prefixed List with " + prefix);
        return prefixedList;
    }

    private static List<CraftStation> AddPrefixStat(List<CraftStation> list, String prefixRecipe) {
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

    private static List<Milestone> AddPrefixMilestone(List<Milestone> list, String prefixRecipe) {
        List<Milestone> prefixedList = new ArrayList<>(list);

        for (int i = 0; i < prefixedList.size(); i++) {
            Milestone c = prefixedList.get(i);
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

}