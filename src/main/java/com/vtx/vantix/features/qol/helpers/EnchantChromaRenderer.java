package com.vtx.vantix.features.qol.helpers;

import com.vtx.vantix.core.VNTXConfig;
import com.vtx.vantix.core.moulconfig.editors.ChromaColour;
import com.vtx.vantix.mixins.accessors.FontRendererAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class EnchantChromaRenderer {

    private static boolean chromaActive;
    private static boolean chromaOn;
    private static boolean renderingShadow;

    private EnchantChromaRenderer() {
    }

    public static void beginRenderString(String text, boolean shadow) {
        chromaOn = false;
        renderingShadow = shadow;
        chromaActive = VNTXConfig.feature != null && VNTXConfig.feature.qol.enchantParser.enchantChroma && text != null && (text.contains("§z") || text.contains("§Z"));
    }

    public static void onChromaCode() {
        if (chromaActive) chromaOn = true;
    }

    public static void onColorCode() {
        if (chromaActive) chromaOn = false;
    }

    public static void changeTextColor() {
        if (!chromaActive || !chromaOn || VNTXConfig.feature == null) return;

        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        FontRendererAccessor accessor = (FontRendererAccessor) fr;
        int base = ChromaColour.specialToChromaRGB(VNTXConfig.feature.qol.enchantParser.enchantPerfectColor);
        int rgb = applyMode(base, accessor.VNTX$getPosX(), accessor.VNTX$getPosY(), VNTXConfig.feature.qol.enchantParser.enchantChromaMode, VNTXConfig.feature.qol.enchantParser.enchantChromaSize);
        if (renderingShadow) {
            int a = (rgb >>> 24) & 255;
            int r = ((rgb >>> 16) & 255) / 4;
            int g = ((rgb >>> 8) & 255) / 4;
            int b = (rgb & 255) / 4;
            rgb = (a << 24) | (r << 16) | (g << 8) | b;
        }
        GlStateManager.color(((rgb >> 16) & 255) / 255F, ((rgb >> 8) & 255) / 255F, (rgb & 255) / 255F, ((rgb >> 24) & 255) / 255F);
    }

    public static void endRenderString() {
        chromaActive = false;
        chromaOn = false;
        renderingShadow = false;
        GlStateManager.color(1F, 1F, 1F, 1F);
    }

    private static int applyMode(int argb, float x, float y, int mode, float size) {
        if (mode == 0) return argb;
        float shift = ((x + y) / Math.max(1F, size)) % 1F;
        int a = (argb >>> 24) & 255;
        int r = (argb >>> 16) & 255;
        int g = (argb >>> 8) & 255;
        int b = argb & 255;
        float[] hsb = Color.RGBtoHSB(r, g, b, null);
        hsb[0] = (hsb[0] + shift) % 1F;
        return (a << 24) | (Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]) & 0x00FFFFFF);
    }

    public static int applyChromaShift(int argb, float x, float y, int mode, float size) {
        return applyMode(argb, x, y, mode, size);
    }
}