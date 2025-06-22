package com.satisfactoryrandomizer.Storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.satisfactoryrandomizer.Console;
import com.satisfactoryrandomizer.Storage.Randomizables.*;

// Power extracheck is actually useless (only serves to add cable and pole)

public class Materials {

    private final List<Component> components = new ArrayList<>(); // Components
    private final List<Component> alternate = new ArrayList<>(); // Alternate components
    private final List<Component> equip = new ArrayList<>(); // Alternate components
    private List<CraftStation> stations = new ArrayList<>(); // Crafting Stations
    private List<Structure> structures = new ArrayList<>(); // Generic structures that don't affect the logic
    private List<EssentialStructure> essentialStructures = new ArrayList<>(); // Structures that need to be checked by
                                                                              // the logic
    private List<Milestone> milestones = new ArrayList<>(); // Milestones
    private int[] phase = {};
    private static List<String> uraniumReq;

    public void prepare(int seed) {
        Random random = new Random(seed);

        // Create a temporary lists to add the prefixes to before adding to components
        List<CraftStation> tempStations = new ArrayList<>();
        List<Structure> tempStructures = new ArrayList<>();
        List<EssentialStructure> tempEssentialStructures = new ArrayList<>();

        // Set recquirements for uranium
        if (UiValues.getOreLocation() == 2) {
            uraniumReq = Arrays.asList("Desc_HazmatFilter", "Desc_HazmatSuit");
        } else {
            uraniumReq = null;
        }

        // Crafting Stations
        // //Game/FactoryGame/Recipes/Buildings/
        tempStations
                .add(new CraftStation("Desc_AssemblerMk1", false, false, "Recipe_AssemblerMk1", "Build_AssemblerMk1",
                        2, 1, 0, 0, Arrays.asList("power")));
        tempStations.add(new CraftStation("Desc_Blender", false, false, "Recipe_Blender", "Build_Blender",
                2, 1, 2, 1, Arrays.asList("power")));
        tempStations.add(
                new CraftStation("Desc_ConstructorMk1", false, false, "Recipe_ConstructorMk1", "Build_ConstructorMk1",
                        1, 1, 0, 0, Arrays.asList("power")));
        tempStations
                .add(new CraftStation("Desc_ManufacturerMk1", false, false, "Recipe_ManufacturerMk1",
                        "Build_ManufacturerMk1",
                        4, 1, 0, 0, Arrays.asList("power")));
        tempStations.add(new CraftStation("Desc_Packager", false, false, "Recipe_Packager", "Build_Packager",
                1, 1, 1, 1, Arrays.asList("power")));
        tempStations.add(
                new CraftStation("Desc_HadronCollider", false, false, "Recipe_HadronCollider", "Build_HadronCollider",
                        2, 1, 1, 0, Arrays.asList("power")));
        tempStations.add(new CraftStation("Desc_OilRefinery", false, false, "Recipe_OilRefinery", "Build_OilRefinery",
                1, 1, 1, 1, Arrays.asList("power")));
        tempStations.add(new CraftStation("Desc_Converter", false, false, "Recipe_Converter", "Build_Converter",
                2, 1, 0, 1, Arrays.asList("power")));
        tempStations
                .add(new CraftStation("Desc_QuantumEncoder", false, false, "Recipe_QuantumEncoder",
                        "Build_QuantumEncoder", 3, 1, 1, 1, Arrays.asList("power")));
        tempStations.add(new CraftStation("Desc_SmelterMk1", false, false, "Recipe_SmelterBasicMk1", "Build_SmelterMk1",
                1, 1, 0, 0, Arrays.asList("power")));
        tempStations.add(new CraftStation("Desc_FoundryMk1", false, false, "Recipe_SmelterMk1", "Build_FoundryMk1",
                2, 1, 0, 0, Arrays.asList("power")));
        this.stations = addPrefixStat(tempStations, "//Game/FactoryGame/Recipes/Buildings/");
        tempStations.clear();

        // Components

        // Raw materials
        this.components.add(new Component("Desc_Water", true, Arrays.asList("Desc_WaterPump", "power")));
        this.components.addAll(generateRawOre()); // They have no path, so no prefix is added.

        // Other materials
        this.components.addAll(addPrefixComp(generateMoreComponents(), "//Game/FactoryGame/Recipes/"));
        this.components.addAll(addPrefixComp(alternateButNotReally(), "//Game/FactoryGame/Recipes/"));

        // Equipment
        this.equip.addAll(addPrefixComp(generateEquipment(), "//Game/FactoryGame/Recipes/"));
        this.equip.addAll(addPrefixComp(generateEquipmnet2(), "//Game/FactoryGame/"));

        // //Game/FactoryGame/Recipes/SpaceElevatorParts/
        this.components.addAll(addPrefixComp(generateElevator(), "//Game/FactoryGame/Recipes/SpaceElevatorParts/"));

        // Milestones

        this.milestones.addAll(generateMilestones());

        // Alternate Recipes
        if (UiValues.alterReci) {
            this.alternate.addAll(addPrefixComp(generateOptional(), "//Game/FactoryGame/Recipes/"));
            this.alternate.addAll(addPrefixComp(generateAlternate(), "//Game/FactoryGame/Recipes/AlternateRecipes/"));

            // Alternate for Turbo Ammo (weird path and only one)
            List<Component> ammo = new ArrayList<>();
            ammo.add(generateComponent("Recipe_CartridgeChaos_Packaged", true));
            this.alternate.addAll(addPrefixComp(ammo, "//Game/FactoryGame/Equipment/Rifle/Ammo/"));
            for (Component alt : this.alternate) {
                alt.setName(null);
            }
        }

        // EssentialStructures
        // //Game/FactoryGame/Recipes/Buildings/
        tempEssentialStructures = generateEssentialBuildings();
        this.essentialStructures
                .addAll(addPrefixEssStr(tempEssentialStructures, "//Game/FactoryGame/Recipes/Buildings/"));
        tempEssentialStructures.clear();

        // Add the required structures to the milestones and the rest to non-essential.
        for (EssentialStructure structure : this.essentialStructures) {
            if (structure.addWhen() == 0) {
                Console.hiddenLog(structure.getName() + " added to Tutorial_6.");
                getMilestoneByName("Tutorial_6").addExtraCheck(structure.getName());
            } else if (structure.addWhen() < 5) {
                String name = "Milestone_" + (2 * structure.addWhen() - random.nextInt(2)) + "-1";
                getMilestoneByName(name).addExtraCheck(structure.getName());
                Console.log(structure.getName() + " added to " + name + ".");
            } else { // Can be added whenever, so it's actually not essential
                structures.add(structure);
                Console.log(structure.getName() + " moved to non-essential structures.");
            }
        }
        this.essentialStructures.removeAll(this.structures);

        // Structures
        tempStructures.add(new Structure("Desc_GeneratorCoal", false, false, "Recipe_GeneratorCoal", true));
        tempStructures.add(new Structure("Desc_GeneratorFuel", false, false, "Recipe_GeneratorFuel", true));
        tempStructures.add(new Structure("Desc_GeneratorGeoThermal", false, false, "Recipe_GeneratorGeoThermal", true));
        tempStructures.add(new Structure("Desc_GeneratorNuclear", false, false, "Recipe_GeneratorNuclear", false));
        structures.addAll(addPrefixStruc(tempStructures, "//Game/FactoryGame/Recipes/Buildings/"));
        tempStructures.clear();
        structures.addAll(addPrefixStruc(generateStructures(), "//Game/FactoryGame/Recipes/Buildings/"));
        structures.addAll(addPrefixStruc(generateMoreStructures(), "//Game/FactoryGame/Recipes/Buildings/"));
        // Vehicles can be treated as structurtes by this generator
        structures.addAll(addPrefixStruc(generateVehicles(), "//Game/FactoryGame/Recipes/"));

        // Recipes/Elevator
        tempStructures.add(generateStructure("Recipe_Elevator"));
        tempStructures.add(generateStructure("Recipe_ElevatorFloorStop"));
        structures.addAll(addPrefixStruc(tempStructures, "//Game/FactoryGame/Recipes/Elevator/"));
        tempStructures.clear();

        // Last thing, add cable and pole to whatever needs power
        for (Structure structure : this.structures) {
            if (structure.getExtraCheck().contains("power")) {
                structure.addExtraCheck("cable");
                structure.addExtraCheck("pole");
                structure.removeExtraCheck("power");
            }
        }
        for (CraftStation structure : this.stations) {
            if (structure.getExtraCheck().contains("power")) {
                structure.addExtraCheck("cable");
                structure.addExtraCheck("pole");
                structure.addExtraCheck("Tutorial_6");
                structure.removeExtraCheck("power");
            }
        }
        for (Component comp : this.components) {
            if (comp.getExtraCheck().contains("power")) {
                comp.addExtraCheck("cable");
                comp.addExtraCheck("pole");
                comp.removeExtraCheck("power");
            }
        }

    }

    // Data creators

    public void generateLimitedMats() {
        // Materials without associated recipe and can't be crafted.
        // They will be added at the end for alternate recipes.
        this.components.add(new Component("Desc_Wood", null, true, false, false));
    }

