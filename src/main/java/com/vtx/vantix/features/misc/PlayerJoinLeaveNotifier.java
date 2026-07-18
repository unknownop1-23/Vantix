package com.vtx.vantix.features.misc;

import com.vtx.vantix.core.VNTXConfig;
import com.vtx.vantix.init.RegisterEvents;
import com.vtx.vantix.utils.chat.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RegisterEvents
public class PlayerJoinLeaveNotifier {

    private static final int TICK_INTERVAL = 20;
    private static final String PREFIX = "§8[Vantix] ";
    private final Set<String> notified = new HashSet<>();
    private int tickCounter = 0;

    private static String format(String template, String playerName) {
        return template.replace("&&", "§").replace("%s", playerName);
    }

    private boolean isEnabled() {
        return VNTXConfig.feature != null && VNTXConfig.feature.misc.playerJoinLeave.enabled && !VNTXConfig.feature.misc.playerJoinLeave.playersList.trim().isEmpty();
    }

    private Set<String> watchList() {
        String raw = VNTXConfig.feature.misc.playerJoinLeave.playersList;
        if (raw == null || raw.trim().isEmpty()) return Collections.emptySet();
        return Arrays.stream(raw.split(",")).map(String::trim).filter(s -> !s.isEmpty()).map(String::toLowerCase).collect(Collectors.toSet());
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if ((tickCounter = (tickCounter + 1) % TICK_INTERVAL) != 0) return;
        if (!isEnabled()) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        String selfName = mc.thePlayer.getGameProfile().getName().toLowerCase();
        Set<String> watch = watchList();

        Set<String> lobby = new HashSet<>();
        for (NetworkPlayerInfo info : mc.thePlayer.sendQueue.getPlayerInfoMap()) {
            String name = info.getGameProfile().getName().toLowerCase();
            if (!name.equals(selfName)) lobby.add(name);
        }

        for (NetworkPlayerInfo info : mc.thePlayer.sendQueue.getPlayerInfoMap()) {
            String lower = info.getGameProfile().getName().toLowerCase();
            if (watch.contains(lower) && lobby.contains(lower) && notified.add(lower)) {
                String display = info.getGameProfile().getName();
                ChatUtils.sendMessage(PREFIX + format(VNTXConfig.feature.misc.playerJoinLeave.joinMessage, display));
            }
        }

        Set<String> left = new HashSet<>(notified);
        left.retainAll(watch);
        left.removeAll(lobby);
        for (String name : left) {
            notified.remove(name);
            ChatUtils.sendMessage(PREFIX + format(VNTXConfig.feature.misc.playerJoinLeave.leaveMessage, name));
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        notified.clear();
    }
}
