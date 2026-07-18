package com.vtx.vantix.features.misc;

import com.vtx.vantix.core.VNTXConfig;
import com.vtx.vantix.events.RenderItemOverlayEvent;
import com.vtx.vantix.init.RegisterEvents;
import com.vtx.vantix.utils.ColorUtils;
import com.vtx.vantix.utils.ContainerUtils;
import com.vtx.vantix.utils.RomanNumeralParser;
import com.vtx.vantix.utils.item.ItemStackUtils;
import com.vtx.vantix.utils.item.ItemUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

@RegisterEvents
public class PartyFinderFloorTip {

    @SubscribeEvent
    public void onItemOverlay(RenderItemOverlayEvent event) {
        if (VNTXConfig.feature == null) return;
        if (!VNTXConfig.feature.misc.partyFinderFloorTip) return;
        if (!ContainerUtils.isInContainer("Party Finder")) return;
        if (event.stack == null || event.stack.getItem() != Items.skull) return;

        String tip = getPartyFinderFloor(event.stack);
        if (tip == null) return;
        ItemStackUtils.drawTip(tip, event.x, event.y);
    }

    private static String getPartyFinderFloor(ItemStack stack) {
        List<String> lore = ItemUtils.getLoreLines(stack);
        if (lore.isEmpty()) return null;

        boolean master = false;
        String floorLabel = null;

        for (String line : lore) {
            String stripped = ColorUtils.stripColor(line).trim();

            if (stripped.startsWith("Dungeon: ")) {
                String dungeon = stripped.substring("Dungeon: ".length()).trim();
                master = dungeon.equals("Master Catacombs");

            } else if (stripped.startsWith("Floor: ")) {
                String value = stripped.substring("Floor: ".length()).trim();
                if (value.equals("Entrance")) {
                    floorLabel = "ENT";
                } else if (value.startsWith("Floor ")) {
                    String numeral = value.substring("Floor ".length()).trim();
                    int n = RomanNumeralParser.parse(numeral);
                    if (n >= 1 && n <= 7) floorLabel = String.valueOf(n);
                }
            }
        }

        if (floorLabel == null) return null;
        if (floorLabel.equals("ENT")) return "ENT";
        return (master ? "M" : "F") + floorLabel;
    }
}