    private static List<EssentialStructure> generateEssentialBuildings() {

        List<EssentialStructure> tempStructures = new ArrayList<>();

        // Belts settings
        if (UiValues.getBelts() <= 1) {
            tempStructures.add(new EssentialStructure("Desc_ConveyorBeltMk1", false, false, "Recipe_ConveyorBeltMk1",
                    true, true, 0));
            tempStructures.add(new EssentialStructure("Desc_ConveyorPole", false, false,
                    "Recipe_ConveyorPole", false, false, 0));
        } else if (UiValues.getBelts() == 2) {
            tempStructures.add(new EssentialStructure("Desc_ConveyorBeltMk1", false, false, "Recipe_ConveyorBeltMk1",
                    true, false, 0));
            tempStructures.add(new EssentialStructure("Desc_ConveyorPole", false, false,
                    "Recipe_ConveyorPole", false, false, 0));
        } else {

            tempStructures.add(new EssentialStructure("Desc_ConveyorBeltMk1", false, false, "Recipe_ConveyorBeltMk1",
                    true, false, 9));
            tempStructures.add(new EssentialStructure("Desc_ConveyorPole", false, false,
                    "Recipe_ConveyorPole", false, false, 9));
        }
        if (UiValues.getBelts() == 0) {
            tempStructures.add(new EssentialStructure("Desc_ConveyorAttachmentMergerLift", false, false,
                    "Recipe_ConveyorAttachmentMergerLift", true, true, 0));
            tempStructures.add(new EssentialStructure("Desc_ConveyorAttachmentSplitterLift", false, false,
                    "Recipe_ConveyorAttachmentSplitterLift", true, true, 0));
        } else {
            tempStructures.add(new EssentialStructure("Desc_ConveyorAttachmentMergerLift", false, false,
                    "Recipe_ConveyorAttachmentMergerLift", false, false, 9));
            tempStructures.add(new EssentialStructure("Desc_ConveyorAttachmentSplitterLift", false, false,
                    "Recipe_ConveyorAttachmentSplitterLift", false, false, 9));
        }

        // Electricity settings
        if (UiValues.getElectricity() == 0) {
            tempStructures.add(new EssentialStructure("Desc_GeneratorBiomass_Automated", false, false,
                    "Recipe_GeneratorBiomass_Automated", true, false, 0));
            tempStructures.add(new EssentialStructure("Desc_PowerPoleMk1", false, false,
                    "Recipe_PowerPoleMk1", false, true, 0));
            tempStructures.add(new EssentialStructure("Desc_PowerLine", false, false,
                    "Recipe_PowerLine", false, true, 0));
        } else if (UiValues.getElectricity() == 1) {
            tempStructures.add(new EssentialStructure("Desc_GeneratorBiomass_Automated", false, false,
                    "Recipe_GeneratorBiomass_Automated", true, false, 0));
            tempStructures.add(new EssentialStructure("Desc_PowerPoleMk1", false, false,
                    "Recipe_PowerPoleMk1", false, false, 0));
            tempStructures.add(new EssentialStructure("Desc_PowerLine", false, false,
                    "Recipe_PowerLine", false, false, 0));
        } else if (UiValues.getElectricity() == 2) {
            tempStructures.add(new EssentialStructure("Desc_GeneratorBiomass_Automated", false, false,
                    "Recipe_GeneratorBiomass_Automated", true, false, 9));
            tempStructures.add(new EssentialStructure("Desc_PowerPoleMk1", false, false,
                    "Recipe_PowerPoleMk1", false, false, 0));
            tempStructures.add(new EssentialStructure("Desc_PowerLine", false, false,
                    "Recipe_PowerLine", false, false, 0));
        } else {
            tempStructures.add(new EssentialStructure("Desc_GeneratorBiomass_Automated", false, false,
                    "Recipe_GeneratorBiomass_Automated", true, false, 9));
            tempStructures.add(new EssentialStructure("Desc_PowerPoleMk1", false, false,
                    "Recipe_PowerPoleMk1", false, false, 9));
            tempStructures.add(new EssentialStructure("Desc_PowerLine", false, false,
                    "Recipe_PowerLine", false, false, 9));
        }

        if (UiValues.getWaste() == 1) {
            tempStructures.add(new EssentialStructure("Desc_ResourceSinkShop", false, false, "Recipe_ResourceSinkShop",
                    true, 0, Arrays.asList("Desc_ResourceSink", "power")));
        } else {
            tempStructures.add(new EssentialStructure("Desc_ResourceSinkShop", false, false, "Recipe_ResourceSinkShop",
                    false, 9, Arrays.asList("Desc_ResourceSink", "power")));
        }

        EssentialStructure spaceElevator = new EssentialStructure("Desc_SpaceElevator", false, false,
                "Recipe_SpaceElevator", false,
                false, 0);
        spaceElevator.setRecipePath("TowTruck/" + spaceElevator.getRecipePath());
        tempStructures.add(spaceElevator);

        return tempStructures;

    }

    private static List<Component> generateRawOre() {

        List<Component> tempRawOre = new ArrayList<>();

        if (UiValues.getOreLocation() == 0) {
            tempRawOre.add(new Component("Desc_OreIron", false, null));
            tempRawOre.add(new Component("Desc_OreCopper", false, Arrays.asList("Tutorial_2")));
            tempRawOre.add(new Component("Desc_Stone", false, Arrays.asList("Tutorial_3")));
            tempRawOre.add(new Component("Desc_Coal", false, Arrays.asList("Milestone_3-1")));
            tempRawOre.add(new Component("Desc_LiquidOil", true,
                    Arrays.asList("Milestone_5-2", "Desc_OilPump", "power")));
            tempRawOre.add(new Component("Desc_OreGold", false, Arrays.asList("Milestone_5-5")));
            tempRawOre.add(new Component("Desc_OreBauxite", false, Arrays.asList("Milestone_7-1")));
            tempRawOre.add(new Component("Desc_RawQuartz", false, Arrays.asList("Milestone_7-1")));
            tempRawOre.add(new Component("Desc_Sulfur", false, Arrays.asList("Milestone_7-5")));
            tempRawOre.add(new Component("Desc_OreUranium", false,
                    Arrays.asList("Milestone_8-2", "Desc_HazmatFilter", "Desc_HazmatSuit")));
            tempRawOre.add(new Component("Desc_NitrogenGas", true,
                    Arrays.asList("Milestone_8-3", "Desc_FrackingExtractor", "Desc_FrackingSmasher", "power")));
            tempRawOre.add(new Component("Desc_SAM", false, Arrays.asList("Milestone_9-1")));
        } else {
            tempRawOre.add(new Component("Desc_OreIron", false, null));
            tempRawOre.add(new Component("Desc_OreCopper", false, null));
            tempRawOre.add(new Component("Desc_Stone", false, null));
            tempRawOre.add(new Component("Desc_Coal", false, null));
            tempRawOre.add(new Component("Desc_LiquidOil", true, Arrays.asList("Desc_OilPump", "power")));
            tempRawOre.add(new Component("Desc_OreGold", false, null));
            tempRawOre.add(new Component("Desc_OreBauxite", false, null));
            tempRawOre.add(new Component("Desc_RawQuartz", false, null));
            tempRawOre.add(new Component("Desc_Sulfur", false, null));
            tempRawOre.add(new Component("Desc_OreUranium", false, uraniumReq));
            tempRawOre.add(new Component("Desc_NitrogenGas", true,
                    Arrays.asList("Desc_FrackingExtractor", "Desc_FrackingSmasher", "power")));
            tempRawOre.add(new Component("Desc_SAM", false, null));
        }

        return tempRawOre;
    }

