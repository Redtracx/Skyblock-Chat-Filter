package com.redtracx.skyblockchatfilter.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "skyblockchatfilter")
public class ModConfig implements ConfigData {

    // --- General ---

    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideRngPopups = false;

    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideLobbyJoinMessages = true;

    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideMysteryBoxMessages = true;

    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideProfileIdMessages = true;

    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideSendingToServer = true;

    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideMinionSpeech = true;

    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideEventAnnouncements = true;

    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideFastClickWarnings = true;

    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideStashMessages = true;

    // --- Trading ---

    @ConfigEntry.Category("trading")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideHubTrades = true;

    @ConfigEntry.Category("trading")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideLowballing = true;

    @ConfigEntry.Category("trading")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideScamAdverts = true;

    @ConfigEntry.Category("trading")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideCarryAdverts = true;

    @ConfigEntry.Category("trading")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideFireSales = true;

    @ConfigEntry.Category("trading")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideBankInterest = true;

    @ConfigEntry.Category("trading")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideAuctionHouseNotifications = false;

    @ConfigEntry.Category("trading")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideTradeNotifications = false;

    // --- Social ---

    @ConfigEntry.Category("social")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideCoopChatter = false;

    @ConfigEntry.Category("social")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hidePartyNotifications = false;

    @ConfigEntry.Category("social")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideGuildNotifications = false;

    // --- Combat & Pets ---

    @ConfigEntry.Category("combat")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hidePetMessages = true;

    @ConfigEntry.Category("combat")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideAbilityMessages = false;

    // --- Dungeons ---

    @ConfigEntry.Category("dungeons")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideDungeonBlessings = true;

    @ConfigEntry.Category("dungeons")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideEssenceDrops = false;

    @ConfigEntry.Category("dungeons")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideWatcherSpam = true;

    @ConfigEntry.Category("dungeons")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideKuudraActionSpam = false;

    @ConfigEntry.Category("dungeons")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideBossDialogue = false;

    // We can add more filters here as requested by the user.
}
