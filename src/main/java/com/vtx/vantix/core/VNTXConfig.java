package com.vtx.vantix.core;

import com.vtx.vantix.command.Command;
import com.vtx.vantix.core.moulconfig.editors.GuiPositionEditor;
import com.vtx.vantix.core.moulconfig.gui.GuiScreenElementWrapper;
import com.vtx.vantix.core.moulconfig.gui.config.ConfigEditor;
import com.vtx.vantix.features.chat.chatfilters.ui.ChatFilterGUI;
import com.vtx.vantix.features.diana.GuiDianaOverlayEditor;
import com.vtx.vantix.features.dungeons.DungeonStats;
import com.vtx.vantix.features.dungeons.overlays.DungeonBreakerOverlay;
import com.vtx.vantix.features.dungeons.overlays.DungeonMapOverlay;
import com.vtx.vantix.features.dungeons.reward.RewardAnalyzerOverlay;
import com.vtx.vantix.features.dungeons.rooms.DungeonRoomOverlay;
import com.vtx.vantix.features.farming.BPSOverlay;
import com.vtx.vantix.features.fishing.trophy.TrophyFishOverlay;
import com.vtx.vantix.features.mining.fetchur.FetchurOverlay;
import com.vtx.vantix.features.mining.powder.PowderOverlay;
import com.vtx.vantix.features.mining.powder.PowderStats;
import com.vtx.vantix.features.misc.itemlog.ItemPickupLog;
import com.vtx.vantix.features.misc.PerformanceHUD;
import com.vtx.vantix.features.misc.SearchBar;
import com.vtx.vantix.features.misc.pet.CurrentPetOverlay;
import com.vtx.vantix.features.misc.ghosttracker.GhostOverlay;
import com.vtx.vantix.features.misc.ghosttracker.GhostStats;
import com.vtx.vantix.features.misc.timer.UptimeOverlay;
import com.vtx.vantix.features.qol.overlays.ItemAbilityTimerOverlay;
import com.vtx.vantix.features.qol.overlays.ItemCooldownOverlay;
import com.vtx.vantix.features.qol.overlays.ItemInvincibilityOverlay;
import com.vtx.vantix.features.scoreboard.CustomScoreboard;
import com.vtx.vantix.features.waypoints.WaypointGroupGui;
import com.vtx.vantix.repo.VNTXRepo;
import com.vtx.vantix.repo.RepoHandler;
import com.vtx.vantix.OptionsMenu;
import com.vtx.vantix.features.mining.pristine.PristineOverlay;
import com.vtx.vantix.features.mining.pristine.PristineStats;
import com.vtx.vantix.features.misc.invbuttons.GuiInvButtonEditor;
import com.vtx.vantix.network.PrivacyNoticeScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.File;

public class VNTXConfig {

    public static final KeyBinding openGuiKey = new KeyBinding("Open VNTX GUI", Keyboard.KEY_RMENU, "vantix");
    public static Config feature;
    public static File configDirectory = new File("config/Vantix");
    public static GuiScreen screenToOpen = null;
    private static File configFile;
    private static int screenTicks = 0;
    private static boolean waypointManagerKeyWasDown = false;
    private static boolean powderToggleKeyWasDown = false;
    private static boolean pristineToggleKeyWasDown = false;
    private static boolean registered = false;

    private static boolean isKeyOrMouseDown(int keyCode) {
        if (keyCode == Keyboard.KEY_NONE) return false;
        if (keyCode < 0) return Mouse.isButtonDown(keyCode + 100);
        return Keyboard.isKeyDown(keyCode);
    }

    public static void register() {
        if (registered) return;
        init();
        MinecraftForge.EVENT_BUS.register(new VNTXConfig());
        ClientRegistry.registerKeyBinding(openGuiKey);
        ClientCommandHandler.instance.registerCommand(new Command());
        registered = true;
    }

