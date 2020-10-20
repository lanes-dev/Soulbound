package com.lanes.soulbound.config;

import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class SoulboundConfig {

    public static ForgeConfigSpec.BooleanValue enchantable;

    public static ForgeConfigSpec.BooleanValue enchantableBooks;

    public static ForgeConfigSpec.BooleanValue cursesApplicable;

    public static ForgeConfigSpec.IntValue maximumLevel;

    public static ForgeConfigSpec.BooleanValue enableMod;

    public static ForgeConfigSpec.DoubleValue dropLevel;

    public static ForgeConfigSpec.DoubleValue additiveDropChance;

    public static ForgeConfigSpec.DoubleValue saveChance;

    public static ForgeConfigSpec.DoubleValue additiveSaveChance;

    public static ForgeConfigSpec.IntValue rarity;

    public static ForgeConfigSpec.BooleanValue durabilityDrop;

    public static ForgeConfigSpec.DoubleValue maximumDurabilityDrop;

    public static ForgeConfigSpec.DoubleValue minimumDurabilityDrop;

    public static ForgeConfigSpec.DoubleValue modeDurabilityDrop;

    public static ForgeConfigSpec.BooleanValue breakItemOnZeroDurability;

    public static ForgeConfigSpec.DoubleValue additiveDurabilityDrop;


    public static void init(ForgeConfigSpec.Builder server, ForgeConfigSpec.Builder client)
    {
        server.comment("Configure Mod");
        server.push("Soulbound_Mod");

        enableMod = server // done
                .comment("If the mod is enabled.")
                .define("enableMod", true);

        enchantable = server // done
                .comment("If the enchantment can be obtained on items through an enchanting table.")
                .define("Soulbound_Enchantment.enchantable", false);

        enchantableBooks = server // done
                .comment("If the enchantment can be obtained on a book through an enchanting table.")
                .define("Soulbound_Enchantment.enchantableBooks", false);

        cursesApplicable = server // done
                .comment("If curses can be applied with the enchant. **NOT RECOMMENDED**")
                .define("Soulbound_Enchantment.cursesApplicable", false);

        maximumLevel = server // done
                .comment("Maximum level the enchant can be.")
                .defineInRange("Soulbound_Enchantment.maximumLevel", 3, 1, 32767);

        rarity = server // done
                .comment(("How rare the enchantment is at the enchantment table (some loot spawns included).\n" +
                          "Acceptable values are listed below.\n" +
                          "1 - COMMON\n" +
                          "2 - UNCOMMON\n" +
                          "3 - RARE\n" +
                          "4 - VERY RARE"))
                .defineInRange("Soulbound_Enchantment.rarity", 4, 1, 4);

        dropLevel = server // done
                .comment("Chance for the enchant to drop down 1 level on death from 0.00 to 1.00.")
                .defineInRange("Soulbound_Enchantment.DropLevel.dropLevel", 1.0, 0.0, 1.0);

        additiveDropChance = server // done
                .comment("Chance for enchant to drop down 1 level with every added level\n" +
                        "So if someone with Soulbound 3 dies the chance of the enchant being downgraded would be:\n" +
                        "(dropLevel) - ((level - 1) * additiveDropChance)\n" +
                        "Remember if you've set a lot of levels this could lead to the higher levels never dropping down")
                .defineInRange("Soulbound_Enchantment.DropLevel.additiveDropChance", 0.0, 0.0, 1.0);

        additiveSaveChance = server // done
                .comment("Chance for item to be kept from 0.00 to 1.00 with every added level.\n" +
                        "So if someone with Soulbound 3 dies their chances of keeping the item would be:\n" +
                        ("(saveChance) + ((level - 1) * additiveSaveChance)\n" +
                         "Remember if you've set a lot of levels this could lead to the higher levels always being saved."))
                .defineInRange("Soulbound_Enchantment.SaveChance.additiveSaveChance", 0.0, 0.0, 1.0);

        saveChance = server // done
                .comment("Chance for item with enchant to be kept from 0.00 to 1.00.")
                .defineInRange("Soulbound_Enchantment.SaveChance.saveChance", 1.0, 0.0, 1.0);

        durabilityDrop = server
                .comment("Set whether durability drop is enabled or not. Durability drop is calculated with the min and max \n"
                + "variables. The values chosen will most likely be in the middle and get rarer towards the ends. (triangular distribution)")
                .define("Soulbound_Enchantment.DurabilityDrop.durabilityDrop", false);

        breakItemOnZeroDurability = server
                .comment("If set to true, the item will be broken if the durability reaches 0 on it's durabilityDrop")
                .define("Soulbound_Enchantment.DurabilityDrop.breakItemOnZeroDurability", false);

        additiveDurabilityDrop = server
                .comment("Subtracts this number from the max, min, and mode each level effectively making the durability \n" +
                        "drop go down the higher the level")
                .defineInRange("Soulbound_Enchantment.DurabilityDrop.additiveDurabilityDrop", 0.05, 0.0, 1.0);

        maximumDurabilityDrop = server
                .comment("Defines the minimum percentage that the durability goes down when returned (this percentage is of\n" +
                        "the items max durability NOT the actual durability)")
                .defineInRange("Soulbound_Enchantment.DurabilityDrop.maximumDurabilityDrop", 0.35, 0.0, 1.0);

        minimumDurabilityDrop = server
                .comment("Defines the maximum percentage that the durability goes down when returned (this percentage is of\n" +
                        "the items max durability NOT the actual durability)")
                .defineInRange("Soulbound_Enchantment.DurabilityDrop.minimumDurabilityDrop", 0.20, 0.0, 1.0);

        modeDurabilityDrop = server
                .comment("Defines the mode (average value) percentage that the durability goes down when returned. This value\n" +
                        "cannot be more than the maximum or less than the minimum. (this percentage is of\n" +
                        "the items max durability NOT the actual durability)")
                .defineInRange("Soulbound_Enchantment.DurabilityDrop.modeDurabilityDrop", 0.25, 0.0, 1.0);


    }
}
