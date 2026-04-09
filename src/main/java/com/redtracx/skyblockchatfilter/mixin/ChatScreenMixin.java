package com.redtracx.skyblockchatfilter.mixin;

import com.redtracx.skyblockchatfilter.SkyblockChatFilterClient;
import com.redtracx.skyblockchatfilter.chat.ChatTab;
import com.redtracx.skyblockchatfilter.chat.ChatTabManager;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
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

import java.util.List;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import org.joml.Matrix4f;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {

    @Shadow
    protected TextFieldWidget chatField;

    protected ChatScreenMixin(Text title) {
        super(title);
    }

    @Unique
    private boolean skyblockchatfilter$wrapping = false;

    @Unique
    private void skyblockchatfilter$drawText(DrawContext context, String text, int x, int y, int color) {
        Matrix4f matrix = context.getMatrices().peek().getPositionMatrix();
        VertexConsumerProvider.Immediate immediate = context.getVertexConsumers();
        this.textRenderer.draw(text, (float) x, (float) y, color, true,
                matrix, immediate, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void renderChatTabs(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!ChatTabManager.isEnabled())
            return;

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
            if (active)
                color = tab.getActiveColor();
            else if (hovered)
                color = 0xFFDDDDDD;
            else if (unread > 0)
                color = 0xFFBBBBBB;
            else
                color = 0xFF888888;

            skyblockchatfilter$drawText(context, name, x, barY + 1, color);

            // underline for active tab
            if (active) {
                context.fill(x - 1, barY + 10, x + textWidth + 1, barY + 11, tab.getActiveColor());
            }

            // unread badge
            int badgeWidth = 0;
            if (unread > 0 && !active) {
                String badge = unread > 9 ? "9+" : String.valueOf(unread);
                skyblockchatfilter$drawText(context, badge, x + textWidth + 2, barY, 0xFFFF5555);
                badgeWidth = this.textRenderer.getWidth(badge) + 2;
            }

            x += textWidth + badgeWidth + 12;
        }

        // prefix indicator on the right side
        ChatTab active = ChatTabManager.getCurrentTab();
        if (active != ChatTab.ALL) {
            String hint = "\u00BB " + active.getChatPrefix().trim();
            int hintWidth = this.textRenderer.getWidth(hint);
            skyblockchatfilter$drawText(context, hint, this.width - hintWidth - 6, barY + 1, active.getActiveColor());
        }
    }

    // ChatScreen doesn't override mouseClicked in 1.21.1, so @Inject won't work
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // tab click (left-click)
        if (button == 0 && ChatTabManager.isEnabled()) {
            int barY = this.height - 26;
            if (mouseY >= barY - 1 && mouseY <= barY + 11) {
                int x = 6;
                for (ChatTab tab : ChatTab.values()) {
                    int textWidth = this.textRenderer.getWidth(tab.getDisplayName());
                    if (mouseX >= x - 2 && mouseX < x + textWidth + 2) {
                        ChatTabManager.setCurrentTab(tab);
                        return true;
                    }
                    x += textWidth + 12;
                }
            }
        }

        // right-click copy
        if (button == 1 && SkyblockChatFilterClient.config != null
                && SkyblockChatFilterClient.config.enableRightClickCopy
                && this.client != null && this.client.inGameHud != null) {
            ChatHud chatHud = this.client.inGameHud.getChatHud();

            // public API check — confirms mouse is over a valid chat line
            if (chatHud.getTextStyleAt(mouseX, mouseY) != null) {
                ChatHudAccessor accessor = (ChatHudAccessor) chatHud;
                int scaledHeight = this.client.getWindow().getScaledHeight();
                double chatScale = chatHud.getChatScale();

                // replicate toChatLineY + getMessageIndex
                double chatLineY = ((double) scaledHeight - mouseY - 40.0) / chatScale;
                int visibleIndex = (int) (chatLineY / 9.0) + accessor.getScrolledLines();

                List<ChatHudLine.Visible> visibleMsgs = accessor.getVisibleMessages();
                List<ChatHudLine> messages = accessor.getMessages();

                if (visibleIndex >= 0 && visibleIndex < visibleMsgs.size()) {
                    // walk visible lines to find the corresponding full message
                    int msgIndex = 0;
                    for (int i = 0; i < visibleIndex; i++) {
                        if (visibleMsgs.get(i).endOfEntry())
                            msgIndex++;
                    }
                    if (msgIndex < messages.size()) {
                        String text = messages.get(msgIndex).content().getString();
                        this.client.keyboard.setClipboard(text);
                        if (this.client.player != null) {
                            this.client.player.sendMessage(
                                    Text.literal("\u00A7a[ChatFilter] \u00A77Copied to clipboard"), true);
                        }
                        return true;
                    }
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    // ChatScreen doesn't override keyPressed in 1.21.1
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

    // ChatScreen doesn't override mouseScrolled in 1.21.1
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

    @Inject(method = "sendMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), cancellable = true)
    private void wrapChatForTab(String chatText, boolean addToHistory, CallbackInfo ci) {
        if (skyblockchatfilter$wrapping)
            return;

        String wrapped = ChatTabManager.wrapOutgoingMessage(chatText);
        if (!wrapped.equals(chatText)) {
            ci.cancel();
            skyblockchatfilter$wrapping = true;
            ((ChatScreen) (Object) this).sendMessage(wrapped, addToHistory);
            skyblockchatfilter$wrapping = false;
        }
    }
}
