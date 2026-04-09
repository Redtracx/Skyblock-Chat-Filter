package com.redtracx.skyblockchatfilter;

import com.redtracx.skyblockchatfilter.chat.ChatFilterManager;
import com.redtracx.skyblockchatfilter.chat.ChatTabManager;
import com.redtracx.skyblockchatfilter.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkyblockChatFilterClient implements ClientModInitializer {
    public static final String MOD_ID = "skyblockchatfilter";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ModConfig config;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Skyblock Chat-Filter for 1.21.x!");
        
        // Register Config
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        
        // Register Fabric API chat event filters (new default method, compatible with other mods)
        ClientReceiveMessageEvents.ALLOW_CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
            if (config != null && !config.advanced.useLegacyMixin) {
                if (ChatFilterManager.shouldHideMessage(message)) return false;
            }
            return true;
        });

        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
            if (overlay) return true; // don't filter action bar / overlay messages
            if (config != null && !config.advanced.useLegacyMixin) {
                if (ChatFilterManager.shouldHideMessage(message)) return false;
            }
            return true;
        });

        // Register Command
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("chatfilter")
                .executes(context -> {
                    MinecraftClient.getInstance().player.sendMessage(
                        Text.literal("§a[ChatFilter] §fPlease use the ModMenu list to configure settings."), false);
                    return 1;
                }));
        });
    }
}

