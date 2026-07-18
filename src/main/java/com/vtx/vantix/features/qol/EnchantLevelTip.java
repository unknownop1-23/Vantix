package com.vtx.vantix.features.qol;

import com.vtx.vantix.core.VNTXConfig;
import com.vtx.vantix.events.RenderItemOverlayEvent;
import com.vtx.vantix.init.RegisterEvents;
import com.vtx.vantix.utils.ColorUtils;
import com.vtx.vantix.utils.RomanNumeralParser;
import com.vtx.vantix.utils.item.ItemStackUtils;
import com.vtx.vantix.utils.item.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterEvents
public class EnchantLevelTip {

    @SubscribeEvent
    public void onItemOverlay(RenderItemOverlayEvent event) {
        if (VNTXConfig.feature == null) return;
        if (!VNTXConfig.feature.misc.itemStackTips) return;

        String id = ItemUtils.getInternalName(event.stack);
        if (!"ENCHANTED_BOOK".equals(id)) return;

        String tip = getEnchantLevel(event.stack);
        if (tip == null) return;
        ItemStackUtils.drawTip(tip, event.x, event.y);
    }

    private static String getEnchantLevel(ItemStack stack) {
        String name = ColorUtils.stripColor(stack.getDisplayName());
        if (name.isEmpty()) return null;
        String[] parts = name.split(" ");
        String last = parts[parts.length - 1];
        if (last.isEmpty() || !last.chars().allMatch(c -> "IVXLCDM".indexOf(c) >= 0)) return null;
        if (!RomanNumeralParser.isValid(last)) return null;
        int level = RomanNumeralParser.parse(last);
        return level > 0 ? String.valueOf(level) : null;
    }
}
