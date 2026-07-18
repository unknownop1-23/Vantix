package com.vtx.vantix.features.qol.overlays;

import com.vtx.vantix.core.VNTXConfig;
import com.vtx.vantix.core.moulconfig.editors.ChromaColour;
import com.vtx.vantix.utils.Position;
import com.vtx.vantix.utils.overlay.TimerOverlay;
import com.vtx.vantix.features.qol.timers.ItemCooldowns;
import com.vtx.vantix.init.RegisterEvents;
import lombok.Getter;
import net.minecraft.item.ItemStack;

import java.util.List;

@RegisterEvents
public class ItemCooldownOverlay extends TimerOverlay {

    @Getter
    private static ItemCooldownOverlay instance;

    public ItemCooldownOverlay() {
        super();
        instance = this;
    }

    @Override
    public Position getPosition() {
        return VNTXConfig.feature.qol.itemCooldown.itemCooldownPos;
    }

    @Override
    public float getScale() {
        return VNTXConfig.feature.qol.itemCooldown.itemCooldownScale;
    }

    @Override
    public int getBgColor() {
        return ChromaColour.specialToChromaRGB(VNTXConfig.feature.qol.itemCooldown.itemCooldownBgColor);
    }

    @Override
    public int getCornerRadius() {
        return VNTXConfig.feature.qol.itemCooldown.itemCooldownCornerRadius;
    }

    @Override
    protected boolean isEnabled() {
        return VNTXConfig.feature != null && VNTXConfig.feature.qol.itemCooldown.itemCooldownOverlay;
    }

    @Override
    protected String getHeaderText() {
        return "§b§lCooldowns";
    }

    @Override
    protected List<String> getActiveTimers() {
        return ItemCooldowns.getActiveCooldowns();
    }

    @Override
    protected ItemStack findItemStack(String id) {
        return ItemCooldowns.findItemStack(id);
    }

    @Override
    protected long getRemainingMs(String id) {
        return ItemCooldowns.getRemainingMs(id);
    }

    @Override
    protected boolean shouldShowWhenEmpty() {
        return VNTXConfig.feature != null && VNTXConfig.feature.qol.itemCooldown.itemCooldownShowWhenEmpty;
    }

    @Override
    protected String getPreviewItemName() {
        return "§5Example Item";
    }
}