    private static List<Component> generateMoreComponents() {
        List<Component> tempComps = new ArrayList<>();
        List<Component> tempNoPrefixComps = new ArrayList<>();

        // Assembler
        tempNoPrefixComps.add(generateComponent("Desc_CircuitBoardHighSpeed", "Recipe_AILimiter", false));
        tempNoPrefixComps.add(generateComponent("Recipe_CircuitBoard", false));
        tempNoPrefixComps.add(generateComponent("Recipe_ElectromagneticControlRod", false));
        tempNoPrefixComps.add(generateComponent("Desc_SteelPlateReinforced", "Recipe_EncasedIndustrialBeam", false));
        tempNoPrefixComps.add(generateComponent("Desc_AluminumPlateReinforced", "Recipe_HeatSink", false));
        tempNoPrefixComps.add(generateComponent("Recipe_IronPlateReinforced", false));
        tempNoPrefixComps.add(generateComponent("Recipe_ModularFrame", false));
        tempNoPrefixComps.add(generateComponent("Recipe_Motor", false));
        tempNoPrefixComps.add(generateComponent("Recipe_PlutoniumCell", false, uraniumReq));
        tempNoPrefixComps.add(generateComponent("Recipe_PressureConversionCube", false));
        tempNoPrefixComps.add(generateComponent("Recipe_Rotor", false));
        tempNoPrefixComps.add(generateComponent("Recipe_SAMFluctuator", false));
        tempNoPrefixComps.add(generateComponent("Recipe_Stator", false));
        tempNoPrefixComps.add(
                generateComponent("Recipe_UraniumCell", false, uraniumReq));
        tempComps.addAll(addPrefixComp(tempNoPrefixComps, "Assembler/"));
        tempNoPrefixComps.clear();

        // Blender
        tempNoPrefixComps.add(generateComponent("Recipe_CoolingSystem", false));
        tempNoPrefixComps.add(generateComponent("Desc_ModularFrameFused", "Recipe_FusedModularFrame", false));
        tempNoPrefixComps.add(generateComponent("Recipe_NitricAcid", true));
        tempNoPrefixComps
                .add(generateComponent("Desc_NonFissibleUranium", "Recipe_NonFissileUranium", false, uraniumReq));
        tempNoPrefixComps.add(generateComponent("Recipe_RocketFuel", true));
        tempComps.addAll(addPrefixComp(tempNoPrefixComps, "Blender/"));
        tempNoPrefixComps.clear();

        // Constructor
        tempNoPrefixComps.add(generateComponent("Recipe_AlienDNACapsule", false));
        tempNoPrefixComps.add(generateComponent("Recipe_AluminumCasing", false));
        tempNoPrefixComps.add(generateComponent("Desc_AluminumPlate", "Recipe_AluminumSheet", false));
        tempNoPrefixComps.add(generateComponent("Recipe_Biofuel", false));
        tempNoPrefixComps.add(generateComponent("Desc_GenericBiomass", "Recipe_Biomass_Leaves", false));
        // Using second biomass recipe for heavy oil
        tempNoPrefixComps.add(generateComponent("Desc_HeavyOilResidue", "Recipe_Biomass_Wood", true));
        tempNoPrefixComps.add(generateComponent("Recipe_Cable", false));
        tempNoPrefixComps.add(generateComponent("Desc_Cement", "Recipe_Concrete", false));
        tempNoPrefixComps.add(generateComponent("Recipe_CopperDust", false));
        tempNoPrefixComps.add(generateComponent("Recipe_CopperSheet", false));
        tempNoPrefixComps.add(generateComponent("Recipe_Fabric", false));
        tempNoPrefixComps.add(generateComponent("Recipe_FicsiteMesh", false));
        tempNoPrefixComps.add(generateComponent("Recipe_FluidCanister", false));
        tempNoPrefixComps.add(generateComponent("Recipe_GasTank", false));
        tempNoPrefixComps.add(generateComponent("Recipe_IronPlate", false));
        tempNoPrefixComps.add(generateComponent("Recipe_IronRod", false));
        tempNoPrefixComps.add(generateComponent("Recipe_NuclearFuelRod", false, uraniumReq));
        tempNoPrefixComps.add(generateComponent("Recipe_QuartzCrystal", false));
        tempNoPrefixComps.add(generateComponent("Desc_HighSpeedWire", "Recipe_Quickwire", false));
        tempNoPrefixComps.add(generateComponent("Desc_IronScrew", "Recipe_Screw", false));
        tempNoPrefixComps.add(generateComponent("Recipe_Silica", false));
        tempNoPrefixComps.add(generateComponent("Desc_SteelPlate", "Recipe_SteelBeam", false));
        tempNoPrefixComps.add(generateComponent("Recipe_SteelPipe", false));
        tempNoPrefixComps.add(generateComponent("Recipe_Wire", false));
        tempComps.addAll(addPrefixComp(tempNoPrefixComps, "Constructor/"));
        tempNoPrefixComps.clear();

        // Converter
        tempNoPrefixComps.add(generateComponent("Recipe_DarkEnergy", true));
        tempNoPrefixComps.add(generateComponent("Desc_FicsiteIngot", "Recipe_FicsiteIngot_AL", false));
        tempNoPrefixComps.add(generateComponent("Recipe_Ficsonium", false));
        tempNoPrefixComps.add(generateComponent("Recipe_QuantumEnergy", true));
        tempNoPrefixComps.add(generateComponent("Recipe_TimeCrystal", false));
        tempComps.addAll(addPrefixComp(tempNoPrefixComps, "Converter/"));
        tempNoPrefixComps.clear();

        // HadronCollider
        tempNoPrefixComps.add(generateComponent("Recipe_DarkMatter", false));
        tempNoPrefixComps.add(generateComponent("Recipe_Diamond", false));
        tempNoPrefixComps.add(generateComponent("Recipe_FicsoniumFuelRod", false));
        tempNoPrefixComps.add(generateComponent("Desc_PlutoniumPellet", "Recipe_Plutonium", false, uraniumReq));
        tempNoPrefixComps.add(generateComponent("Recipe_SingularityCell", false));
        tempComps.addAll(addPrefixComp(tempNoPrefixComps, "HadronCollider/"));
        tempNoPrefixComps.clear();

        // Manufacturer
        tempNoPrefixComps.add(generateComponent("Recipe_Battery", false));
        tempNoPrefixComps.add(generateComponent("Recipe_Computer", false));
        tempNoPrefixComps.add(generateComponent("Recipe_ComputerSuper", false));
        tempNoPrefixComps.add(generateComponent("Recipe_CrystalOscillator", false));
        tempNoPrefixComps.add(generateComponent("Recipe_HighSpeedConnector", false));
        tempNoPrefixComps.add(generateComponent("Recipe_ModularFrameHeavy", false));
        tempNoPrefixComps.add(generateComponent("Desc_MotorLightweight", "Recipe_MotorTurbo", false));
        tempNoPrefixComps.add(generateComponent("Recipe_PlutoniumFuelRod", false, uraniumReq));
        tempNoPrefixComps.add(generateComponent("Desc_ModularFrameLightweight", "Recipe_RadioControlUnit", false));
        tempComps.addAll(addPrefixComp(tempNoPrefixComps, "Manufacturer/"));
        tempNoPrefixComps.clear();

        // OilRefinery
        tempNoPrefixComps.add(generateComponent("Recipe_AluminaSolution", true));
        tempNoPrefixComps.add(generateComponent("Recipe_AluminumScrap", false));
        tempNoPrefixComps.add(generateComponent("Recipe_IonizedFuel", true));
        tempNoPrefixComps.add(generateComponent("Recipe_LiquidBiofuel", true));
        tempNoPrefixComps.add(generateComponent("Recipe_LiquidFuel", true));
        tempNoPrefixComps.add(generateComponent("Recipe_PetroleumCoke", false));
        tempNoPrefixComps.add(generateComponent("Recipe_Plastic", false));
        tempNoPrefixComps.add(generateComponent("Recipe_Rubber", false));
        tempNoPrefixComps.add(generateComponent("Recipe_SulfuricAcid", true));
        tempNoPrefixComps.add(generateComponent("Recipe_Fuel", false));
        tempNoPrefixComps.add(generateComponent("Recipe_PackagedBiofuel", false));
        tempNoPrefixComps.add(generateComponent("Desc_PackagedOil", "Recipe_PackagedCrudeOil", false));
        tempNoPrefixComps.add(generateComponent("Recipe_PackagedOilResidue", false));
        tempNoPrefixComps.add(generateComponent("Desc_TurboFuel", "Recipe_PackagedTurboFuel", false));
        tempNoPrefixComps.add(generateComponent("Recipe_PackagedWater", false));
        tempComps.addAll(addPrefixComp(tempNoPrefixComps, "OilRefinery/"));
        tempNoPrefixComps.clear();

        // Packager
        tempNoPrefixComps.add(generateComponent("Recipe_PackagedAlumina", false));
        tempNoPrefixComps.add(generateComponent("Recipe_PackagedIonizedFuel", false));
        tempNoPrefixComps.add(generateComponent("Recipe_PackagedNitricAcid", false));
        tempNoPrefixComps.add(generateComponent("Desc_PackagedNitrogenGas", "Recipe_PackagedNitrogen", false));
        tempNoPrefixComps.add(generateComponent("Recipe_PackagedRocketFuel", false));
        tempNoPrefixComps.add(generateComponent("Recipe_PackagedSulfuricAcid", false));
        tempComps.addAll(addPrefixComp(tempNoPrefixComps, "Packager/"));
        tempNoPrefixComps.clear();

        // Packaging is essential (only way to obtain packaged version, unpackage is
        // alternative).

        // QuantumEncoder
        tempNoPrefixComps.add(generateComponent("Recipe_AlienPowerFuel", false));
        tempNoPrefixComps.add(generateComponent("Desc_QuantumOscillator", "Recipe_SuperpositionOscillator", false));
        tempNoPrefixComps.add(generateComponent("Desc_CrystalShard", "Recipe_SyntheticPowerShard", false));
        tempNoPrefixComps.add(generateComponent("Recipe_TemporalProcessor", false));
        tempComps.addAll(addPrefixComp(tempNoPrefixComps, "QuantumEncoder/"));
        tempNoPrefixComps.clear();

        // Smelter
        tempNoPrefixComps.add(generateComponent("Desc_AluminumIngot", "Recipe_IngotAluminum", false));
        tempNoPrefixComps.add(generateComponent("Desc_GoldIngot", "Recipe_IngotCaterium", false));
        tempNoPrefixComps.add(generateComponent("Desc_CopperIngot", "Recipe_IngotCopper", false));
        tempNoPrefixComps.add(generateComponent("Desc_IronIngot", "Recipe_IngotIron", false));
        tempNoPrefixComps.add(generateComponent("Desc_SAMIngot", "Recipe_IngotSAM", false));
        tempNoPrefixComps.add(generateComponent("Desc_SteelIngot", "Recipe_IngotSteel", false));
        tempComps.addAll(addPrefixComp(tempNoPrefixComps, "Smelter/"));
        tempNoPrefixComps.clear();

        return tempComps;
    }

    private static List<Component> generateEquipment() {
        List<Component> tempComps = new ArrayList<>();
        List<Component> tempNoPrefixComps = new ArrayList<>();

        // Equipment
        tempNoPrefixComps.add(generateComponent("BP_EquipmentDescriptorJumpingStilts", "Recipe_BladeRunners", 1));
        tempNoPrefixComps.add(generateComponent("Desc_CartridgeStandard", "Recipe_Cartridge", false));
        tempNoPrefixComps.add(generateComponent("Desc_Chainsaw", "Recipe_Chainsaw", false));
        tempNoPrefixComps.add(generateComponent("Desc_Filter", "Recipe_FilterGasMask", false));
        tempNoPrefixComps.add(generateComponent("Desc_HazmatFilter", "Recipe_FilterHazmat", false));
        tempNoPrefixComps.add(generateComponent("BP_EquipmentDescriptorGasmask", "Recipe_Gasmask", false));
        tempNoPrefixComps.add(generateComponent("Desc_Gunpowder", "Recipe_Gunpowder", false));
        tempNoPrefixComps.add(generateComponent("Desc_GunpowderMK2", "Recipe_GunpowderMK2", false));
        tempNoPrefixComps.add(generateComponent("BP_EquipmentDescriptorHazmatSuit", "Recipe_HazmatSuit", false));
        tempNoPrefixComps.add(generateComponent("BP_EquipmentDescriptorHoverPack", "Recipe_Hoverpack", false));
        tempNoPrefixComps.add(generateComponent("BP_EquipmentDescriptorJetPack", "Recipe_JetPack", false));
        tempNoPrefixComps.add(generateComponent("Desc_Medkit", "Recipe_MedicinalInhaler", false));
        tempNoPrefixComps.add(generateComponent("Desc_NobeliskExplosive", "Recipe_Nobelisk", false));
        tempNoPrefixComps
                .add(generateComponent("BP_EquipmentDescriptorNobeliskDetonator", "Recipe_NobeliskDetonator", false));
        tempNoPrefixComps.add(generateComponent("BP_EquipmentDescriptorObjectScanner", "Recipe_ObjectScanner", false));
        tempNoPrefixComps.add(generateComponent("Desc_Parachute", "Recipe_Parachute", false));
        tempNoPrefixComps.add(generateComponent("BP_ItemDescriptorPortableMiner", "Recipe_PortableMiner", false));
        tempNoPrefixComps.add(generateComponent("Desc_RebarGunProjectile", "Recipe_RebarGun", false));
        tempNoPrefixComps.add(generateComponent("BP_EquipmentDescriptorRifle", "Recipe_SpaceRifleMk1", false));
        tempNoPrefixComps.add(generateComponent("Desc_SpikedRebar", "Recipe_SpikedRebar", false));
        tempNoPrefixComps.add(generateComponent("BP_EquipmentDescriptorStunSpear", "Recipe_XenoBasher", false));
        tempNoPrefixComps.add(generateComponent("BP_EquipmentDescriptorShockShank", "Recipe_XenoZapper", false));
        tempNoPrefixComps.add(generateComponent("BP_EqDescZipLine", "Recipe_ZipLine", false));
        tempComps.addAll(addPrefixComp(tempNoPrefixComps, "Equipment/"));
        tempNoPrefixComps.clear();

        // Not found recipe: Desc_Rebar_Stunshot, Desc_Rebar_Spreadshot,
        // Desc_Rebar_Explosive, Desc_NobeliskGas, Desc_NobeliskShockwave,
        // Desc_NobeliskCluster, Desc_NobeliskNuke,

        return tempComps;
    }

