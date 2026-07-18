package com.vtx.vantix;

import com.vtx.vantix.core.VNTXConfig;
import com.vtx.vantix.utils.chat.ChatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class DebugLogger {

    private static final Logger LOG = LogManager.getLogger("VNTX");
    private static final String PREFIX = "§8[§6VNTX Debug§8] §r";

    private DebugLogger() {}

    public static void log(String message) {
        LOG.info("[VNTX DEBUG] {}", message);
        if (VNTXConfig.feature != null && VNTXConfig.feature.debug.enableDebug) {
            ChatUtils.sendMessage(PREFIX + message);
        }
    }
}