    public static void init() {
        if (!configDirectory.exists()){
            File oldConfigFolder = new File("config/JustEnoughFakepixel");
            if(oldConfigFolder.exists()){
                oldConfigFolder.renameTo(configDirectory);
            }else {
                configDirectory.mkdirs();
            }
        }
        configFile = new File(configDirectory, "config.json");
        loadConfig();
    }

    private static void loadConfig() {
        if (configFile.exists()) {
            // Uses shared loadSafe for consistent corruption handling
            feature = StorageManager.loadSafe(configFile, Config.class, GsonBuilder.GSON_STRICT);
        }
        if (feature == null) {
            feature = new Config();
            saveConfig();
        }
    }

    public static void saveConfig() {
        // Uses shared saveAtomic — .tmp → verify → atomic rename, same as every other storage
        StorageManager.saveAtomic(configFile, feature, GsonBuilder.GSON_STRICT);
    }

    public static void reloadRepo() {
        RepoHandler.refresh(VNTXRepo.KEY_TIMERS);
        RepoHandler.refresh(VNTXRepo.KEY_PLAYERSIZES);
        RepoHandler.refresh(VNTXRepo.KEY_UPDATE);
        RepoHandler.refresh(VNTXRepo.KEY_TAGS);
    }

    public static void openGui() {
        screenToOpen = new GuiScreenElementWrapper(new ConfigEditor(feature));
    }

    public static void openCategory(String categoryName) {
        screenToOpen = new GuiScreenElementWrapper(new ConfigEditor(feature, categoryName));
    }

    public static void openWaypointGroupGui() {
        screenToOpen = new GuiScreenElementWrapper(new WaypointGroupGui());
    }

    public static void openStatsEditor() {
        if (feature == null) return;
        DungeonStats stats = DungeonStats.getInstance();
        screenToOpen = new GuiPositionEditor(feature.dungeons.dungeonOverlay.statsPos, stats::getOverlayWidth, stats::getOverlayHeight, () -> stats.render(true), VNTXConfig::saveConfig, VNTXConfig::saveConfig).withOverlayScale(feature.dungeons.dungeonOverlay.statsScale).withParent(Minecraft.getMinecraft().currentScreen);
    }

    public static void openDungeonRoomOverlayEditor() {
        if (feature == null) return;
        DungeonRoomOverlay overlay = DungeonRoomOverlay.getInstance();
        if (overlay == null) return;
        screenToOpen = new GuiPositionEditor(feature.dungeons.dungeonRoomOverlayConfig.dungeonRoomOverlayPos, overlay::getOverlayWidth, overlay::getOverlayHeight, () -> overlay.render(true), VNTXConfig::saveConfig, VNTXConfig::saveConfig).withOverlayScale(feature.dungeons.dungeonRoomOverlayConfig.dungeonRoomOverlayScale).withParent(Minecraft.getMinecraft().currentScreen);
    }

    public static void openDungeonAnalyzerOverlayEditor() {
        if (feature == null) return;
        RewardAnalyzerOverlay overlay = RewardAnalyzerOverlay.getInstance();
        if (overlay == null) return;
        screenToOpen = new GuiPositionEditor(feature.dungeons.priceEstimator.analyzerPosition, overlay::getOverlayWidth, overlay::getOverlayHeight, () -> overlay.render(true), VNTXConfig::saveConfig, VNTXConfig::saveConfig).withOverlayScale(feature.dungeons.priceEstimator.overlayScale).withParent(Minecraft.getMinecraft().currentScreen);
    }

    public static void openHudEditor() {
        if (feature == null) return;
        PerformanceHUD hud = PerformanceHUD.getInstance();
        screenToOpen = new GuiPositionEditor(feature.misc.performanceHudConfig.hudPos, hud::getOverlayWidth, hud::getOverlayHeight, () -> hud.render(true), VNTXConfig::saveConfig, VNTXConfig::saveConfig).withOverlayScale(feature.misc.performanceHudConfig.hudScale).withParent(Minecraft.getMinecraft().currentScreen);
    }

