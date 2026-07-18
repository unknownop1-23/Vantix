package com.vtx.vantix.mixins;

import com.vtx.vantix.core.VNTXConfig;
import net.minecraft.client.renderer.entity.RenderItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItem.class)
public class MixinRenderItem_Glint {

    @Inject(method = "renderEffect", at = @At("HEAD"), cancellable = true)
    private void VNTX$disableEnchantGlint(CallbackInfo ci) {
        if (VNTXConfig.feature != null && VNTXConfig.feature.qol.disableEnchantGlint)
            ci.cancel();
    }
}
