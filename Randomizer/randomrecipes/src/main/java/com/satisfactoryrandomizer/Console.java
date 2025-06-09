package com.satisfactoryrandomizer;

import java.io.FileWriter;
import java.io.IOException;

public class Console {

    static String log = "";
    static String cheatsheet = "";

    public static void log(String message) {
        String temp = message;
        System.out.println(temp);
        log += temp + "\n";
    }

    public static void cheatsheet(String message) {
        String temp = message;
        System.out.println(temp);
        cheatsheet += temp + "\n";
    }

    public static void saveLogs() {

        try {
            FileWriter writer = new FileWriter("Randomizer_Log.txt");
            writer.write(log);
            writer.close();
            writer = new FileWriter("Randomizer_Cheatsheet.txt");
            writer.write(cheatsheet);
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing JSON file: " + e.getMessage());
        }

    }
}