    public static void openFetchurEditor() {
        if (feature == null) return;
        FetchurOverlay fetchur = FetchurOverlay.getInstance();
        screenToOpen = new GuiPositionEditor(feature.mining.fetchur.fetchurOverlayPos, fetchur::getOverlayWidth, fetchur::getOverlayHeight, () -> fetchur.render(true), VNTXConfig::saveConfig, VNTXConfig::saveConfig).withOverlayScale(feature.mining.fetchur.fetchurOverlayScale).withParent(Minecraft.getMinecraft().currentScreen);
    }

    public static void openDianaOverlayEditor() {
        if (feature == null) return;
        screenToOpen = new GuiDianaOverlayEditor(Minecraft.getMinecraft().currentScreen, VNTXConfig::saveConfig);
    }

    public static void openScoreboardEditor() {
        if (feature == null) return;
        CustomScoreboard sb = CustomScoreboard.getInstance();
        screenToOpen = new GuiPositionEditor(feature.scoreboard.position, sb::getOverlayWidth, sb::getOverlayHeight, () -> sb.render(true), VNTXConfig::saveConfig, VNTXConfig::saveConfig).withOverlayScale(feature.scoreboard.scale).withParent(Minecraft.getMinecraft().currentScreen);
    }

    public static void openSearchBarEditor() {
        if (feature == null) return;
        SearchBar sb = SearchBar.getInstance();
        screenToOpen = new GuiPositionEditor(feature.misc.searchBarConfig.searchBarPos, sb::getOverlayWidth, sb::getOverlayHeight, () -> sb.render(true), VNTXConfig::saveConfig, VNTXConfig::saveConfig).withParent(Minecraft.getMinecraft().currentScreen);
    }

    public static void openCurrentPetEditor() {
        if (feature == null) return;
        CurrentPetOverlay overlay = CurrentPetOverlay.getInstance();
        if (overlay == null) return;
        overlay.render(true);
        screenToOpen = new GuiPositionEditor(feature.misc.currentPet.currentPetPos, overlay::getOverlayWidth, overlay::getOverlayHeight, () -> overlay.render(true), VNTXConfig::saveConfig, VNTXConfig::saveConfig).withOverlayScale(feature.misc.currentPet.currentPetScale).withParent(Minecraft.getMinecraft().currentScreen);
    }

    public static void openItemPickupLogEditor() {
        if (feature == null) return;
        ItemPickupLog overlay = ItemPickupLog.getInstance();
        if (overlay == null) return;
        overlay.render(true);
        screenToOpen = new GuiPositionEditor(feature.misc.itemPickupLogConfig.itemPickupLogPos, overlay::getOverlayWidth, overlay::getOverlayHeight, () -> overlay.render(true), VNTXConfig::saveConfig, VNTXConfig::saveConfig).withOverlayScale(feature.misc.itemPickupLogConfig.itemPickupLogScale).withParent(Minecraft.getMinecraft().currentScreen);
    }

    public static void openItemCooldownEditor() {
        if (feature == null) return;
        ItemCooldownOverlay overlay = ItemCooldownOverlay.getInstance();
        if (overlay == null) return;
        screenToOpen = new GuiPositionEditor(feature.qol.itemCooldown.itemCooldownPos, overlay::getOverlayWidth, overlay::getOverlayHeight, () -> overlay.render(true), VNTXConfig::saveConfig, VNTXConfig::saveConfig).withOverlayScale(feature.qol.itemCooldown.itemCooldownScale).withParent(Minecraft.getMinecraft().currentScreen);
    }

    public static void openItemAbilityTimerEditor() {
        if (feature == null) return;
        ItemAbilityTimerOverlay overlay = ItemAbilityTimerOverlay.getInstance();
        if (overlay == null) return;
        screenToOpen = new GuiPositionEditor(feature.qol.abilityTimer.itemAbilityTimerPos, overlay::getOverlayWidth, overlay::getOverlayHeight, () -> overlay.render(true), VNTXConfig::saveConfig, VNTXConfig::saveConfig).withOverlayScale(feature.qol.abilityTimer.itemAbilityTimerScale).withParent(Minecraft.getMinecraft().currentScreen);
    }

