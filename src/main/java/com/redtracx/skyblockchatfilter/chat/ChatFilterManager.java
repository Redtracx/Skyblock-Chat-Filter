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

        // --- New Community Filters ---

        if (com.redtracx.skyblockchatfilter.SkyblockChatFilterClient.config.hideScamAdverts) {
            if (rawFilter.contains("quitting skyblock") || rawFilter.contains("discord.gg/") || rawFilter.contains("selling coins") || rawFilter.contains("giveaway") || (rawFilter.contains("visit") && rawFilter.contains("free"))) {
                return true;
            }
        }

        if (com.redtracx.skyblockchatfilter.SkyblockChatFilterClient.config.hideCarryAdverts) {
            if ((rawFilter.contains("selling") || rawFilter.contains("sell") || rawFilter.contains("free")) && (rawFilter.contains("carry") || rawFilter.contains("carries") || rawFilter.contains("s+"))) {
                return true;
            }
        }

        if (com.redtracx.skyblockchatfilter.SkyblockChatFilterClient.config.hidePetMessages) {
            if (rawFilter.startsWith("autopet equipped your") || rawFilter.contains("leveled up to lvl")) {
                return true;
            }
        }

        if (com.redtracx.skyblockchatfilter.SkyblockChatFilterClient.config.hideAbilityMessages) {
            if (rawFilter.contains("not enough mana") || rawFilter.contains("used wither impact") || rawFilter.contains("this ability is on cooldown")) {
                return true;
            }
        }

        if (com.redtracx.skyblockchatfilter.SkyblockChatFilterClient.config.hideKuudraActionSpam) {
            if (rawFilter.contains("has recovered a supply") || rawFilter.contains("ballista has been built") || rawFilter.contains("is using the cannon") || rawFilter.contains("kuudra's hollow") || rawFilter.contains("token of kuudra")) {
                return true;
            }
        }

        if (com.redtracx.skyblockchatfilter.SkyblockChatFilterClient.config.hideFireSales) {
            if (rawFilter.contains("[fire sale]")) {
                return true;
            }
        }

        if (com.redtracx.skyblockchatfilter.SkyblockChatFilterClient.config.hideStashMessages) {
            if (rawFilter.contains("stashed away!") || (rawFilter.contains("added to your") && rawFilter.contains("stash!")) || rawFilter.contains("click here to pick") || rawFilter.contains("pick them up") || rawFilter.contains("type(s) of material(s) stashed!") || rawFilter.contains("from your item stash") || rawFilter.contains("material stash")) {
                return true;
            }
        }

        if (com.redtracx.skyblockchatfilter.SkyblockChatFilterClient.config.hideBossDialogue) {
            if (rawFilter.startsWith("[boss] ") && !rawFilter.contains("blood door has been opened") && !rawFilter.contains("you have proven yourself")) {
                return true;
            }
        }

        if (com.redtracx.skyblockchatfilter.SkyblockChatFilterClient.config.hideWatchdogAnnouncements) {
            if (rawFilter.contains("[watchdog announcement]") || rawFilter.contains("watchdog has banned") || rawFilter.contains("staff have banned an additional") || rawFilter.contains("blacklisted modifications are a bannable offense")) {
                return true;
            }
        }

        if (com.redtracx.skyblockchatfilter.SkyblockChatFilterClient.config.hideWarpingMessages) {
            if (rawFilter.startsWith("warping...") || rawFilter.startsWith("request join for")) {
                return true;
            }
        }

        return false;
    }
}
