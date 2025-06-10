package com.satisfactoryrandomizer;

/**
 * This only actually runs the UI and SequenceGenerator classes.
 */
public final class App {
    private App() {
    }

    public static void main(String[] args) {

        CreateJSON.createDirectories();

        SequenceGenerator.generateSequence();

        //Tests.test();

        Console.saveLogs();
    }
}
