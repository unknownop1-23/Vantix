package com.vtx.vantix.features.chocolate;

import com.vtx.vantix.core.VNTXConfig;
import com.vtx.vantix.init.RegisterEvents;
import com.vtx.vantix.utils.StringUtils;
import com.vtx.vantix.utils.data.SkyblockData;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterEvents
public class EggHuntNotifier {

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (VNTXConfig.feature == null || VNTXConfig.feature.chocolateFactory == null) return;

        String msg = StringUtils.stripFormattingFast(event.message.getUnformattedText());

        if (msg.contains("HOPPITY'S HUNT A Chocolate Breakfast Egg has appeared!") ||
                msg.contains("HOPPITY'S HUNT A Chocolate Lunch Egg has appeared!") ||
                msg.contains("HOPPITY'S HUNT A Chocolate Dinner Egg has appeared!")) {

            if (SkyblockData.getSeason() != SkyblockData.Season.SPRING) return;

            if (((SkyblockData.getSbHour() == 7 || SkyblockData.getSbHour() == 6) && SkyblockData.isAm()) ||
                    ((SkyblockData.getSbHour() == 2 || SkyblockData.getSbHour() == 1) && !SkyblockData.isAm())) {
                ChocolateEggTimer.setGoalEpochMs(System.currentTimeMillis() + 350000);
            } else if (((SkyblockData.getSbHour() == 9 || SkyblockData.getSbHour() == 8) && !SkyblockData.isAm())) {
                ChocolateEggTimer.setGoalEpochMs(System.currentTimeMillis() + 500000);

                if (VNTXConfig.feature.chocolateFactory.huntDayNotifier) {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§d[Vantix] §eNew Egg Hunt Day!"));
                    Minecraft.getMinecraft().thePlayer.playSound("random.orb", 1.0f, 1.0f);
                }
            }
        }
    }
}