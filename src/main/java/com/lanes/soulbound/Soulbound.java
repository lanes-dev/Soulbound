package com.lanes.soulbound;

import com.lanes.soulbound.config.Config;
import com.lanes.soulbound.config.SoulboundConfig;
import com.lanes.soulbound.lists.EnchantmentList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod("soulbound")
@Mod.EventBusSubscriber(modid = Soulbound.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Soulbound
{
	public static final String MOD_ID = "soulbound";
	public static Soulbound instance;

	public Soulbound() {
		instance = this;

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.common_config);

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

		Config.loadConfig(Config.common_config, FMLPaths.CONFIGDIR.get().resolve("soulbound-common.toml").toString());

		if(!SoulboundConfig.enabled.get())
			return;

		EnchantmentList.ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());

		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup(final FMLCommonSetupEvent event) {
		// NO-OP
	}

	private void doClientStuff(final FMLClientSetupEvent event){
		// NO-OP
	}
}
