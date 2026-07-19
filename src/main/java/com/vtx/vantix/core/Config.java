package com.vtx.vantix.core;

import com.google.gson.annotations.Expose;
import com.vtx.vantix.core.features.Chocolate.ChocolateFactoryConfig;
import com.vtx.vantix.core.moulconfig.gui.config.ConfigAnnotations.Category;
import com.vtx.vantix.core.features.about.About;
import com.vtx.vantix.core.features.chat.ChatConfig;
import com.vtx.vantix.core.features.cosmetics.Cosmetics;
import com.vtx.vantix.core.features.debug.Debug;
import com.vtx.vantix.core.features.diana.Diana;
import com.vtx.vantix.core.features.dungeons.Dungeons;
import com.vtx.vantix.core.features.fishing.Fishing;
import com.vtx.vantix.core.features.misc.Misc;
import com.vtx.vantix.core.features.mining.Mining;
import com.vtx.vantix.core.features.overlays.Overlays;
import com.vtx.vantix.core.features.qol.Qol;
import com.vtx.vantix.core.features.waypoints.Waypoints;
import com.vtx.vantix.core.features.farming.Farming;
import com.vtx.vantix.core.features.scoreboard.Scoreboard;
import com.vtx.vantix.core.features.storage.Storage;
import com.vtx.vantix.core.features.network.NetworkConfig;
import com.vtx.vantix.features.capes.CapeManager;

import java.awt.*;
import java.net.URI;

public class Config {

    @Expose
    @Category(name = "About", desc = "Links, credits & used software")
    public final About about = new About();

    @Expose
    @Category(name = "Quality of life", desc = "QOL features")
    public final Qol qol = new Qol();

    @Expose
    @Category(name = "Chocolate Factory", desc = "Settings for the Hoppity Chocolate Factory.")
    public ChocolateFactoryConfig chocolateFactory = new ChocolateFactoryConfig();

    @Expose
    @Category(name = "Scoreboard", desc = "Custom scoreboard panel")
    public final Scoreboard scoreboard = new Scoreboard();

    @Expose
    @Category(name = "Chat Utils", desc = "Chat compacting, timestamps, chat heads, copy & visual tweaks")
    public final ChatConfig chat = new ChatConfig();

    @Expose
    @Category(name = "Misc", desc = "Misc features")
    public final Misc misc = new Misc();

    @Expose
    @Category(name = "Storage", desc = "Storage Overlay features")
    public final Storage storage = new Storage();

    @Expose
    @Category(name = "Cosmetics", desc = "Capes and Cosmetics Feature")
    public final Cosmetics cosmetics = new Cosmetics();

    @Expose
    @Category(name = "Waypoints", desc = "Waypoints config & GUI")
    public final Waypoints waypoints = new Waypoints();

    @Expose
    @Category(name = "Mining", desc = "Mining features")
    public final Mining mining = new Mining();

    @Expose
    @Category(name = "Diana", desc = "Diana event tracking & overlays")
    public final Diana diana = new Diana();

    @Expose
    @Category(name = "Dungeons", desc = "Dungeon features")
    public final Dungeons dungeons = new Dungeons();

    @Expose
    @Category(name = "Farming", desc = "Farming features")
    public final Farming farming = new Farming();

    @Expose
    @Category(name = "Fishing", desc = "Fishing features")
    public final Fishing fishing = new Fishing();

    @Expose
    @Category(name = "Overlays", desc = "Various Overlay features")
    public final Overlays overlays = new Overlays();

    @Expose
    @Category(name = "Privacy", desc = "Manage network calls")
    public final NetworkConfig network = new NetworkConfig();

    @Expose
    @Category(name = "Debug", desc = "Debug tools")
    public final Debug debug = new Debug();

    private static void openUrl(String url) {
        try { Desktop.getDesktop().browse(new URI(url)); } catch (Exception ignored) {}
    }

    public void executeRunnable(String runnableId) {
        switch (runnableId) {
            case "reloadRepo": VNTXConfig.reloadRepo(); break;
            case "openScoreboardEditor": VNTXConfig.openScoreboardEditor(); break;
            case "openWaypointGroupGui": VNTXConfig.openWaypointGroupGui(); break;
            case "openStatsEditor": VNTXConfig.openStatsEditor(); break;
            case "openHudEditor": VNTXConfig.openHudEditor(); break;
            case "openFetchurEditor": VNTXConfig.openFetchurEditor(); break;
            case "openDianaOverlayEditor": VNTXConfig.openDianaOverlayEditor(); break;
            case "openSearchBarEditor": VNTXConfig.openSearchBarEditor(); break;
            case "openCurrentPetEditor": VNTXConfig.openCurrentPetEditor(); break;
            case "openItemPickupLogEditor": VNTXConfig.openItemPickupLogEditor(); break;
            case "openItemCooldownEditor": VNTXConfig.openItemCooldownEditor(); break;
            case "openPowderEditor": VNTXConfig.openPowderEditor(); break;
            case "openInvButtonEditor": VNTXConfig.openInvButtonEditor(); break;
            case "resetPowderTracker": VNTXConfig.resetPowderTracker(); break;
            case "openPristineEditor": VNTXConfig.openPristineEditor(); break;
            case "resetPristineTracker": VNTXConfig.resetPristineTracker(); break;
            case "openDungeonBreakerEditor": VNTXConfig.openDungeonBreakerEditor(); break;
            case "editDungeonMapPos": VNTXConfig.openDungeonMapEditor(); break;
            case "openTrophyFishEditor": VNTXConfig.openTrophyFishEditor(); break;
            case "openDungeonRoomOverlayEditor": VNTXConfig.openDungeonRoomOverlayEditor(); break;
            case "editAnalyzerOverlay": VNTXConfig.openDungeonAnalyzerOverlayEditor(); break;
            case "openItemInvincibilityEditor": VNTXConfig.openItemInvincibilityEditor(); break;
            case "openItemAbilityTimerEditor": VNTXConfig.openItemAbilityTimerEditor(); break;
            case "openBpsEditor": VNTXConfig.openBpsEditor(); break;
            case "openUptimeEditor": VNTXConfig.openUptimeEditor(); break;
            case "openGhostEditor": VNTXConfig.openGhostEditor(); break;
            case "resetGhostTracker": VNTXConfig.resetGhostTracker(); break;
            case "chatFiltersGUI": VNTXConfig.openChatFilterUI(); break;
            case "openPrivacyNotice": VNTXConfig.openPrivacyNotice(); break;
            case "reloadCapes": CapeManager.reload(); break;
            case "openWebsite": openUrl("https://aetheria.github.io"); break;
            case "openDiscord": openUrl("https://discord.gg/tdMFbmhFTb"); break;
            case "openGithub": openUrl("https://github.com/aetheria-org/Aetheria"); break;
            case "openLicenseForge": openUrl("https://github.com/MinecraftForge/MinecraftForge"); break;
            case "openLicenseMixin": openUrl("https://github.com/SpongePowered/Mixin/"); break;
            case "openLicenseMoulConfig": openUrl("https://github.com/NotEnoughUpdates/MoulConfig"); break;
            case "openLicenseLombok": openUrl("https://projectlombok.org/"); break;
            case "openLicenseJbAnnotations": openUrl("https://github.com/JetBrains/java-annotations"); break;
            case "openModrinth": openUrl("https://modrinth.com/mod/aetheriamod"); break;
            case "openSkyAtlas": openUrl("https://skyatlas.qzz.io"); break;
        }
    }
}