package com.satisfactoryrandomizer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.satisfactoryrandomizer.Storage.UiValues;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

public class Ui {
        private JFrame frame;
        private JComboBox<String> oreLocationComboBox;
        private JComboBox<String> beltsComboBox;
        private JComboBox<String> electricityComboBox;
        private JComboBox<String> wasteComboBox;
        private JComboBox<String> stationBiasComboBox;
        private JComboBox<String> forceLongGameBiasComboBox;
        private JCheckBox advLogCheckBox;
        private JCheckBox startWithMiner;
        private JCheckBox progressiveBias;
        private JCheckBox capProdIn;
        private JCheckBox capProdOut;
        private JTextField seedField;
        private JSpinner maxStackCraftSpinner;
        private JSpinner maxProdCraftSpinner;
        private JSpinner maxStackStructSpinner;
        private JSpinner maxItemStructSpinner;
        private JSpinner maxStackMileSpinner;
        private JSpinner maxItemMileSpinner;
        private JSpinner maxTimeMileSpinner;
        private JSpinner maxTimeCraftSpinner;
        private JSpinner handcraftSpeedSpinnerMin;
        private JSpinner handcraftSpeedSpinnerMax;
        private JSpinner inputBiasSpinner;
        private JSpinner maxRecipesUsedSpinner;
        private JSpinner freeChanceSpinner;

        private static JTextArea logArea = new JTextArea(15, 60);

        private int position = 0;
        private int columns = 2;

        private Boolean startRandomization = false;

        Preferences prefs = Preferences.userNodeForPackage(getClass());
        private Map<String, JComponent> fields = new HashMap<>(); // Changed to JComponent

        public Ui() {
                createUI();
        }

        private void createUI() {
                frame = new JFrame("Satisfactory Recipe Randomizer");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setResizable(false);

                // Tooltip management
                ToolTipManager.sharedInstance().setInitialDelay(0); // show tooltip immediately
                ToolTipManager.sharedInstance().setReshowDelay(0); // show tooltip again immediately if mouse moves away
                                                                   // and comes back
                ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE); // don't dismiss tooltip while mouse
                                                                                    // is hovering

                JPanel panel = new JPanel();
                panel.setLayout(new GridBagLayout());

                GridBagConstraints gbcFull = new GridBagConstraints();
                gbcFull.gridx = 0;
                gbcFull.gridwidth = columns;
                gbcFull.anchor = GridBagConstraints.WEST;

                GridBagConstraints gbcFullCentered = new GridBagConstraints();
                gbcFullCentered.gridx = 0;
                gbcFullCentered.gridwidth = columns;
                gbcFullCentered.anchor = GridBagConstraints.CENTER;
                gbcFullCentered.insets = new Insets(10, 10, 10, 10);

                GridBagConstraints gbcFullRight = new GridBagConstraints();
                gbcFullRight.gridx = 0;
                gbcFullRight.gridwidth = columns;
                gbcFullRight.anchor = GridBagConstraints.EAST;

                // Seed Field

                JPanel panelSeed = new JPanel();
                panelSeed.setLayout(new FlowLayout(FlowLayout.LEFT));

                JLabel label = new JLabel("Seed:");
                label.setToolTipText("");
                panelSeed.add(label);
                seedField = new JTextField(30);
                panelSeed.add(seedField);

