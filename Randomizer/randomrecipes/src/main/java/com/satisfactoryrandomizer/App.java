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

        Console.init(Ui.getLogArea());
        Ui ui = new Ui();
        ui.getFrame().setVisible(true); // Show the UI

        while (true) {
            try {
                CreateJSON.createDirectories();
                SequenceGenerator.reset();

                while (!ui.getStart()) { // Wait for the UI to close
                    Thread.sleep(100); // Sleep for 100ms to avoid busy-waiting
                }
                ui.saveValues();

                SequenceGenerator.generateSequence();

            } catch (Exception e) {
                Console.importantLog("There was an exception:");
                Console.log(e.getMessage());
                Console.importantLog(
                        "Most of these are rare and happen in a small number of seeds, so try another seed.");
                Console.hiddenLog("Stack Trace:");
                Console.hiddenLog(getStackTrace(e));
            }

            // Tests.test();

            Console.saveLogs();

            ui.randomizationDone();
        }

    }

    private static String getStackTrace(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
