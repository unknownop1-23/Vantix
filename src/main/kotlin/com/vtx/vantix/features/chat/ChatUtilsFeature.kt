package com.vtx.vantix.features.chat

import com.vtx.vantix.init.RegisterEvents
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

@RegisterEvents
class ChatUtilsFeature {

    private var ticks = 0

    @SubscribeEvent
    fun onClientTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START) return
        if (++ticks >= 12000) {          // ~10 minutes at 20 TPS
            ChatCompactHandler.cleanupExpired()
            ticks = 0
        }
    }

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load) {
        ticks = 0
        ChatCompactHandler.reset()
    }
}
