package com.vtx.vantix.mixins;

import com.vtx.vantix.core.VNTXConfig;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Render.class)
public class MixinRender {

    @Inject(method = "renderEntityOnFire", at = @At("HEAD"), cancellable = true)
    private void VNTX$disableEntityFire(Entity entity, double x, double y, double z, float partialTicks, CallbackInfo ci) {
        if (VNTXConfig.feature != null && VNTXConfig.feature.misc.disableEntityFire)
            ci.cancel();
    }
}
