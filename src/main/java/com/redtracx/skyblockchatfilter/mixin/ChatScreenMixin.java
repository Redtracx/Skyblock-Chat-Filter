package com.redtracx.skyblockchatfilter.mixin;

import com.redtracx.skyblockchatfilter.chat.ChatTab;
import com.redtracx.skyblockchatfilter.chat.ChatTabManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Note: ChatScreen itself does not declare mouseClicked/keyPressed/mouseScrolled
// (they're inherited from Screen), which is why this mixin extends Screen and
// fully overrides them instead of using @Inject (Mixin can't @Inject into a
// method a class doesn't declare itself). If a future Minecraft version adds
// one of these directly to ChatScreen, that specific override needs to become
// an @Inject/@Redirect instead, or Mixin will fail to apply with a duplicate
// method error.
@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {

    protected ChatScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void skyblockchatfilter$renderChatTabs(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!ChatTabManager.isEnabled()) return;

        int barY = this.height - 26;
        guiGraphics.fill(2, barY - 1, this.width - 2, barY + 11, 0x80000000);

        int x = 6;
        for (ChatTab tab : ChatTab.values()) {
            String name = tab.getDisplayName();
            int textWidth = this.font.width(name);
            int unread = ChatTabManager.getUnreadCount(tab);

            boolean active = tab == ChatTabManager.getCurrentTab();
            boolean hovered = mouseX >= x - 2 && mouseX < x + textWidth + 2
                    && mouseY >= barY - 1 && mouseY < barY + 11;

            int color;
            if (active) color = tab.getActiveColor();
            else if (hovered) color = 0xFFDDDDDD;
            else if (unread > 0) color = 0xFFBBBBBB;
            else color = 0xFF888888;

            guiGraphics.drawString(this.font, name, x, barY + 1, color, true);

            if (active) {
                guiGraphics.fill(x - 1, barY + 10, x + textWidth + 1, barY + 11, tab.getActiveColor());
            }

            int badgeWidth = 0;
            if (unread > 0 && !active) {
                String badge = unread > 9 ? "9+" : String.valueOf(unread);
                guiGraphics.drawString(this.font, badge, x + textWidth + 2, barY, 0xFFFF5555, true);
                badgeWidth = this.font.width(badge) + 2;
            }

            x += textWidth + badgeWidth + 12;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && ChatTabManager.isEnabled()) {
            int barY = this.height - 26;
            if (mouseY >= barY - 1 && mouseY <= barY + 11) {
                int x = 6;
                for (ChatTab tab : ChatTab.values()) {
                    int textWidth = this.font.width(tab.getDisplayName());
                    if (mouseX >= x - 2 && mouseX < x + textWidth + 2) {
                        ChatTabManager.setCurrentTab(tab);
                        return true;
                    }
                    x += textWidth + 12;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (ChatTabManager.isEnabled() && (modifiers & GLFW.GLFW_MOD_ALT) != 0) {
            if (keyCode == GLFW.GLFW_KEY_RIGHT) {
                ChatTabManager.cycleTab(true);
                return true;
            } else if (keyCode == GLFW.GLFW_KEY_LEFT) {
                ChatTabManager.cycleTab(false);
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (ChatTabManager.isEnabled()) {
            int barY = this.height - 26;
            if (mouseY >= barY - 1 && mouseY <= barY + 11) {
                ChatTabManager.cycleTab(verticalAmount < 0);
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
}
