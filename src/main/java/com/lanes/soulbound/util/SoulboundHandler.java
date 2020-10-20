package com.lanes.soulbound.util;

import com.google.common.collect.Lists;
import com.lanes.soulbound.config.SoulboundConfig;
import com.lanes.soulbound.config.SoulboundGlobals;
import com.lanes.soulbound.lists.EnchantmentList;
import net.minecraft.block.SoundType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.client.event.sound.SoundEvent;

import javax.annotation.Nullable;
import java.util.*;

public class SoulboundHandler
{
	private static HashMap<PlayerEntity, SoulboundHandler> handlerMap = new HashMap<>();
	public PlayerEntity player;
	public ArrayList<ItemStack> finalItems = new ArrayList<>();
	public ArrayList<ItemEntity> drops = new ArrayList<>();

	public static SoulboundHandler getOrCreateSoulboundHandler(PlayerEntity player) {
		if (hasActiveSoulboundHandler(player))
			return getSoulboundHandler(player);
		else
			return createSoulboundHandler(player);
	}

	public static SoulboundHandler getSoulboundHandler(PlayerEntity player) {
		return handlerMap.get(player);
	}

	public static SoulboundHandler createSoulboundHandler(PlayerEntity player) {
		SoulboundHandler newHandler = new SoulboundHandler(player);
		handlerMap.put(player, newHandler);
		return newHandler;
	}

	public static boolean hasActiveSoulboundHandler(PlayerEntity player) {
		return handlerMap.get(player) != null;
	}

	private SoulboundHandler(PlayerEntity playerIn)
	{
		this.player = playerIn;
	}

	public void filterEnchantment(Collection<ItemEntity> retrievedDrops)
	{
		List<ItemEntity> needsRemoval = Lists.newArrayList();
		for(ItemEntity retrievedDrop : retrievedDrops)
		{
			ItemStack item = retrievedDrop.getItem();
			if(item.isEnchanted() && EnchantmentHelper.getEnchantments(item).containsKey(EnchantmentList.SOULBOUND.get()))
			{
				int level = EnchantmentHelper.getEnchantmentLevel(EnchantmentList.SOULBOUND.get(), item);
				double chance = SoulboundGlobals.saveChance + (SoulboundGlobals.additiveSaveChance * (level - 1));
				double rng = Math.random();
				if(rng < chance)
				{
					this.finalItems.add(this.itemEditor(item).copy());
					needsRemoval.add(retrievedDrop);
				}
			}
		}

		needsRemoval.forEach(dropItem -> {
			retrievedDrops.remove(dropItem);
		});
	}

	public ItemStack itemEditor(ItemStack item)
	{
		int level = EnchantmentHelper.getEnchantmentLevel(EnchantmentList.SOULBOUND.get(), item);
		if(SoulboundGlobals.durabilityDrop)
		{
			double minimum = SoulboundGlobals.minimumDurabilityDrop - (SoulboundGlobals.additiveDurabilityDrop  * (level - 1));
			if(minimum < 0) { minimum = 0; }
			double maximum = SoulboundGlobals.maximumDurabilityDrop - (SoulboundGlobals.additiveDurabilityDrop  * (level - 1));
			if(maximum < 0) { maximum = 0; }
			double mode = SoulboundGlobals.modeDurabilityDrop - (SoulboundGlobals.additiveDurabilityDrop * (level - 1));
			if(mode < 0) { mode = 0; }

			int newDurability = (int) (item.getMaxDamage() * this.triangularDistribution(minimum, maximum, mode));
			if(item.attemptDamageItem(newDurability, this.player.getRNG(), (ServerPlayerEntity) this.player))
			{
				if (this.player instanceof PlayerEntity) {
					this.player.addStat(Stats.ITEM_BROKEN.get(item.getItem()));
				}

				if(SoulboundGlobals.breakItemOnZeroDurability)
				{
					item.setDamage(0);
					return ItemStack.EMPTY;
				}
				item.setDamage(item.getMaxDamage() - 1);
			}

		}
		double chance = SoulboundGlobals.dropLevel - (SoulboundGlobals.additiveDropChance * (level - 1));
		if(!(Math.random() < chance))
			return item;
		if(level > 1)
		{
			Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(item);
			enchantments.remove(EnchantmentList.SOULBOUND.get());
			enchantments.put(EnchantmentList.SOULBOUND.get(), level - 1);
			EnchantmentHelper.setEnchantments(enchantments, item);
		}
		else
		{
			Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(item);
			enchantments.remove(EnchantmentList.SOULBOUND.get());
			EnchantmentHelper.setEnchantments(enchantments, item);
		}
		return item;
	}

	/**
	 * Okay I won't even try to pretend I understand your math, so I won't touch it.
	 * @author Extegral
	 */

	public double triangularDistribution(double a, double b, double c) { // where a = min, b = max, c = mode
		// 25 , 50, 33
		double F = (c - a) / (b - a); // f = (.33 - .25) / (.50 - .25), f = .32
		double rand = Math.random();
		if (rand < F)
			return a + Math.sqrt(rand * (b - a) * (c - a)); // RETURNS .25 + sqrt((.20 * (.50 - .25) * (.33 - .25)) .313 (Nearing .33 with larger values, with lesser values it nears .25)
		else
			return b - Math.sqrt((1 - rand) * (b - a) * (b - c)); // RETURNS .50 - sqrt((1 - .50) * (.50 - .25) * (.50 - .33)) .354 (Nearing .33 with lesser values, with larger values it nears .50)
	}

	public void transferItems() {
		boolean breakonce = false;
		if(this.finalItems.isEmpty())
			return;
		for (Object finalItem : this.finalItems) {
			ItemStack item = (ItemStack) finalItem;
			if(item == (ItemStack.EMPTY) && !breakonce)
			{
				this.player.playSound(SoundEvents.ENTITY_ITEM_BREAK, 1.0f, 1.0f); // this line of code isn't working. THIS CODE NEEDS WORK
				breakonce = true;
			}
			this.player.inventory.addItemStackToInventory(item);
		}
	}
}
