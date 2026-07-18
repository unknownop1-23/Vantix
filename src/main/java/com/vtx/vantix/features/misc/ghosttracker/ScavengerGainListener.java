package com.vtx.vantix.features.misc.ghosttracker;

import com.vtx.vantix.events.ScavengerGainEvent;
import com.vtx.vantix.init.RegisterEvents;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterEvents
public class ScavengerGainListener {

    @SubscribeEvent
    public void onScavengerGain(ScavengerGainEvent event) {
        GhostStats.getInstance().addScavenger(event.getAmount());
    }
}
