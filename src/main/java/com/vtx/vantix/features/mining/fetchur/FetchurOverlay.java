package com.vtx.vantix.features.mining.fetchur;

import com.vtx.vantix.core.VNTXConfig;
import com.vtx.vantix.core.moulconfig.editors.ChromaColour;
import com.vtx.vantix.utils.Position;
import com.vtx.vantix.features.scoreboard.CustomScoreboard;
import com.vtx.vantix.init.RegisterEvents;
import com.vtx.vantix.utils.data.SkyblockData;
import com.vtx.vantix.utils.overlay.Overlay;
import lombok.Getter;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collections;
import java.util.List;

@RegisterEvents
public class FetchurOverlay extends Overlay {

    @Getter
    private static FetchurOverlay instance;

    public FetchurOverlay() {
        super(160, LINE_HEIGHT + PADDING * 2);
        instance = this;
    }

    @Override
    protected int getBaseWidth() {
        return 160;
    }

    @Override
    public Position getPosition() {
        return VNTXConfig.feature.mining.fetchur.fetchurOverlayPos;
    }

    @Override
    public float getScale() {
        return VNTXConfig.feature.mining.fetchur.fetchurOverlayScale;
    }

    @Override
    public int getBgColor() {
        return ChromaColour.specialToChromaRGB(VNTXConfig.feature.mining.fetchur.overlayBgColor);
    }

    @Override
    public int getCornerRadius() {
        return VNTXConfig.feature.mining.fetchur.overlayCornerRadius;
    }

    @Override
    protected boolean extraGuard() {
        return SkyblockData.isOnSkyblock() && !CustomScoreboard.isActive();
    }

    @Override
    protected boolean isEnabled() {
        return VNTXConfig.feature.mining.fetchur.showFetchurOverlay;
    }

    @Override
    public List<String> getLines(boolean preview) {
        String item = preview ? "Yellow Stained Glass x20" : FetchurData.getTodaysItem();
        return Collections.singletonList(EnumChatFormatting.GOLD + "Fetchur: " + EnumChatFormatting.YELLOW + item);
    }
}