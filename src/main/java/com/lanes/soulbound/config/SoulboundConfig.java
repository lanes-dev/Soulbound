package com.lanes.soulbound.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SoulboundConfig {
    public static ForgeConfigSpec.BooleanValue enabled;

    public static ForgeConfigSpec.BooleanValue enchantable;

    public static ForgeConfigSpec.BooleanValue enchantableBooks;

    public static ForgeConfigSpec.BooleanValue cursesApplicable;

    public static ForgeConfigSpec.IntValue maximumLevel;

    public static ForgeConfigSpec.IntValue rarity;

    public static ForgeConfigSpec.DoubleValue dropLevel;

    public static ForgeConfigSpec.DoubleValue additiveDropChance;

    public static ForgeConfigSpec.DoubleValue additiveSaveChance;

    public static ForgeConfigSpec.DoubleValue saveChance;

    public static ForgeConfigSpec.BooleanValue durabilityDrop;

    public static ForgeConfigSpec.BooleanValue breakItemOnZeroDurability;

    public static ForgeConfigSpec.DoubleValue additiveDurabilityDrop;

    public static ForgeConfigSpec.DoubleValue maximumDurabilityDrop;

    public static ForgeConfigSpec.DoubleValue minimumDurabilityDrop;

    public static ForgeConfigSpec.DoubleValue modeDurabilityDrop;

    public static void init(ForgeConfigSpec.Builder builder)
    {
        builder.comment("Soulbound Config");

        enabled = builder
                .comment("If the mod is enabled.")
                .translation("soulbound.config.enabled")
                .define("enableMod", true);

        builder.push("Basic Enchantment Values");

        enchantable = builder
                .comment("If the enchantment can be obtained on items through an enchanting table.")
                .translation("soulbound.config.enchantable")
                .define("enchantable", false);

        enchantableBooks = builder
                .comment("If the enchantment can be obtained on a book through an enchanting table.")
                .translation("soulbound.config.enchantable_books")
                .define("enchantable_books", false);

        cursesApplicable = builder
                .comment("If curses can be applied with the enchant. **NOT RECOMMENDED**")
                .translation("soulbound.config.curses_applicable")
                .define("curses_applicable", false);

        maximumLevel = builder
                .comment("Maximum level the enchant can be.")
                .translation("soulbound.config.maximum_level")
                .defineInRange("maximum_level", 3, 1, 32767);

        rarity = builder
                .comment(("How rare the enchantment is at the enchantment table (some loot spawns included).\n" +
                        "Acceptable values are listed below.\n" +
                        "1 - COMMON\n" +
                        "2 - UNCOMMON\n" +
                        "3 - RARE\n" +
                        "4 - VERY RARE"))
                .translation("soulbound.config.rarity")
                .defineInRange("rarity", 4, 1, 4);

        builder.pop();
        builder.push("Level Drop Values");

        dropLevel = builder
                .comment("Chance for the enchant to drop down 1 level on death from 0.00 to 1.00.")
                .translation("soulbound.config.drop_level")
                .defineInRange("drop_level", 1.0, 0.0, 1.0);

        additiveDropChance = builder
                .comment("Chance for enchant to drop down 1 level with every added level\n" +
                        "So if someone with Soulbound 3 dies the chance of the enchant being downgraded would be:\n" +
                        "(dropLevel) - ((level - 1) * additiveDropChance)\n" +
                        "Remember if you've set a lot of levels this could lead to the higher levels never dropping down")
                .translation("soulbound.config.additive_drop_chance")
                .defineInRange("additive_drop_chance", 0.0, 0.0, 1.0);

        builder.pop();
        builder.push("Save Chance Values");

        additiveSaveChance = builder
                .comment("Chance for item to be kept from 0.00 to 1.00 with every added level.\n" +
                        "So if someone with Soulbound 3 dies their chances of keeping the item would be:\n" +
                        ("(saveChance) + ((level - 1) * additiveSaveChance)\n" +
                                "Remember if you've set a lot of levels this could lead to the higher levels always being saved."))
                .translation("soulbound.config.additive_save_chance")
                .defineInRange("additive_save_chance", 0.0, 0.0, 1.0);

        saveChance = builder // done
                .comment("Chance for item with enchant to be kept from 0.00 to 1.00.")
                .translation("soulbound.config.save_chance")
                .defineInRange("save_chance", 1.0, 0.0, 1.0);

        builder.pop();
        builder.push("Durability Drop Values");

        durabilityDrop = builder
                .comment("Set whether durability drop is enabled or not. Durability drop is calculated with the min and max \n"
                        + "variables. The values chosen will most likely be in the middle and get rarer towards the ends. (triangular distribution)")
                .translation("soulbound.config.durability_drop")
                .define("durability_drop", false);

        breakItemOnZeroDurability = builder
                .comment("If set to true, the item will be broken if the durability reaches 0 on it's durabilityDrop")
                .translation("soulbound.config.break_item_on_zero_durability")
                .define("break_item_on_zero_durability", false);

        additiveDurabilityDrop = builder
                .comment("Subtracts this number from the max, min, and mode each level effectively making the durability \n" +
                        "drop go down the higher the level")
                .translation("soulbound.config.additive_durability_drop")
                .defineInRange("additive_durability_drop", 0.05, 0.0, 1.0);

        maximumDurabilityDrop = builder
                .comment("Defines the minimum percentage that the durability goes down when returned (this percentage is of\n" +
                        "the items max durability NOT the actual durability)")
                .translation("soulbound.config.maximum_durability_drop")
                .defineInRange("maximum_durability_drop", 0.35, 0.0, 1.0);

        minimumDurabilityDrop = builder
                .comment("Defines the maximum percentage that the durability goes down when returned (this percentage is of\n" +
                        "the items max durability NOT the actual durability)")
                .translation("soulbound.config.minimum_durability_drop")
                .defineInRange("minimum_durability_drop", 0.20, 0.0, 1.0);

        modeDurabilityDrop = builder
                .comment("Defines the mode (average value) percentage that the durability goes down when returned. This value\n" +
                        "cannot be more than the maximum or less than the minimum. (this percentage is of\n" +
                        "the items max durability NOT the actual durability)")
                .translation("soulbound.config.mode_durability_drop")
                .defineInRange("mode_durability_drop", 0.25, 0.0, 1.0);

        builder.pop();
    }
}
