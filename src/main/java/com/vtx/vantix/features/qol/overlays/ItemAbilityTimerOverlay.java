package com.vtx.vantix.features.qol.overlays;

import com.vtx.vantix.core.VNTXConfig;
import com.vtx.vantix.core.moulconfig.editors.ChromaColour;
import com.vtx.vantix.utils.Position;
import com.vtx.vantix.utils.overlay.TimerOverlay;
import com.vtx.vantix.features.qol.timers.ItemAbilityTimers;
import com.vtx.vantix.init.RegisterEvents;
import lombok.Getter;
import net.minecraft.item.ItemStack;

import java.util.List;

@RegisterEvents
public class ItemAbilityTimerOverlay extends TimerOverlay {

    @Getter
    private static ItemAbilityTimerOverlay instance;

    public ItemAbilityTimerOverlay() {
        super();
        instance = this;
    }

    @Override
    public Position getPosition() {
        return VNTXConfig.feature.qol.abilityTimer.itemAbilityTimerPos;
    }

    @Override
    public float getScale() {
        return VNTXConfig.feature.qol.abilityTimer.itemAbilityTimerScale;
    }

    @Override
    public int getBgColor() {
        return ChromaColour.specialToChromaRGB(VNTXConfig.feature.qol.abilityTimer.itemAbilityTimerBgColor);
    }

    @Override
    public int getCornerRadius() {
        return VNTXConfig.feature.qol.abilityTimer.itemAbilityTimerCornerRadius;
    }

    @Override
    protected boolean isEnabled() {
        return VNTXConfig.feature != null && VNTXConfig.feature.qol.abilityTimer.itemAbilityTimerOverlay;
    }

    @Override
    protected String getHeaderText() {
        return "§b§lAbility Timers";
    }

    @Override
    protected List<String> getActiveTimers() {
        return ItemAbilityTimers.getActiveTimers();
    }

    @Override
    protected ItemStack findItemStack(String id) {
        return ItemAbilityTimers.findItemStack(id);
    }

    @Override
    protected long getRemainingMs(String id) {
        return ItemAbilityTimers.getRemainingMs(id);
    }

    @Override
    protected boolean shouldShowWhenEmpty() {
        return VNTXConfig.feature != null && VNTXConfig.feature.qol.abilityTimer.itemAbilityTimerShowWhenEmpty;
    }

    @Override
    protected String getPreviewItemName() {
        return "§6Fire Veil";
    }
}