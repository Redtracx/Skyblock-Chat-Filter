package com.redtracx.skyblockchatfilter.chat;

import com.redtracx.skyblockchatfilter.SkyblockChatFilterClient;
import com.redtracx.skyblockchatfilter.config.ModConfig;
import net.minecraft.text.Text;

import java.util.Locale;

public class ChatFilterManager {
    public static boolean shouldHideMessage(Text message) {
        ModConfig config = SkyblockChatFilterClient.config;
        if (config == null) return false;

        String rawFilter = message.getString().trim().toLowerCase(Locale.ROOT);
        if (rawFilter.isEmpty()) return false;

        // Match messages where the actual text starts with "lb [numbers]" or "lowballing" after the username prefix (e.g. ": " or "> ")
        if (config.hideLowballing && (rawFilter.matches(".*(: |> )lb \\d.*") || rawFilter.matches(".*(: |> )lowballing.*"))) {
            return true;
        }
        if (config.hideHubTrades && (rawFilter.contains("ah ") || rawFilter.contains("/ah ") || rawFilter.contains("selling ") || rawFilter.contains("buying ") || rawFilter.contains("trading "))) {
            return true;
        }
        if (config.hideRngPopups && (rawFilter.contains("crazy rare drop") || rawFilter.contains("rng meter"))) {
            return true;
        }
        if (config.hideLobbyJoinMessages && rawFilter.contains("joined the lobby!")) {
            return true;
        }
        if (config.hideMysteryBoxMessages && rawFilter.contains("found a") && rawFilter.contains("mystery box!")) {
            return true;
        }
        if (config.hideProfileIdMessages && (rawFilter.startsWith("profile id: ") || rawFilter.startsWith("you are playing on profile:"))) {
            return true;
        }
        if (config.hideSendingToServer && rawFilter.startsWith("sending to server")) {
            return true;
        }
        if (config.hideMinionSpeech && (rawFilter.contains("my storage is full!") || rawFilter.contains("i can't reach any") || rawFilter.contains("is almost full!"))) {
            return true;
        }
        if (config.hideEventAnnouncements && (rawFilter.contains("dark auction") || rawFilter.contains("jacob's farming contest") || rawFilter.contains("spooky festival") || rawFilter.contains("mythological ritual"))) {
            return true;
        }
        if (config.hideFastClickWarnings && (rawFilter.contains("this is a little fast") || rawFilter.contains("placing blocks too fast"))) {
            return true;
        }
        if (config.hideCoopChatter && rawFilter.startsWith("co-op >")) {
            return true;
        }

        // Dungeons
        if (config.hideDungeonBlessings && (rawFilter.contains("your blessing of") || rawFilter.contains("a blessing of") || rawFilter.contains("granted you"))) {
            return true;
        }
        if (config.hideEssenceDrops && rawFilter.contains("you found a") && rawFilter.contains("essence!")) {
            return true;
        }
        // Hides Watcher dialogue but keeps essential stage changes
        if (config.hideWatcherSpam && rawFilter.contains("[boss] the watcher:") && !rawFilter.contains("blood door has been opened") && !rawFilter.contains("you have proven yourself")) {
            return true;
        }
        if (config.hideKuudraActionSpam && (rawFilter.contains("has recovered a supply") || rawFilter.contains("ballista has been built") || rawFilter.contains("is using the cannon") || rawFilter.contains("kuudra's hollow") || rawFilter.contains("token of kuudra"))) {
            return true;
        }
        if (config.hideBossDialogue && rawFilter.startsWith("[boss] ") && !rawFilter.contains("blood door has been opened") && !rawFilter.contains("you have proven yourself")) {
            return true;
        }

        // Trading
        if (config.hideScamAdverts && (rawFilter.contains("quitting skyblock") || rawFilter.contains("discord.gg/") || rawFilter.contains("selling coins") || rawFilter.contains("giveaway") || (rawFilter.contains("visit") && rawFilter.contains("free")))) {
            return true;
        }
        if (config.hideCarryAdverts && (rawFilter.contains("selling") || rawFilter.contains("sell") || rawFilter.contains("free")) && (rawFilter.contains("carry") || rawFilter.contains("carries") || rawFilter.contains("s+"))) {
            return true;
        }
        if (config.hideFireSales && rawFilter.contains("[fire sale]")) {
            return true;
        }
        if (config.hideBankInterest && rawFilter.contains("bank interest")) {
            return true;
        }
        if (config.hideAuctionHouseNotifications && rawFilter.startsWith("auction house >") && (rawFilter.contains("sold") || rawFilter.contains("outbid") || rawFilter.contains("purchased") || rawFilter.contains("expired") || rawFilter.contains("claimed"))) {
            return true;
        }
        if (config.hideTradeNotifications && (rawFilter.contains("trade completed with") || rawFilter.contains("trade was cancelled") || rawFilter.contains("has completed the trade") || rawFilter.contains("is not ready for the trade"))) {
            return true;
        }

        // Social
        if (config.hidePartyNotifications && (rawFilter.contains("has joined the party") || rawFilter.contains("has left the party") || rawFilter.contains("has been removed from the party") || rawFilter.contains("kicked from the party") || rawFilter.contains("party was disbanded") || rawFilter.contains("invited you to join their party"))) {
            return true;
        }
        if (config.hideGuildNotifications && rawFilter.startsWith("guild >") && (rawFilter.contains("joined the guild") || rawFilter.contains("left the guild") || rawFilter.contains("was kicked from the guild") || rawFilter.contains("was promoted") || rawFilter.contains("was demoted") || rawFilter.contains(" joined.") || rawFilter.contains(" left."))) {
            return true;
        }

        // Pets & Combat
        if (config.hidePetMessages && (rawFilter.startsWith("autopet equipped your") || rawFilter.contains("leveled up to lvl"))) {
            return true;
        }
        if (config.hideAbilityMessages && (rawFilter.contains("not enough mana") || rawFilter.contains("used wither impact") || rawFilter.contains("this ability is on cooldown"))) {
            return true;
        }

        if (config.hideStashMessages && (rawFilter.contains("stashed away!") || (rawFilter.contains("added to your") && rawFilter.contains("stash!")) || rawFilter.contains("click here to pick") || rawFilter.contains("type(s) of material(s) stashed!"))) {
            return true;
        }

        return false;
    }
}
