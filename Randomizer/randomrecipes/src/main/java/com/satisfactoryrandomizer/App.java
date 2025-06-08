package com.satisfactoryrandomizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import com.satisfactoryrandomizer.Storage.*;

/**
 * This only actually runs the UI and SequenceGenerator classes.
 */
public final class App {
    private App() {
    }

    public static void main(String[] args) {

        //SequenceGenerator.generateSequence(UiValues.getSeed());

        Tests.test();
    }
}
