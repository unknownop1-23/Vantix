package com.vtx.vantix.core.features.overlays;

import com.google.gson.annotations.Expose;
import com.vtx.vantix.core.moulconfig.gui.config.ConfigAnnotations;

public class Overlays {

    @Expose
    @ConfigAnnotations.ConfigOption(name = "Scale", desc = "Adjust the overall scale of Profile Viewer")
    @ConfigAnnotations.ConfigEditorSliderAnnotation(minValue = 0.5f,maxValue = 1.5f,minStep = 0.1f)
    public float pvScale = 1f;
}
