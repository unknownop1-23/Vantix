package com.vtx.vantix.features.misc

import com.vtx.vantix.core.VNTXConfig
import com.vtx.vantix.core.moulconfig.editors.ChromaColour
import com.vtx.vantix.init.RegisterEvents
import com.vtx.vantix.utils.ContainerUtils
import com.vtx.vantix.utils.data.SkyblockData
import com.vtx.vantix.utils.item.ItemUtils
import com.vtx.vantix.utils.render.HighlightUtils

@RegisterEvents
object BazaarOrderHighlight {

    private const val CONTAINER_NAME = "Bazaar Orders"
    private const val CLAIM_LINE = "§eClick to claim!"
    private const val SELL_PREFIX = "§6§lSELL"
    private const val BUY_PREFIX = "§a§lBUY"

    init {
        HighlightUtils.registerHighlighter { gui, slot ->
            if (!SkyblockData.isOnSkyblock()) return@registerHighlighter null
            val config = VNTXConfig.feature?.misc?.bazaarOrders ?: return@registerHighlighter null
            if (!config.highlightSellOrders && !config.highlightBuyOrders) return@registerHighlighter null

            val container = ContainerUtils.getOpenChest(gui) ?: return@registerHighlighter null
            if (ContainerUtils.getTitle(container)?.contains(CONTAINER_NAME) != true) {
                return@registerHighlighter null
            }

            val stack = slot.stack ?: return@registerHighlighter null
            if (ItemUtils.getLoreLines(stack).none { it == CLAIM_LINE }) return@registerHighlighter null

            val name = stack.displayName
            when {
                config.highlightSellOrders && name.startsWith(SELL_PREFIX) -> ChromaColour.specialToChromaRGB(config.sellOrderColor)
                config.highlightBuyOrders && name.startsWith(BUY_PREFIX) -> ChromaColour.specialToChromaRGB(config.buyOrderColor)
                else -> null
            }
        }
    }
}