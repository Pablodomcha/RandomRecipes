# Recipe Randomizer

A simple recipe randomizer created in Java to create the JSON files that ContentLib will introduce into the game.

Why Java? I had VSCode set up to use Java from my last project and it was as good a choice as any for such a simple task.

## How to use:

    1. Install the mod as any other Satisfactory mod.

    2. Go to the mod folder. In Steam, when you browse the game's local files, it's in:
       FactoryGame -> Mods -> RandomRecipes

    3. Run the .jar file. You need Java installed for this, I could waste my time explaining how to
       do it, or you can Google it or ask any AI.

    4. Select your settings and press "Randomize" (save them if you want to load them later).

## Info:
    0. Hove over the options to see more info about them.

    1. Handcrafts: The first unlockable crafting station will have it's recipes able to be made by hand.
    Recipes with liquids may appear for handcrafting, but you obviously can't make them.

    2. Craft unlocked != Craft Possible.

    3. Production buildings may not be able to reach 100% efficiency on certain recipes.

    4. Excited Photonic Matter may not be free. Blame Ficsit.

    5. Scannable resources and basic game essentials are not moved. I tried it and the game just crashed.
    I don't get paid enough to figure it out (or paid at all tbh).

    6. MAM researches are treated as normal milestones.

    7. All materials are treated the same, so you could end up with the screw being as expensive as a
    vanilla plutonium fuel rod and vice versa.

    8. If for whatever reason introducing the same seed and settings create different paths for different
    players, you can work arround it by having one player create the files and send them to the others.
    This should be reported as a bug regardless.

    9. Particle accelerator/Converter recipes use energy based on the expected unlock time of the recipe.
    Generating 1500MW on Biomass Burners is only fun the 1st time.

    10. Crafting times used have 2 decimals, the game only shows 1, so the craft time displayed might be
    slightly inaccurate, the production/consumption per miute is correct though.

    11. If the randomizer takes too long, it's probably in an infinite loop, happens in some seeds.
    Close it and run again (changing the seed if you introduced it manually).

    12. Values of 0 may crash the randomizer if they are for quantities. For probabilities, 0 should be
    fine.

    13. Negative values for probabilities will be either treated as 0, produce unexpected results or crash
    the randomizer. Idem for 100 and values over it.  Don't put either preferably.

    14. Space elevator stages untouched (Changing them would be a pain and the path to get to it is already
    random anyway).

    15. Milestones are not necessarily linear, tier 1 milestones could need tier 9 materials to unlock.
    In this case, anything unlocked by that tier 1 milestone won't be needed until it's unlockable.

    16. If you can't get infinite of something, it's not required to beat the game.

    17. Names for alternate recipes are unchanged (and some recipes have "alternate:" in the name despite
    not being optional).

    18. Some recipes may become unusable because they need a limited material you already used. They are
    not needed to complete the game.

    19. Awesome shop recipes can be obtained through MAM research or milestones, but they can still be
    bought there. None of them are essential anyway.

    20. The default values are just random values I put for testing. They don't guarantee a good run.

## FAQ:

    If you haven't read the info and don't find your answer here, it may be there.

    ### Why are some values wrongly displayed in the cost portion of the building menu?

    For some reason many structures have more than 1 recipe, one is taken for the actual building but
    the displayed cost in building screen is sometimes a different one.

    #### What does "Remaining loops to cap" mean?

    The randomizer only loops for 3000 times, in testing no settings ever took more than 1000 loops,
    so I set the cap a bit higher to avoid infinite loops if it fails. For the most part you can
    ignore that message as long as the number is not 0 (You will get an error message then).

    ### Should I care about what the log says when generating?

    For the most part, no, the only thing you have to check is that all items have been randomized.
    Cases where not every item gets randomized are not very frequent but not really rare, you can
    beat the game in them anyway, but won't have access to all the items that weren't randomized.

    ### Are MAM researches needed for beating the game/progression?

    They can be. The logic can use anything you can unlock and is not an alternate recipe to make
    the main win path. Seeds that don't need the MAM are rare.

    ### Can a seed be unbeatable?

    Yes, the values that may make a seed unbeatable are marked.

    ### Is there a way to change values to complete an uncompletable seed?

    1. If the problem is inventory space (which should be the only one that is not a bug), a mod
       to increase inventory space may save you.

    2. You can just randomize again, all your recipes will change, so you will essentially have a
       non-functioning base.

    3. You can go to the problematic recipe (they are all in FactoryGame/Mods/RandomRecipes/ContentLib)
       and edit it. You should check ContentLib format in that mod to do this and have it work.

    4. If it's just the quantities that make the recipe impossible, you can just edit the number in
       the "Amount" space. I would avoid touching anything else if you're not sure about what you
       are doing. It won't break your game, but may make your file uncompletable if it wasn't.

    Good luck figuring out what's the name for each recipe if it's not on the wiki and you don't
    want to use mods like ContentInspector. My patch files use the name of the component (the
    one with "Desc_" prefix) for any case where the recipe and component aren't named the same.

    Many of the names of the milestones are wrong (for example Milestone 3-2 is a tier 2 milestone).
    The easiest way to figure out which is wich is to check the materials for each in the .json file
    and see which it is in game.

    Alternate recipes may be a nightmare to check, but they are not needed to complete the run, so
    you can just accept it's a useless recipe.