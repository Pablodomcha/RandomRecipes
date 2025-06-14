package com.satisfactoryrandomizer.Storage.Randomizables;

import java.util.List;

public class ElevatorPart extends Component {

    private int addWhen = 0; // Elevator Phase (0 = tutorial, 1 = built, 5 = all available)

    // Basic constructor
    public ElevatorPart(String name, String recipepath, Boolean available, Boolean craftable, Boolean liquid, int maxstack,
            List<String> extraCheck, int addWhen) {
        super(name, recipepath, available, craftable, liquid, maxstack, extraCheck);
        this.addWhen = addWhen;
    }

    public int addWhen() {
        return this.addWhen;
    }

    public void setAddWhen(int addWhen) {
        this.addWhen = addWhen;
    }

}
