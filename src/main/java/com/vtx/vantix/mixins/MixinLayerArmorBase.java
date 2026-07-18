package com.vtx.vantix.mixins;

import com.vtx.vantix.core.VNTXConfig;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerArmorBase.class)
public class MixinLayerArmorBase {

    @Inject(method = "renderGlint", at = @At("HEAD"), cancellable = true)
    private void VNTX$disableEnchantGlint(CallbackInfo ci) {
        if (VNTXConfig.feature != null && VNTXConfig.feature.qol.disableEnchantGlint)
            ci.cancel();
    }
}
