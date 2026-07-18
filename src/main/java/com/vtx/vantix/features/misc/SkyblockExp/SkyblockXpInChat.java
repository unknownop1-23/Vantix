package com.vtx.vantix.features.misc.SkyblockExp;

import com.vtx.vantix.core.VNTXConfig;
import com.vtx.vantix.events.ActionBarXpGainEvent;
import com.vtx.vantix.init.RegisterEvents;
import com.vtx.vantix.utils.chat.ChatUtils;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterEvents
public class SkyblockXpInChat {

    private static final String PREFIX = EnumChatFormatting.DARK_AQUA + "[SkyBlock XP] " + EnumChatFormatting.RESET;

    @SubscribeEvent
    public void onXpGain(ActionBarXpGainEvent event) {
        if (VNTXConfig.feature == null || !VNTXConfig.feature.misc.skyblockXpInChat) return;

        ChatUtils.sendMessage(PREFIX + event.getFormattedText());
    }
}