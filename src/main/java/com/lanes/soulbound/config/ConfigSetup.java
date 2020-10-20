package com.lanes.soulbound.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.lanes.soulbound.Soulbound;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.io.File;

@Mod.EventBusSubscriber
public class ConfigSetup
{
	private static final ForgeConfigSpec.Builder common_builder = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec common_config;

	private static final ForgeConfigSpec.Builder client_builder = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec client_config;

	/*
	 * Your config setup looks very confusing tbh,
	 * but again this isn't as critical and we can adress this later.
	 */

	static
	{
		SoulboundConfig.init(common_builder, client_builder);

		common_config = common_builder.build();
		client_config = client_builder.build();
	}

	public static void loadConfig(ForgeConfigSpec config, String path)
	{
		Soulbound.LOGGER.info("Loading Config: " + path);
		final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
		Soulbound.LOGGER.info("Built Config: " + path);
		file.load();
		Soulbound.LOGGER.info("Loaded Config: " + path);
		config.setConfig(file);
	}
}
