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
        List<Structure> tempStructures = new ArrayList<>();
        List<EssentialStructure> tempEssentialStructures = new ArrayList<>();

        // Crafting Stations
        tempStations
                .add(new CraftStation("Desc_AssemblerMk1", false, false, "Recipe_AssemblerMk1", "Build_AssemblerMk1",
                        2, 1, 0, 0));
        tempStations.add(new CraftStation("Desc_Blender", false, false, "Recipe_Blender", "Build_Blender",
                2, 1, 2, 1));
        tempStations.add(
                new CraftStation("Desc_ConstructorMk1", false, false, "Recipe_ConstructorMk1", "Build_ConstructorMk1",
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
        tempStations.add(new CraftStation("Desc_SmelterMk1", false, false, "Recipe_SmelterBasicMk1", "Build_SmelterMk1",
                1, 1, 0, 0));
        tempStations.add(new CraftStation("Desc_FoundryMk1", false, false, "Recipe_SmelterMk1", "Build_FoundryMk1",
                2, 1, 0, 0));
        this.stations = addPrefixStat(tempStations, "//Game/FactoryGame/Recipes/Buildings/");
        tempStations.clear();

        // Components

        // Raw materials
        // Have to add empty arraylists or the constructor will be ambiguous
        this.components.add(new Component("Desc_OreIron", "", true, false, new ArrayList<>()));
        this.components.add(new Component("Desc_OreCopper", "", false, false, new ArrayList<>()));
        this.components.add(new Component("Desc_Stone", "", false, false, new ArrayList<>()));

        // /Recipes/Constructor/
        tempComp.add(new Component("Desc_IronPlate", "Recipe_IronPlate", true, false, false));
        tempComp.add(new Component("Desc_IronRod", "Recipe_IronRod", true, false, false));
        tempComp.add(new Component("Desc_Wire", "Recipe_Wire", false, false, false));
        tempComp.add(new Component("Desc_Cable", "Recipe_Cable", false, false, false));
        tempComp.add(new Component("Desc_Cement", "Recipe_Concrete", false, false, false));
        tempComp.add(new Component("Desc_IronScrew", "Recipe_Screw", false, false, false));
        tempComp.add(new Component("Desc_GenericBiomass", "Recipe_Biomass_Leaves", false, false, false));

        this.components.addAll((List<Component>) addPrefixComp(tempComp, "//Game/FactoryGame/Recipes/Constructor/"));
        tempComp.clear();

        // /Recipes/Smelter/
        tempComp.add(
                new Component("Desc_IronIngot", "Recipe_IngotIron", true, false, false));
        tempComp.add(new Component("Desc_CopperIngot", "Recipe_IngotCopper", false, false, false));

        this.components.addAll(addPrefixComp(tempComp, "//Game/FactoryGame/Recipes/Constructor/"));
        tempComp.clear();

        // /Recipes/Assembler/
        tempComp.add(new Component("Desc_IronPlateReinforced", "Recipe_IronPlateReinforced", false, false, false));

        this.components.addAll(addPrefixComp(tempComp, "//Game/FactoryGame/Recipes/Constructor/"));
        tempComp.clear();

        // Structures

        // Milestones
        // Tutorial are marked as available so that they unlock when their extraChecks
        // are met (the previous tutorial)
        // //Game/FactoryGame/Schematics/Tutorial/
        tempMilestones.add(new Milestone("Tutorial_1", true, false, "Schematic_Tutorial1", null));
        tempMilestones.add(
                new Milestone("Tutorial_2", true, false, "Schematic_Tutorial1_5", Arrays.asList("Tutorial_1")));
        tempMilestones
                .add(new Milestone("Tutorial_3", true, false, "Schematic_Tutorial2", Arrays.asList("Tutorial_2")));
        tempMilestones
                .add(new Milestone("Tutorial_4", true, false, "Schematic_Tutorial3", Arrays.asList("Tutorial_3")));
        tempMilestones
                .add(new Milestone("Tutorial_5", true, false, "Schematic_Tutorial4", Arrays.asList("Tutorial_4")));
        tempMilestones
                .add(new Milestone("Tutorial_6", true, false, "Schematic_Tutorial5", Arrays.asList("Tutorial_5")));

        this.milestones.addAll(addPrefixMile(tempMilestones, "//Game/FactoryGame/Schematics/Tutorial/"));
        tempMilestones.clear();

        // this.milestones = addHubUpgrades(milestones);

        // EssentialStructures

        // Structures
        for (EssentialStructure structure : this.essentialStructures) {
            if (structure.addWhen() == 9) { // Can be added whenever, so it's actually not essential
                structures.add(structure);
                essentialStructures.remove(structure);

                Console.log(structure.getName() + " moved to non-essential structures.");
            }
        }

    }

    public void generateLimitedMats() {
        // Materials without associated recipe and can't be crafted.
        // They will be added at the end for alternate recipes.
        this.components.add(new Component("Desc_Wood", null, true, false, false));
    }

    private static List<Component> addPrefixComp(List<Component> list, String prefix) {
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

    private static List<Milestone> addPrefixMile(List<Milestone> list, String prefix) {
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

    private static List<CraftStation> addPrefixStat(List<CraftStation> list, String prefixRecipe) {
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

    public Milestone getMilestoneByName(String name) {
        for (Milestone milestone : this.milestones) {
            if (milestone.getName().equals(name)) {
                return milestone;
            }
        }
        Console.log("Milestone not found: " + name);
        return null;
    }

    public Randomizable getRandomizableByName(String name) {
        List<Randomizable> randomizables = this.getAllRandomizables();
        for (Randomizable randomizable : randomizables) {
            if (randomizable.getName().equals(name)) {
                return randomizable;
            }
        }
        Console.log("Randomizable not found: " + name);
        return null;
    }

    public EssentialStructure getEssentialStructureByName(String name) {
        for (EssentialStructure structure : this.essentialStructures) {
            if (structure.getName().equals(name)) {
                return structure;
            }
        }
        Console.log("Structure not found: " + name);
        return null;
    }

    public Structure getStructureByName(String name) {
        for (Structure structure : this.structures) {
            if (structure.getName().equals(name)) {
                return structure;
            }
        }
        Console.log("Structure not found: " + name);
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

    public List<Randomizable> getAvailableAndCraftableRandomizables() {
        List<Randomizable> result = new ArrayList<>();
        for (Randomizable randomizable : this.getAllRandomizables()) {
            if (randomizable.isAvailable() && randomizable.isCraftable()) {
                result.add(randomizable);
            }
        }
        return result;
    }

    public List<Randomizable> getCraftableRandomizables() {
        List<Randomizable> result = new ArrayList<>();
        for (Randomizable randomizable : this.getAllRandomizables()) {
            if (randomizable.isCraftable()) {
                result.add(randomizable);
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

    public List<Randomizable> getAllRandomizables() {
        List<Randomizable> result = new ArrayList<>();
        result.addAll(this.stations);
        result.addAll(this.components);
        result.addAll(this.essentialStructures);
        result.addAll(this.milestones);
        result.addAll(this.structures);
        return result;
    }

    public List<Milestone> getAllMilestones() {
        return this.milestones;
    }

    public List<Randomizable> getUnavailableRandomizables() {
        List<Randomizable> result = new ArrayList<>();

        for (Randomizable randomizable : this.getAllRandomizables()) {
            if (!randomizable.isAvailable()) {
                result.add(randomizable);
            }
        }

        return result;
    }

    public List<Randomizable> getUncraftableRandomizables() {
        List<Randomizable> result = new ArrayList<>();
        for (Randomizable randomizable : this.getAllRandomizables()) {
            if (!randomizable.isCraftable()) {
                result.add(randomizable);
            }
        }
        return result;
    }

    public List<Randomizable> getUnavailableAndUncraftableRandomizables() {
        List<Randomizable> result = new ArrayList<>();
        for (Randomizable randomizable : this.getAllRandomizables()) {
            if (!randomizable.isAvailable() && !randomizable.isCraftable()) {
                result.add(randomizable);
            }
        }
        return result;
    }

    public List<Randomizable> rawUnavailable() {
        List<Randomizable> result = new ArrayList<>();
        for (Component comp : this.components) {
            if (!comp.trueAvailable() && comp.isRaw()) {
                result.add(comp);
            }
        }
        return result;
    }

    public void setRandomizableAvailable(String name, Boolean available) {

        for (Randomizable r : this.getAllRandomizables()) {
            if (r.getName().equals(name)) {
                r.setAvailable(available);
                return;
            }
        }

    }

    public List<Randomizable> getAvailableButUncraftableRandomizables() {
        List<Randomizable> result = new ArrayList<>();
        for (Randomizable randomizable : this.getAllRandomizables()) {
            if (randomizable.isAvailable() && !randomizable.isCraftable()) {
                result.add(randomizable);
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

    public void setMilestoneAvailable(String name, Boolean available) {
        for (Milestone milestone : this.milestones) {
            if (milestone.getName().equals(name)) {
                milestone.setAvailable(available);
                return;
            }
        }
        Console.log("Could not set Available, Milestone not found: " + name);
    }

    public void setMilestoneCraftable(String name, Boolean craftable) {
        for (Milestone milestone : this.milestones) {
            if (milestone.getName().equals(name)) {
                milestone.setCraftable(craftable);
                return;
            }
        }
        Console.log("Could not set Craftable, Milestone not found: " + name);
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

    public int getAllNonMilestonedRandomizables() {
        int result = 0;
        for (Randomizable r : this.getAllRandomizables()) {
            if (!(r instanceof Milestone) && !r.trueAvailable()) {
                result++;
            }
        }
        return result;
    }

    public void fillExtraChecks() throws Exception {

        // Get the randomizables enabled by this randomizable

        List<Randomizable> randomizables = this.getAllRandomizables();

        for (Randomizable r : randomizables) {
            for (String extra : r.getExtraCheck()) {
                if (extra != null) {
                    Randomizable item = this.getRandomizableByName(extra);
                    if (item instanceof Component) {
                        getComponentByName(extra).addCheckAlso(r.getName());
                    } else if (item instanceof CraftStation) {
                        getStructureByName(extra).addCheckAlso(r.getName());
                    } else if (item instanceof EssentialStructure) {
                        getStructureByName(extra).addCheckAlso(r.getName());
                    } else if (item instanceof Milestone) {
                        getMilestoneByName(extra).addCheckAlso(r.getName());
                    } else if (item instanceof Structure) {
                        getStructureByName(extra).addCheckAlso(r.getName());
                    } else {
                        Console.log("Unknown extra check: " + extra);
                    }
                }
            }
        }
    }

    public void doExtraChecks(String nameToRemove, List<String> whereToRemove) {

        for (String where : whereToRemove) {
            Boolean done = false;
            for (Component c : this.components) {
                if (c.getName().equals(where)) {
                    c.removeExtraCheck(nameToRemove);
                    done = true;
                }
            }
            if (done)
                continue;

            for (CraftStation s : this.stations) {
                if (s.getName().equals(where)) {
                    s.removeExtraCheck(nameToRemove);
                    done = true;
                }
            }
            if (done)
                continue;

            for (EssentialStructure s : this.essentialStructures) {
                if (s.getName().equals(where)) {
                    s.removeExtraCheck(nameToRemove);
                    done = true;
                }
            }
            if (done)
                continue;

            for (Milestone m : this.milestones) {
                if (m.getName().equals(where)) {
                    m.removeExtraCheck(nameToRemove);
                    done = true;
                }
            }
            if (done)
                continue;

            for (Structure s : this.structures) {
                if (s.getName().equals(where)) {
                    s.removeExtraCheck(nameToRemove);
                    done = true;
                }
            }
            if (done)
                continue;

            Console.log("Could not remove extra check, Randomizable not found: " + whereToRemove);
        }
    }

    public static List<Milestone> addHubUpgrades(List<Milestone> milestones) {
        List<Milestone> result = new ArrayList<>(milestones);
        for (Milestone milestone : milestones) {

            if (false) {
                // Check for Milestones that have one of these and add it
            } else {
                result.add(milestone);
            }

        }
        return result;
    }

    // Only for debugging
    public void testSetup() {
        Console.test("Generating data for testing...");
        // Does nothing RN
    }

}