    public static void openItemInvincibilityEditor() {
        if (feature == null) return;
        ItemInvincibilityOverlay overlay = ItemInvincibilityOverlay.getInstance();
        if (overlay == null) return;
        screenToOpen = new GuiPositionEditor(feature.qol.invincibility.itemInvincibilityPos, overlay::getOverlayWidth, overlay::getOverlayHeight, () -> overlay.render(true), VNTXConfig::saveConfig, VNTXConfig::saveConfig).withOverlayScale(feature.qol.invincibility.itemInvincibilityScale).withParent(Minecraft.getMinecraft().currentScreen);
    }

    public static void openPowderEditor() {
        if (feature == null) return;
        PowderOverlay overlay = PowderOverlay.getInstance();
        if (overlay == null) return;
        screenToOpen = new GuiPositionEditor(feature.mining.powderTrackerConfig.powderOverlayPos, overlay::getOverlayWidth, overlay::getOverlayHeight, () -> overlay.render(true), VNTXConfig::saveConfig, VNTXConfig::saveConfig).withOverlayScale(feature.mining.powderTrackerConfig.powderOverlayScale).withParent(Minecraft.getMinecraft().currentScreen);
    }

    public static void openDungeonBreakerEditor() {
        if (feature == null) return;
        DungeonBreakerOverlay overlay = DungeonBreakerOverlay.getInstance();
        if (overlay == null) return;
        screenToOpen = new GuiPositionEditor(feature.dungeons.dungeonBreaker.dungeonBreakerPos, overlay::getOverlayWidth, overlay::getOverlayHeight, () -> overlay.render(true), VNTXConfig::saveConfig, VNTXConfig::saveConfig).withOverlayScale(feature.dungeons.dungeonBreaker.dungeonBreakerScale).withParent(Minecraft.getMinecraft().currentScreen);
    }

    public static void openDungeonMapEditor(){
        if (feature == null) return;
        DungeonMapOverlay overlay = DungeonMapOverlay.getInstance();
        if (overlay == null) return;
        screenToOpen = new GuiPositionEditor(feature.dungeons.dungeonMapConfig.dungeonMapPos, overlay::getOverlayWidth, overlay::getOverlayHeight, () -> overlay.render(true), VNTXConfig::saveConfig, VNTXConfig::saveConfig).withOverlayScale(feature.dungeons.dungeonMapConfig.scale).withParent(Minecraft.getMinecraft().currentScreen);

    }


    public static void openInvButtonEditor() {
        screenToOpen = new GuiInvButtonEditor();
    }

    public static void openOptionsGui() {
        screenToOpen = new OptionsMenu();
    }

    public static void openTrophyFishEditor() {
        if (feature == null) return;
        TrophyFishOverlay overlay = TrophyFishOverlay.getInstance();
        if (overlay == null) return;
        screenToOpen = new GuiPositionEditor(feature.fishing.trophyFish.trophyFishPos, overlay::getOverlayWidth, overlay::getOverlayHeight, () -> overlay.render(true), VNTXConfig::saveConfig, VNTXConfig::saveConfig).withOverlayScale(feature.fishing.trophyFish.trophyFishScale).withParent(Minecraft.getMinecraft().currentScreen);
    }

    public static void openBpsEditor() {
        if (feature == null) return;
        BPSOverlay overlay = BPSOverlay.getInstance();
        assert overlay != null;
        screenToOpen = new GuiPositionEditor(feature.farming.bps.bpsPosition, overlay::getOverlayWidth, overlay::getOverlayHeight, () -> overlay.render(true), VNTXConfig::saveConfig, VNTXConfig::saveConfig).withOverlayScale(feature.farming.bps.bpsScale).withParent(Minecraft.getMinecraft().currentScreen);
    }

    public static void resetPowderTracker() {
        PowderStats.getInstance().reset();
    }

