package com.vtx.vantix.features.farming.mouse;

import com.vtx.vantix.core.VNTXConfig;
import com.vtx.vantix.utils.KeybindHelper;
import com.vtx.vantix.init.RegisterEvents;
import com.vtx.vantix.utils.chat.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@RegisterEvents
public class LockMouse {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final String PREFIX = EnumChatFormatting.GREEN + " " + EnumChatFormatting.RESET;

    private boolean keyWasDown = false;

    public static boolean isLocked() {
        return VNTXConfig.feature != null && VNTXConfig.feature.farming.lockMouse;
    }

    public static void setLocked(boolean locked) {
        if (VNTXConfig.feature == null) return;
        VNTXConfig.feature.farming.lockMouse = locked;
        VNTXConfig.saveConfig();
        ChatUtils.sendMessage(PREFIX + (locked ? EnumChatFormatting.GREEN + "Mouse locked." : EnumChatFormatting.RED + "Mouse unlocked."));
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (VNTXConfig.feature == null || mc.thePlayer == null || mc.currentScreen != null) return;

        boolean keyDown = KeybindHelper.isKeyDown(VNTXConfig.feature.farming.lockMouseKey);
        if (keyDown && !keyWasDown) setLocked(!isLocked());
        keyWasDown = keyDown;
    }
}