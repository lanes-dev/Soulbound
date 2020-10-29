package com.lanes.soulbound.events;

import com.lanes.soulbound.Soulbound;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Soulbound.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LootTables
{
	@SubscribeEvent
	public static void bastionBridgeInject(LootTableLoadEvent event)
	{
		if(!event.isCanceled())
		{
			if(event.getName().equals(new ResourceLocation("minecraft", "chests/bastion_bridge")) || event.getName().equals(new ResourceLocation("minecraft", "chests/bastion_other"))
					|| event.getName().equals(new ResourceLocation("minecraft", "chests/bastion_treasure")) || event.getName().equals(new ResourceLocation("minecraft", "chests/end_city_treasure")))
			{
				event.getTable().addPool(LootPool.builder().name("soulboundloot").addEntry(TableLootEntry.builder(new ResourceLocation(Soulbound.MOD_ID, "chests/soulbound_loot"))).build());
			}
		}
	}
}
