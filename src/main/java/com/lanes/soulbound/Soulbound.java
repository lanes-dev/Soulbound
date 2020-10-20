package com.lanes.soulbound;

import com.lanes.soulbound.config.ConfigSetup;
import com.lanes.soulbound.config.SoulboundConfig;
import com.lanes.soulbound.config.SoulboundGlobals;
import com.lanes.soulbound.lists.EnchantmentList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("soulbound")
@Mod.EventBusSubscriber(modid = Soulbound.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Soulbound
{
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "soulbound";
	public static Soulbound instance;

	public Soulbound() {
		instance = this;

		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigSetup.server_config);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigSetup.client_config);

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

		ConfigSetup.loadConfig(ConfigSetup.server_config, FMLPaths.CONFIGDIR.get().resolve("soulbound-server.toml").toString());
		ConfigSetup.loadConfig(ConfigSetup.client_config, FMLPaths.CONFIGDIR.get().resolve("soulbound-client.toml").toString());

		if(!SoulboundGlobals.enableMod)
			return;

		EnchantmentList.ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());

		MinecraftForge.EVENT_BUS.register(this);
	}

	public void ModCheck() {
		//Loader.
	}

	private void setup(final FMLCommonSetupEvent event) {
		// NO-OP
	}

	private void doClientStuff(final FMLClientSetupEvent event){
		// NO-OP
	}
}
