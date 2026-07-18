package com.vtx.vantix.features.diana.overlays;

import com.vtx.vantix.core.VNTXConfig;
import com.vtx.vantix.core.moulconfig.editors.ChromaColour;
import com.vtx.vantix.features.diana.DianaStats;
import com.vtx.vantix.features.diana.LootshareDetect;
import com.vtx.vantix.init.RegisterEvents;
import com.vtx.vantix.utils.Position;
import com.vtx.vantix.utils.overlay.Overlay;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RegisterEvents
public class DianaMobHealthOverlay extends Overlay {

    @Getter
    private static DianaMobHealthOverlay instance;

    public DianaMobHealthOverlay() {
        super(180, LINE_HEIGHT + PADDING * 2);
        instance = this;
    }

    @Override
    protected int getBaseWidth() {
        return 180;
    }

    @Override
    public Position getPosition() {
        return VNTXConfig.feature.diana.dianaMobHp.dianaMobHealthPos;
    }

    @Override
    public float getScale() {
        return VNTXConfig.feature.diana.dianaMobHp.mobScale;
    }

    @Override
    public int getBgColor() {
        return ChromaColour.specialToChromaRGB(VNTXConfig.feature.diana.dianaMobHp.mobBgColor);
    }

    @Override
    public int getCornerRadius() {
        return VNTXConfig.feature.diana.dianaMobHp.mobCornerRadius;
    }

    @Override
    protected boolean extraGuard() {
        DianaStats s = DianaStats.getInstance();
        return s.isTracking() && s.isDianaMayor();
    }

    @Override
    protected boolean isEnabled() {
        return VNTXConfig.feature.diana.enabled && VNTXConfig.feature.diana.dianaMobHp.showDianaMobHealthOverlay;
    }

    @Override
    public List<String> getLines(boolean preview) {
        if (preview) return Collections.singletonList("§2[Lv260] ✤✿ Gaia Construct 839.6k§f/§a1.5M§c❤");

        String raw = LootshareDetect.getClosestNonInqMobName();
        if (raw == null) return new ArrayList<>();
        return Collections.singletonList(raw);
    }
}