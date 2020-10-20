package com.lanes.soulbound.events;

import com.lanes.soulbound.Soulbound;
import com.lanes.soulbound.util.SoulboundHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Resource;
import java.util.Collection;

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
                event.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(new ResourceLocation(Soulbound.MOD_ID, "chests/soulbound_loot"))).build());
            }
        }
    }
}
