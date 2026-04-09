package com.redtracx.skyblockchatfilter.mixin;

import com.redtracx.skyblockchatfilter.chat.ChatTab;
import com.redtracx.skyblockchatfilter.chat.ChatTabManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {

    @Shadow protected TextFieldWidget chatField;

    protected ChatScreenMixin(Text title) {
        super(title);
    }

    @Unique
    private boolean skyblockchatfilter$wrapping = false;

    @Inject(method = "render", at = @At("TAIL"))
    private void renderChatTabs(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!ChatTabManager.isEnabled()) return;

        int barY = this.height - 26;
        context.fill(2, barY - 1, this.width - 2, barY + 11, 0x80000000);

        int x = 6;
        for (ChatTab tab : ChatTab.values()) {
            String name = tab.getDisplayName();
            int textWidth = this.textRenderer.getWidth(name);
            int unread = ChatTabManager.getUnreadCount(tab);

            boolean active = tab == ChatTabManager.getCurrentTab();
            boolean hovered = mouseX >= x - 2 && mouseX < x + textWidth + 2
                    && mouseY >= barY - 1 && mouseY < barY + 11;

            int color;
            if (active) color = tab.getActiveColor();
            else if (hovered) color = 0xFFDDDDDD;
            else if (unread > 0) color = 0xFFBBBBBB;
            else color = 0xFF888888;

            context.drawTextWithShadow(this.textRenderer, name, x, barY + 1, color);

            // underline for active tab
            if (active) {
                context.fill(x - 1, barY + 10, x + textWidth + 1, barY + 11, tab.getActiveColor());
            }

            // unread badge
            int badgeWidth = 0;
            if (unread > 0 && !active) {
                String badge = unread > 9 ? "9+" : String.valueOf(unread);
                context.drawTextWithShadow(this.textRenderer, badge, x + textWidth + 2, barY, 0xFFFF5555);
                badgeWidth = this.textRenderer.getWidth(badge) + 2;
            }

            x += textWidth + badgeWidth + 12;
        }

        // prefix indicator on the right side
        ChatTab active = ChatTabManager.getCurrentTab();
        if (active != ChatTab.ALL) {
            String hint = "\u00BB " + active.getChatPrefix().trim();
            int hintWidth = this.textRenderer.getWidth(hint);
            context.drawTextWithShadow(this.textRenderer, hint, this.width - hintWidth - 6, barY + 1, active.getActiveColor());
        }
    }

    @Inject(method = "mouseClicked(DDI)Z", at = @At("HEAD"), cancellable = true)
    private void onChatTabClick(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (!ChatTabManager.isEnabled() || button != 0) return;

        int barY = this.height - 26;
        if (mouseY < barY - 1 || mouseY > barY + 11) return;

        int x = 6;
        for (ChatTab tab : ChatTab.values()) {
            int textWidth = this.textRenderer.getWidth(tab.getDisplayName());
            if (mouseX >= x - 2 && mouseX < x + textWidth + 2) {
                ChatTabManager.setCurrentTab(tab);
                cir.setReturnValue(true);
                return;
            }
            x += textWidth + 12;
        }
    }

    // Alt+Left/Right to cycle tabs
    @Inject(method = "keyPressed(III)Z", at = @At("HEAD"), cancellable = true)
    private void onTabKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!ChatTabManager.isEnabled()) return;
        if ((modifiers & GLFW.GLFW_MOD_ALT) == 0) return;

        if (keyCode == GLFW.GLFW_KEY_RIGHT) {
            ChatTabManager.cycleTab(true);
            cir.setReturnValue(true);
        } else if (keyCode == GLFW.GLFW_KEY_LEFT) {
            ChatTabManager.cycleTab(false);
            cir.setReturnValue(true);
        }
    }

    // scroll on tab bar to cycle
    @Inject(method = "mouseScrolled", at = @At("HEAD"), cancellable = true)
    private void onTabBarScroll(double mouseX, double mouseY, double horizontalAmount, double verticalAmount, CallbackInfoReturnable<Boolean> cir) {
        if (!ChatTabManager.isEnabled()) return;

        int barY = this.height - 26;
        if (mouseY < barY - 1 || mouseY > barY + 11) return;

        ChatTabManager.cycleTab(verticalAmount < 0);
        cir.setReturnValue(true);
    }

    @Inject(method = "sendMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), cancellable = true)
    private void wrapChatForTab(String chatText, boolean addToHistory, CallbackInfo ci) {
        if (skyblockchatfilter$wrapping) return;

        String wrapped = ChatTabManager.wrapOutgoingMessage(chatText);
        if (!wrapped.equals(chatText)) {
            ci.cancel();
            skyblockchatfilter$wrapping = true;
            ((ChatScreen)(Object)this).sendMessage(wrapped, addToHistory);
            skyblockchatfilter$wrapping = false;
        }
    }
}
