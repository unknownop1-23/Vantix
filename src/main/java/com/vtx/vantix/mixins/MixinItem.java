package com.vtx.vantix.mixins;

import com.vtx.vantix.core.VNTXConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// thanks to odtheking, wouldnt have known how to implement this correctly LOL

@Mixin(Item.class)
public class MixinItem {

    @Inject(method = "shouldCauseReequipAnimation", at = @At("HEAD"), cancellable = true, remap = false)
    private void cancelReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged, CallbackInfoReturnable<Boolean> ci) {
        if (VNTXConfig.feature == null) return;
        if (!VNTXConfig.feature.misc.noItemSwitchAnimation) return;
        ci.setReturnValue(false);
    }
}