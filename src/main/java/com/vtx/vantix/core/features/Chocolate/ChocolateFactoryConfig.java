package com.vtx.vantix.core.features.Chocolate;

import com.vtx.vantix.core.moulconfig.gui.config.ConfigAnnotations.*;
import com.vtx.vantix.utils.Position;

public class ChocolateFactoryConfig {

    @ConfigOption(name = "Egg Timer", desc = "Shows a timer for the next Chocolate Egg.")
    @ConfigEditorBoolean
    public boolean chocolateEggTimer = true;

    public Position eggTimerPos = new Position(10, 10);

    @ConfigOption(name = "Egg Timer Scale", desc = "Scale of the egg timer.")
    @ConfigEditorSliderAnnotation(minValue = 0.5f, maxValue = 3.0f, minStep = 0.1f)
    public float eggTimerScale = 1.0f;

    @ConfigOption(name = "Show Best Upgrade", desc = "Highlights the most cost-efficient upgrade.")
    @ConfigEditorBoolean
    public boolean chocolateChocolateShowBestUpgrade = true;

    @ConfigOption(name = "Egg Waypoints", desc = "Shows waypoints for Chocolate Eggs.")
    @ConfigEditorBoolean
    public boolean chocolateChocolateEggWaypoints = true;

    @ConfigOption(name = "Egg Waypoint Color", desc = "Color of the egg waypoints.")
    @ConfigEditorColour
    public String chocolateChocolateEggWaypointsColor = "0:255:0:150";

    @ConfigOption(name = "Hunt Day Notifier", desc = "Notifies you when an egg hunt starts.")
    @ConfigEditorBoolean
    public boolean huntDayNotifier = true;
}