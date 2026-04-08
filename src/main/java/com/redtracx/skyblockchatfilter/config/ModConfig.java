package com.redtracx.skyblockchatfilter.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "skyblockchatfilter")
public class ModConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideHubTrades = true;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideLowballing = true;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideRngPopups = false;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideLobbyJoinMessages = true;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideMysteryBoxMessages = true;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideProfileIdMessages = true;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideSendingToServer = true;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideMinionSpeech = true;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideEventAnnouncements = true;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideFastClickWarnings = true;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideCoopChatter = false;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideDungeonBlessings = true;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideEssenceDrops = false;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideWatcherSpam = true;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideScamAdverts = true;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideCarryAdverts = true;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hidePetMessages = true;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideAbilityMessages = false;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideKuudraActionSpam = false;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideFireSales = true;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideStashMessages = true;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideBossDialogue = false;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideWatchdogAnnouncements = true;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideWarpingMessages = true;

    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip(count = 2)
    public AdvancedSettings advanced = new AdvancedSettings();

    public static class AdvancedSettings {
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean useLegacyMixin = false;
    }
}
