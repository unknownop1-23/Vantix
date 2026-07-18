package com.vtx.vantix;

import com.vtx.vantix.features.chat.chatfilters.ChatFilterManager;
import com.vtx.vantix.features.misc.itemList.ItemRegistry;
import com.vtx.vantix.core.VNTXConfig;
import com.vtx.vantix.core.StorageManager;
import com.vtx.vantix.data.ApiHandler;
import com.vtx.vantix.features.capes.CapeManager;
import com.vtx.vantix.features.dungeons.caseopening.CitManager;
import com.vtx.vantix.features.misc.invbuttons.SkyblockItemCache;
import com.vtx.vantix.features.misc.pet.PetCache;
import com.vtx.vantix.features.profile.GuiWaiter;
import com.vtx.vantix.init.EventRegistrar;
import com.vtx.vantix.repo.VNTXRepo;
import com.vtx.vantix.repo.RepoHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.logging.Logger;

@Mod(modid = Vantix.MODID, name = Vantix.NAME, version = Vantix.VERSION, clientSideOnly = true, guiFactory = "com.vtx.vantix.GuiFactory")
public class Vantix {

    public static final String MODID = "vantix";
    public static final String NAME = "Vantix";
    public static final String VERSION = "1.1.0";

    public static VNTXConfig config;
    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        VNTXConfig.init();
        VNTXRepo.init();
        logger = Logger.getLogger("[VNTX] ");
        StorageManager.initAll(VNTXConfig.configDirectory);
        CapeManager.initialise(false);
        TesterWhitelist.init(VERSION);
    }

    @Mod.EventHandler
    public void clientInit(FMLInitializationEvent event) {
        VNTXConfig.register();
        StorageManager.loadAll();
        StorageManager.startAutoSave();
        SkyblockItemCache.getInstance().loadAsync();
        ItemRegistry.initialise();
        ChatFilterManager.initialise();
        new CitManager();
        if (VNTXConfig.feature.misc.currentPet.showCurrentPet) PetCache.getInstance().warmupTextures();
        MinecraftForge.EVENT_BUS.register(GuiWaiter.INSTANCE);
        MinecraftForge.EVENT_BUS.register(this);
        EventRegistrar.registerAll();
    }

    @SubscribeEvent
    public void onServerJoin(FMLNetworkEvent.ClientConnectedToServerEvent e) {
        RepoHandler.refresh(VNTXRepo.KEY_PLAYERSIZES);
        RepoHandler.refresh(VNTXRepo.KEY_TIMERS);
        RepoHandler.refresh(VNTXRepo.KEY_UPDATE);
        ApiHandler.onServerJoin();
    }
}