                // Random Seed Button
                JButton randomSeedButton = new JButton("Generate");
                randomSeedButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                long randomSeed = (long) (Math.random() * Long.MAX_VALUE);
                                seedField.setText(String.valueOf(randomSeed));
                        }
                });
                panelSeed.add(randomSeedButton);

                panel.add(panelSeed, gbcFull);

                JPanel panelWarning = new JPanel();
                panelWarning.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel(
                                "<html><p>Hover over fields for descriptions.</p>"
                                                + "<p>Settings that could render a seed impossible to complete are marked with \"<font color='red'>*</font>\"</html>");
                label.setToolTipText(
                                "This is an example tooltip");
                panelWarning.add(label);
                panel.add(panelWarning, getBagColumn());

                panel = nextSection(panel);

                // Dropdown Fields

                // Ore Location
                JPanel panelOre = new JPanel();
                panelOre.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Ore location:");
                label.setToolTipText(
                                "<html><p>Located: You don't need ores you can't scan to unlock the ability to scan them. Uranium will only be needed for progression if you can have radiation protection. A lot of things will come from iron (for better or worse).</p>"
                                                +
                                                "<p>Unlocated: Any ore can be part of any recipe. Uranium will only be needed for progression if you can have radiation protection.</p>"
                                                +
                                                "<p>Radiation Party: Uranium could be needed for progression before having radiation protection.</p></html>");
                panelOre.add(label);
                String[] oreLocations = { "Located", "Unlocated", "Radiation Party" };
                oreLocationComboBox = new JComboBox<>(oreLocations);
                panelOre.add(oreLocationComboBox);
                panel.add(panelOre, getBagColumn());

                // Belts
                JPanel panelBelts = new JPanel();
                panelBelts.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Belts:");
                label.setToolTipText(
                                "<html><p>Easy: Tier 1 belts, splitters and mergers use small amounts of early materials and are unlocked in the tutorial.</p>"
                                                + "<p>Medium: Tier 1 belts use small amounts of early materials and are unlocked in the tutorial. Other belts and poles can use anything.</p>"
                                                + "<p>Hard: Guarantees access to tier 1 belts in the tutorial. Their recipes may be expensive.</p>"
                                                + "<p>Mr Transport Wagon: Completely random, you could end up with no access to belts for part/most of your playthrough.</p></html>");
                panelBelts.add(label);
                String[] beltOptions = { "Easy", "Medium", "Hard", "Mr Transport Wagon" };
                beltsComboBox = new JComboBox<>(beltOptions);
                panelBelts.add(beltsComboBox);
                panel.add(panelBelts, getBagColumn());

                // Electricity
                JPanel panelElectricity = new JPanel();
                panelElectricity.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Electricity:");
                label.setToolTipText(
                                "<html><p>Easy: Basic electric poles, powerlines and biomass burners use early materials only and are unlocked in the tutorial. Basic electric poles and powerlines also use small amounts of them.</p>"
                                                + "<p>Medium: Basic electric poles, powerlines and biomass burners use early materials only and are unlocked in the tutorial. They may be expensive.</p>"
                                                + "<p>Hub Burners Carry: Basic electric poles and powerlines are unlocked early. They may be expensive. You're not guaranteed a powersource.</p>"
                                                + "<p>Happy Handcrafting: Completely random.</p></html>");
                panelElectricity.add(label);
                String[] electricityOptions = { "Easy", "Medium", "Hub Burners Carry", "Happy Handcrafting" };
                electricityComboBox = new JComboBox<>(electricityOptions);
                panelElectricity.add(electricityComboBox);
                panel.add(panelElectricity, getBagColumn());

                // Waste
                JPanel panelWaste = new JPanel();
                panelWaste.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Waste:");
                label.setToolTipText(
                                "<html><p>Easy: Recipes only have 1 output, so you don't need to handle a second resource you may not need.</p>"
                                                + "<p>Medium: Recipes can have solid outputs other than the desired, but the Awesome sink recipe will be available early and will have a simple recipe.</p>"
                                                + "<p>Hard: Recipes can have solid outputs other than the desired and the Awesome sink may not be available or have really hard crafting recipe.</p>"
                                                + "<p>Liquids too: Recipes can have solid/liquid outputs other than the desired and the sink may not be available or have really hard crafting recipe. You may need many deposits (if they're even available) and manual flushing.</p>"
                                                + "<p>Double party: Always get an extra output for muliple output stations once you can use liquids.</p>"
                                                + " <p> (The \"desired\" output is the one for which the recipe is generated, even if you like more the other output)</p></html>");
                panelWaste.add(label);
                String[] wasteOptions = { "Easy", "Medium", "Hard", "Liquids too", "Double party" };
                wasteComboBox = new JComboBox<>(wasteOptions);
                panelWaste.add(wasteComboBox);
                panel.add(panelWaste, getBagColumn());

                // Station Bias
                JPanel panelStationBias = new JPanel();
                panelStationBias.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Station bias:");
                label.setToolTipText(
                                "<html><p>None: The stations are selected completely random, this makes the earlier unlocked stations have way more recipes than the later ones.</p>"
                                                + "<p>Slight bias: Increases the chance of using stations unlocked later for later recipes, increaseing (hopefully) station variability leaning slightly on newer unlocked stations.</p>"
                                                + "<p>Medium Bias: Increases the chance of using stations unlocked later for later recipes, increaseing (hopefully) station variability leaning on newer unlocked stations.</p>"
                                                + "<p>Heavy Bias: Increases the chance of using stations unlocked later for later recipes, increaseing (hopefully) station variability leaning heavily on newer unlocked stations.</p></html>");
                panelStationBias.add(label);
                String[] stationBiasOptions = { "None", "Slight bias", "Medium Bias", "Heavy Bias" };
                stationBiasComboBox = new JComboBox<>(stationBiasOptions);
                panelStationBias.add(stationBiasComboBox);
                panel.add(panelStationBias, getBagColumn());

                // Force Long Game Bias
                JPanel panelForceLongGameBias = new JPanel();
                panelForceLongGameBias.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Force long game bias:");
                label.setToolTipText(
                                "<html><p>None: Milestones are randomized entirely.</p>"
                                                + "<p>Slight: Tries to make the game longer by trying to put milestones later in the logic.</p>"
                                                + "<p>Medium: Tries to make the game longer by trying (harder than slight) to put milestones later in the logic.</p>"
                                                + "<p>Heavy: Tries to make the game longer by trying (harder than medium) to put milestones later in the logic.</p></html>");
                panelForceLongGameBias.add(label);
                String[] forceLongGameBiasOptions = { "None", "Slight", "Medium", "Heavy" };
                forceLongGameBiasComboBox = new JComboBox<>(forceLongGameBiasOptions);
                panelForceLongGameBias.add(forceLongGameBiasComboBox);
                panel.add(panelForceLongGameBias, getBagColumn());

                panel = nextSection(panel);

                // Numeric Value Fields

                // Max Stack Craft
                JPanel panelMaxStackCraft = new JPanel();
                panelMaxStackCraft.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Max amount of each material for crafting:");
                label.setToolTipText(
                                "Maximum number of each component needed for a craft. Each material max will cap at it's stack size.");
                panelMaxStackCraft.add(label);
                maxStackCraftSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 500, 1));
                panelMaxStackCraft.add(maxStackCraftSpinner);
                panel.add(panelMaxStackCraft, getBagColumn());

                // Max Prod Craft
                JPanel panelMaxProdCraft = new JPanel();
                panelMaxProdCraft.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Max amount produced per craft:");
                label.setToolTipText(
                                "Maximum number of each component produced by a craft.  Each material max will cap at it's stack size.");
                panelMaxProdCraft.add(label);
                maxProdCraftSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 500, 1));
                panelMaxProdCraft.add(maxProdCraftSpinner);
                panel.add(panelMaxProdCraft, getBagColumn());

                // Max Stack Struct
                JPanel panelMaxStackStruct = new JPanel();
                panelMaxStackStruct.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("<html>Max amount of each material for building<font color='red'>*</font>:</html>");
                label.setToolTipText(
                                "<html>Maximum number of each component needed to build with the builder tool. Big values will need a lot of inventory slots to build (<font color='red'>this could render the seed uncompletable</font>).</html>");
                panelMaxStackStruct.add(label);
                maxStackStructSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 1000, 1));
                panelMaxStackStruct.add(maxStackStructSpinner);
                panel.add(panelMaxStackStruct, getBagColumn());

                // Max Item Struct
                JPanel panelMaxItemStruct = new JPanel();
                panelMaxItemStruct.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel(
                                "<html>Max number of different items for building<font color='red'>*</font>:</html>");
                label.setToolTipText(
                                "<html>Maximum number of different items to build with the builder tool. Big values will need a lot of inventory slots to build (<font color='red'>this could render the seed uncompletable</font>).</html>");
                panelMaxItemStruct.add(label);
                maxItemStructSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 20, 1));
                panelMaxItemStruct.add(maxItemStructSpinner);
                panel.add(panelMaxItemStruct, getBagColumn());

                // Max Stack Mile
                JPanel panelMaxStackMile = new JPanel();
                panelMaxStackMile.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel(
                                "<html>Max amount of each material for milestones<font color='red'>*</font>:</html>");
                label.setToolTipText(
                                "<html><p>Maximum number of each component needed to complete a milestone. Accepts numbers in the thousands at least. Not sure how high it has to be to break.<p>Affects MAM milestones which may be mandatory.</p><p><font color='red'>A value too high may render the seed uncompletable.</font></p></html>");
                panelMaxStackMile.add(label);
                maxStackMileSpinner = new JSpinner(new SpinnerNumberModel(500, 1, 100000, 1));
                panelMaxStackMile.add(maxStackMileSpinner);
                panel.add(panelMaxStackMile, getBagColumn());

                // Max Item Mile
                JPanel panelMaxItemMile = new JPanel();
                panelMaxItemMile.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel(
                                "<html>Max number of different items for milestones<font color='red'>*</font>:</html>");
                label.setToolTipText(
                                "<html><p>Maximum number of different items to complete a milestone.<p>Affects MAM milestones which may be mandatory.</p><p><font color='red'>A value too high may render the seed uncompletable.</font></p></html>");
                panelMaxItemMile.add(label);
                maxItemMileSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 20, 1));
                panelMaxItemMile.add(maxItemMileSpinner);
                panel.add(panelMaxItemMile, getBagColumn());

                // Max Time Mile
                JPanel panelMaxTimeMile = new JPanel();
                panelMaxTimeMile.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Max time needed for milestones:");
                label.setToolTipText(
                                "Maximum time to research a new milestone after completing one or time to complete a MAM research in seconds.");
                panelMaxTimeMile.add(label);
                maxTimeMileSpinner = new JSpinner(new SpinnerNumberModel(1000, 1, 100000, 1));
                panelMaxTimeMile.add(maxTimeMileSpinner);
                panel.add(panelMaxTimeMile, getBagColumn());

                // Max Time Craft
                JPanel panelMaxTimeCraft = new JPanel();
                panelMaxTimeCraft.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Max time needed for crafting:");
                label.setToolTipText("Maximum time needed for a craft in seconds. If you put 1000, don't blame me.");
                panelMaxTimeCraft.add(label);
                maxTimeCraftSpinner = new JSpinner(new SpinnerNumberModel(60, 1, 1000, 1));
                panelMaxTimeCraft.add(maxTimeCraftSpinner);
                panel.add(panelMaxTimeCraft, getBagColumn());

                // Min Handcraft Speed
                JPanel panelHandcraftSpeed = new JPanel();
                panelHandcraftSpeed.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Handcrafting speed:");
                label.setToolTipText(
                                "Multiplier for handcrafting speed. Lower is faster (the base game value is usually between 0.5 and 1 depending on recipe).");
                panelHandcraftSpeed.add(label);
                handcraftSpeedSpinnerMin = new JSpinner(new SpinnerNumberModel(0.5, 0.1, 100, 0.1));
                panelHandcraftSpeed.add(handcraftSpeedSpinnerMin);

                label = new JLabel("-");
                label.setToolTipText("");
                panelHandcraftSpeed.add(label);
                handcraftSpeedSpinnerMax = new JSpinner(new SpinnerNumberModel(1, 0.1, 50, 0.1));
                panelHandcraftSpeed.add(handcraftSpeedSpinnerMax);

                // Ensure min is lower than max
                ChangeListener listener = new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                                double min = (double) handcraftSpeedSpinnerMin.getValue();
                                double max = (double) handcraftSpeedSpinnerMax.getValue();
                                if (min >= max) {
                                        if (e.getSource() == handcraftSpeedSpinnerMin) {
                                                handcraftSpeedSpinnerMax.setValue(min);
                                        } else {
                                                handcraftSpeedSpinnerMin.setValue(max);
                                        }
                                }
                        }
                };
                handcraftSpeedSpinnerMin.addChangeListener(listener);
                handcraftSpeedSpinnerMax.addChangeListener(listener);

                panel.add(panelHandcraftSpeed, getBagColumn());

                // Input Bias
                JPanel panelInputBias = new JPanel();
                panelInputBias.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Number of different items for crafting bias(%):");
                label.setToolTipText(
                                "Higher numbers mean that the randomizer will try to use more different ingredients per recipe/milestone. put -1 to disable.");
                panelInputBias.add(label);
                inputBiasSpinner = new JSpinner(new SpinnerNumberModel(50, -1, 100, 1));
                panelInputBias.add(inputBiasSpinner);
                panel.add(panelInputBias, getBagColumn());

                // Max Recipes Used
                JPanel panelMaxRecipesUsed = new JPanel();
                panelMaxRecipesUsed.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Max number of different recipes to use each material:");
                label.setToolTipText(
                                "<html><div>Maximum number of recipes that can use a material. The randomizer will increase this value if it can't complete a randomization.</div><div>Lower values increase component diversity.</div><div>Alternate recipes ignore this.</div></html>");
                panelMaxRecipesUsed.add(label);
                maxRecipesUsedSpinner = new JSpinner(new SpinnerNumberModel(20, 1, 10000, 1));
                panelMaxRecipesUsed.add(maxRecipesUsedSpinner);
                panel.add(panelMaxRecipesUsed, getBagColumn());

                // Free Chance
                JPanel panelFreeChance = new JPanel();
                panelFreeChance.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Free chance:");
                label.setToolTipText(
                                "Chance for a randomizable to be free, like Excited Photonic Matter, except that one may no longer be free. This affects Structures and milestones too.");
                panelFreeChance.add(label);
                freeChanceSpinner = new JSpinner(new SpinnerNumberModel(10, 0, 100, 1));
                panelFreeChance.add(freeChanceSpinner);
                panel.add(panelFreeChance, getBagColumn());

                // Checkboxes

                // Starting Miner
                JPanel panelStartMiner = new JPanel();
                panelStartMiner.setLayout(new FlowLayout(FlowLayout.LEFT));
                label = new JLabel("Starting Miner:");
                label.setToolTipText(
                                "Mining by hand tends to be really boring early game. This adds the portable miner as starting recipe.");
                panelStartMiner.add(label);
                startWithMiner = new JCheckBox();
                startWithMiner.setSelected(true);
                panelStartMiner.add(startWithMiner);
                panel.add(panelStartMiner, getBagColumn());

                // Progressive Bias
                JPanel panelProgressiveBias = new JPanel();
                panelProgressiveBias.setLayout(new FlowLayout(FlowLayout.LEFT));
                label = new JLabel("Progressive bias:");
                label.setToolTipText(
                                "Makes the amount of materials used for milestones increase progressively, making it so that early milestones tend to take less materials and later ones to take more.");
                panelProgressiveBias.add(label);
                progressiveBias = new JCheckBox();
                progressiveBias.setSelected(true);
                panelProgressiveBias.add(progressiveBias);
                panel.add(panelProgressiveBias, getBagColumn());

                // Cap productivity In
                JPanel panelCapProdIn = new JPanel();
                panelCapProdIn.setLayout(new FlowLayout(FlowLayout.LEFT));
                label = new JLabel("Cap Input for productivity:");
                label.setToolTipText(
                                "<html><div>Caps the input for crafting recipes at half the maximum stack, allowing productivity to reach 100% (needs both caps to reach 100%)</div>"
                                                + "<div>Production speed decreases greatly with this disabled, as resuming production after input reaches needed amount is not instant and the time is not negligible sometimes.</div></html>");
                panelCapProdIn.add(label);
                capProdIn = new JCheckBox();
                capProdIn.setSelected(true);
                panelCapProdIn.add(capProdIn);
                panel.add(panelCapProdIn, getBagColumn());

                // Cap productivity Out
                JPanel panelCapProdOut = new JPanel();
                panelCapProdOut.setLayout(new FlowLayout(FlowLayout.LEFT));
                label = new JLabel("Cap Output for productivity:");
                label.setToolTipText(
                                "<html><div>Caps the output for crafting recipes at half the maximum stack, allowing productivity to reach 100% (needs both caps to reach 100%)</div>"
                                                + "<div>Unlike capping the input, while this does decrease productivity, it doesn't affect amount produced, as the time lost is lost because the produced amount per craft is too large.</div></html>");
                panelCapProdOut.add(label);
                capProdOut = new JCheckBox();
                capProdOut.setSelected(true);
                panelCapProdOut.add(capProdOut);
                panel.add(panelCapProdOut, getBagColumn());

                // Advanced Logging
                JPanel panelAdvLog = new JPanel();
                panelAdvLog.setLayout(new FlowLayout(FlowLayout.LEFT));
                label = new JLabel("Advanced logging:");
                label.setToolTipText(
                                "Logs many things helpful for troubleshooting, but is way slower. Use only for debugging.");
                panelAdvLog.add(label);
                advLogCheckBox = new JCheckBox();
                panelAdvLog.add(advLogCheckBox);
                panel.add(panelAdvLog, getBagColumn());

                // Add buttons
                JPanel buttonPanel = new JPanel();

                JButton saveButton = new JButton("Randomize");
                saveButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                startRandomization = true;
                        }
                });
                saveButton.setToolTipText(
                                "<html><p>Generate the randomization.</p> <p>You know? That which this thing is all about to begin with.</p></html>");

                JButton loadLastButton = new JButton("Load Preferences");
                buttonPanel.add(saveButton);
                panel.add(buttonPanel, gbcFullRight);
                loadLastButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                int result = JOptionPane.showConfirmDialog(frame,
                                                "<html>Are you sure you want to <b>LOAD</b> your saved settings?</html>",
                                                "Confirm", JOptionPane.YES_NO_OPTION);
                                if (result == JOptionPane.YES_OPTION) {
                                        loadPreferences();
                                }
                        }
                });
                loadLastButton.setToolTipText("Load saved settings.");

                JButton saveSettingsButton = new JButton("Save Preferences");
                buttonPanel.add(saveButton);
                panel.add(buttonPanel, gbcFullRight);
                saveSettingsButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                int result = JOptionPane.showConfirmDialog(frame,
                                                "<html>Are you sure you want to <b>SAVE</b> these settings?</html>",
                                                "Confirm", JOptionPane.YES_NO_OPTION);
                                if (result == JOptionPane.YES_OPTION) {
                                        savePreferences();
                                }
                        }
                });
                saveSettingsButton.setToolTipText("Save these settings.");

                buttonPanel.add(saveSettingsButton);
                buttonPanel.add(loadLastButton);
                buttonPanel.add(saveButton);
                panel.add(buttonPanel, gbcFullRight);

                // Add log
                JPanel panelLog = new JPanel();
                panelLog.setLayout(new BoxLayout(panelLog, BoxLayout.Y_AXIS));

                JPanel labelPanel = new JPanel();
                labelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                JLabel labelLog = new JLabel("Log:");
                labelPanel.add(labelLog);
                panelLog.add(labelPanel);

                logArea.setEditable(false);
                logArea.setBackground(frame.getBackground());
                logArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                JScrollPane logScrollPane = new JScrollPane(logArea);
                panelLog.add(logScrollPane);
                panel.add(panelLog, gbcFullCentered);

                // Add the panel to the frame and generate the size of the frame
                frame.add(panel);
                frame.pack();

                // Populate fields for storing values - Store JComponent references
                fields.put("seedField", seedField);
                fields.put("oreLocationComboBox", oreLocationComboBox);
                fields.put("beltsComboBox", beltsComboBox);
                fields.put("electricityComboBox", electricityComboBox);
                fields.put("wasteComboBox", wasteComboBox);
                fields.put("stationBiasComboBox", stationBiasComboBox);
                fields.put("forceLongGameBiasComboBox", forceLongGameBiasComboBox);
                fields.put("startWithMiner", startWithMiner);
                fields.put("progressiveBias", progressiveBias);
                fields.put("advLogCheckBox", advLogCheckBox);
                fields.put("capProdOut", capProdOut);
                fields.put("capProdIn", capProdIn);
                fields.put("maxStackCraftSpinner", maxStackCraftSpinner);
                fields.put("maxProdCraftSpinner", maxProdCraftSpinner);
                fields.put("maxStackStructSpinner", maxStackStructSpinner);
                fields.put("maxItemStructSpinner", maxItemStructSpinner);
                fields.put("maxStackMileSpinner", maxStackMileSpinner);
                fields.put("maxItemMileSpinner", maxItemMileSpinner);
                fields.put("maxTimeMileSpinner", maxTimeMileSpinner);
                fields.put("maxTimeCraftSpinner", maxTimeCraftSpinner);
                fields.put("handcraftSpeedSpinnerMin", handcraftSpeedSpinnerMin);
                fields.put("handcraftSpeedSpinnerMax", handcraftSpeedSpinnerMax);
                fields.put("inputBiasSpinner", inputBiasSpinner);
                fields.put("maxRecipesUsedSpinner", maxRecipesUsedSpinner);
                fields.put("freeChanceSpinner", freeChanceSpinner);

        }

        public JFrame getFrame() {
                return frame;
        }

        public Boolean getStart() {
                return startRandomization;
        }

        public void randomizationDone() {
                startRandomization = false;
        }

        public static void scroll() {
                logArea.setCaretPosition(logArea.getDocument().getLength());
        }

        public void saveValues() {
                String seedText = seedField.getText().trim();
                UiValues.setSeed(seedText.isEmpty() ? 0 : Long.parseLong(seedText));
                UiValues.setOreLocation(oreLocationComboBox.getSelectedIndex());
                UiValues.setBelts(beltsComboBox.getSelectedIndex());
                UiValues.setElectricity(electricityComboBox.getSelectedIndex());
                UiValues.setWaste(wasteComboBox.getSelectedIndex());
                UiValues.setStationBias(stationBiasComboBox.getSelectedIndex());
                UiValues.setForceLongGameBias(forceLongGameBiasComboBox.getSelectedIndex());

                UiValues.setMaxStackCraft((int) maxStackCraftSpinner.getValue());
                UiValues.setMaxProdCraft((int) maxProdCraftSpinner.getValue());
                UiValues.setMaxStackStruct((int) maxStackStructSpinner.getValue());
                UiValues.setMaxItemStruct((int) maxItemStructSpinner.getValue());
                UiValues.setMaxStackMile((int) maxStackMileSpinner.getValue());
                UiValues.setMaxItemMile((int) maxItemMileSpinner.getValue());
                UiValues.setMaxTimeMile((int) maxTimeMileSpinner.getValue());
                UiValues.setMaxTimeCraft((int) maxTimeCraftSpinner.getValue());
                UiValues.setHandcraftSpeed(new double[] { (double) handcraftSpeedSpinnerMin.getValue(),
                                (double) handcraftSpeedSpinnerMax.getValue() });
                UiValues.setInputBias((int) inputBiasSpinner.getValue());
                UiValues.setMaxRecipesUsed((int) maxRecipesUsedSpinner.getValue());
                UiValues.setFreeChance((int) freeChanceSpinner.getValue());

                UiValues.setStartWithMiner(startWithMiner.isSelected());
                UiValues.setProgressiveBias(progressiveBias.isSelected());
                UiValues.setAdvLog(advLogCheckBox.isSelected());
                UiValues.setProductivity(new Boolean[] { capProdIn.isSelected(), capProdOut.isSelected() });

        }

        public static JTextArea getLogArea() {
                return logArea;
        }

        private GridBagConstraints getBagColumn() {
                GridBagConstraints ans = new GridBagConstraints();
                ans.anchor = GridBagConstraints.WEST;
                ans.gridwidth = 1;
                if (position == columns) {
                        this.position = 0;
                }
                ans.gridx = position++;
                return ans;
        }

        private JPanel nextSection(JPanel panel) {
                JPanel ret = panel;
                while (position < columns) {
                        ret.add(new JPanel(), getBagColumn());
                }

                for (int i = 0; i < columns; i++) {
                        GridBagConstraints gbcLocal = getBagColumn();
                        gbcLocal.insets = new Insets(0, 0, 10, 0);
                        gbcLocal.anchor = GridBagConstraints.CENTER;
                        ret.add(new JLabel(" "), gbcLocal);
                }
                return ret;
        }

        private void loadPreferences() {
                Console.log("Loading preferences..."); //

                // For JTextField, use the default from the component itself if no preference is
                // found
                String seedText = prefs.get("seedField", seedField.getText());
                seedField.setText(seedText);

                // No need to log other individual components here, the loop below handles them
                for (Map.Entry<String, JComponent> entry : fields.entrySet()) { // Iterate JComponent references
                        String key = entry.getKey();
                        JComponent component = entry.getValue();

                        if (component instanceof JTextField) {
                                // For JTextField, retrieve and set text directly. The default text is already
                                // set during UI creation.
                                String text = prefs.get(key, ((JTextField) component).getText());
                                ((JTextField) component).setText(text);
                        } else if (component instanceof JComboBox) {
                                int selectedIndex = prefs.getInt(key, ((JComboBox) component).getSelectedIndex());
                                ((JComboBox) component).setSelectedIndex(selectedIndex);
                        } else if (component instanceof JCheckBox) {
                                boolean isSelected = prefs.getBoolean(key, ((JCheckBox) component).isSelected());
                                ((JCheckBox) component).setSelected(isSelected);
                        } else if (component instanceof JSpinner) {
                                JSpinner spinner = (JSpinner) component;
                                // Determine the type of the spinner model's value
                                Object spinnerValue = spinner.getValue();
                                if (spinnerValue instanceof Integer) {
                                        spinner.setValue(prefs.getInt(key, (int) spinnerValue));
                                } else if (spinnerValue instanceof Double) {
                                        spinner.setValue(prefs.getDouble(key, (double) spinnerValue));
                                }
                                // No need to update the 'fields' map here, it holds the components
                        }
                }
        }

        public void savePreferences() {
                Console.log("Saving preferences..."); //
                // Iterating over 'fields' (which now correctly stores JComponent references)
                for (Map.Entry<String, JComponent> entry : fields.entrySet()) {
                        String key = entry.getKey();
                        JComponent component = entry.getValue();

                        if (component instanceof JTextField) {
                                prefs.put(key, ((JTextField) component).getText());
                        } else if (component instanceof JComboBox) {
                                prefs.putInt(key, ((JComboBox) component).getSelectedIndex());
                        } else if (component instanceof JCheckBox) {
                                prefs.putBoolean(key, ((JCheckBox) component).isSelected());
                        } else if (component instanceof JSpinner) {
                                Object spinnerValue = ((JSpinner) component).getValue();
                                if (spinnerValue instanceof Integer) {
                                        prefs.putInt(key, (int) spinnerValue);
                                } else if (spinnerValue instanceof Double) {
                                        prefs.putDouble(key, (double) spinnerValue);
                                }
                        }
                }
                try {
                        prefs.flush();
                } catch (Exception e) {
                        Console.log("The flush call failed:");
                        e.printStackTrace();
                }
        }

}