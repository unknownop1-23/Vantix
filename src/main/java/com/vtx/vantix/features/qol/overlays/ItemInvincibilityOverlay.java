package com.vtx.vantix.features.qol.overlays;

import com.vtx.vantix.core.VNTXConfig;
import com.vtx.vantix.core.moulconfig.editors.ChromaColour;
import com.vtx.vantix.utils.Position;
import com.vtx.vantix.utils.overlay.TimerOverlay;
import com.vtx.vantix.features.qol.timers.ItemInvincibilityTimers;
import com.vtx.vantix.init.RegisterEvents;
import lombok.Getter;
import net.minecraft.item.ItemStack;

import java.util.List;

@RegisterEvents
public class ItemInvincibilityOverlay extends TimerOverlay {

    @Getter
    private static ItemInvincibilityOverlay instance;

    public ItemInvincibilityOverlay() {
        super();
        instance = this;
    }

    @Override
    public Position getPosition() {
        return VNTXConfig.feature.qol.invincibility.itemInvincibilityPos;
    }

    @Override
    public float getScale() {
        return VNTXConfig.feature.qol.invincibility.itemInvincibilityScale;
    }

    @Override
    public int getBgColor() {
        return ChromaColour.specialToChromaRGB(VNTXConfig.feature.qol.invincibility.itemInvincibilityBgColor);
    }

    @Override
    public int getCornerRadius() {
        return VNTXConfig.feature.qol.invincibility.itemInvincibilityCornerRadius;
    }

    @Override
    protected boolean isEnabled() {
        return VNTXConfig.feature != null && VNTXConfig.feature.qol.invincibility.itemInvincibilityOverlay;
    }

    @Override
    protected String getHeaderText() {
        return "§b§lInvincibility";
    }

    @Override
    protected List<String> getActiveTimers() {
        return ItemInvincibilityTimers.getActiveTimers();
    }

    @Override
    protected ItemStack findItemStack(String id) {
        return ItemInvincibilityTimers.findItemStack(id);
    }

    @Override
    protected long getRemainingMs(String id) {
        return ItemInvincibilityTimers.getRemainingMs(id);
    }

    @Override
    protected boolean shouldShowWhenEmpty() {
        return VNTXConfig.feature != null && VNTXConfig.feature.qol.invincibility.itemInvincibilityShowWhenEmpty;
    }

    @Override
    protected String getPreviewItemName() {
        return "§5Bonzo's Mask";
    }
}