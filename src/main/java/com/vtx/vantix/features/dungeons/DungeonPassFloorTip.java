package com.vtx.vantix.features.dungeons;

import com.vtx.vantix.core.VNTXConfig;
import com.vtx.vantix.events.RenderItemOverlayEvent;
import com.vtx.vantix.init.RegisterEvents;
import com.vtx.vantix.utils.ContainerUtils;
import com.vtx.vantix.utils.item.ItemStackUtils;
import com.vtx.vantix.utils.item.ItemUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterEvents
public class DungeonPassFloorTip {

    @SubscribeEvent
    public void onItemOverlay(RenderItemOverlayEvent event) {
        if (VNTXConfig.feature == null) return;
        if (!VNTXConfig.feature.misc.itemStackTips) return;
        if (!ContainerUtils.isInContainer("Catacombs Gate")) return;

        String id = ItemUtils.getInternalName(event.stack);
        String tip = getDungeonFloor(id);
        if (tip == null) return;
        ItemStackUtils.drawTip(tip, event.x, event.y);
    }

    private static String getDungeonFloor(String id) {
        String suffix = null;
        if (id.startsWith("MASTER_CATACOMBS_PASS_")) {
            suffix = id.substring("MASTER_CATACOMBS_PASS_".length());
        } else if (id.startsWith("CATACOMBS_PASS_")) {
            suffix = id.substring("CATACOMBS_PASS_".length());
        }
        if (suffix == null) return null;
        try {
            int floor = Integer.parseInt(suffix) - 3;
            return floor > 0 ? String.valueOf(floor) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
