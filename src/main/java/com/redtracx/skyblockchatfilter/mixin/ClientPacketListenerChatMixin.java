package com.redtracx.skyblockchatfilter.mixin;

import com.redtracx.skyblockchatfilter.chat.ChatFilterManager;
import com.redtracx.skyblockchatfilter.chat.ChatTabManager;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerChatPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerChatMixin {

    @Inject(method = "handleSystemChat(Lnet/minecraft/network/protocol/game/ClientboundSystemChatPacket;)V", at = @At("HEAD"), cancellable = true)
    private void skyblockchatfilter$onSystemChat(ClientboundSystemChatPacket packet, CallbackInfo ci) {
        if (packet.overlay()) return; // never touch the action bar

        Component content = packet.content();
        if (content == null) return;

        if (skyblockchatfilter$handle(content)) {
            ci.cancel();
        }
    }

    @Inject(method = "handlePlayerChat(Lnet/minecraft/network/protocol/game/ClientboundPlayerChatPacket;)V", at = @At("HEAD"), cancellable = true)
    private void skyblockchatfilter$onPlayerChat(ClientboundPlayerChatPacket packet, CallbackInfo ci) {
        String body = packet.body().content();
        if (body == null) return;

        if (skyblockchatfilter$handle(Component.literal(body))) {
            ci.cancel();
        }
    }

    private static boolean skyblockchatfilter$handle(Component message) {
        if (ChatTabManager.isReplaying()) return false;

        if (ChatFilterManager.shouldHideMessage(message)) {
            return true;
        }

        if (ChatTabManager.isEnabled()) {
            ChatTabManager.bufferMessage(message);
            return !ChatTabManager.shouldShowInCurrentTab(message);
        }

        return false;
    }
}
