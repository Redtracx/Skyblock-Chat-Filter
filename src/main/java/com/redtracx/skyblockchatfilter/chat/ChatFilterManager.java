package com.redtracx.skyblockchatfilter.chat;

import net.minecraft.text.Text;

public class ChatFilterManager {
    public static boolean shouldHideMessage(Text message) {
        if (com.redtracx.skyblockchatfilter.SkyblockChatFilterClient.config == null) return false;
        
        String rawFilter = message.getString().trim().toLowerCase();
        
        if (com.redtracx.skyblockchatfilter.SkyblockChatFilterClient.config.hideLowballing) {
            // Match messages where the actual text starts with "lb [numbers]" or "lowballing" after the username prefix (e.g. ": " or "> ")
            if (rawFilter.matches(".*(: |> )lb \\d.*") || rawFilter.matches(".*(: |> )lowballing.*")) {
                return true;
            }
        }

        if (com.redtracx.skyblockchatfilter.SkyblockChatFilterClient.config.hideHubTrades) {
            if (rawFilter.contains("ah ") || rawFilter.contains("/ah ") || rawFilter.contains("selling ") || rawFilter.contains("buying ") || rawFilter.contains("trading ")) {
                return true;
            }
        }
        if (com.redtracx.skyblockchatfilter.SkyblockChatFilterClient.config.hideRngPopups) {
            if (rawFilter.contains("crazy rare drop") || rawFilter.contains("rng meter")) {
                return true;
            }
        }
        if (com.redtracx.skyblockchatfilter.SkyblockChatFilterClient.config.hideLobbyJoinMessages) {
            if (rawFilter.contains("joined the lobby!")) {
                return true;
            }
        }
        if (com.redtracx.skyblockchatfilter.SkyblockChatFilterClient.config.hideMysteryBoxMessages) {
            if (rawFilter.contains("found a") && rawFilter.contains("mystery box!")) {
                return true;
            }
        }
        if (com.redtracx.skyblockchatfilter.SkyblockChatFilterClient.config.hideProfileIdMessages) {
            if (rawFilter.startsWith("profile id: ") || rawFilter.startsWith("you are playing on profile:")) {
                return true;
            }
        }
        if (com.redtracx.skyblockchatfilter.SkyblockChatFilterClient.config.hideSendingToServer) {
            if (rawFilter.startsWith("sending to server")) {
                return true;
            }
        }
        if (com.redtracx.skyblockchatfilter.SkyblockChatFilterClient.config.hideMinionSpeech) {
            if (rawFilter.contains("my storage is full!") || rawFilter.contains("i can't reach any") || rawFilter.contains("is almost full!")) {
                return true;
            }
        }
        if (com.redtracx.skyblockchatfilter.SkyblockChatFilterClient.config.hideEventAnnouncements) {
            if (rawFilter.contains("dark auction") || rawFilter.contains("jacob's farming contest") || rawFilter.contains("spooky festival") || rawFilter.contains("mythological ritual")) {
                return true;
            }
        }
        if (com.redtracx.skyblockchatfilter.SkyblockChatFilterClient.config.hideFastClickWarnings) {
            if (rawFilter.contains("this is a little fast") || rawFilter.contains("placing blocks too fast")) {
                return true;
            }
        }
        if (com.redtracx.skyblockchatfilter.SkyblockChatFilterClient.config.hideCoopChatter) {
            if (rawFilter.startsWith("co-op >")) {
                return true;
            }
        }
        
        // Dungeons
        if (com.redtracx.skyblockchatfilter.SkyblockChatFilterClient.config.hideDungeonBlessings) {
            if (rawFilter.contains("your blessing of") || rawFilter.contains("a blessing of") || rawFilter.contains("granted you")) {
                return true;
            }
        }
        if (com.redtracx.skyblockchatfilter.SkyblockChatFilterClient.config.hideEssenceDrops) {
            if (rawFilter.contains("you found a") && rawFilter.contains("essence!")) {
                return true;
            }
        }
        if (com.redtracx.skyblockchatfilter.SkyblockChatFilterClient.config.hideWatcherSpam) {
            if (rawFilter.contains("[boss] the watcher:") && !rawFilter.contains("blood door has been opened") && !rawFilter.contains("you have proven yourself")) {
                // Hides Watcher dialogue but keeps essential stage changes
                return true;
            }
        }

        return false;
    }
}
