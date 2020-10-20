package com.lanes.soulbound.events;

import com.lanes.soulbound.Soulbound;
import com.lanes.soulbound.util.SoulboundHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;
import java.util.HashMap;

@Mod.EventBusSubscriber(modid = Soulbound.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SoulboundEvent {

	@SubscribeEvent(priority = EventPriority.HIGH) // will be using this for overriding gravestone mods.
	public static void retrievalEvent(LivingDropsEvent event) // runs second (before respawn)
	{
		if (event.getEntity() instanceof PlayerEntity) {
			SoulboundHandler.getOrCreateSoulboundHandler((PlayerEntity) event.getEntityLiving()).filterEnchantment(event.getDrops());
		}

	}

	@SubscribeEvent
	public static void itemTransferEvent(PlayerEvent.Clone event) // runs third (when respawned)
	{
		if (event.isWasDeath()) {
			if (SoulboundHandler.hasActiveSoulboundHandler(event.getPlayer())) {
				SoulboundHandler.getSoulboundHandler(event.getPlayer()).transferItems();
			}
		}
	}

}
