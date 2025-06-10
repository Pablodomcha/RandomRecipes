package com.satisfactoryrandomizer;

import java.io.FileWriter;
import java.io.IOException;

public class Console {

    static String log = "";
    static String cheatsheet = "";

    public static void log(String message) {
        String temp = message;
        System.out.println(temp);
        Console.log += temp + "\n";
    }

    public static void cheatsheet(String message) {
        Console.cheatsheet += message + "\n";
    }

    public static void test() {
        String testMessage = "You are running test code. If this message is on release, some test code was left in.";
        System.out.println(testMessage);
        Console.log += testMessage + "\n";
    }

    public static void saveLogs() {

        try {
            FileWriter writer = new FileWriter("Randomizer_Log.txt");
            writer.write(Console.log);
            writer.close();
            writer = new FileWriter("Randomizer_Cheatsheet.txt");
            writer.write(Console.cheatsheet);
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing JSON file: " + e.getMessage());
        }

    }
}
