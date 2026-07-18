package com.vtx.vantix.features.qol;

import com.vtx.vantix.init.RegisterEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

@RegisterEvents
public class CursorResetHandler {

    private static final Minecraft mc = Minecraft.getMinecraft();

    // Cached mouse coordinates
    public static int cachedX;
    public static int cachedY;


    public static void cacheMouse() {
        cachedX = Mouse.getX();
        cachedY = Mouse.getY();
    }


    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        GuiScreen oldGui = mc.currentScreen;
        if (oldGui != null) {
            cacheMouse();
        }
    }
}