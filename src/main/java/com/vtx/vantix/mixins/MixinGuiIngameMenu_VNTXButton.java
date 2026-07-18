package com.vtx.vantix.mixins;

import com.vtx.vantix.OptionsMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngameMenu.class)
public abstract class MixinGuiIngameMenu_VNTXButton extends GuiScreen {

    @Unique
    private static final int BTN_VNTX = 0x4EF;

    @Unique
    @Inject(method = "initGui", at = @At("TAIL"))
    private void VNTX$addButton(CallbackInfo ci) {
        // Find the lowest Y position among all existing buttons so we never overlap
        int lowestY = this.height / 4 + 8;
        for (GuiButton btn : this.buttonList) {
            lowestY = Math.max(lowestY, btn.yPosition + btn.height);
        }

        this.buttonList.add(new GuiButton(
                BTN_VNTX,
                this.width / 2 - 100,
                lowestY + 4,
                200,
                20,
                "Vantix Mod"
        ));
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
    private void VNTX$actionPerformed(GuiButton button, CallbackInfo ci) {
        if (button.id == BTN_VNTX) {
            Minecraft.getMinecraft().displayGuiScreen(new OptionsMenu());
            ci.cancel();
        }
    }
}