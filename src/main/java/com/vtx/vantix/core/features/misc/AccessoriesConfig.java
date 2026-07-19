package com.vtx.vantix.core.features.misc;


import com.vtx.vantix.core.moulconfig.gui.config.ConfigAnnotations.*;
import org.lwjgl.input.Keyboard;

public class AccessoriesConfig {

    @ConfigOption(name = "Enable Accessories Features", desc = "Enables tracking and features related to accessories.")
    @ConfigEditorBoolean
    public boolean accessoriesEnabled = true;

    @ConfigOption(name = "Show Missing Accessories", desc = "Shows a list of missing accessories in your accessory bag.")
    @ConfigEditorBoolean
    public boolean showMissingAccessoriesList = true;

    @ConfigOption(name = "Scroll Up Keybind", desc = "Key to scroll up the missing accessories list.")
    @ConfigEditorKeybind(defaultKey = Keyboard.KEY_UP)
    public int accessoriesDataScrollUpKey = Keyboard.KEY_UP;

    @ConfigOption(name = "Scroll Down Keybind", desc = "Key to scroll down the missing accessories list.")
    @ConfigEditorKeybind(defaultKey = Keyboard.KEY_DOWN)
    public int accessoriesDataScrollDownKey = Keyboard.KEY_DOWN;

}