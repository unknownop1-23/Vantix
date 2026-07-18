package com.vtx.vantix.mixins;

import com.vtx.vantix.core.VNTXConfig;
import com.vtx.vantix.features.scoreboard.CustomScoreboard;
import com.vtx.vantix.utils.overlay.OverlayUtils;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.ScoreObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class MixinGuiIngame_HideScoreboard {

    @Inject(method = "renderScoreboard", at = @At("HEAD"), cancellable = true)
    public void renderScoreboard(ScoreObjective objective, ScaledResolution scaledRes, CallbackInfo ci) {
        if (!CustomScoreboard.isActive()) return;
        if (VNTXConfig.feature.scoreboard.hideOnTab
                && OverlayUtils.shouldHide()) return;
        ci.cancel();
    }
}