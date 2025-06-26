# Recipe Randomizer

A simple recipe randomizer created in Java to create the JSON files that ContentLib will introduce into the game.

Why Java? I had VSCode set up to use Java from my last project and it was as good a choice as any for such a simple task.

Info:

    1. Handcrafts: The first unlockable crafting station will have it's recipes able to be made by hand. Recipes with liquids may appear for handcrafting, but you obviously can't make them.

    2. Craft unlocked != Craft Possible.

    3. Production buildings may not be able to reach 100% efficiency on certain recipes.

    4. Excited Photonic Matter may not be free. Blame Ficsit.

    5. Scannable resources and basic game essentials are not moved. I tried it and the game just crashed. I don't get paid enough to figure it out (or paid at all tbh).

    6. MAM researches are treated as normal milestones.

    7. All materials are treated the same, so you could end up with the screw being as expensive as a vanilla plutonium fuel rod and vice versa.

    8. If for whatever reason introducing the same seed and settings create different paths for different players, you can work arround it by having one player create the files and send them to the others. This should be reported as a bug regardless.

    9. Particle accelerator/Converter recipes use energy based on the expected unlock time of the recipe. Generating 1500MW on Biomass Burners is only fun the 1st time.

    10. Crafting times used have 2 decimals, the game only shows 1, so the craft time displayed might be slightly inaccurate, the production/consumption per miute is correct though.

    11. If the randomizer takes too long, it's probably in an infinite loop, happens in some seeds. Close it and run again (changing the seed if you introduced it manually).

    12. Values of 0 may crash the randomizer if they are for quantities. For probabilities, 0 should be fine.

    13. Negative values for probabilities will be either treated as 0, produce unexpected results or crash the randomizer. Idem for 100 and values over it.  Don't put either preferably.

    14. Space elevator stages untouched (Changing them would be a pain and the path to get to it is already random anyway).

    15. Milestones are not necessarily linear, tier 1 milestones could need tier 9 materials to unlock. In this case, anything unlocked by that tier 1 milestone won't be needed until it's unlockable.

    16. If you can't get infinite of something, it's not required to beat the game.

    17. Names for alternate recipes are unchanged (and some recipes have "alternate:" in the name despite not being optional).

    18. Some recipes may become unusable because they need a limited material you already used. They are not needed to complete the game.

    19. Awesome shop recipes can be obtained through MAM research or milestones, but they can still be bought there. None of them are essential anyway.

    20. The defaul values are around what you'd need to get vanilla-like values, but they don't guarantee a good randomization.