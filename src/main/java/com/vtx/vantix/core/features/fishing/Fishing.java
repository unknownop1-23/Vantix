package com.vtx.vantix.core.features.fishing;

import com.google.gson.annotations.Expose;
import com.vtx.vantix.core.moulconfig.gui.config.ConfigAnnotations.*;

public class Fishing {

    @Expose
    @Category(name = "Trophy Fish", desc = "Trophy fish tracking and display")
    public TrophyFishConfig trophyFish = new TrophyFishConfig();

    @Expose
    @Category(name = "Fishing Timer", desc = "Fishing timer overlay settings")
    public FishingTimerConfig fishingTimerConfig = new FishingTimerConfig();
}