    private static List<Component> generateEquipmnet2() {
        List<Component> tempComps = new ArrayList<>();
        List<Component> tempNoPrefixComps = new ArrayList<>();

        // Equipment
        tempNoPrefixComps.add(generateComponent("Desc_CartridgeChaos", "Recipe_CartridgeChaos", 500));
        tempNoPrefixComps.add(generateComponent("Desc_CartridgeSmart", "Recipe_CartridgeSmart", 500));
        tempComps.addAll(addPrefixComp(tempNoPrefixComps, "Equipment/Rifle/Ammo/"));
        tempNoPrefixComps.clear();

        tempNoPrefixComps.add(generateComponent("Desc_NobeliskCluster", "Recipe_NobeliskCluster", 50));
        tempNoPrefixComps.add(generateComponent("Desc_NobeliskGas", "Recipe_NobeliskGas", 50));
        tempNoPrefixComps.add(generateComponent("Desc_NobeliskNuke", "Recipe_NobeliskNuke", 50));
        tempNoPrefixComps.add(generateComponent("Desc_NobeliskShockwave", "Recipe_NobeliskShockwave", 50));
        tempComps.addAll(addPrefixComp(tempNoPrefixComps, "Equipment/NobeliskDetonator/Ammo/"));
        tempNoPrefixComps.clear();

        tempNoPrefixComps.add(generateComponent("Desc_Rebar_Explosive", "Recipe_Rebar_Explosive", 100));
        tempNoPrefixComps.add(generateComponent("Desc_Rebar_Spreadshot", "Recipe_Rebar_Spreadshot", 100));
        tempNoPrefixComps.add(generateComponent("Desc_Rebar_Stunshot", "Recipe_Rebar_Stunshot", 100));
        tempComps.addAll(addPrefixComp(tempNoPrefixComps, "Equipment/RebarGun/Ammo/"));
        tempNoPrefixComps.clear();

        return tempComps;
    }

    private static List<Component> alternateButNotReally() {
        List<Component> emptyRecipes = new ArrayList<>();
        List<Component> returnvalue = new ArrayList<>();

        emptyRecipes.add(generateComponent("Recipe_Alternate_Turbofuel", true));
        returnvalue.addAll(addPrefixComp(emptyRecipes, "AlternateRecipes/Parts/"));
        emptyRecipes.clear();

        return returnvalue;

    }

    private static List<Component> generateOptional() {
        List<Component> emptyRecipes = new ArrayList<>();
        List<Component> returnValues = new ArrayList<>();

        // OilRefinery
        emptyRecipes.add(generateComponent(null, "Recipe_UnpackageBioFuel", false));
        emptyRecipes.add(generateComponent(null, "Recipe_UnpackageFuel", false));
        emptyRecipes.add(generateComponent(null, "Recipe_UnpackageOil", false));
        emptyRecipes.add(generateComponent(null, "Recipe_UnpackageOilResidue", false));
        emptyRecipes.add(generateComponent(null, "Recipe_UnpackageTurboFuel", false));
        emptyRecipes.add(generateComponent(null, "Recipe_UnpackageWater", false));
        emptyRecipes.add(generateComponent(null, "Recipe_ResidualFuel", false));
        emptyRecipes.add(generateComponent(null, "Recipe_ResidualPlastic", false));
        emptyRecipes.add(generateComponent(null, "Recipe_ResidualRubber", false));
        returnValues.addAll(addPrefixComp(emptyRecipes, "OilRefinery/"));
        emptyRecipes.clear();

        // Packager
        emptyRecipes.add(generateComponent(null, "Recipe_UnpackageAlumina", false));
        emptyRecipes.add(generateComponent(null, "Recipe_UnpackageIonizedFuel", false));
        emptyRecipes.add(generateComponent(null, "Recipe_UnpackageNitricAcid", false));
        emptyRecipes.add(generateComponent(null, "Recipe_UnpackageNitrogen", false));
        emptyRecipes.add(generateComponent(null, "Recipe_UnpackageRocketFuel", false));
        emptyRecipes.add(generateComponent(null, "Recipe_UnpackageSulfuricAcid", false));
        returnValues.addAll(addPrefixComp(emptyRecipes, "Packager/"));
        emptyRecipes.clear();

        // Constructor
        emptyRecipes.add(generateComponent(null, "Recipe_Biomass_Mycelia", false));
        emptyRecipes.add(generateComponent(null, "Recipe_Biomass_AlienProtein", false));
        emptyRecipes.add(generateComponent(null, "Recipe_Protein_Crab", false));
        emptyRecipes.add(generateComponent(null, "Recipe_Protein_Hog", false));
        emptyRecipes.add(generateComponent(null, "Recipe_Protein_Spitter", false));
        emptyRecipes.add(generateComponent(null, "Recipe_Protein_Stinger", false));
        emptyRecipes.add(generateComponent(null, "Recipe_PowerCrystalShard_1", false));
        emptyRecipes.add(generateComponent(null, "Recipe_PowerCrystalShard_2", false));
        emptyRecipes.add(generateComponent(null, "Recipe_PowerCrystalShard_3", false));
        returnValues.addAll(addPrefixComp(emptyRecipes, "Constructor/"));
        emptyRecipes.clear();

        // Converter
        emptyRecipes.add(generateComponent(null, "Recipe_FicsiteIngot_CAT", false));
        emptyRecipes.add(generateComponent(null, "Recipe_FicsiteIngot_Iron", false));
        returnValues.addAll(addPrefixComp(emptyRecipes, "Converter/"));
        emptyRecipes.clear();

        // Converter/ResourceConversion
        emptyRecipes.add(generateComponent(null, "Recipe_Bauxite_Caterium", false));
        emptyRecipes.add(generateComponent(null, "Recipe_Bauxite_Copper", false));
        emptyRecipes.add(generateComponent(null, "Recipe_Caterium_Copper", false));
        emptyRecipes.add(generateComponent(null, "Recipe_Caterium_Quartz", false));
        emptyRecipes.add(generateComponent(null, "Recipe_Coal_Iron", false));
        emptyRecipes.add(generateComponent(null, "Recipe_Coal_Limestone", false));
        emptyRecipes.add(generateComponent(null, "Recipe_Copper_Quartz", false));
        emptyRecipes.add(generateComponent(null, "Recipe_Copper_Sulfur", false));
        emptyRecipes.add(generateComponent(null, "Recipe_Iron_Limestone", false));
        emptyRecipes.add(generateComponent(null, "Recipe_Limestone_Sulfur", false));
        emptyRecipes.add(generateComponent(null, "Recipe_Nitrogen_Bauxite", false));
        emptyRecipes.add(generateComponent(null, "Recipe_Nitrogen_Caterium", false));
        emptyRecipes.add(generateComponent(null, "Recipe_Quartz_Bauxite", false));
        emptyRecipes.add(generateComponent(null, "Recipe_Quartz_Coal", false));
        emptyRecipes.add(generateComponent(null, "Recipe_Sulfur_Coal", false));
        emptyRecipes.add(generateComponent(null, "Recipe_Sulfur_Iron", false));
        emptyRecipes.add(generateComponent(null, "Recipe_Uranium_Bauxite", false));
        returnValues.addAll(addPrefixComp(emptyRecipes, "Converter/ResourceConversion/"));
        emptyRecipes.clear();

        // Equipment
        emptyRecipes.add(generateComponent(null, "Recipe_MedicinalInhalerAlienOrgans", false));
        emptyRecipes.add(generateComponent(null, "Recipe_TherapeuticInhaler", false));
        emptyRecipes.add(generateComponent(null, "Recipe_NutritionalInhaler", false));
        returnValues.addAll(addPrefixComp(emptyRecipes, "Equipment/"));
        emptyRecipes.clear();

        return returnValues;
    }

