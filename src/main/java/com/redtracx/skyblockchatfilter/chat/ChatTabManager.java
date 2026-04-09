package com.redtracx.skyblockchatfilter.chat;

import com.redtracx.skyblockchatfilter.SkyblockChatFilterClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ChatTabManager {
    private static ChatTab currentTab = ChatTab.ALL;
    private static final List<Text> messageBuffer = new ArrayList<>();
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

    public static void bufferMessage(Text message) {
        if (replaying) return;
        messageBuffer.add(message);
        while (messageBuffer.size() > MAX_BUFFER_SIZE) {
            messageBuffer.remove(0);
        }
        // track unread for inactive tabs
        String raw = message.getString().trim().toLowerCase();
        for (ChatTab tab : ChatTab.values()) {
            if (tab == currentTab || tab == ChatTab.ALL) continue;
            if (tab.matches(raw)) unreadCounts.merge(tab, 1, Integer::sum);
        }
    }

    public static boolean shouldShowInCurrentTab(Text message) {
        if (currentTab == ChatTab.ALL) return true;
        String raw = message.getString().trim().toLowerCase();
        return currentTab.matches(raw);
    }

    public static String wrapOutgoingMessage(String text) {
        if (!isEnabled() || currentTab == ChatTab.ALL || text.startsWith("/")) return text;
        return currentTab.getChatPrefix() + text;
    }

    private static void replayMessages() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.inGameHud == null) return;

        ChatHud chatHud = client.inGameHud.getChatHud();
        chatHud.clear(false);

        replaying = true;
        for (Text msg : messageBuffer) {
            if (currentTab == ChatTab.ALL || currentTab.matches(msg.getString().trim().toLowerCase())) {
                chatHud.addMessage(msg);
            }
        }
        replaying = false;
    }
}
