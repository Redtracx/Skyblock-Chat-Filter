<div align="center">
  <h1>Skyblock Chat-Filter</h1>
  <p>An intelligent, highly-configurable Chat-Filter and Tab-Manager for Hypixel Skyblock on Minecraft 26.x.</p>
  
  [![Fabric](https://img.shields.io/badge/Platform-Fabric-orange.svg)]()
  [![Minecraft 26.1.x | 26.2.x](https://img.shields.io/badge/Minecraft-26.1.x%20%7C%2026.2.x-brightgreen.svg)]()
</div>

<br/>

**Skyblock Chat-Filter** is a client-side Fabric mod designed to declutter your chat experience on the Hypixel Skyblock network. It intelligently filters out annoying spam, useless notifications, and repetitive dialogue, leaving your chat clean and focusing on what matters.

## ✨ Features
Block out the noise with dedicated toggles for everything you don't care to see, grouped into clear categories in the config screen:
- **Chat Tabs (off by default):** A clickable All/Party/Guild/Co-op/DMs tab bar above the chat input. Click a tab or scroll over the bar to switch — unread messages are badge-counted per tab. Enable it in the config; leave it off for plain, unfiltered chat.
- **Trading & Economy:** Hides Hub Trading spam, lowballers, scam/carry adverts, Fire Sales, Bank Interest payouts, and your own Auction House / Trade confirmations.
- **Party & Guild:** Mutes party join/leave/kick/disband spam and guild join/leave/promotion spam, without hiding actual party or guild chat.
- **Dungeon Notifications:** Clean up Dungeon runs by hiding Blessing messages, Essence drops, Kuudra action spam, boss dialogue and annoying Watcher spam.
- **Combat & Pets:** Hides Autopet/pet level-up spam and ability feedback like "Not enough mana!".
- **Server Popups & Join Alerts:** Easily mute Lobby Join alerts, RNG Drop broadcasts, and Mystery Box discovery messages.
- **Internal Prints & System Alerts:** Hides the "Sending to server", "Profile ID:" strings, Co-op chatter, Stash notifications, and "Placing blocks too fast!" messages.
- **Minions & Events:** Quiet down your minions ("My storage is full!") and disable automatic event reminder broadcasts.

## ⚙️ Configuration
The mod is completely customizable in-game! All filters — including the chat tab bar itself — can be toggled on or off individually based on your preferences.
You only need to install [Mod Menu](https://modrinth.com/mod/modmenu) and [Cloth Config API](https://modrinth.com/mod/cloth-config) (which is bundled in many modpacks but can be installed manually).

*To edit your settings, simply navigate to your `Mods` menu, select `Skyblock Chat-Filter`, and hit the configuration button! Every setting features detailed tooltips and examples to help you understand what it filters.*

## 📥 Installation
Minecraft 26.x's internal APIs have shifted between point releases, so this mod ships as **two separate jars** — grab the one matching your Minecraft version from the [Releases page](https://github.com/redtracx/Skyblock-Chat-Filter/releases):
- `skyblock-chat-filter-26.1-*.jar` for Minecraft 26.1.x
- `skyblock-chat-filter-26.2-*.jar` for Minecraft 26.2.x

Installing the wrong one for your Minecraft version will make Fabric Loader refuse to start with an "incompatible mods" error naming the required version — if you see that, grab the other jar.

1. Ensure you have the **[Fabric Loader](https://fabricmc.net/)** installed for your Minecraft version.
2. Ensure you have downloaded the **[Fabric API](https://modrinth.com/mod/fabric-api)** build matching that same version.
3. Download the matching **Skyblock Chat-Filter** jar from the Releases page.
4. Drop the `.jar` file into your `.minecraft/mods` folder.
5. Launch the game and enjoy a clean chat!

## 📜 License
This project is licensed under the [MIT License](LICENSE). Feel free to use and read the code.
