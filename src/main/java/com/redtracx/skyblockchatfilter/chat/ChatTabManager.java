package com.redtracx.skyblockchatfilter.chat;

import com.redtracx.skyblockchatfilter.SkyblockChatFilterClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChatTabManager {
    private static ChatTab currentTab = ChatTab.ALL;
    private static final List<Component> messageBuffer = new ArrayList<>();
    private static final int MAX_BUFFER_SIZE = 200;
    private static boolean replaying = false;
    private static final Map<ChatTab, Integer> unreadCounts = new EnumMap<>(ChatTab.class);

    public static ChatTab getCurrentTab() { return currentTab; }
    public static boolean isReplaying() { return replaying; }

    public static boolean isEnabled() {
        return SkyblockChatFilterClient.config != null
            && SkyblockChatFilterClient.config.enableChatTabs;
    }

    public static void setCurrentTab(ChatTab tab) {
        if (currentTab == tab) return;
        currentTab = tab;
        unreadCounts.put(tab, 0);
        replayMessages();
    }

    public static void cycleTab(boolean forward) {
        ChatTab[] tabs = ChatTab.values();
        int idx = forward
            ? (currentTab.ordinal() + 1) % tabs.length
            : (currentTab.ordinal() - 1 + tabs.length) % tabs.length;
        setCurrentTab(tabs[idx]);
    }

    public static int getUnreadCount(ChatTab tab) {
        return unreadCounts.getOrDefault(tab, 0);
    }

    public static void bufferMessage(Component message) {
        if (replaying) return;
        messageBuffer.add(message);
        while (messageBuffer.size() > MAX_BUFFER_SIZE) {
            messageBuffer.remove(0);
        }
        String raw = message.getString().trim().toLowerCase(Locale.ROOT);
        for (ChatTab tab : ChatTab.values()) {
            if (tab == currentTab || tab == ChatTab.ALL) continue;
            if (tab.matches(raw)) unreadCounts.merge(tab, 1, Integer::sum);
        }
    }

    public static boolean shouldShowInCurrentTab(Component message) {
        if (currentTab == ChatTab.ALL) return true;
        return currentTab.matches(message.getString().trim().toLowerCase(Locale.ROOT));
    }

    private static void replayMessages() {
        Minecraft client = Minecraft.getInstance();
        if (client == null) return;

        ChatComponent chat = getChatComponent(client);
        if (chat == null) return;
        chat.clearMessages(false);

        replaying = true;
        for (Component msg : messageBuffer) {
            if (currentTab == ChatTab.ALL || currentTab.matches(msg.getString().trim().toLowerCase(Locale.ROOT))) {
                addMessage(chat, msg);
            }
        }
        replaying = false;
    }

    // Where the chat component hangs off Gui has moved between 26.x point
    // releases (directly via Gui#getChat() on some, nested one level deeper
    // under a Gui#hud field on others). Reflection here - rather than a hard
    // import of whichever shape one specific version happens to have - lets
    // the same compiled class work across both the 26.1 and 26.2 builds.
    private static ChatComponent getChatComponent(Minecraft client) {
        Object gui = client.gui;
        if (gui == null) return null;

        ChatComponent chat = invokeGetChat(gui);
        if (chat != null) return chat;

        try {
            Field hudField = gui.getClass().getField("hud");
            Object hud = hudField.get(gui);
            if (hud != null) {
                chat = invokeGetChat(hud);
                if (chat != null) return chat;
            }
        } catch (ReflectiveOperationException ignored) {
            // fall through
        }

        return null;
    }

    private static ChatComponent invokeGetChat(Object holder) {
        try {
            Method method = holder.getClass().getMethod("getChat");
            Object result = method.invoke(holder);
            if (result instanceof ChatComponent chat) return chat;
        } catch (ReflectiveOperationException ignored) {
            // fall through
        }
        return null;
    }

    // ChatComponent#addMessage has picked up extra required parameters across
    // 26.x point releases (a GuiMessageSource, a GuiMessageTag, ...) whose
    // exact enum/type isn't documented anywhere reachable here. Reflectively
    // calling whichever overload takes just the message (defaulting the rest
    // to null) avoids hard-coding a signature that may drift again.
    private static void addMessage(ChatComponent chat, Component message) {
        for (Method method : chat.getClass().getMethods()) {
            if (!method.getName().equals("addMessage")) continue;
            Class<?>[] params = method.getParameterTypes();
            if (params.length == 0 || !params[0].isInstance(message)) continue;
            try {
                Object[] args = new Object[params.length];
                args[0] = message;
                method.invoke(chat, args);
                return;
            } catch (ReflectiveOperationException | IllegalArgumentException ignored) {
                // try the next candidate overload
            }
        }
    }
}
