package com.vtx.vantix.features.dungeons;

import com.vtx.vantix.core.VNTXConfig;
import com.vtx.vantix.init.RegisterEvents;
import com.vtx.vantix.utils.chat.ChatFilter;

import java.util.regex.Pattern;

@RegisterEvents
public class HideBlessing {

    private static final Pattern BLESSING = Pattern.compile("§r§6§lDUNGEON BUFF! .*?found a §r§dBlessing of \\w+.*?§r§f!§r");

    public HideBlessing() {
        ChatFilter.hide("dungeons.blessingMessages", msg -> VNTXConfig.feature != null && VNTXConfig.feature.dungeons.hideBlessingMessages && BLESSING.matcher(msg).find());
    }
}
