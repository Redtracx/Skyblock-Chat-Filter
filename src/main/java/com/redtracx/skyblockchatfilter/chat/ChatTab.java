package com.redtracx.skyblockchatfilter.chat;

import net.minecraft.util.Formatting;

public enum ChatTab {
    ALL("All", Formatting.WHITE, ""),
    PARTY("Party", Formatting.BLUE, "/pc "),
    GUILD("Guild", Formatting.DARK_GREEN, "/gc "),
    COOP("Co-op", Formatting.AQUA, "/coopchat "),
    DMS("DMs", Formatting.LIGHT_PURPLE, "/r ");

    private final String displayName;
    private final Formatting formatting;
    private final String chatPrefix;

    ChatTab(String displayName, Formatting formatting, String chatPrefix) {
        this.displayName = displayName;
        this.formatting = formatting;
        this.chatPrefix = chatPrefix;
    }

    public String getDisplayName() { return displayName; }
    public Formatting getFormatting() { return formatting; }
    public String getChatPrefix() { return chatPrefix; }

    public int getActiveColor() {
        Integer color = formatting.getColorValue();
        return color != null ? color | 0xFF000000 : 0xFFFFFFFF;
    }

    public boolean matches(String rawMessage) {
        return switch (this) {
            case ALL -> true;
            case PARTY -> rawMessage.contains("party >");
            case GUILD -> rawMessage.contains("guild >");
            case COOP -> rawMessage.contains("co-op >");
            case DMS -> (rawMessage.startsWith("from ") || rawMessage.startsWith("to "))
                        && rawMessage.contains(": ");
        };
    }
}
