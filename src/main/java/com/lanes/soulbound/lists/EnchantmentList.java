package com.lanes.soulbound.lists;

import com.lanes.soulbound.Soulbound;
import com.lanes.soulbound.effects.enchantments.EnchantmentSoulbound;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantmentList {

	// This is weird but okay

	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Soulbound.MOD_ID);

	public static final RegistryObject<Enchantment> SOULBOUND = ENCHANTMENTS.register("soulbound", () -> new EnchantmentSoulbound(Enchantment.Rarity.RARE, EnchantmentType.VANISHABLE, EquipmentSlotType.values()));



}