    private static List<Component> generateAlternate() {
        List<Component> emptyRecipes = new ArrayList<>();
        List<Component> returnValues = new ArrayList<>();

        // Alt_Tier9
        emptyRecipes.add(generateComponent("Recipe_Alternate_DarkMatter_Crystallization", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_DarkMatter_Trap", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_Diamond_Cloudy", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_Diamond_OilBased", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_Diamond_Petroleum", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_Diamond_Pink", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_Diamond_Turbo", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_IonizedFuel_Dark", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_RocketFuel_Nitro", false));
        returnValues.addAll(addPrefixComp(emptyRecipes, "Alt_Tier9/"));
        emptyRecipes.clear();

        // New_Update3
        emptyRecipes.add(generateComponent("Recipe_Alternate_AdheredIronPlate", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_BoltedFrame", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_CoatedCable", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_CoatedIronCanister", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_CoatedIronPlate", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_CokeSteelIngot", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_CopperAlloyIngot", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_CopperRotor", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_DilutedPackagedFuel", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_ElectroAluminumScrap", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_ElectrodeCircuitBoard", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_FlexibleFramework", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_FusedWire", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_HeavyFlexibleFrame", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_HeavyOilResidue", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_HighSpeedWiring", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_PlasticSmartPlating", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_PolyesterFabric", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_PolymerResin", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_PureCateriumIngot", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_PureCopperIngot", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_PureIronIngot", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_PureQuartzCrystal", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_RecycledRubber", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_RubberConcrete", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_SteamedCopperSheet", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_SteelCanister", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_SteelRod", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_TurboHeavyFuel", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_WetConcrete", false));
        emptyRecipes.add(generateComponent("Recipe_PureAluminumIngot", false));
        returnValues.addAll(addPrefixComp(emptyRecipes, "New_Update3/"));
        emptyRecipes.clear();

        // New_Update4
        emptyRecipes.add(generateComponent("Recipe_Alternate_AlcladCasing", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_AutomatedMiner", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_ClassicBattery", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_CoolingDevice", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_DilutedFuel", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_ElectricMotor", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_FertileUranium", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_HeatFusedFrame", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_InstantPlutoniumCell", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_InstantScrap", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_OCSupercomputer", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_PlutoniumFuelUnit", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_RadioControlSystem", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_SloppyAlumina", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_SuperStateComputer", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_TurboBlendFuel", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_TurboPressureMotor", false));
        returnValues.addAll(addPrefixComp(emptyRecipes, "New_Update4/"));
        emptyRecipes.clear();

        // New_Update9
        emptyRecipes.add(generateComponent("Recipe_Alternate_AILimiter_Plastic", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_AluminumRod", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_SteelBeam_Aluminum", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_SteelBeam_Molded", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_SteelCastedPlate", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_SteelPipe_Iron", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_SteelPipe_Molded", false));
        returnValues.addAll(addPrefixComp(emptyRecipes, "New_Update9/"));
        emptyRecipes.clear();

        // New_Update9_Ingots
        emptyRecipes.add(generateComponent("Recipe_Alternate_CateriumIngot_Leached", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_CateriumIngot_Tempered", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_CopperIngot_Leached", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_CopperIngot_Tempered", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_IronIngot_Basic", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_IronIngot_Leached", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_Quartz_Fused", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_Quartz_Purified", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_Silica_Distilled", false));
        returnValues.addAll(addPrefixComp(emptyRecipes, "New_Update9_Ingots/"));
        emptyRecipes.clear();

        // Parts
        emptyRecipes.add(generateComponent("Recipe_Alternate_Cable_1", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_Cable_2", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_CircuitBoard_1", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_CircuitBoard_2", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_Coal_1", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_Coal_2", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_Computer_1", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_Computer_2", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_Concrete", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_CrystalOscillator", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_ElectromagneticControlRod_1", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_EncasedIndustrialBeam", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_EnrichedCoal", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_Gunpowder_1", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_HeatSink_1", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_HighSpeedConnector", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_IngotIron", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_IngotSteel_1", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_IngotSteel_2", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_ModularFrame", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_ModularFrameHeavy", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_Motor_1", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_NuclearFuelRod_1", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_Plastic_1", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_Quickwire", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_RadioControlUnit_1", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_ReinforcedIronPlate_1", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_ReinforcedIronPlate_2", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_Rotor", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_Screw", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_Screw_2", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_Silica", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_Stator", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_TurboMotor_1", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_UraniumCell_1", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_Wire_1", false));
        emptyRecipes.add(generateComponent("Recipe_Alternate_Wire_2", false));
        returnValues.addAll(addPrefixComp(emptyRecipes, "Parts/"));
        emptyRecipes.clear();

        return returnValues;
    }

    private List<Milestone> generateMilestones() {
        List<Milestone> tempMilestones = new ArrayList<>();
        List<Milestone> tempReturn = new ArrayList<>();
        // Tutorial are marked as available so that they unlock when their extraChecks
        // are met (the previous tutorial)

        // Elevator Phases
        List<String> phase1 = Arrays.asList("Desc_SpaceElevatorPart_1");
        List<String> phase2 = Arrays.asList("Desc_SpaceElevatorPart_2", "Desc_SpaceElevatorPart_3");
        List<String> phase3 = Arrays.asList("Desc_SpaceElevatorPart_4", "Desc_SpaceElevatorPart_5");
        List<String> phase4 = Arrays.asList("Desc_SpaceElevatorPart_6", "Desc_SpaceElevatorPart_7",
                "Desc_SpaceElevatorPart_8", "Desc_SpaceElevatorPart_9");

        int[] p = { 1, phase1.size(), phase2.size(), phase3.size(), phase4.size() };
        this.phase = p;

        // //Game/FactoryGame/Schematics/Tutorial/
        tempMilestones.add(new Milestone("Tutorial_1", true, false, "Schematic_Tutorial1", null, 0));
        tempMilestones.add(
                new Milestone("Tutorial_2", true, false, "Schematic_Tutorial1_5", Arrays.asList("Tutorial_1"), 0));
        tempMilestones
                .add(new Milestone("Tutorial_3", true, false, "Schematic_Tutorial2", Arrays.asList("Tutorial_2"), 0));
        tempMilestones
                .add(new Milestone("Tutorial_4", true, false, "Schematic_Tutorial3", Arrays.asList("Tutorial_3"), 0));
        tempMilestones
                .add(new Milestone("Tutorial_5", true, false, "Schematic_Tutorial4", Arrays.asList("Tutorial_4"), 0));
        tempMilestones
                .add(new Milestone("Tutorial_6", true, false, "Schematic_Tutorial5", Arrays.asList("Tutorial_5"), 0));

        tempReturn.addAll(addPrefixMile(tempMilestones, "//Game/FactoryGame/Schematics/Tutorial/"));
        tempMilestones.clear();

        tempMilestones
                .add(new Milestone("Milestone_1-1", true, false, "Schematic_1-1", Arrays.asList("Tutorial_6"), 1));
        tempMilestones
                .add(new Milestone("Milestone_1-2", true, false, "Schematic_1-2", Arrays.asList("Tutorial_6"), 1));
        tempMilestones
                .add(new Milestone("Milestone_1-3", true, false, "Schematic_1-3", Arrays.asList("Tutorial_6"), 1));
        tempMilestones
                .add(new Milestone("Milestone_2-1", true, false, "Schematic_2-1", Arrays.asList("Tutorial_6"), 1));
        tempMilestones
                .add(new Milestone("Milestone_2-2", true, false, "Schematic_2-2", Arrays.asList("Tutorial_6"), 1));
        tempMilestones
                .add(new Milestone("Milestone_2-3", true, false, "Schematic_2-3", Arrays.asList("Tutorial_6"), 1));
        tempMilestones
                .add(new Milestone("Milestone_2-5", true, false, "Schematic_2-5", Arrays.asList("Tutorial_6"), 1));
        tempMilestones
                .add(new Milestone("Milestone_3-1", true, false, "Schematic_3-1", phase1, 2));
        tempMilestones // This one and the one avove are right despite not looking like it
                .add(new Milestone("Milestone_3-2", true, false, "Schematic_3-2", Arrays.asList("Tutorial_6"), 1));
        tempMilestones
                .add(new Milestone("Milestone_3-3", true, false, "Schematic_3-3", phase1, 2));
        tempMilestones
                .add(new Milestone("Milestone_3-4", true, false, "Schematic_3-4", phase1, 2));
        tempMilestones
                .add(new Milestone("Milestone_4-1", true, false, "Schematic_4-1", phase1, 2));
        tempMilestones
                .add(new Milestone("Milestone_4-2", true, false, "Schematic_4-2", phase1, 2));
        tempMilestones
                .add(new Milestone("Milestone_4-3", true, false, "Schematic_4-3", phase1, 2));
        tempMilestones
                .add(new Milestone("Milestone_4-4", true, false, "Schematic_4-4", phase1, 2));
        tempMilestones
                .add(new Milestone("Milestone_4-5", true, false, "Schematic_4-5", phase1, 2));
        tempMilestones
                .add(new Milestone("Milestone_5-1", true, false, "Schematic_5-1", phase2, 3));
        tempMilestones
                .add(new Milestone("Milestone_5-2", true, false, "Schematic_5-2", phase2, 3));
        tempMilestones
                .add(new Milestone("Milestone_5-4", true, false, "Schematic_5-4", phase2, 3));
        tempMilestones
                .add(new Milestone("Milestone_5-5", true, false, "Schematic_5-5", phase2, 3));
        tempMilestones
                .add(new Milestone("Milestone_6-1", true, false, "Schematic_6-1", phase2, 3));
        tempMilestones
                .add(new Milestone("Milestone_6-2", true, false, "Schematic_6-2", phase2, 3));
        tempMilestones
                .add(new Milestone("Milestone_6-3", true, false, "Schematic_6-3", phase2, 3));
        tempMilestones
                .add(new Milestone("Milestone_6-5", true, false, "Schematic_6-5", phase2, 3));
        tempMilestones
                .add(new Milestone("Milestone_6-7", true, false, "Schematic_6-7", phase2, 3));
        tempMilestones
                .add(new Milestone("Milestone_7-1", true, false, "Schematic_7-1", phase3, 4));
        tempMilestones
                .add(new Milestone("Milestone_7-2", true, false, "Schematic_7-2", phase3, 4));
        tempMilestones
                .add(new Milestone("Milestone_7-3", true, false, "Schematic_7-3", phase3, 4));
        tempMilestones
                .add(new Milestone("Milestone_7-4", true, false, "Schematic_7-4", phase3, 4));
        tempMilestones
                .add(new Milestone("Milestone_7-5", true, false, "Schematic_7-5", phase3, 4));
        tempMilestones
                .add(new Milestone("Milestone_8-1", true, false, "Schematic_8-1", phase3, 4));
        tempMilestones
                .add(new Milestone("Milestone_8-2", true, false, "Schematic_8-2", phase3, 4));
        tempMilestones
                .add(new Milestone("Milestone_8-3", true, false, "Schematic_8-3", phase3, 4));
        tempMilestones
                .add(new Milestone("Milestone_8-4", true, false, "Schematic_8-4", phase3, 4));
        tempMilestones
                .add(new Milestone("Milestone_8-5", true, false, "Schematic_8-5", phase3, 4));
        tempMilestones
                .add(new Milestone("Milestone_9-1", true, false, "Schematic_9-1", phase4, 5));
        tempMilestones
                .add(new Milestone("Milestone_9-2", true, false, "Schematic_9-2", phase4, 5));
        tempMilestones
                .add(new Milestone("Milestone_9-3", true, false, "Schematic_9-3", phase4, 5));
        tempMilestones
                .add(new Milestone("Milestone_9-4", true, false, "Schematic_9-4", phase4, 5));
        tempMilestones
                .add(new Milestone("Milestone_9-5", true, false, "Schematic_9-5", phase4, 5));

        tempReturn.addAll(addPrefixMile(tempMilestones, "//Game/FactoryGame/Schematics/Progression/"));

        for (Milestone milestone : tempMilestones) {
            Console.hiddenLog(milestone.getName() + " is in phase " + milestone.getPhase());
        }
        Console.hiddenLog("Yes, 3-2 is correctly in phase 1 (at least as of patch 1.1).");

        return tempReturn;
    }

    private List<Milestone> generateMamMilestones() {
        List<Milestone> tempReturn = new ArrayList<>();
        List<Milestone> tempNoPrefix = new ArrayList<>();

        // AlienOrganisms_RS
        // tempMilestones.add(new Milestone("Tutorial_1", true, false,
        // "Schematic_Tutorial1", null, 0));

        return tempReturn;
    }

    private static List<Structure> generateStructures() {
        List<Structure> tempStructures = new ArrayList<>();
        tempStructures.add(new Structure("Desc_Mam", false, false, "Recipe_Mam", Arrays.asList("power")));
        tempStructures.add(generateStructure("Recipe_ResourceSink", Arrays.asList("power")));
        tempStructures.add(generateStructure("Recipe_AlienPowerBuilding"));
        tempStructures.add(generateStructure("Recipe_BlueprintDesigner"));
        tempStructures.add(generateStructure("Recipe_BlueprintDesigner_Mk3"));
        tempStructures.add(generateStructure("Recipe_CeilingLight"));
        tempStructures.add(generateStructure("Recipe_CentralStorage"));
        tempStructures.add(generateStructure("Recipe_ConveyorAttachmentMergerPriorityLift"));
        tempStructures.add(generateStructure("Recipe_ConveyorAttachmentSplitterSmartLift"));
        tempStructures.add(generateStructure("Recipe_ConveyorAttachmentSplitterProgrammableLift"));
        tempStructures.add(generateStructure("Recipe_ConveyorCeilingAttachment"));
        tempStructures.add(generateStructure("Recipe_ConveyorMonitor"));
        tempStructures.add(generateStructure("Recipe_ConveyorPoleStackable"));
        tempStructures.add(generateStructure("Recipe_ConveyorPoleWall"));
        tempStructures.add(generateStructure("Recipe_DroneStation"));
        tempStructures.add(generateStructure("Recipe_DroneTransport"));
        tempStructures.add(generateStructure("Recipe_FloodlightPole"));
        tempStructures.add(generateStructure("Recipe_FloodlightWall"));
        tempStructures.add(generateStructure("Recipe_FrackingExtractor"));
        tempStructures.add(generateStructure("Recipe_FrackingSmasher"));
        tempStructures.add(generateStructure("Recipe_HyperPoleStackable"));
        tempStructures.add(generateStructure("Recipe_HyperTubeJunction"));
        tempStructures.add(generateStructure("Recipe_HyperTubeTJunction"));
        tempStructures.add(generateStructure("Recipe_HyperTubeWallHole"));
        tempStructures.add(generateStructure("Recipe_HyperTubeWallSupport"));
        tempStructures.add(generateStructure("Recipe_IndustrialTank"));
        tempStructures.add(generateStructure("Recipe_JumpPad"));
        tempStructures.add(generateStructure("Recipe_LookoutTower"));
        tempStructures.add(generateStructure("Recipe_OilPump"));
        tempStructures.add(generateStructure("Recipe_PipeHyperSupport"));
        tempStructures.add(generateStructure("Recipe_Pipeline"));
        tempStructures.add(generateStructure("Recipe_PipelineMK2"));
        tempStructures.add(generateStructure("Recipe_PipelinePump"));
        tempStructures.add(generateStructure("Recipe_PipelinePumpMK2"));
        tempStructures.add(generateStructure("Recipe_PipeStorageTank"));
        tempStructures.add(generateStructure("Recipe_PipeSupport"));
        tempStructures.add(generateStructure("Recipe_PipeSupportStackable"));
        tempStructures.add(generateStructure("Recipe_PipeSupportWall"));
        tempStructures.add(generateStructure("Recipe_PipeSupportWallHole"));
        tempStructures.add(generateStructure("Recipe_Portal"));
        tempStructures.add(generateStructure("Recipe_PortalSatellite"));
        tempStructures.add(generateStructure("Recipe_PowerPoleMk2"));
        tempStructures.add(generateStructure("Recipe_PowerPoleMk3"));
        tempStructures.add(generateStructure("Recipe_PowerPoleWall"));
        tempStructures.add(generateStructure("Recipe_PowerPoleWallDouble"));
        tempStructures.add(generateStructure("Recipe_PowerPoleWallDoubleMk2"));
        tempStructures.add(generateStructure("Recipe_PowerPoleWallDoubleMk3"));
        tempStructures.add(generateStructure("Recipe_PowerPoleWallMk2"));
        tempStructures.add(generateStructure("Recipe_PowerPoleWallMk3"));
        tempStructures.add(generateStructure("Recipe_PowerStorageMk1"));
        tempStructures.add(generateStructure("Recipe_PowerSwitch"));
        tempStructures.add(generateStructure("Recipe_PowerTower"));
        tempStructures.add(generateStructure("Recipe_PowerTowerPlatform"));
        tempStructures.add(generateStructure("Recipe_PriorityPowerSwitch"));
        tempStructures.add(generateStructure("Recipe_RadarTower"));
        tempStructures.add(generateStructure("Recipe_RailroadTrack"));
        tempStructures.add(generateStructure("Recipe_RailroadTrackIntegrated"));
        tempStructures.add(generateStructure("Recipe_SignPole_Huge"));
        tempStructures.add(generateStructure("Recipe_SignPole_Large"));
        tempStructures.add(generateStructure("Recipe_SignPole_Medium"));
        tempStructures.add(generateStructure("Recipe_SignPole_Portrait"));
        tempStructures.add(generateStructure("Recipe_SignPole_Small"));
        tempStructures.add(generateStructure("Recipe_StandaloneWidgetSign_Huge"));
        tempStructures.add(generateStructure("Recipe_StandaloneWidgetSign_Large"));
        tempStructures.add(generateStructure("Recipe_StandaloneWidgetSign_Medium"));
        tempStructures.add(generateStructure("Recipe_StandaloneWidgetSign_Portrait"));
        tempStructures.add(generateStructure("Recipe_StandaloneWidgetSign_Small"));
        tempStructures.add(generateStructure("Recipe_StandaloneWidgetSign_SmallVeryWide"));
        tempStructures.add(generateStructure("Recipe_StandaloneWidgetSign_SmallWide"));
        tempStructures.add(generateStructure("Recipe_StandaloneWidgetSign_Square"));
        tempStructures.add(generateStructure("Recipe_StandaloneWidgetSign_Square_Small"));
        tempStructures.add(generateStructure("Recipe_StandaloneWidgetSign_Square_Tiny"));
        tempStructures.add(generateStructure("Recipe_StorageContainerMk1"));
        tempStructures.add(generateStructure("Recipe_StorageContainerMk2"));
        tempStructures.add(generateStructure("Recipe_TruckStation"));
        tempStructures.add(generateStructure("Recipe_UJellyLandingPad"));
        tempStructures.add(generateStructure("Recipe_Valve"));
        tempStructures.add(generateStructure("Recipe_WaterPump"));
        tempStructures.add(generateStructure("Recipe_WorkBench"));
        tempStructures.add(generateStructure("Recipe_Workshop"));

        tempStructures.addAll(generateMkN("Recipe_ConveyorBeltMk1", 2, 6));
        tempStructures.addAll(generateMkN("Recipe_ConveyorLiftMk1", 1, 6));
        tempStructures.addAll(generateMkN("Recipe_MinerMk1", 1, 3));

        return tempStructures;
    }

    private static List<Structure> generateMoreStructures() {
        List<Structure> tempStructures = new ArrayList<>();
        List<Structure> tempReturnStructures = new ArrayList<>();

        tempStructures.add(generateStructure("Recipe_Flat_Frame_01"));
        tempStructures.add(generateStructure("Recipe_Foundation_8x1_01"));
        tempStructures.add(generateStructure("Recipe_Foundation_8x2_01"));
        tempStructures.add(generateStructure("Recipe_Foundation_8x4_01"));
        tempStructures.add(generateStructure("Recipe_Foundation_Frame_01"));
        tempStructures.add(generateStructure("Recipe_FoundationGlass_01"));
        tempStructures.add(generateStructure("Recipe_PillarBase"));
        tempStructures.add(generateStructure("Recipe_PillarMiddle"));
        tempStructures.add(generateStructure("Recipe_PillarMiddle_Concrete"));
        tempStructures.add(generateStructure("Recipe_PillarMiddle_Frame"));
        tempStructures.add(generateStructure("Recipe_PillarTop"));
        tempStructures.add(generateStructure("Recipe_QuarterPipe"));
        tempStructures.add(generateStructure("Recipe_QuarterPipe_02"));
        tempStructures.add(generateStructure("Recipe_QuarterPipeCorner_01"));
        tempStructures.add(generateStructure("Recipe_QuarterPipeCorner_02"));
        tempStructures.add(generateStructure("Recipe_QuarterPipeCorner_03"));
        tempStructures.add(generateStructure("Recipe_QuarterPipeCorner_04"));
        tempReturnStructures.addAll(addPrefixStruc(tempStructures, "Foundations/"));
        tempStructures.clear();

        tempStructures.add(generateStructure("Recipe_Catwalk_Cross"));
        tempStructures.add(generateStructure("Recipe_Catwalk_Ramp"));
        tempStructures.add(generateStructure("Recipe_Catwalk_Stairs"));
        tempStructures.add(generateStructure("Recipe_Catwalk_Straight"));
        tempStructures.add(generateStructure("Recipe_Catwalk_T"));
        tempStructures.add(generateStructure("Recipe_Catwalk_Turn"));
        tempReturnStructures.addAll(addPrefixStruc(tempStructures, "Catwalks/"));
        tempStructures.clear();

        tempStructures.add(generateStructure("Recipe_Barrier_Corner"));
        tempStructures.add(generateStructure("Recipe_Barrier_Low_01"));
        tempStructures.add(generateStructure("Recipe_Barrier_Tall_01"));
        tempStructures.add(generateStructure("Recipe_ChainLinkFence"));
        tempStructures.add(generateStructure("Recipe_Concrete_Barrier_01"));
        tempStructures.add(generateStructure("Recipe_Fence_01"));
        tempStructures.add(generateStructure("Recipe_Railing_01"));
        tempStructures.add(generateStructure("Recipe_TarpFence"));
        tempReturnStructures.addAll(addPrefixStruc(tempStructures, "Fence/"));
        tempStructures.clear();

        tempReturnStructures.addAll(addPrefixStruc(Arrays.asList(generateStructure("Recipe_Ladder")), "Ladder/"));
        tempReturnStructures
                .addAll(addPrefixStruc(Arrays.asList(generateStructure("Recipe_Stairs_Left_01")), "Stairs/"));
        tempReturnStructures
                .addAll(addPrefixStruc(Arrays.asList(generateStructure("Recipe_Stairs_Right_01")), "Stairs/"));

        tempStructures.add(generateStructure("Recipe_Ramp_8x1_01"));
        tempStructures.add(generateStructure("Recipe_Ramp_8x2_01"));
        tempStructures.add(generateStructure("Recipe_Ramp_8x4_01"));
        tempStructures.add(generateStructure("Recipe_Ramp_8x4_Inverted_01"));
        tempStructures.add(generateStructure("Recipe_Ramp_8x8x8"));
        tempStructures.add(generateStructure("Recipe_Ramp_Diagonal_8x1_01"));
        tempStructures.add(generateStructure("Recipe_Ramp_Diagonal_8x1_02"));
        tempStructures.add(generateStructure("Recipe_Ramp_Diagonal_8x2_01"));
        tempStructures.add(generateStructure("Recipe_Ramp_Diagonal_8x2_02"));
        tempStructures.add(generateStructure("Recipe_Ramp_Diagonal_8x4_01"));
        tempStructures.add(generateStructure("Recipe_Ramp_Diagonal_8x4_02"));
        tempStructures.add(generateStructure("Recipe_Ramp_Frame_01"));
        tempStructures.add(generateStructure("Recipe_Ramp_Frame_Inverted_01"));
        tempStructures.add(generateStructure("Recipe_RampDouble"));
        tempStructures.add(generateStructure("Recipe_RampDouble_8x1"));
        tempStructures.add(generateStructure("Recipe_RampInverted_8x1"));
        tempStructures.add(generateStructure("Recipe_RampInverted_8x1_Corner_01"));
        tempStructures.add(generateStructure("Recipe_RampInverted_8x1_Corner_02"));
        tempStructures.add(generateStructure("Recipe_RampInverted_8x2_01"));
        tempStructures.add(generateStructure("Recipe_RampInverted_8x2_Corner_01"));
        tempStructures.add(generateStructure("Recipe_RampInverted_8x2_Corner_02"));
        tempStructures.add(generateStructure("Recipe_RampInverted_8x4_Corner_01"));
        tempStructures.add(generateStructure("Recipe_RampInverted_8x4_Corner_02"));
        tempReturnStructures.addAll(addPrefixStruc(tempStructures, "Ramps/"));
        tempStructures.clear();

        tempStructures.addAll(generateMkN("Recipe_Roof_A_01", 1, 4));
        tempStructures.addAll(generateMkN("Recipe_Roof_Metal_InCorner_01", 1, 3));
        tempStructures.addAll(generateMkN("Recipe_Roof_Metal_OutCorner_01", 1, 3));
        tempStructures.addAll(generateMkN("Recipe_Roof_Orange_01", 1, 4));
        tempStructures.addAll(generateMkN("Recipe_Roof_Orange_InCorner_01", 1, 3));
        tempStructures.addAll(generateMkN("Recipe_Roof_Orange_OutCorner_01", 1, 3));
        tempStructures.addAll(generateMkN("Recipe_Roof_Tar_01", 1, 4));
        tempStructures.addAll(generateMkN("Recipe_Roof_Tar_InCorner_01", 1, 3));
        tempStructures.addAll(generateMkN("Recipe_Roof_Tar_OutCorner_01", 1, 3));
        tempStructures.addAll(generateMkN("Recipe_Roof_Window_01", 1, 4));
        tempStructures.addAll(generateMkN("Recipe_Roof_Window_InCorner_01", 1, 3));
        tempStructures.addAll(generateMkN("Recipe_Roof_Window_OutCorner_01", 1, 3));
        tempReturnStructures.addAll(addPrefixStruc(tempStructures, "Roof/"));
        tempStructures.clear();

        tempStructures.add(generateStructure("Recipe_Walkway_Cross"));
        tempStructures.add(generateStructure("Recipe_Walkway_Ramp"));
        tempStructures.add(generateStructure("Recipe_Walkway_Straight"));
        tempStructures.add(generateStructure("Recipe_Walkway_T"));
        tempStructures.add(generateStructure("Recipe_Walkway_Turn"));
        tempReturnStructures.addAll(addPrefixStruc(tempStructures, "Walkways/"));
        tempStructures.clear();

        tempStructures.add(generateStructure("Recipe_SteelWall_8x1"));
        tempStructures.add(generateStructure("Recipe_SteelWall_8x4"));
        tempStructures.add(generateStructure("Recipe_SteelWall_FlipTris_8x1"));
        tempStructures.add(generateStructure("Recipe_SteelWall_FlipTris_8x2"));
        tempStructures.add(generateStructure("Recipe_SteelWall_FlipTris_8x4"));
        tempStructures.add(generateStructure("Recipe_SteelWall_FlipTris_8x8"));
        tempStructures.add(generateStructure("Recipe_SteelWall_Tris_8x1"));
        tempStructures.add(generateStructure("Recipe_SteelWall_Tris_8x2"));
        tempStructures.add(generateStructure("Recipe_SteelWall_Tris_8x4"));
        tempStructures.add(generateStructure("Recipe_SteelWall_Tris_8x8"));
        tempStructures.add(generateStructure("Recipe_Wall_8x4_01"));
        tempStructures.add(generateStructure("Recipe_Wall_Conveyor_8x4_01"));
        tempStructures.add(generateStructure("Recipe_Wall_Conveyor_8x4_01_Steel"));
        tempStructures.add(generateStructure("Recipe_Wall_Conveyor_8x4_02"));
        tempStructures.add(generateStructure("Recipe_Wall_Conveyor_8x4_02_Steel"));
        tempStructures.add(generateStructure("Recipe_Wall_Conveyor_8x4_03"));
        tempStructures.add(generateStructure("Recipe_Wall_Conveyor_8x4_03_Steel"));
        tempStructures.add(generateStructure("Recipe_Wall_Conveyor_8x4_04"));
        tempStructures.add(generateStructure("Recipe_Wall_Conveyor_8x4_04_Steel"));
        tempStructures.add(generateStructure("Recipe_Wall_Door_8x4_01"));
        tempStructures.add(generateStructure("Recipe_Wall_Door_8x4_01_Steel"));
        tempStructures.add(generateStructure("Recipe_Wall_Door_8x4_03"));
        tempStructures.add(generateStructure("Recipe_Wall_Door_8x4_03_Steel"));
        tempStructures.add(generateStructure("Recipe_Wall_Frame_01"));
        tempStructures.add(generateStructure("Recipe_Wall_Gate_8x4_01"));
        tempStructures.add(generateStructure("Recipe_Wall_Orange_8x1"));
        tempStructures.add(generateStructure("Recipe_Wall_Orange_8x4_Corner_01"));
        tempStructures.add(generateStructure("Recipe_Wall_Orange_8x4_Corner_02"));
        tempStructures.add(generateStructure("Recipe_Wall_Orange_8x8_Corner_01"));
        tempStructures.add(generateStructure("Recipe_Wall_Orange_8x8_Corner_02"));
        tempStructures.add(generateStructure("Recipe_Wall_Orange_Angular_8x4"));
        tempStructures.add(generateStructure("Recipe_Wall_Orange_Angular_8x8"));
        tempStructures.add(generateStructure("Recipe_Wall_Orange_FlipTris_8x1"));
        tempStructures.add(generateStructure("Recipe_Wall_Orange_FlipTris_8x2"));
        tempStructures.add(generateStructure("Recipe_Wall_Orange_FlipTris_8x4"));
        tempStructures.add(generateStructure("Recipe_Wall_Orange_FlipTris_8x8"));
        tempStructures.add(generateStructure("Recipe_Wall_Orange_Tris_8x1"));
        tempStructures.add(generateStructure("Recipe_Wall_Orange_Tris_8x2"));
        tempStructures.add(generateStructure("Recipe_Wall_Orange_Tris_8x4"));
        tempStructures.add(generateStructure("Recipe_Wall_Orange_Tris_8x8"));
        tempStructures.addAll(generateMkN("Recipe_Wall_Window_8x4_01", 1, 7));
        tempStructures.add(generateStructure("Recipe_Wall_Window_8x4_03_Steel"));
        tempStructures.add(generateStructure("Recipe_Wall_Window_Thin_8x4_01"));
        tempStructures.add(generateStructure("Recipe_Wall_Window_Thin_8x4_02"));
        tempStructures.add(generateStructure("Recipe_WallSet_Steel_Angular_8x4"));
        tempStructures.add(generateStructure("Recipe_WallSet_Steel_Angular_8x8"));
        tempReturnStructures.addAll(addPrefixStruc(tempStructures, "Walls/"));
        tempStructures.clear();

        return tempReturnStructures;
    }

    private static List<Structure> generateVehicles() {
        List<Structure> tempStructures = new ArrayList<>();
        List<Structure> tempReturnStructures = new ArrayList<>();

        tempStructures.add(generateStructure("Recipe_CyberWagon"));
        tempStructures.add(generateStructure("Recipe_Explorer"));
        tempStructures.add(generateStructure("Recipe_FactoryCart"));
        tempStructures.add(generateStructure("Recipe_GoldenCart"));
        tempStructures.add(generateStructure("Recipe_Tractor"));
        tempStructures.add(generateStructure("Recipe_Truck"));
        tempReturnStructures.addAll(addPrefixStruc(tempStructures, "Vehicle/"));
        tempStructures.clear();

        
        tempStructures.add(generateStructure("Recipe_FreightWagon"));
        tempStructures.add(generateStructure("Recipe_Locomotive"));
        tempReturnStructures.addAll(addPrefixStruc(tempStructures, "Vehicle/Train/"));
        tempStructures.clear();

        return tempReturnStructures;
    }

    // Generators and prefixers

    private static Structure generateStructure(String recipe) {
        return new Structure(recipe.replace("Recipe", "Desc"), false, false, recipe, false);
    }

    private static Structure generateStructure(String recipe, List<String> extraChecks) {
        return new Structure(recipe.replace("Recipe", "Desc"), false, false, recipe, extraChecks);
    }

    private static Component generateComponent(String recipe, Boolean liquid) {
        return new Component(recipe.replace("Recipe", "Desc"), recipe, false, false, liquid);
    }

    private static Component generateComponent(String name, String recipe, Boolean liquid) {
        return new Component(name, recipe, false, false, liquid);
    }

    private static Component generateComponent(String recipe, Boolean liquid, List<String> extraChecks) {
        return new Component(recipe.replace("Recipe", "Desc"), recipe, false, false, liquid, 50, extraChecks);
    }

    private static Component generateComponent(String name, String recipe, Boolean liquid, List<String> extraChecks) {
        return new Component(recipe.replace("Recipe", "Desc"), recipe, false, false, liquid, 50, extraChecks);
    }

    private static Component generateComponent(String name, String recipe, int maxstack) {
        return new Component(name, recipe, false, false, false, maxstack, null);
    }

    private static List<Structure> generateMkN(String recipe, int min, int max) {
        List<Structure> tempStructures = new ArrayList<>();
        for (int i = min; i <= max; i++) {
            tempStructures.add(
                    new Structure(recipe.replace("Recipe", "Desc").replace("1", (String.valueOf(i))), false, false,
                            recipe.replace("1", (String.valueOf(i))), false));
        }
        return tempStructures;
    }

    private static List<Component> generateElevator() {
        List<Component> temComps = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            String recipe = "Recipe_SpaceElevatorPart_1";
            int phase;

            if (i == 0) {
                phase = 0;
                temComps.add(
                        new ElevatorPart(recipe.replace("Recipe", "Desc").replace("1", (String.valueOf(i + 1))),
                                recipe.replace("1", (String.valueOf(i + 1))), true, false, false, 50,
                                Arrays.asList("Tutorial_6"), phase));
                continue; // To avoid creating it twice
            } else if (i < 3) {
                phase = 1;
            } else if (i < 5) {
                phase = 2;
            } else if (i < 9) {
                phase = 3;
            } else if (i < 12) {
                phase = 4;
            } else {
                Console.hiddenLog("Couldn't assign requirements for: " + recipe);
                return temComps;
            }

            temComps.add(
                    new ElevatorPart(recipe.replace("Recipe", "Desc").replace("1", (String.valueOf(i + 1))),
                            recipe.replace("1", (String.valueOf(i + 1))), true, false, false, 50, null, phase));

        }
        return temComps;
    }

    private static List<Structure> addPrefixStruc(List<Structure> list, String prefix) {
        List<Structure> prefixedList = new ArrayList<>(list);

        for (int i = 0; i < prefixedList.size(); i++) {
            Structure c = prefixedList.get(i);
            if (c.getRecipePath() != null && !c.getRecipePath().startsWith(prefix)) {
                prefixedList.get(i).setRecipePath(prefix + c.getRecipePath());
            }
        }
        Console.hiddenLog("Prefixed List with " + prefix);
        return prefixedList;

    }

    private static List<Component> addPrefixComp(List<Component> list, String prefix) {
        List<Component> prefixedList = new ArrayList<>(list);

        for (int i = 0; i < prefixedList.size(); i++) {
            Component c = prefixedList.get(i);
            if (c.getRecipePath() != null && !c.getRecipePath().startsWith(prefix)) {
                prefixedList.get(i).setRecipePath(prefix + c.getRecipePath());
            }
        }
        Console.hiddenLog("Prefixed List with " + prefix);
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
        Console.hiddenLog("Prefixed List with " + prefix);
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
        Console.hiddenLog("Prefixed CraftStation List with " + prefixRecipe);
        return prefixedList;
    }

    private static List<EssentialStructure> addPrefixEssStr(List<EssentialStructure> list, String prefixRecipe) {
        List<EssentialStructure> prefixedList = new ArrayList<>(list);

        for (int i = 0; i < prefixedList.size(); i++) {
            EssentialStructure c = prefixedList.get(i);
            if (c.getRecipePath() != null && !c.getRecipePath().startsWith(prefixRecipe)) {
                prefixedList.get(i).setRecipePath(prefixRecipe + c.getRecipePath());
            }
        }
        Console.hiddenLog("Prefixed EssentialStructure List with " + prefixRecipe);
        return prefixedList;
    }

    // Getters and Setters

    public Component getComponentByName(String name) {
        for (Component component : this.components) {
            if (component.getName().equals(name)) {
                return component;
            }
        }
        for (Component eq : equip) {
            if (eq.getName().equals(name)) {
                Console.hiddenLog("Found equipment instead of component: " + name);
                return eq;
            }
        }

        Console.log("Component not found: " + name);
        return null;
    }

    public Component getComponentByPath(String path) {
        for (Component component : this.components) {
            if (component.getPath().equals(path)) {
                return component;
            }
        }
        Console.log("Component not found: " + path);
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
            if (randomizable.getName() == null) {
                return null;
            } else if (randomizable.getName().equals(name)) {
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
        Console.log("Essential Structure not found: " + name);
        return null;
    }

    public Structure getStructureByName(String name) {
        for (Structure structure : this.structures) {
            if (structure.getName().equals(name)) {
                return structure;
            }
        }
        for (EssentialStructure structure : this.essentialStructures) {
            if (structure.getName().equals(name)) {
                Console.hiddenLog("Found Essential Structure instead of Structure: " + name);
                return structure;
            }
        }
        Console.log("Structure not found: " + name);
        return null;
    }

    public List<Structure> getGenerators() {
        List<Structure> result = new ArrayList<>();
        for (Structure structure : this.structures) {
            if (structure.getName().contains("Desc_Generator")) {
                result.add(structure);
            }
        }
        return result;
    }

    public List<CraftStation> getStations() {
        return this.stations;
    }

    public List<Structure> getPoles() {
        List<Structure> result = new ArrayList<>();
        for (Structure structure : this.structures) {
            if (structure.getName().contains("Desc_PowerPole")) {
                result.add(structure);
            }
        }
        return result;
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

    public List<Component> getAllComponents(Boolean liquid) {
        List<Component> result = new ArrayList<>();
        for (Component component : this.components) {
            if (component.isLiquid().equals(liquid)) {
                result.add(component);
            }
        }
        return result;
    }

    public List<Milestone> getAvailableMilestones() {
        List<Milestone> result = new ArrayList<>();
        for (Milestone milestone : this.milestones) {
            if (milestone.isAvailable()) {
                result.add(milestone);
            }
        }
        return result;
    }

    public List<Milestone> getCraftableMilestones() {
        List<Milestone> result = new ArrayList<>();
        for (Milestone milestone : this.milestones) {
            if (milestone.isCraftable()) {
                result.add(milestone);
            }
        }
        return result;
    }

    public List<String> getMilestonesPhase(int phase) {
        List<String> result = new ArrayList<>();
        for (Milestone milestone : this.milestones) {
            if (milestone.getPhase() == phase) {
                result.add(milestone.getName());
            }
        }
        return result;
    }

    public List<Component> getElevatorPhase() {
        List<Component> result = new ArrayList<>();
        for (Component part : this.components) {
            if (part instanceof ElevatorPart) {
                if (((ElevatorPart) part).addWhen() == this.getPhase()) {
                    result.add(part);
                }
            }
        }
        return result;

    }

    public Component getRandomElevatorPhase(int phase, int seed) {
        List<Component> result = new ArrayList<>();
        for (Component part : this.components) {
            if (part instanceof ElevatorPart) {
                if (((ElevatorPart) part).addWhen() == phase) {
                    if (part.isCraftable() == false) {
                        result.add(part);
                    }
                }
            }
        }
        Random random = new Random(seed);
        return result.get(random.nextInt(result.size()));
    }

    public int getPhase() {
        for (int i = 0; i < this.phase.length; i++) {
            if (this.phase[i] > 0) {
                return i;
            }
        }
        return 5;
    }

    public void setPhase() {
        int currentPhase = this.getPhase();
        if (currentPhase == 5) {
            return;
        }
        this.phase[currentPhase]--;
        if (this.phase[currentPhase] <= 0) {
            Console.hiddenLog("Phase " + currentPhase + " finished.");
        }
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
        result.addAll(this.equip);
        result.addAll(this.alternate);
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

    public void setRandomizableAvailable(String name, Boolean available) {

        for (Randomizable r : this.getAllRandomizables()) {
            if (r.getName() == null) {
                continue;
            } else if (r.getName().equals(name)) {
                r.setAvailable(available);
                return;
            }
        }

    }

    public void setRandomizableAvailablePath(String path, Boolean available) {
        for (Randomizable r : this.getAllRandomizables()) {
            if (r.getPath() == null) {
                continue;
            } else if (r.getPath().equals(path)) {
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

    public Boolean setRandomizableCraftable(String name, Boolean craftable) {
        for (Randomizable rand : this.getAvailableButUncraftableRandomizables()) {
            if (rand.getName() == null) {
                continue;
            } else if (rand.getName().equals(name)) {
                rand.setCraftable(craftable);
                return true;
            }
        }
        Console.log("Could not set Craftable, randomizable not found: " + name);
        return false;
    }

    public Boolean setRandomizableCraftablePath(String path, Boolean craftable) {
        for (Randomizable rand : this.getAvailableButUncraftableRandomizables()) {
            if (rand.getPath().equals(path)) {
                rand.setCraftable(craftable);
                return true;
            }
        }
        Console.log("Could not set Craftable, null randomizable not found with path: " + path);
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
                if (milestone.getName().equals("Tutorial_6")) {
                    this.phase[0] = 0;
                } else if (milestone.getExtraCheck().equals(Arrays.asList("Desc_SpaceElevatorPart_1"))) {
                    this.phase[1]--;
                } else if (milestone.getExtraCheck()
                        .equals(Arrays.asList("Desc_SpaceElevatorPart_2", "Desc_SpaceElevatorPart_3"))) {
                    this.phase[2]--;
                } else if (milestone.getExtraCheck()
                        .equals(Arrays.asList("Desc_SpaceElevatorPart_4", "Desc_SpaceElevatorPart_5"))) {
                    this.phase[3]--;
                } else if (milestone.getExtraCheck()
                        .equals(Arrays.asList("Desc_SpaceElevatorPart_6", "Desc_SpaceElevatorPart_7",
                                "Desc_SpaceElevatorPart_8", "Desc_SpaceElevatorPart_9"))) {
                    this.phase[4]--;
                } else {
                    Console.log(milestone.getName()
                            + " did not update phase counter. This should be the case for all tutorials but the last one, but not for the other milestones.");
                }
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
            if (component.isCraftable()) {
                component.refill();
            }
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
            if (r instanceof Component) {
                Component liq = (Component) r;
                if (liq.isLiquid()) {
                    liq.addExtraCheck("pipe");
                    Console.hiddenLog("Adding extra checks for " + r.getName() + " : pipe");
                }
            }
        }

        for (Randomizable r : randomizables) {
            Console.hiddenLog("Adding extra checks for " + r.getName() + " : " + r.getExtraCheck());
            for (String extra : r.getExtraCheck()) {
                if (extra != null) {
                    Console.hiddenLog("Adding extra check for " + r.getName() + " : " + extra);

                    if (extra.equals("power")) {
                        for (Structure gen : getGenerators()) {
                            gen.addCheckAlso("power");
                        }
                    } else if (extra.equals("cable")) {
                        getStructureByName("Desc_PowerLine").addCheckAlso("cable");
                    } else if (extra.equals("pole")) {
                        for (Structure pol : getPoles()) {
                            pol.addCheckAlso("pole");
                        }
                    } else if (extra.equals("pipe")) {
                        getStructureByName("Desc_Pipeline").addCheckAlso("pipe");
                        getStructureByName("Desc_PipelineMK2").addCheckAlso("pipe");
                    } else {
                        Randomizable item = this.getRandomizableByName(extra);
                        if (item instanceof Component) {
                            getComponentByName(extra).addCheckAlso(r.getName());
                        } else if (item instanceof EssentialStructure) {
                            getStructureByName(extra).addCheckAlso(r.getName());
                        } else if (item instanceof CraftStation) {
                            getStationByName(extra).addCheckAlso(r.getName());
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
    }

    public void doExtraChecks(String nameToRemove, List<String> whereToRemove) {

        Console.hiddenLog("nametoremove: " + nameToRemove + " wheretoremove: " + whereToRemove);
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

            if (where.equals("power")) {
                for (Randomizable ran : getAllRandomizables()) {
                    ran.removeExtraCheck("power");
                }
            }
            if (done)
                continue;

            if (where.equals("pipe")) {
                for (Randomizable ran : getAllRandomizables()) {
                    ran.removeExtraCheck("pipe");
                }
            }
            if (done)
                continue;

            if (where.equals("cable")) {
                for (Randomizable ran : getAllRandomizables()) {
                    ran.removeExtraCheck("cable");
                }
            }
            if (done)
                continue;

            if (where.equals("pole")) {
                for (Randomizable ran : getAllRandomizables()) {
                    ran.removeExtraCheck("pole");
                }
            }
            if (done)
                continue;

            Console.log("Could not remove extra check, Randomizable not found: " + where);
        }
    }

    /**
     * Adds fixed unlocks to a milestone to make sure a recipe is available at a
     * certain point
     *
     * @param mile    The name of the milestone to which the fixed unlock should be
     *                added.
     * @param unlocks A list of unlocks to add to the milestone's fixed unlock list.
     */
    public void addFixedUnlocks(String mile, List<String> unlocks) {
        for (Milestone milestone : this.milestones) {
            if (milestone.getName().equals(mile)) {
                for (String unlock : unlocks) {
                    milestone.addFixedUnlock(unlock);
                }
            }
        }

    }

    // Only for debugging
    public void testSetup() {
        Console.test("Generating data for testing...");
        // Does nothing RN
    }

}