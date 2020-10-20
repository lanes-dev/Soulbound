package com.lanes.soulbound.util;

import com.google.common.collect.Lists;
import com.lanes.soulbound.config.SoulboundConfig;
import com.lanes.soulbound.config.SoulboundGlobals;
import com.lanes.soulbound.lists.EnchantmentList;
import net.minecraft.block.SoundType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.client.event.sound.SoundEvent;

import javax.annotation.Nullable;
import java.util.*;

public class SoulboundHandler {
	private static final HashMap<PlayerEntity, SoulboundHandler> handlerMap = new HashMap<>();
	public static final String soulboundTag = "SoulboundItems";
	public static final String storedStacksTag = "StoredStacks";
	public static final String stackTag = "Stack";
	private final PlayerEntity player;

	public static SoulboundHandler getOrCreateSoulboundHandler(PlayerEntity player) {
		if (getSoulboundHandler(player) != null)
			return getSoulboundHandler(player);
		else
			return createSoulboundHandler(player);
	}

	@Nullable
	public static SoulboundHandler getSoulboundHandler(PlayerEntity player) {
		return handlerMap.get(player);
	}

	public static SoulboundHandler createSoulboundHandler(PlayerEntity player) {
		SoulboundHandler newHandler = new SoulboundHandler(player);
		handlerMap.put(player, newHandler);
		return newHandler;
	}

	public static boolean hasStoredDrops(PlayerEntity player) {
		return hasSerializedDrops(player);
	}

	private SoulboundHandler(PlayerEntity playerIn) {
		this.player = playerIn;
	}

	public void retainDrops(Collection<ItemEntity> eventDrops) {
		List<ItemEntity> retainedDrops = Lists.newArrayList();
		for (ItemEntity eventDrop : eventDrops) {
			ItemStack item = eventDrop.getItem();
			if (item.isEnchanted() && EnchantmentHelper.getEnchantments(item).containsKey(EnchantmentList.SOULBOUND.get())) {
				int level = EnchantmentHelper.getEnchantmentLevel(EnchantmentList.SOULBOUND.get(), item);
				double chance = SoulboundGlobals.saveChance + (SoulboundGlobals.additiveSaveChance * (level - 1));
				double rng = Math.random();
				if (rng < chance) {
					retainedDrops.add(eventDrop);
				}
			}
		}

		retainedDrops.forEach(dropItem -> {
			eventDrops.remove(dropItem);
		});

		this.serializeDrops(retainedDrops);
	}

	private void serializeDrops(Collection<ItemEntity> drops) {
		CompoundNBT soulData = new CompoundNBT();
		soulData.putInt(storedStacksTag, drops.size());
		int counter = 0;

		for (ItemEntity drop : drops) {
			ItemStack stack = this.itemEditor(drop.getItem()).copy();
			if (stack != null) {
				CompoundNBT serializedStack = stack.serializeNBT();
				soulData.put(stackTag + counter, serializedStack);
				counter++;
			}
		}

		this.player.getPersistentData().put(soulboundTag, soulData);
	}

	private static boolean hasSerializedDrops(PlayerEntity player) {
		return player.getPersistentData().contains(soulboundTag);
	}

	private List<ItemStack> deserializeDrops() {
		List<ItemStack> deserialized = Lists.newArrayList();
		CompoundNBT soulData = this.player.getPersistentData().getCompound(soulboundTag);
		int counter = soulData.getInt(storedStacksTag) - 1;

		for (int c = counter; c >= 0; c--) {
			CompoundNBT nbt = soulData.getCompound(stackTag + c);
			ItemStack stack = ItemStack.read(nbt);

			if (!stack.isEmpty()) {
				deserialized.add(stack);
			}

			soulData.remove(stackTag + c);
		}

		this.player.getPersistentData().remove(soulboundTag);
		return deserialized;
	}

	private ItemStack itemEditor(ItemStack item) {
		int level = EnchantmentHelper.getEnchantmentLevel(EnchantmentList.SOULBOUND.get(), item);
		if (SoulboundGlobals.durabilityDrop) {
			double minimum = SoulboundGlobals.minimumDurabilityDrop - (SoulboundGlobals.additiveDurabilityDrop * (level - 1));
			if (minimum < 0) {
				minimum = 0;
			}
			double maximum = SoulboundGlobals.maximumDurabilityDrop - (SoulboundGlobals.additiveDurabilityDrop * (level - 1));
			if (maximum < 0) {
				maximum = 0;
			}
			double mode = SoulboundGlobals.modeDurabilityDrop - (SoulboundGlobals.additiveDurabilityDrop * (level - 1));
			if (mode < 0) {
				mode = 0;
			}

			int newDurability = (int) (item.getMaxDamage() * this.triangularDistribution(minimum, maximum, mode));
			if (item.attemptDamageItem(newDurability, this.player.getRNG(), (ServerPlayerEntity) this.player)) {
				if (this.player instanceof PlayerEntity) {
					this.player.addStat(Stats.ITEM_BROKEN.get(item.getItem()));
				}

				if (SoulboundGlobals.breakItemOnZeroDurability) {
					item.setDamage(item.getMaxDamage());
					return item;
				}
				item.setDamage(item.getMaxDamage() - 1);
			}

		}
		double chance = SoulboundGlobals.dropLevel - (SoulboundGlobals.additiveDropChance * (level - 1));
		if (!(Math.random() < chance))
			return item;
		if (level > 1) {
			Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(item);
			enchantments.remove(EnchantmentList.SOULBOUND.get());
			enchantments.put(EnchantmentList.SOULBOUND.get(), level - 1);
			EnchantmentHelper.setEnchantments(enchantments, item);
		} else {
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

	public void transferItems(PlayerEntity rebornPlayer) {
		List<ItemStack> retainedDrops = this.deserializeDrops();

		boolean breakonce = false;
		if (retainedDrops.isEmpty())
			return;
		for (ItemStack item : retainedDrops) {
			if (item.getDamage() == item.getMaxDamage()) {
				if (!breakonce) {
					// I have tried different stuff, but it might be not possible to play sounds in clone event at all, as player
					// isn't spawned into the world yet and has no definitive coordinates to play sound at. We need other event for that

					rebornPlayer.world.playSound(null, rebornPlayer.getPosX(), rebornPlayer.getPosY(), rebornPlayer.getPosZ(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1.0f, 1.0f);

					/*
					item.setDamage(item.getDamage()-1);
					item.damageItem(Short.MAX_VALUE, rebornPlayer, p -> p.sendBreakAnimation(EquipmentSlotType.MAINHAND));

					System.out.println("broken");
					 */

					breakonce = true;
				}

				continue;
			}
			rebornPlayer.inventory.addItemStackToInventory(item);
		}

		handlerMap.remove(this.player);
	}
}
