package com.redtracx.skyblockchatfilter.mixin;

import com.redtracx.skyblockchatfilter.SkyblockChatFilterClient;
import com.redtracx.skyblockchatfilter.chat.ChatFilterManager;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {
    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", at = @At("HEAD"), cancellable = true)
    private void onAddMessage(Text message, @Nullable MessageSignatureData signature, @Nullable MessageIndicator indicator, CallbackInfo ci) {
        // Only use the legacy Mixin cancel method if explicitly enabled in config
        if (SkyblockChatFilterClient.config != null
                && SkyblockChatFilterClient.config.advanced.useLegacyMixin
                && ChatFilterManager.shouldHideMessage(message)) {
            ci.cancel();
        }
    }
}

