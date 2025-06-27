package com.satisfactoryrandomizer;

import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JTextArea;

import com.satisfactoryrandomizer.Storage.UiValues;

public class Console {

    static String log = "";
    static String cheatsheet = "";
    static Boolean testPrinted = false;
    private static JTextArea logArea;

    public static void init(JTextArea logArea) {
        Console.logArea = logArea;
    }

    public static void hiddenLog(String message) {
        Console.log += message + "\n";
    }

    public static void log(String message) {
        if (message == null) {
            System.err.println("Null message: " + message);
            Console.log += "Null message" + "\n";
        } else {
            System.err.println(message);

            Console.log += message + "\n";
            String[] words = message.split("\\s+");
            StringBuilder line = new StringBuilder();
            for (String word : words) {
                if (line.length() + word.length() + 1 > 100) {
                    logArea.append(line.toString() + "\n");
                    line.setLength(0);
                }
                line.append(word).append(" ");
            }
            logArea.append(line.toString().trim() + "\n");
            Ui.scroll();
        }
    }

    public static void advLog(String message) {
        if (UiValues.getAdvLog()) {
            Console.hiddenLog(message);
        }
    }

    public static void importantLog(String message) {
        log("------------------------------------------------------------------------------------------------------------------------------------------------------");
        log(message
                + " \n------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    public static void cheatsheet(String message) {
        Console.cheatsheet += message + "\n";
        Console.log += message + "\n";
    }

    public static void test(String message) {

        System.err.println(message);
        Console.log += message + "\n";

        if (!Console.testPrinted) {
            String testMessage = "----------------------------------------------------------------------------------------------------------------------------------------------------\n"
                    + "You are running test code. If this message is on release, some test code was left in."
                    + "\n----------------------------------------------------------------------------------------------------------------------------------------------------";
            System.out.println(testMessage);
            Console.log += testMessage + "\n";
            Console.testPrinted = true;
        }
    }

    public static void saveLogs() {

        try {
            FileWriter writer = new FileWriter("Randomizer_Log.txt");
            writer.write(Console.log);
            writer.close();
            writer = new FileWriter("Randomizer_Cheatsheet.txt");
            writer.write(Console.cheatsheet);
            writer.close();

            log = "";
            cheatsheet = "";

        } catch (IOException e) {
            System.err.println("Error writing JSON file: " + e.getMessage());
        }

    }
}
