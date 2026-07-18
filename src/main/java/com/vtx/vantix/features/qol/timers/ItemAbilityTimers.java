package com.vtx.vantix.features.qol.timers;

import com.vtx.vantix.repo.VNTXRepo;
import com.vtx.vantix.repo.RepoHandler;
import com.vtx.vantix.repo.TimerRepo;
import com.vtx.vantix.utils.item.ItemStackFinder;
import com.vtx.vantix.utils.time.TimerManager;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemAbilityTimers {

    private static final TimerManager timerManager = new TimerManager(TimerRepo.getAbilityDurations());

    static {
        RepoHandler.addListener(VNTXRepo.KEY_TIMERS, () -> timerManager.updateDurations(TimerRepo.getAbilityDurations()));
    }

    public static void markActive(String itemId) {
        timerManager.markActive(itemId);
    }

    public static void markActive(String itemId, long durationMs) {
        timerManager.markActive(itemId, durationMs);
    }

    public static long getRemainingMs(String itemId) {
        return timerManager.getRemainingMs(itemId);
    }

    public static boolean isActive(String itemId) {
        return timerManager.isActive(itemId);
    }

    public static List<String> getActiveTimers() {
        return timerManager.getActiveTimers();
    }

    public static ItemStack findItemStack(String itemId) {
        return ItemStackFinder.findItemStack(itemId);
    }
}