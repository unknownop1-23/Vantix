package com.vtx.vantix.features.chocolate;

import com.vtx.vantix.core.VNTXConfig;
import com.vtx.vantix.init.RegisterEvents;
import com.vtx.vantix.features.overlays.Timer;
import com.vtx.vantix.utils.data.SkyblockData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

@RegisterEvents
public class ChocolateEggTimer extends Timer {

    private static long goal = 0L;
    private static final ResourceLocation EGG_HUNT_ICON = new ResourceLocation("vantix", "skyblock/egg_hunt.png");

    @Override
    public boolean shouldShow() {
        if (VNTXConfig.feature == null || VNTXConfig.feature.chocolateFactory == null) return false;
        return SkyblockData.isSkyblock() &&
                VNTXConfig.feature.chocolateFactory.chocolateEggTimer &&
                SkyblockData.getSeason() == SkyblockData.Season.SPRING;
    }

    @Override
    public ResourceLocation getIcon() {
        return EGG_HUNT_ICON;
    }

    @Override
    public long getGoalEpochMs() {
        return goal;
    }

    public static void setGoalEpochMs(long goal) {
        ChocolateEggTimer.goal = goal;
    }

    @Override
    public boolean getTextShadow() {
        return true;
    }

    @Override
    public int getX() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int x = VNTXConfig.feature.chocolateFactory.eggTimerPos.getAbsX(sr, this.getObjectWidth());
        return x - getObjectWidth()/2;
    }

    @Override
    public int getY() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int y = VNTXConfig.feature.chocolateFactory.eggTimerPos.getAbsY(sr, this.getObjectHeight());
        return y - getObjectHeight()/2;
    }

    @Override
    public float getScale() {
        return VNTXConfig.feature.chocolateFactory.eggTimerScale;
    }

    @Override
    public int getTextColor(long deltaMs) {
        if (deltaMs < 0) return 0xFFFFFF55;
        if (deltaMs < 60000L) return 0xFFFF5555;
        return 0xFFFFAA00;
    }
}