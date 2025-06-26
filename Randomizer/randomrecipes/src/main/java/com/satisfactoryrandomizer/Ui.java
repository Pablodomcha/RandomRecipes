package com.satisfactoryrandomizer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.satisfactoryrandomizer.Storage.UiValues;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ui {
        private JFrame frame;
        private JComboBox<String> oreLocationComboBox;
        private JComboBox<String> beltsComboBox;
        private JComboBox<String> electricityComboBox;
        private JComboBox<String> wasteComboBox;
        private JComboBox<String> stationBiasComboBox;
        private JComboBox<String> forceLongGameBiasComboBox;
        private JCheckBox advLogCheckBox;
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

        private static JTextArea logArea = new JTextArea(10, 60);

        private int position = 0;
        private int columns = 3;

        private Boolean startRandomization = false;

        public Ui() {
                createUI();
        }

        private void createUI() {
                frame = new JFrame("Satisfactory Recipe Randomizer");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setResizable(false);

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

                // Dropdown Fields

                // Ore Location
                JPanel panelOre = new JPanel();
                panelOre.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Ore location:");
                label.setToolTipText("Select the location of the ore");
                panelOre.add(label);
                String[] oreLocations = { "Located", "Unlocated", "Radiation Party" };
                oreLocationComboBox = new JComboBox<>(oreLocations);
                panelOre.add(oreLocationComboBox);
                panel.add(panelOre, getBagColumn());

                // Belts
                JPanel panelBelts = new JPanel();
                panelBelts.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Belts:");
                label.setToolTipText("Select the belt");
                panelBelts.add(label);
                String[] beltOptions = { "Easy", "Medium", "Hard", "Mr Transport Wagon" };
                beltsComboBox = new JComboBox<>(beltOptions);
                panelBelts.add(beltsComboBox);
                panel.add(panelBelts, getBagColumn());

                // Electricity
                JPanel panelElectricity = new JPanel();
                panelElectricity.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Electricity:");
                label.setToolTipText("Select the electricity");
                panelElectricity.add(label);
                String[] electricityOptions = { "Easy", "Medium", "Hub Burners Carry", "Happy Handcrafting" };
                electricityComboBox = new JComboBox<>(electricityOptions);
                panelElectricity.add(electricityComboBox);
                panel.add(panelElectricity, getBagColumn());

                // Waste
                JPanel panelWaste = new JPanel();
                panelWaste.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Waste:");
                label.setToolTipText("Select the waste");
                panelWaste.add(label);
                String[] wasteOptions = { "Easy", "Medium", "Hard", "No U" };
                wasteComboBox = new JComboBox<>(wasteOptions);
                panelWaste.add(wasteComboBox);
                panel.add(panelWaste, getBagColumn());

                // Station Bias
                JPanel panelStationBias = new JPanel();
                panelStationBias.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Station bias:");
                label.setToolTipText("Select the station bias");
                panelStationBias.add(label);
                String[] stationBiasOptions = { "None", "Slight bias", "Medium Bias", "Heavy Bias" };
                stationBiasComboBox = new JComboBox<>(stationBiasOptions);
                panelStationBias.add(stationBiasComboBox);
                panel.add(panelStationBias, getBagColumn());

                // Force Long Game Bias
                JPanel panelForceLongGameBias = new JPanel();
                panelForceLongGameBias.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Force long game bias:");
                label.setToolTipText("Select the force long game bias");
                panelForceLongGameBias.add(label);
                String[] forceLongGameBiasOptions = { "None", "Slight", "Medium", "Heavy" };
                forceLongGameBiasComboBox = new JComboBox<>(forceLongGameBiasOptions);
                panelForceLongGameBias.add(forceLongGameBiasComboBox);
                panel.add(panelForceLongGameBias, getBagColumn());

                while (position < columns) {
                        panel.add(new JPanel(), getBagColumn());
                }

                for (int i = 0; i < columns; i++) {
                        GridBagConstraints gbcLocal = getBagColumn();
                        gbcLocal.insets = new Insets(0, 0, 10, 0);
                        gbcLocal.anchor = GridBagConstraints.CENTER;
                        panel.add(new JLabel("___________________________________"), gbcLocal);
                }

                // Numeric Value Fields

                // Max Stack Craft
                JPanel panelMaxStackCraft = new JPanel();
                panelMaxStackCraft.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Max amount of each material for crafting:");
                label.setToolTipText("");
                panelMaxStackCraft.add(label);
                maxStackCraftSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 50, 1));
                panelMaxStackCraft.add(maxStackCraftSpinner);
                panel.add(panelMaxStackCraft, getBagColumn());

                // Max Prod Craft
                JPanel panelMaxProdCraft = new JPanel();
                panelMaxProdCraft.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Max amount produced per craft:");
                label.setToolTipText("");
                panelMaxProdCraft.add(label);
                maxProdCraftSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 50, 1));
                panelMaxProdCraft.add(maxProdCraftSpinner);
                panel.add(panelMaxProdCraft, getBagColumn());

                // Max Stack Struct
                JPanel panelMaxStackStruct = new JPanel();
                panelMaxStackStruct.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Max amount of each material for building:");
                label.setToolTipText("");
                panelMaxStackStruct.add(label);
                maxStackStructSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 1000, 1));
                panelMaxStackStruct.add(maxStackStructSpinner);
                panel.add(panelMaxStackStruct, getBagColumn());

                // Max Item Struct
                JPanel panelMaxItemStruct = new JPanel();
                panelMaxItemStruct.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Max number of different items for building:");
                label.setToolTipText("");
                panelMaxItemStruct.add(label);
                maxItemStructSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 20, 1));
                panelMaxItemStruct.add(maxItemStructSpinner);
                panel.add(panelMaxItemStruct, getBagColumn());

                // Max Stack Mile
                JPanel panelMaxStackMile = new JPanel();
                panelMaxStackMile.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Max amount of each material for milestones:");
                label.setToolTipText("");
                panelMaxStackMile.add(label);
                maxStackMileSpinner = new JSpinner(new SpinnerNumberModel(500, 1, 100000, 1));
                panelMaxStackMile.add(maxStackMileSpinner);
                panel.add(panelMaxStackMile, getBagColumn());

                // Max Item Mile
                JPanel panelMaxItemMile = new JPanel();
                panelMaxItemMile.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Max number of different items for milestones:");
                label.setToolTipText("");
                panelMaxItemMile.add(label);
                maxItemMileSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 20, 1));
                panelMaxItemMile.add(maxItemMileSpinner);
                panel.add(panelMaxItemMile, getBagColumn());

                // Max Time Mile
                JPanel panelMaxTimeMile = new JPanel();
                panelMaxTimeMile.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Max time needed for milestones:");
                label.setToolTipText("");
                panelMaxTimeMile.add(label);
                maxTimeMileSpinner = new JSpinner(new SpinnerNumberModel(1000, 1, 100000, 1));
                panelMaxTimeMile.add(maxTimeMileSpinner);
                panel.add(panelMaxTimeMile, getBagColumn());

                // Max Time Craft
                JPanel panelMaxTimeCraft = new JPanel();
                panelMaxTimeCraft.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Max time needed for crafting:");
                label.setToolTipText("");
                panelMaxTimeCraft.add(label);
                maxTimeCraftSpinner = new JSpinner(new SpinnerNumberModel(60, 1, 1000, 1));
                panelMaxTimeCraft.add(maxTimeCraftSpinner);
                panel.add(panelMaxTimeCraft, getBagColumn());

                // Min Handcraft Speed
                JPanel panelHandcraftSpeed = new JPanel();
                panelHandcraftSpeed.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Handcrafting speed:");
                label.setToolTipText("");
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

                label = new JLabel("Number of different items for crafting bias:");
                label.setToolTipText("");
                panelInputBias.add(label);
                inputBiasSpinner = new JSpinner(new SpinnerNumberModel(50, 0, 100, 1));
                panelInputBias.add(inputBiasSpinner);
                panel.add(panelInputBias, getBagColumn());

                // Max Recipes Used
                JPanel panelMaxRecipesUsed = new JPanel();
                panelMaxRecipesUsed.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Max number of different recipes to use each material:");
                label.setToolTipText("");
                panelMaxRecipesUsed.add(label);
                maxRecipesUsedSpinner = new JSpinner(new SpinnerNumberModel(20, 1, 10000, 1));
                panelMaxRecipesUsed.add(maxRecipesUsedSpinner);
                panel.add(panelMaxRecipesUsed, getBagColumn());

                // Free Chance
                JPanel panelFreeChance = new JPanel();
                panelFreeChance.setLayout(new FlowLayout(FlowLayout.LEFT));

                label = new JLabel("Free chance:");
                label.setToolTipText("");
                panelFreeChance.add(label);
                freeChanceSpinner = new JSpinner(new SpinnerNumberModel(10, 0, 100, 1));
                panelFreeChance.add(freeChanceSpinner);
                panel.add(panelFreeChance, getBagColumn());

                // Checkboxes

                JPanel panelAdvLog = new JPanel();
                panelAdvLog.setLayout(new FlowLayout(FlowLayout.LEFT));
                label = new JLabel("Advanced logging:");
                label.setToolTipText("");
                panelAdvLog.add(label);
                advLogCheckBox = new JCheckBox();
                panelAdvLog.add(advLogCheckBox);
                panel.add(panelAdvLog, gbcFull);

                // Add buttons
                JPanel buttonPanel = new JPanel();
                JButton saveButton = new JButton("Randomize");
                saveButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                startRandomization = true;
                        }
                });
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
                UiValues.setSeed(seedField.getText().isEmpty() ? 0 : Long.parseLong(seedField.getText()));
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

                UiValues.setAdvLog(advLogCheckBox.isSelected());

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

}