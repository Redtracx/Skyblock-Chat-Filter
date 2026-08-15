package com.redtracx.skyblockchatfilter.chat;

public enum ChatTab {
    ALL("All", 0xFFFFFFFF),
    PARTY("Party", 0xFF5555FF),
    GUILD("Guild", 0xFF00AA00),
    COOP("Co-op", 0xFF55FFFF),
    DMS("DMs", 0xFFFF55FF);

    private final String displayName;
    private final int activeColor;

    ChatTab(String displayName, int activeColor) {
        this.displayName = displayName;
        this.activeColor = activeColor;
    }

    public String getDisplayName() { return displayName; }
    public int getActiveColor() { return activeColor; }

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