    public static void openPristineEditor() {
        if (feature == null) return;
        PristineOverlay overlay = PristineOverlay.getInstance();
        if (overlay == null) return;
        screenToOpen = new GuiPositionEditor(feature.mining.pristineTrackerConfig.pristineOverlayPos, overlay::getOverlayWidth, overlay::getOverlayHeight, () -> overlay.render(true), VNTXConfig::saveConfig, VNTXConfig::saveConfig).withOverlayScale(feature.mining.pristineTrackerConfig.pristineOverlayScale).withParent(Minecraft.getMinecraft().currentScreen);
    }

    public static void resetPristineTracker() {
        PristineStats.getInstance().reset();
    }


    public static void openUptimeEditor() {
        if (feature == null) return;
        UptimeOverlay overlay = UptimeOverlay.getInstance();
        if (overlay == null) return;
        screenToOpen = new GuiPositionEditor(feature.misc.uptimeConfig.uptimePos, overlay::getOverlayWidth, overlay::getOverlayHeight, () -> overlay.render(true), VNTXConfig::saveConfig, VNTXConfig::saveConfig).withOverlayScale(feature.misc.uptimeConfig.uptimeScale).withParent(Minecraft.getMinecraft().currentScreen);
    }

    public static void openGhostEditor() {
        if (feature == null) return;
        GhostOverlay overlay = GhostOverlay.getInstance();
        if (overlay == null) return;
        screenToOpen = new GuiPositionEditor(feature.misc.ghostTrackerConfig.ghostOverlayPos, overlay::getOverlayWidth, overlay::getOverlayHeight, () -> overlay.render(true), VNTXConfig::saveConfig, VNTXConfig::saveConfig).withOverlayScale(feature.misc.ghostTrackerConfig.ghostScale).withParent(Minecraft.getMinecraft().currentScreen);
    }

    public static void resetGhostTracker() {
        GhostStats.getInstance().reset();
        com.vtx.vantix.features.misc.ghosttracker.GhostStats.getInstance().reset();
    }

    public static void openChatFilterUI() {
        if (feature == null) return;
        screenToOpen = new ChatFilterGUI();
    }

    public static void openPrivacyNotice() {
        if (feature == null) return;
        Minecraft.getMinecraft().displayGuiScreen(new PrivacyNoticeScreen(Minecraft.getMinecraft().currentScreen));
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (!(event.gui instanceof GuiMainMenu)) return;

        if (VNTXConfig.feature.network.hasSeenPrivacyNotice) return;

        event.gui = new PrivacyNoticeScreen(event.gui);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (Minecraft.getMinecraft().thePlayer == null) return;

        if (screenToOpen != null) {
            screenTicks++;
            if (screenTicks == 5) {
                Minecraft.getMinecraft().displayGuiScreen(screenToOpen);
                screenTicks = 0;
                screenToOpen = null;
            }
        }

        if (openGuiKey.isPressed() && Minecraft.getMinecraft().currentScreen == null) openOptionsGui();

        boolean managerKeyDown = feature != null && isKeyOrMouseDown(feature.waypoints.waypointManagerKey);
        if (managerKeyDown && !waypointManagerKeyWasDown && Minecraft.getMinecraft().currentScreen == null)
            openWaypointGroupGui();
        waypointManagerKeyWasDown = managerKeyDown;

        if (feature != null && isKeyOrMouseDown(feature.mining.powderTrackerConfig.powderToggleKey) && !powderToggleKeyWasDown && Minecraft.getMinecraft().currentScreen == null) {
            PowderStats.getInstance().toggleTracking();
        }

        powderToggleKeyWasDown = feature != null && isKeyOrMouseDown(feature.mining.powderTrackerConfig.powderToggleKey);

        if (feature != null && isKeyOrMouseDown(feature.mining.pristineTrackerConfig.pristineToggleKey) && !pristineToggleKeyWasDown && Minecraft.getMinecraft().currentScreen == null) {
            PristineStats.getInstance().toggleTracking();
        }

        pristineToggleKeyWasDown = feature != null && isKeyOrMouseDown(feature.mining.pristineTrackerConfig.pristineToggleKey);
    }
}