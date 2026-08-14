package com.redtracx.skyblockchatfilter.mixin;

import com.redtracx.skyblockchatfilter.chat.ChatTab;
import com.redtracx.skyblockchatfilter.chat.ChatTabManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumMap;
import java.util.Map;

// Click/keyboard handling for the tab bar goes through real Button widgets
// (added in init()) rather than a hand-rolled mouseClicked/keyPressed override:
// Minecraft's raw input handling was reworked around Click/KeyInput records in
// the 26.x cycle, and Button's own hit-testing is the stable, well-documented
// API that isn't affected by that internal change.
@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {

    @Unique
    private final Map<ChatTab, Button> skyblockchatfilter$tabButtons = new EnumMap<>(ChatTab.class);

    protected ChatScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void skyblockchatfilter$addTabButtons(CallbackInfo ci) {
        skyblockchatfilter$tabButtons.clear();
        if (!ChatTabManager.isEnabled()) return;

        int barY = this.height - 24;
        int x = 4;
        for (ChatTab tab : ChatTab.values()) {
            int width = this.font.width(tab.getDisplayName()) + 12;
            Button button = Button.builder(Component.literal(tab.getDisplayName()), btn -> ChatTabManager.setCurrentTab(tab))
                    .bounds(x, barY, width, 14)
                    .build();
            skyblockchatfilter$tabButtons.put(tab, button);
            this.addRenderableWidget(button);
            x += width + 2;
        }
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void skyblockchatfilter$updateTabButtons(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ChatTab active = ChatTabManager.getCurrentTab();
        for (Map.Entry<ChatTab, Button> entry : skyblockchatfilter$tabButtons.entrySet()) {
            ChatTab tab = entry.getKey();
            Button button = entry.getValue();
            int unread = ChatTabManager.getUnreadCount(tab);

            String label = tab == active
                    ? "> " + tab.getDisplayName()
                    : unread > 0
                        ? tab.getDisplayName() + " (" + (unread > 9 ? "9+" : unread) + ")"
                        : tab.getDisplayName();
            button.setMessage(Component.literal(label));
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (ChatTabManager.isEnabled()) {
            int barY = this.height - 24;
            if (mouseY >= barY - 1 && mouseY <= barY + 15) {
                ChatTabManager.cycleTab(verticalAmount < 0);
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
}
