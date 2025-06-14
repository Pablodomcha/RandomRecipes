package com.satisfactoryrandomizer;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * This only actually runs the UI and SequenceGenerator classes.
 */
public final class App {
    private App() {
    }

    public static void main(String[] args) {

        CreateJSON.createDirectories();
        try {
            SequenceGenerator.generateSequence();
        } catch (Exception e) {
            Console.importantLog("There was an exception:");
            Console.log(e.getMessage());
            Console.importantLog("Most of these are rare and happen in a small number of seeds, so try another seed.");

            Console.hiddenLog("Stack Trace:");
            Console.hiddenLog(getStackTrace(e));
        }

        // Tests.test();

        Console.saveLogs();
    }

    private static String getStackTrace(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
