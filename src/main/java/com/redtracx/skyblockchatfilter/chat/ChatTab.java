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
            case PARTY -> rawMessage.contains("party >") || isPartySystemMessage(rawMessage);
            case GUILD -> rawMessage.contains("guild >");
            case COOP -> rawMessage.contains("co-op >");
            case DMS -> (rawMessage.startsWith("from ") || rawMessage.startsWith("to "))
                        && rawMessage.contains(": ");
        };
    }

    private static boolean isPartySystemMessage(String raw) {
        return raw.contains("has invited") && raw.contains("to the party")
            || raw.contains("joined the party")
            || raw.contains("has left the party")
            || raw.contains("was removed from the party")
            || raw.contains("was kicked from the party")
            || raw.contains("the party was disbanded")
            || raw.contains("party leader")
            || raw.contains("transferred to")
            || raw.contains("you have joined")
            || raw.contains("you left the party")
            || raw.contains("disbanded the party")
            || raw.contains("party finder")
            || raw.startsWith("party members");
    }
}
