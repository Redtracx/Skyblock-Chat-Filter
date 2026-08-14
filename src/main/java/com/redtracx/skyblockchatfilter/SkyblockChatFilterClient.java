package com.redtracx.skyblockchatfilter;

import com.redtracx.skyblockchatfilter.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkyblockChatFilterClient implements ClientModInitializer {
    public static final String MOD_ID = "skyblockchatfilter";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ModConfig config;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Skyblock Chat-Filter for 26.x!");

        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}
