package com.satisfactoryrandomizer;

import java.io.FileWriter;
import java.io.IOException;

import com.satisfactoryrandomizer.Storage.UiValues;

public class Console {

    static String log = "";
    static String cheatsheet = "";
    static Boolean testPrinted = false;

    public static void hiddenLog(String message) {
        String temp = message;
        Console.log += temp + "\n";
    }

    public static void log(String message) {
        String temp = message;
        System.out.println(temp);
        Console.log += temp + "\n";
    }

    public static void advLog( String message){
        if(UiValues.getAdvLog()){
            Console.hiddenLog(message);
        }
    }

    public static void importantLog(String message) {
        log("------------------------------------------------------------------------------------------------------------------------------------------------------");
        log(message);
        log("------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    public static void cheatsheet(String message) {
        Console.cheatsheet += message + "\n";
        Console.log += message + "\n";
    }

    public static void test(String message) {

        String temp = message;
        System.out.println(temp);
        Console.log += temp + "\n";

        if (!Console.testPrinted) {
            String testMessage = "------------------------------------------------------------------------------------------------------------------------------------------------------\n"
                    + "You are running test code. If this message is on release, some test code was left in."
                    + "\n------------------------------------------------------------------------------------------------------------------------------------------------------";
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
        } catch (IOException e) {
            System.err.println("Error writing JSON file: " + e.getMessage());
        }

    }
}
