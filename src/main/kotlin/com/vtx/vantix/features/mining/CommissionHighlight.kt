package com.vtx.vantix.features.mining

import com.vtx.vantix.core.VNTXConfig
import com.vtx.vantix.init.RegisterEvents
import com.vtx.vantix.utils.ColorUtils
import com.vtx.vantix.utils.ContainerUtils
import com.vtx.vantix.utils.item.ItemUtils
import com.vtx.vantix.utils.render.HighlightUtils
import net.minecraft.item.ItemStack

@RegisterEvents
object CommissionHighlight {

    private const val COMPLETED_HIGHLIGHT_COLOR = 0x8000FF00.toInt()

    init {
        HighlightUtils.registerHighlighter { gui, slot ->
            if (VNTXConfig.feature?.mining?.commissionHighlight != true) return@registerHighlighter null

            val container = ContainerUtils.getOpenChest(gui) ?: return@registerHighlighter null
            if (ContainerUtils.getTitle(container)?.contains("Commissions") != true) return@registerHighlighter null

            val stack = slot.stack ?: return@registerHighlighter null
            if (isCommissionCompleted(stack)) COMPLETED_HIGHLIGHT_COLOR else null
        }
    }

    private fun isCommissionCompleted(stack: ItemStack): Boolean {
        val loreLines = ItemUtils.getLoreLines(stack)
        return loreLines.any { line ->
            ColorUtils.stripColor(line) == "COMPLETED"
        }
    }
}