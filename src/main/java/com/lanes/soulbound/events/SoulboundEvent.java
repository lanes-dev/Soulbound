package com.lanes.soulbound.events;

import com.lanes.soulbound.Soulbound;
import com.lanes.soulbound.util.SoulboundHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
<<<<<<< Updated upstream
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
=======
>>>>>>> Stashed changes
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;

@Mod.EventBusSubscriber(modid = Soulbound.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SoulboundEvent
{
    public static SoulboundHandler handler;
    public static Boolean dead = false;


    @SubscribeEvent(priority = EventPriority.HIGHEST) // will be using this for overriding gravestone mods.
    public static void retrievalEvent(LivingDropsEvent event) // runs second (before respawn)
    {
        if(!event.isCanceled())
        {
            if(event.getEntity() instanceof PlayerEntity)
            {
                handler.filterEnchantment(event.getDrops());
            }
        }
    }

    @SubscribeEvent
    public static void playerDeathEvent(LivingDeathEvent event) // runs first (before respawn)
    {
        if(!event.isCanceled())
        {
            if(event.getEntity() instanceof PlayerEntity)
            {
                dead = true;
                handler = new SoulboundHandler(event.getEntityLiving());
            }
        }
    }

    @SubscribeEvent
    public static void itemTransferEvent(PlayerEvent.Clone event) // runs third (when respawned)
    {
        if(!event.isCanceled())
        {
            if(event.isWasDeath())
            {
                if(dead)
                {
                    if(event.getEntity() instanceof PlayerEntity)
                    {
                        handler.transferItems(event.getPlayer());
                    }
                }
            }
        }
    }



}
