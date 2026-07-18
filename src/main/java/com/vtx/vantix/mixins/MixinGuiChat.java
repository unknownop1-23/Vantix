package com.vtx.vantix.mixins;

import com.vtx.vantix.core.VNTXConfig;
import com.vtx.vantix.features.qol.ChatStateManager;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiChat.class)
public class MixinGuiChat {

    @Shadow
    protected GuiTextField inputField;

    @Inject(method = "keyTyped", at = @At("RETURN"))
    protected void VNTX$onKeyTyped(char typedChar, int keyCode, CallbackInfo ci) {
        if (VNTXConfig.feature == null || !VNTXConfig.feature.qol.chatStateRestore) return;

        if (keyCode != Keyboard.KEY_ESCAPE && keyCode != Keyboard.KEY_RETURN) {
            ChatStateManager.getInstance().updateState(inputField.getText());
        } else {
            ChatStateManager.getInstance().resetState();
        }
    }

    @Inject(method = "initGui", at = @At("RETURN"))
    public void VNTX$onInitGui(CallbackInfo ci) {
        if (VNTXConfig.feature == null || !VNTXConfig.feature.qol.chatStateRestore) return;

        if (ChatStateManager.getInstance().shouldRestore()) {
            inputField.setText(ChatStateManager.getInstance().getSavedText());
        }
    }
}
