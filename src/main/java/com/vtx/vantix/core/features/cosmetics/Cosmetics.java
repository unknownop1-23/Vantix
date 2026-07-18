package com.vtx.vantix.core.features.cosmetics;

import com.google.gson.annotations.Expose;
import com.vtx.vantix.core.moulconfig.gui.config.ConfigAnnotations.*;
public class Cosmetics {

    @Expose
    @Category(name = "Capes", desc = "Settings for the Capes")
    public CapesConfig capes = new CapesConfig();
}