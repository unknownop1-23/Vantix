package com.vtx.vantix.features.misc

import com.vtx.vantix.core.VNTXConfig
import com.vtx.vantix.init.RegisterEvents
import com.vtx.vantix.utils.ColorUtils
import com.vtx.vantix.utils.ContainerUtils
import com.vtx.vantix.utils.render.HighlightUtils

@RegisterEvents
object HoppityHighlight {

    private const val HIGHLIGHT_COLOR = 0x8000FF00.toInt()

    init {
        HighlightUtils.registerHighlighter { gui, slot ->
            if (VNTXConfig.feature?.misc?.hoppityHighlight != true) return@registerHighlighter null

            val container = ContainerUtils.getOpenChest(gui) ?: return@registerHighlighter null
            if (ContainerUtils.getTitle(container)?.contains("Hoppity") != true) return@registerHighlighter null

            val stack = slot.stack ?: return@registerHighlighter null
            if (ColorUtils.stripColor(stack.displayName).contains("NEW RABBIT!")) HIGHLIGHT_COLOR else null
        }
    }
}