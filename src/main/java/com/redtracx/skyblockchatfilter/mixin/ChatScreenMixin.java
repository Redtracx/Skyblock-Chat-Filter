package com.redtracx.skyblockchatfilter.mixin;

import com.redtracx.skyblockchatfilter.chat.ChatTab;
import com.redtracx.skyblockchatfilter.chat.ChatTabManager;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
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

    @Unique
    private int skyblockchatfilter$barY;

    protected ChatScreenMixin(Component title) {
        super(title);
    }

    // require = 0: if this target ever goes stale again on some future
    // Minecraft point release, the tab bar silently stops appearing instead
    // of crashing the whole game at launch (see the tick() note below for why
    // that's a real risk on this API surface).
    @Inject(method = "init", at = @At("TAIL"), require = 0)
    private void skyblockchatfilter$addTabButtons(CallbackInfo ci) {
        skyblockchatfilter$tabButtons.clear();
        if (!ChatTabManager.isEnabled()) return;

        skyblockchatfilter$barY = skyblockchatfilter$findInputBoxY() - 16;

        int x = 4;
        for (ChatTab tab : ChatTab.values()) {
            int width = this.font.width(tab.getDisplayName()) + 12;
            Button button = Button.builder(Component.literal(tab.getDisplayName()), btn -> ChatTabManager.setCurrentTab(tab))
                    .bounds(x, skyblockchatfilter$barY, width, 14)
                    .build();
            skyblockchatfilter$tabButtons.put(tab, button);
            this.addRenderableWidget(button);
            x += width + 2;
        }
    }

    // The bar sits a fixed distance above the chat input box, wherever that
    // box actually ends up - a hardcoded "screen height minus a constant"
    // position (the previous approach) overlapped the input text once another
    // row (e.g. a search bar from another mod, or a future vanilla addition)
    // pushed the real input box away from its usual spot. Located by field
    // TYPE rather than name, since @Shadow-ing the field by its (unverified)
    // exact name would carry the same stale-mapping risk that broke tick()
    // in 1.1.1 - a missing EditBox here just falls back to a fixed offset
    // instead of crashing.
    @Unique
    private int skyblockchatfilter$findInputBoxY() {
        try {
            for (Field field : this.getClass().getDeclaredFields()) {
                if (EditBox.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    Object value = field.get(this);
                    if (value instanceof EditBox editBox) {
                        return editBox.getY();
                    }
                }
            }
        } catch (ReflectiveOperationException | SecurityException ignored) {
            // fall through to the default below
        }
        return this.height - 12;
    }

    // Refreshing the button labels off tick() instead of render() sidesteps
    // needing to know the exact type of Screen#render's graphics-context
    // parameter (renamed GuiGraphics -> GuiGraphicsExtractor as part of a
    // wider 26.x rendering rework) since this method never draws anything itself.
    //
    // This has to be a plain @Override, not @Inject: ChatScreen doesn't declare
    // tick() itself (it's only inherited from Screen), and @Inject can only
    // attach to a method the mixin's target class declares directly - trying
    // it as @Inject crashes at mixin-apply time with "could not find any
    // targets matching 'tick' in ChatScreen", not at compile time, since the
    // method name is just an annotation string javac never checks.
    @Override
    public void tick() {
        super.tick();
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
        if (ChatTabManager.isEnabled() && !skyblockchatfilter$tabButtons.isEmpty()) {
            if (mouseY >= skyblockchatfilter$barY - 1 && mouseY <= skyblockchatfilter$barY + 15) {
                ChatTabManager.cycleTab(verticalAmount < 0);
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
}
