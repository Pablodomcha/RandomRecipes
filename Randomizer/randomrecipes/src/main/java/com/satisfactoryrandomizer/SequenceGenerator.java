package com.satisfactoryrandomizer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SequenceGenerator {

    /**
     * Generates the randomized lsit of JSON recipes for Tier 0
     * 
     * @return the randomized lsit of JSON items for Tier 0
     */
    public List<String> generateT0() {
        List<String> list = new ArrayList<>();

        

        return list;
    }

    /**
     * Returns a random element from the given list, or null if the list is empty.
     */
    public String getRandomElement(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        int randomIndex = ThreadLocalRandom.current().nextInt(list.size());
        return list.get(randomIndex);
    }
}
