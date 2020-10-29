package com.lanes.soulbound.effects.enchantments;

import com.lanes.soulbound.config.CommonConfig;
import com.lanes.soulbound.lists.EnchantmentList;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;

public class EnchantmentSoulbound extends Enchantment {

    public EnchantmentSoulbound(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel()
    {
        return CommonConfig.COMMON.maximumLevel.get();
    }

    @Override
    public int getMinLevel()
    {
        return 1;
    }

    @Override
    public boolean isTreasureEnchantment() {
        return !CommonConfig.COMMON.enchantable.get();
    }

    @Override
    public boolean isAllowedOnBooks() {
        return CommonConfig.COMMON.enchantableBooks.get();
    }

    @Override
    public boolean canApplyTogether(Enchantment ench)
    {
        if(ench == EnchantmentList.SOULBOUND.get())
        {
            return false;
        }
        if(CommonConfig.COMMON.cursesApplicable.get())
        {
            return true;
        }
        else if(ench == Enchantments.BINDING_CURSE || ench == Enchantments.VANISHING_CURSE)
        {
            return false;
        }
        return true;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel)
    {
        return (int) ((CommonConfig.COMMON.rarity.get() * 4 * enchantmentLevel) - 3);
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel)
    {
        return getMinEnchantability(enchantmentLevel) + 50;
    }
}
