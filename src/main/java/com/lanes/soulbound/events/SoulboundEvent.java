package com.lanes.soulbound.events;

import com.lanes.soulbound.Soulbound;
import com.lanes.soulbound.util.SoulboundHandler;
import net.minecraft.block.SoundType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
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

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void retrievalEvent(LivingDropsEvent event)
	{
		if (event.getEntity() instanceof PlayerEntity) {
			SoulboundHandler.getOrCreateSoulboundHandler((PlayerEntity)event.getEntityLiving()).retainDrops(event.getDrops());
		}

	}

	@SubscribeEvent
	public static void itemTransferEvent(PlayerEvent.Clone event)
	{
		if (event.isWasDeath()) {
			PlayerEntity oldPlayer = event.getOriginal();
			if (SoulboundHandler.hasStoredDrops(oldPlayer)) {
				SoulboundHandler.getOrCreateSoulboundHandler(oldPlayer).transferItems(event.getPlayer());
			} else if (SoulboundHandler.hasStoredDrops(event.getPlayer())) {
				SoulboundHandler.getOrCreateSoulboundHandler(event.getPlayer()).transferItems(event.getPlayer());
			}
		}
	}

}
