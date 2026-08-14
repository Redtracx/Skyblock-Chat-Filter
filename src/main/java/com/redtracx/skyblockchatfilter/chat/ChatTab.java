package com.redtracx.skyblockchatfilter.chat;

import net.minecraft.ChatFormatting;

public enum ChatTab {
    ALL("All", ChatFormatting.WHITE),
    PARTY("Party", ChatFormatting.BLUE),
    GUILD("Guild", ChatFormatting.DARK_GREEN),
    COOP("Co-op", ChatFormatting.AQUA),
    DMS("DMs", ChatFormatting.LIGHT_PURPLE);

    private final String displayName;
    private final ChatFormatting formatting;

    ChatTab(String displayName, ChatFormatting formatting) {
        this.displayName = displayName;
        this.formatting = formatting;
    }

    public String getDisplayName() { return displayName; }

    public int getActiveColor() {
        Integer color = formatting.getColor();
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
