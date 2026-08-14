package com.redtracx.skyblockchatfilter.chat;

import com.redtracx.skyblockchatfilter.SkyblockChatFilterClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;

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
        if (client == null || client.gui == null) return;

        ChatComponent chat = client.gui.getChat();
        chat.clearMessages(false);

        replaying = true;
        for (Component msg : messageBuffer) {
            if (currentTab == ChatTab.ALL || currentTab.matches(msg.getString().trim().toLowerCase(Locale.ROOT))) {
                chat.addMessage(msg);
            }
        }
        replaying = false;
    }
}
