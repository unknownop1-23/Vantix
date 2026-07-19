package com.vtx.vantix.utils.render;

import com.vtx.vantix.Resources;
import com.vtx.vantix.core.VNTXConfig;
import com.vtx.vantix.variables.MobDisplayTypes;
import net.minecraft.block.BlockLever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.util.vector.Vector3f;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RenderUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final ResourceLocation SEARCH_BAR_TEX = Resources.SEARCH_BAR_TEX;
    private static final ResourceLocation SEARCH_BAR_TEX_GOLD = Resources.SEARCH_BAR_TEX_GOLD;
    private static final Map<ResourceLocation, Boolean> RESOURCE_CACHE = new HashMap<>();

    // Fallback resource for NEF's beacon beam if it's not in Vantix's Resources yet
    private static final ResourceLocation BEACON_BEAM = new ResourceLocation("minecraft", "textures/entity/beacon_beam.png");

    private RenderUtils() {
    }

    // ==========================================
    // VANTIX ORIGINAL METHODS
    // ==========================================

    public static void drawSearchBar(GuiTextField field, boolean useTexture) {
        drawSearchBar(field, useTexture, false);
    }

    public static void drawSearchBar(GuiTextField field, boolean useTexture, boolean useGoldTexture) {
        if (field == null) return;

        int x = field.xPosition;
        int y = field.yPosition;
        int w = field.width;
        int h = field.height;

        GlStateManager.color(1f, 1f, 1f, 1f);

        ResourceLocation texture = useGoldTexture ? SEARCH_BAR_TEX_GOLD : SEARCH_BAR_TEX;
        if (useTexture && drawSearchBarTexture(texture, x, y, w, h)) {
        } else {
            Gui.drawRect(x, y, x + w, y + h, 0xFF2C2C2C);
            Gui.drawRect(x + 1, y + 1, x + w - 1, y + h - 1, 0xFF111111);
        }

        FontRenderer fr = mc.fontRendererObj;
        String text = field.getText();
        int textY = y - 4 + h / 2;
        int maxWidth = Math.max(8, w - 10);
        String display = fr.trimStringToWidth(text, maxWidth);

        if (field.isFocused()) {
            String toDisplay = display.isEmpty() ? "§7Search..." : display;
            fr.drawStringWithShadow(toDisplay, x + 5, textY, display.isEmpty() ? 0x8F8F8F : 0xFFFFFFFF);

            if (System.currentTimeMillis() % 1000 > 500) {
                int cursor = Math.min(field.getCursorPosition(), text.length());
                String beforeCursor = text.substring(0, cursor);
                int beforeWidth = fr.getStringWidth(fr.trimStringToWidth(beforeCursor, maxWidth));
                Gui.drawRect(x + 5 + beforeWidth, y - 5 + h / 2, x + 6 + beforeWidth, y + 4 + h / 2, 0xFFFFFFFF);
            }
        } else {
            String toDisplay = display.isEmpty() ? "§7Search..." : display;
            fr.drawString(toDisplay, x + 5, textY, 0x8F8F8F);
        }
    }

    private static boolean drawSearchBarTexture(ResourceLocation texture, int x, int y, int w, int h) {
        if (!resourceExists(texture)) return false;

        mc.getTextureManager().bindTexture(texture);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        for (int yi = 0; yi <= 2; yi++) {
            for (int xi = 0; xi <= 2; xi++) {
                float uMin = 0f, uMax = 4f / 20f;
                int px = x, pw = 4;
                if (xi == 1) {
                    px += 4;
                    uMin = 4f / 20f;
                    uMax = 16f / 20f;
                    pw = w - 8;
                } else if (xi == 2) {
                    px += w - 4;
                    uMin = 16f / 20f;
                    uMax = 1f;
                }

                float vMin = 0f, vMax = 4f / 20f;
                int py = y, ph = 4;
                if (yi == 1) {
                    py += 4;
                    vMin = 4f / 20f;
                    vMax = 16f / 20f;
                    ph = h - 8;
                } else if (yi == 2) {
                    py += h - 4;
                    vMin = 16f / 20f;
                    vMax = 1f;
                }

                drawSearchBarTexturedRect(px, py, pw, ph, uMin, uMax, vMin, vMax);
            }
        }

        GlStateManager.disableBlend();
        return true;
    }

    private static void drawSearchBarTexturedRect(int x, int y, int w, int h, float uMin, float uMax, float vMin, float vMax) {
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + h, 0).tex(uMin, vMax).endVertex();
        worldrenderer.pos(x + w, y + h, 0).tex(uMax, vMax).endVertex();
        worldrenderer.pos(x + w, y, 0).tex(uMax, vMin).endVertex();
        worldrenderer.pos(x, y, 0).tex(uMin, vMin).endVertex();
        tessellator.draw();
    }

    private static boolean resourceExists(ResourceLocation location) {
        return RESOURCE_CACHE.computeIfAbsent(location, loc -> {
            try {
                mc.getResourceManager().getResource(loc);
                return true;
            } catch (Exception ignored) {
                return false;
            }
        });
    }

    public static void drawWorldCircle(double radius, int steps, float lineWidth, float r, float g, float b, float a) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableDepth();
        GL11.glLineWidth(lineWidth);
        GL11.glColor4f(r, g, b, a);

        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();
        wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        for (int i = 0; i <= steps; i++) {
            double angle = (Math.PI * 2) * i / steps;
            wr.pos(Math.cos(angle) * radius, 0, Math.sin(angle) * radius).endVertex();
        }
        tess.draw();

        GL11.glColor4f(1f, 1f, 1f, 1f);
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }

    public static void drawFloatingRectDark(int x, int y, int width, int height) {
        drawFloatingRectDark(x, y, width, height, true);
    }

    public static void drawFloatingRectDark(int x, int y, int width, int height, boolean shadow) {
        int alpha = OpenGlHelper.isFramebufferEnabled() ? 0xf0000000 : 0xff000000;
        int main = alpha | 0x202020;
        int light = 0xff2e2e2e;
        int dark = 0xff101010;
        Gui.drawRect(x, y, x + 1, y + height, light);
        Gui.drawRect(x + 1, y, x + width, y + 1, light);
        Gui.drawRect(x + width - 1, y + 1, x + width, y + height, dark);
        Gui.drawRect(x + 1, y + height - 1, x + width - 1, y + height, dark);
        Gui.drawRect(x + 1, y + 1, x + width - 1, y + height - 1, main);
        if (shadow) {
            Gui.drawRect(x + width, y + 2, x + width + 2, y + height + 2, 0x70000000);
            Gui.drawRect(x + 2, y + height, x + width, y + height + 2, 0x70000000);
        }
    }

    public static void drawFloatingRect(int x, int y, int width, int height) {
        drawFloatingRectWithAlpha(x, y, width, height, 0xFF, true);
    }

    public static void drawFloatingRectWithAlpha(int x, int y, int width, int height, int alpha, boolean shadow) {
        int main = (alpha << 24) | 0xc0c0c0;
        int light = (alpha << 24) | 0xf0f0f0;
        int dark = (alpha << 24) | 0x909090;
        Gui.drawRect(x, y, x + 1, y + height, light);
        Gui.drawRect(x + 1, y, x + width, y + 1, light);
        Gui.drawRect(x + width - 1, y + 1, x + width, y + height, dark);
        Gui.drawRect(x + 1, y + height - 1, x + width - 1, y + height, dark);
        Gui.drawRect(x + 1, y + 1, x + width - 1, y + height - 1, main);
        if (shadow) {
            Gui.drawRect(x + width, y + 2, x + width + 2, y + height + 2, (alpha * 3 / 5) << 24);
            Gui.drawRect(x + 2, y + height, x + width, y + height + 2, (alpha * 3 / 5) << 24);
        }
    }

    public static void drawInnerBox(int left, int top, int width, int height) {
        Gui.drawRect(left, top, left + width, top + height, 0x60080808);
        Gui.drawRect(left, top, left + 1, top + height, 0xff080808);
        Gui.drawRect(left, top, left + width, top + 1, 0xff080808);
        Gui.drawRect(left + width - 1, top, left + width, top + height, 0xff282828);
        Gui.drawRect(left, top + height - 1, left + width, top + height, 0xff282828);
    }

    public static void drawTexturedRect(float x, float y, float width, float height) {
        drawTexturedRect(x, y, width, height, 0, 1, 0, 1);
    }

    public static void drawTexturedRect(float x, float y, float width, float height, int filter) {
        drawTexturedRect(x, y, width, height, 0, 1, 0, 1, filter);
    }

    public static void drawTexturedRect(float x, float y, float width, float height, float uMin, float uMax, float vMin, float vMax) {
        drawTexturedRect(x, y, width, height, uMin, uMax, vMin, vMax, GL11.GL_NEAREST);
    }

    public static void drawTexturedRect(float x, float y, float width, float height, float uMin, float uMax, float vMin, float vMax, int filter) {
        GlStateManager.enableBlend();
        GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
        drawTexturedRectNoBlend(x, y, width, height, uMin, uMax, vMin, vMax, filter);
        GlStateManager.disableBlend();
    }

    public static void drawTexturedRectNoBlend(float x, float y, float width, float height, float uMin, float uMax, float vMin, float vMax, int filter) {
        GlStateManager.enableTexture2D();
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, filter);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, filter);

        Tessellator t = Tessellator.getInstance();
        WorldRenderer wr = t.getWorldRenderer();
        wr.begin(7, DefaultVertexFormats.POSITION_TEX);
        wr.pos(x, y + height, 0).tex(uMin, vMax).endVertex();
        wr.pos(x + width, y + height, 0).tex(uMax, vMax).endVertex();
        wr.pos(x + width, y, 0).tex(uMax, vMin).endVertex();
        wr.pos(x, y, 0).tex(uMin, vMin).endVertex();
        t.draw();

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
    }

    public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        final double f = 0.00390625;
        Tessellator t = Tessellator.getInstance();
        WorldRenderer wr = t.getWorldRenderer();
        wr.begin(7, DefaultVertexFormats.POSITION_TEX);
        wr.pos(x, y + height, 0).tex((textureX) * f, (textureY + height) * f).endVertex();
        wr.pos(x + width, y + height, 0).tex((textureX + width) * f, (textureY + height) * f).endVertex();
        wr.pos(x + width, y, 0).tex((textureX + width) * f, (textureY) * f).endVertex();
        wr.pos(x, y, 0).tex((textureX) * f, (textureY) * f).endVertex();
        t.draw();
    }

    public static void drawGradientRect(int zLevel, int left, int top, int right, int bottom, int startColor, int endColor) {
        float sA = (startColor >> 24 & 255) / 255f, sR = (startColor >> 16 & 255) / 255f;
        float sG = (startColor >> 8 & 255) / 255f, sB = (startColor & 255) / 255f;
        float eA = (endColor >> 24 & 255) / 255f, eR = (endColor >> 16 & 255) / 255f;
        float eG = (endColor >> 8 & 255) / 255f, eB = (endColor & 255) / 255f;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);

        Tessellator t = Tessellator.getInstance();
        WorldRenderer wr = t.getWorldRenderer();
        wr.begin(7, DefaultVertexFormats.POSITION_COLOR);
        wr.pos(right, top, zLevel).color(sR, sG, sB, sA).endVertex();
        wr.pos(left, top, zLevel).color(sR, sG, sB, sA).endVertex();
        wr.pos(left, bottom, zLevel).color(eR, eG, eB, eA).endVertex();
        wr.pos(right, bottom, zLevel).color(eR, eG, eB, eA).endVertex();
        t.draw();

        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawLine(int x1, int y1, int x2, int y2, int color, float lineWidth) {
        float a = (color >> 24 & 0xFF) / 255f;
        float r = (color >> 16 & 0xFF) / 255f;
        float g = (color >> 8 & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(lineWidth);

        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();
        wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        wr.pos(x1, y1, 0).color(r, g, b, a).endVertex();
        wr.pos(x2, y2, 0).color(r, g, b, a).endVertex();
        tess.draw();

        GL11.glLineWidth(1f);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static int renderStringTrimWidth(String str, boolean shadow, int x, int y, int width, int color, int maxLines) {
        FontRenderer fr = mc.fontRendererObj;

        if (str == null || str.isEmpty()) return 0;

        String[] words = str.split(" ");
        StringBuilder currentLine = new StringBuilder();
        int linesRendered = 0;
        int yOffset = 0;

        for (String word : words) {
            String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;
            int testWidth = fr.getStringWidth(testLine);

            if (testWidth > width && currentLine.length() > 0) {
                if (shadow) {
                    fr.drawStringWithShadow(currentLine.toString(), x, y + yOffset, color);
                } else {
                    fr.drawString(currentLine.toString(), x, y + yOffset, color);
                }
                yOffset += fr.FONT_HEIGHT;
                linesRendered++;

                if (maxLines > 0 && linesRendered >= maxLines) {
                    return yOffset;
                }

                currentLine = new StringBuilder(word);
            } else {
                currentLine = new StringBuilder(testLine);
            }
        }

        if (currentLine.length() > 0) {
            if (shadow) {
                fr.drawStringWithShadow(currentLine.toString(), x, y + yOffset, color);
            } else {
                fr.drawString(currentLine.toString(), x, y + yOffset, color);
            }
            yOffset += fr.FONT_HEIGHT;
        }

        return yOffset;
    }


    // ==========================================
    // NEF PORTED METHODS
    // ==========================================

    public static void drawOnSlot(int size, int xSlotPos, int ySlotPos, int colour) {
        drawOnSlot(size, xSlotPos, ySlotPos, colour, -1);
    }

    public static void drawOnSlot(int size, int xSlotPos, int ySlotPos, int colour, int number) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int guiLeft = (scaledResolution.getScaledWidth() - 176) / 2;
        int guiTop = (scaledResolution.getScaledHeight() - 222) / 2;
        int x = guiLeft + xSlotPos;
        int y = guiTop + ySlotPos;

        // Move down when chest isn't 6 rows
        if (size != 90) y += (6 - (size - 36) / 9) * 9;

        GL11.glTranslated(0, 0, 1);
        Gui.drawRect(x, y, x + 16, y + 16, colour);
        GL11.glTranslated(0, 0, -1);

        if (number != -1) {
            String text = String.valueOf(number);
            int textWidth = mc.fontRendererObj.getStringWidth(text);

            // Push OpenGL states
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, 300); // Bring the text to the foreground
            GlStateManager.enableBlend();
            GlStateManager.disableLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GlStateManager.disableDepth();

            // Render the string
            mc.fontRendererObj.drawStringWithShadow(text, x + 8 - textWidth / 2, y + 8 - 4, 0xFFFFFF);

            // Restore OpenGL states
            GlStateManager.enableDepth();
            GlStateManager.disableBlend();
            GL11.glEnable(GL11.GL_LIGHTING);
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }

    public static void drawItemStack(ItemStack stack, int x, int y) {
        drawItemStackWithText(stack, x, y, null);
    }

    public static void drawItemStackWithText(ItemStack stack, int x, int y, String text) {
        if (stack == null) return;

        RenderItem itemRender = mc.getRenderItem();

        RenderHelper.enableGUIStandardItemLighting();
        itemRender.zLevel = -145;
        itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        itemRender.renderItemOverlayIntoGUI(mc.fontRendererObj, stack, x, y, text);
        itemRender.zLevel = 0;
        RenderHelper.disableStandardItemLighting();
    }

    public static void renderBeaconBeam(BlockPos block, int rgb, float alphaMult, float partialTicks) {
        double viewerX;
        double viewerY;
        double viewerZ;

        Entity viewer = mc.getRenderViewEntity();
        viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks;
        viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks;
        viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks;

        double x = block.getX() - viewerX;
        double y = block.getY() - viewerY;
        double z = block.getZ() - viewerZ;

        double distSq = x * x + y * y + z * z;

        RenderUtils.renderBeaconBeam(x, y, z, rgb, 1.0f, partialTicks, distSq > 10 * 10);
    }

    public static void renderBeaconBeam(
            double x, double y, double z, int rgb, float alphaMult,
            float partialTicks, Boolean disableDepth
    ) {
        int height = 300;
        int bottomOffset = 0;
        int topOffset = bottomOffset + height;

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        if (disableDepth) {
            GlStateManager.disableDepth();
        }

        mc.getTextureManager().bindTexture(BEACON_BEAM);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glDisable(GL11.GL_LIGHTING);
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        double time = mc.theWorld.getTotalWorldTime() + (double) partialTicks;
        double d1 = MathHelper.func_181162_h(-time * 0.2D - (double) MathHelper.floor_double(-time * 0.1D));

        float r = ((rgb >> 16) & 0xFF) / 255f;
        float g = ((rgb >> 8) & 0xFF) / 255f;
        float b = (rgb & 0xFF) / 255f;
        double d2 = time * 0.025D * -1.5D;
        double d4 = 0.5D + Math.cos(d2 + 2.356194490192345D) * 0.2D;
        double d5 = 0.5D + Math.sin(d2 + 2.356194490192345D) * 0.2D;
        double d6 = 0.5D + Math.cos(d2 + (Math.PI / 4D)) * 0.2D;
        double d7 = 0.5D + Math.sin(d2 + (Math.PI / 4D)) * 0.2D;
        double d8 = 0.5D + Math.cos(d2 + 3.9269908169872414D) * 0.2D;
        double d9 = 0.5D + Math.sin(d2 + 3.9269908169872414D) * 0.2D;
        double d10 = 0.5D + Math.cos(d2 + 5.497787143782138D) * 0.2D;
        double d11 = 0.5D + Math.sin(d2 + 5.497787143782138D) * 0.2D;
        double d14 = -1.0D + d1;
        double d15 = (double) (height) * 2.5D + d14;

        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(x + d4, y + topOffset, z + d5).tex(1.0D, d15).color(r, g, b, alphaMult).endVertex();
        worldrenderer.pos(x + d4, y + bottomOffset, z + d5).tex(1.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d6, y + bottomOffset, z + d7).tex(0.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d6, y + topOffset, z + d7).tex(0.0D, d15).color(r, g, b, alphaMult).endVertex();
        worldrenderer.pos(x + d10, y + topOffset, z + d11).tex(1.0D, d15).color(r, g, b, alphaMult).endVertex();
        worldrenderer.pos(x + d10, y + bottomOffset, z + d11).tex(1.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d8, y + bottomOffset, z + d9).tex(0.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d8, y + topOffset, z + d9).tex(0.0D, d15).color(r, g, b, alphaMult).endVertex();
        worldrenderer.pos(x + d6, y + topOffset, z + d7).tex(1.0D, d15).color(r, g, b, alphaMult).endVertex();
        worldrenderer.pos(x + d6, y + bottomOffset, z + d7).tex(1.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d10, y + bottomOffset, z + d11).tex(0.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d10, y + topOffset, z + d11).tex(0.0D, d15).color(r, g, b, alphaMult).endVertex();
        worldrenderer.pos(x + d8, y + topOffset, z + d9).tex(1.0D, d15).color(r, g, b, alphaMult).endVertex();
        worldrenderer.pos(x + d8, y + bottomOffset, z + d9).tex(1.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d4, y + bottomOffset, z + d5).tex(0.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d4, y + topOffset, z + d5).tex(0.0D, d15).color(r, g, b, alphaMult).endVertex();
        tessellator.draw();

        GlStateManager.disableCull();
        double d12 = -1.0D + d1;
        double d13 = height + d12;
        float alphaConst = 0.25F;

        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(x + 0.2D, y + topOffset, z + 0.2D).tex(1.0D, d13).color(r, g, b, alphaConst * alphaMult).endVertex();
        worldrenderer.pos(x + 0.2D, y + bottomOffset, z + 0.2D).tex(1.0D, d12).color(r, g, b, alphaConst).endVertex();
        worldrenderer.pos(x + 0.8D, y + bottomOffset, z + 0.2D).tex(0.0D, d12).color(r, g, b, alphaConst).endVertex();
        worldrenderer.pos(x + 0.8D, y + topOffset, z + 0.2D).tex(0.0D, d13).color(r, g, b, alphaConst * alphaMult).endVertex();
        worldrenderer.pos(x + 0.8D, y + topOffset, z + 0.8D).tex(1.0D, d13).color(r, g, b, alphaConst * alphaMult).endVertex();
        worldrenderer.pos(x + 0.8D, y + bottomOffset, z + 0.8D).tex(1.0D, d12).color(r, g, b, alphaConst).endVertex();
        worldrenderer.pos(x + 0.2D, y + bottomOffset, z + 0.8D).tex(0.0D, d12).color(r, g, b, alphaConst).endVertex();
        worldrenderer.pos(x + 0.2D, y + topOffset, z + 0.8D).tex(0.0D, d13).color(r, g, b, alphaConst * alphaMult).endVertex();
        worldrenderer.pos(x + 0.8D, y + topOffset, z + 0.2D).tex(1.0D, d13).color(r, g, b, alphaConst * alphaMult).endVertex();
        worldrenderer.pos(x + 0.8D, y + bottomOffset, z + 0.2D).tex(1.0D, d12).color(r, g, b, alphaConst).endVertex();
        worldrenderer.pos(x + 0.8D, y + bottomOffset, z + 0.8D).tex(0.0D, d12).color(r, g, b, alphaConst).endVertex();
        worldrenderer.pos(x + 0.8D, y + topOffset, z + 0.8D).tex(0.0D, d13).color(r, g, b, alphaConst * alphaMult).endVertex();
        worldrenderer.pos(x + 0.2D, y + topOffset, z + 0.8D).tex(1.0D, d13).color(r, g, b, alphaConst * alphaMult).endVertex();
        worldrenderer.pos(x + 0.2D, y + bottomOffset, z + 0.8D).tex(1.0D, d12).color(r, g, b, alphaConst).endVertex();
        worldrenderer.pos(x + 0.2D, y + bottomOffset, z + 0.2D).tex(0.0D, d12).color(r, g, b, alphaConst).endVertex();
        worldrenderer.pos(x + 0.2D, y + topOffset, z + 0.2D).tex(0.0D, d13).color(r, g, b, alphaConst * alphaMult).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        if (disableDepth) {
            GlStateManager.enableDepth();
        }
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    public static void renderBoxAtCoords(
            double minX, double minY, double minZ,
            double maxX, double maxY, double maxZ,
            float partialTicks,
            Color color, boolean disableDepth
    ) {
        Entity player = mc.getRenderViewEntity();

        // Interpolated player position
        double playerX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double playerY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double playerZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

        // Offset box coords relative to player
        double x1 = minX - playerX;
        double y1 = minY - playerY;
        double z1 = minZ - playerZ;
        double x2 = maxX - playerX;
        double y2 = maxY - playerY;
        double z2 = maxZ - playerZ;

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();
        GL11.glDisable(GL11.GL_LIGHTING);
        GlStateManager.disableLighting();
        if (disableDepth) GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        GL11.glLineWidth(2.0f);

        // Set color
        GlStateManager.color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);

        // Draw bounding box
        AxisAlignedBB box = new AxisAlignedBB(x1, y1, z1, x2, y2, z2);
        drawOutlinedBox(box);

        if (disableDepth) GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        GL11.glEnable(GL11.GL_LIGHTING);
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    public static void drawOutlinedBox(AxisAlignedBB box) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer buffer = tessellator.getWorldRenderer();

        buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

        // Bottom square
        buffer.pos(box.minX, box.minY, box.minZ).endVertex();
        buffer.pos(box.maxX, box.minY, box.minZ).endVertex();

        buffer.pos(box.maxX, box.minY, box.minZ).endVertex();
        buffer.pos(box.maxX, box.minY, box.maxZ).endVertex();

        buffer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        buffer.pos(box.minX, box.minY, box.maxZ).endVertex();

        buffer.pos(box.minX, box.minY, box.maxZ).endVertex();
        buffer.pos(box.minX, box.minY, box.minZ).endVertex();

        // Top square
        buffer.pos(box.minX, box.maxY, box.minZ).endVertex();
        buffer.pos(box.maxX, box.maxY, box.minZ).endVertex();

        buffer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        buffer.pos(box.maxX, box.maxY, box.maxZ).endVertex();

        buffer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        buffer.pos(box.minX, box.maxY, box.maxZ).endVertex();

        buffer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        buffer.pos(box.minX, box.maxY, box.minZ).endVertex();

        // Vertical lines
        buffer.pos(box.minX, box.minY, box.minZ).endVertex();
        buffer.pos(box.minX, box.maxY, box.minZ).endVertex();

        buffer.pos(box.maxX, box.minY, box.minZ).endVertex();
        buffer.pos(box.maxX, box.maxY, box.minZ).endVertex();

        buffer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        buffer.pos(box.maxX, box.maxY, box.maxZ).endVertex();

        buffer.pos(box.minX, box.minY, box.maxZ).endVertex();
        buffer.pos(box.minX, box.maxY, box.maxZ).endVertex();

        tessellator.draw();
    }

    public static void renderEntityHitbox(Entity entity, float partialTicks, Color color, MobDisplayTypes type) {
        if (type == MobDisplayTypes.ITEMBIG) {
            renderItemBigHitbox(entity, partialTicks, color);
            return;
        }

        Vector3f loc = new Vector3f(
                (float) entity.posX - 0.5f,
                (float) entity.posY - 0.5f,
                (float) entity.posZ - 0.5f);

        if (type == MobDisplayTypes.BAT ||
                type == MobDisplayTypes.ENDERMAN_BOSS ||
                type == MobDisplayTypes.WOLF_BOSS ||
                type == MobDisplayTypes.SPIDER_BOSS ||
                type == MobDisplayTypes.M7ORBS ||
                type == MobDisplayTypes.AUTOMATON
        ) {
            GlStateManager.disableDepth();
        }

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();
        GL11.glDisable(GL11.GL_LIGHTING);
        GlStateManager.disableLighting();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        Entity player = mc.getRenderViewEntity();
        double playerX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double playerY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double playerZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

        double x = loc.x - playerX + 0.5;
        double y = loc.y - playerY - 0.5;
        if (type == MobDisplayTypes.BAT) {
            y = (loc.y - playerY) + 1;
        } else if (type == MobDisplayTypes.FEL) {
            y = loc.y - playerY + 2.3;
        }
        double z = loc.z - playerZ + 0.5;

        double y1 = y + type.getY1();
        double y2 = y + type.getY2();
        double x1 = x + type.getX1();
        double x2 = x + type.getX2();
        double z1 = z + type.getZ1();
        double z2 = z + type.getZ2();

        drawHitbox(x1, x2, y1, y2, z1, z2, color, type);

        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        GL11.glEnable(GL11.GL_LIGHTING);
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    private static void renderItemBigHitbox(Entity entity, float partialTicks, Color color) {
        AxisAlignedBB bb = entity.getEntityBoundingBox();
        if (bb == null) return;

        // Vantix fallback scale if config is missing (matches NEF's default intended scale)
        // Hardcoded scale since scoreSecrets config is not yet ported to Vantix
        double scale = 1.5D;

        Entity player = mc.getRenderViewEntity();
        double playerX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double playerY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double playerZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

        // Compute original box coordinates relative to player
        double x1 = bb.minX - playerX;
        double x2 = bb.maxX - playerX;
        double y1 = bb.minY - playerY;
        double y2 = bb.maxY - playerY;
        double z1 = bb.minZ - playerZ;
        double z2 = bb.maxZ - playerZ;

        // Compute the center of the bounding box
        double centerX = (x1 + x2) / 2;
        double centerY = (y1 + y2) / 2;
        double centerZ = (z1 + z2) / 2;

        //Scale bounding box relative to center
        x1 = centerX + (x1 - centerX) * scale;
        x2 = centerX + (x2 - centerX) * scale;
        y1 = centerY + (y1 - centerY) * scale;
        y2 = centerY + (y2 - centerY) * scale;
        z1 = centerZ + (z1 - centerZ) * scale;
        z2 = centerZ + (z2 - centerZ) * scale;

        double yOffset = (scale - 1f) * (entity.height / 2f);
        y1 += yOffset;
        y2 += yOffset;

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();
        GL11.glDisable(GL11.GL_LIGHTING);
        GlStateManager.disableLighting();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        drawHitbox(x1, x2, y1, y2, z1, z2, color, MobDisplayTypes.ITEMBIG);

        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        GL11.glEnable(GL11.GL_LIGHTING);
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    private static void drawHitbox(double x1, double x2, double y1, double y2, double z1, double z2, Color color, MobDisplayTypes type) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

        if (type == MobDisplayTypes.GAIA) {
            GL11.glLineWidth(5.0f);
        } else {
            GL11.glLineWidth(3.0f);
        }

        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        int alpha = color.getAlpha();

        double[][] vertices = {
                {x1, y1, z1}, {x2, y1, z1}, {x2, y2, z1}, {x1, y2, z1},
                {x1, y1, z2}, {x2, y1, z2}, {x2, y2, z2}, {x1, y2, z2}
        };

        int[][] edges = {
                {0, 1}, {1, 2}, {2, 3}, {3, 0},
                {4, 5}, {5, 6}, {6, 7}, {7, 4},
                {0, 4}, {1, 5}, {2, 6}, {3, 7}
        };

        for (int[] edge : edges) {
            worldRenderer.pos(vertices[edge[0]][0], vertices[edge[0]][1], vertices[edge[0]][2])
                    .color(red, green, blue, alpha).endVertex();
            worldRenderer.pos(vertices[edge[1]][0], vertices[edge[1]][1], vertices[edge[1]][2])
                    .color(red, green, blue, alpha).endVertex();
        }

        tessellator.draw();
    }

    public static void drawTag(String str, double[] pos, Color color, float partialTicks) {
        FontRenderer font = mc.fontRendererObj;
        EntityPlayerSP player = mc.thePlayer;
        RenderManager renderManager = mc.getRenderManager();

        Vec3 viewerPos = getInterpolatedPos(player, partialTicks);
        Vec3 tagPos = new Vec3(pos[0] - viewerPos.xCoord + 0.5, pos[1] - viewerPos.yCoord + 0.5, pos[2] - viewerPos.zCoord + 0.5);

        double distance = player.getDistance(pos[0], pos[1], pos[2]);
        float scale = Math.max(2.0F, (float) distance / 5.0F) * 0.016666668F;

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        GlStateManager.translate(tagPos.xCoord, tagPos.yCoord + 2.5, tagPos.zCoord);
        GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-scale, -scale, scale);

        setupRenderStateForText();

        drawTagBackground(font, str);
        font.drawString(str, -font.getStringWidth(str) / 2, 0, colorToInt(color));

        restoreRenderState();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    private static Vec3 getInterpolatedPos(Entity entity, float partialTicks) {
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
        return new Vec3(x, y, z);
    }

    private static void setupRenderStateForText() {
        GlStateManager.disableLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
    }

    private static void restoreRenderState() {
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GL11.glEnable(GL11.GL_LIGHTING);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.resetColor();
    }

    private static void drawTagBackground(FontRenderer font, String str) {
        int width = font.getStringWidth(str) / 2;

        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();

        GlStateManager.disableTexture2D();
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        wr.pos(-width - 1, -1, 0.0D).color(0, 0, 0, 64).endVertex();
        wr.pos(-width - 1, 8, 0.0D).color(0, 0, 0, 64).endVertex();
        wr.pos(width + 1, 8, 0.0D).color(0, 0, 0, 64).endVertex();
        wr.pos(width + 1, -1, 0.0D).color(0, 0, 0, 64).endVertex();
        tess.draw();
        GlStateManager.enableTexture2D();
    }

    public static void draw3DLine(Vec3 pos1, Vec3 pos2, Color color, int lineWidth, boolean depth, float partialTicks) {
        draw3DLine(pos1, pos2, color, lineWidth, depth, partialTicks, false, false, null);
    }

    public static void draw3DLine(double startX, double startY, double startZ,
                                  double endX, double endY, double endZ,
                                  Color color, int lineWidth, boolean depth, float partialTicks) {
        draw3DLine(new Vec3(startX, startY, startZ), new Vec3(endX, endY, endZ), color, lineWidth, depth, partialTicks);
    }

    public static void draw3DLine(Vec3 pos1, Vec3 pos2, Color color, int lineWidth, boolean depth,
                                  float partialTicks, boolean fromHead, boolean isLever, BlockLever.EnumOrientation orientation) {

        Entity viewer = mc.getRenderViewEntity();
        Vec3 interp = getInterpolatedPos(viewer, partialTicks);

        Vec3 start = isLever ? getLeverCenter(pos1, orientation) : pos1;
        Vec3 end = fromHead ? getPlayerLookVec() : pos2;

        GlStateManager.pushMatrix();
        GlStateManager.translate(-interp.xCoord, -interp.yCoord, -interp.zCoord);
        setupRenderState(depth, lineWidth);

        renderLine(start, end, color);

        cleanupRenderState(depth);
        GlStateManager.translate(interp.xCoord, interp.yCoord, interp.zCoord);
        GlStateManager.popMatrix();
    }

    private static Vec3 getLeverCenter(Vec3 pos, BlockLever.EnumOrientation orientation) {
        double x = pos.xCoord, y = pos.yCoord, z = pos.zCoord;

        switch (orientation) {
            case UP_X:
            case UP_Z:
                return new Vec3(x + 0.5, y + 0.1, z + 0.5);
            case NORTH:
                return new Vec3(x + 0.5, y + 0.5, z + 0.875);
            case SOUTH:
                return new Vec3(x + 0.5, y + 0.5, z + 0.125);
            case WEST:
                return new Vec3(x + 0.875, y + 0.5, z + 0.5);
            case EAST:
                return new Vec3(x + 0.125, y + 0.5, z + 0.5);
            default:
                return new Vec3(x + 0.5, y + 0.5, z - 1.125);
        }
    }

    private static Vec3 getPlayerLookVec() {
        float yaw = -mc.thePlayer.rotationYaw;
        float pitch = -mc.thePlayer.rotationPitch;
        return new Vec3(0, 0, 1)
                .rotatePitch((float) Math.toRadians(pitch))
                .rotateYaw((float) Math.toRadians(yaw));
    }

    private static void setupRenderState(boolean depth, int lineWidth) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GL11.glDisable(GL11.GL_LIGHTING);
        GlStateManager.disableLighting();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glLineWidth(lineWidth);

        if (!depth) {
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
        }
    }

    private static void cleanupRenderState(boolean depth) {
        if (!depth) {
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
        }

        GlStateManager.disableBlend();
        GL11.glEnable(GL11.GL_LIGHTING);
        GlStateManager.enableLighting();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static void renderLine(Vec3 start, Vec3 end, Color color) {
        WorldRenderer wr = Tessellator.getInstance().getWorldRenderer();
        GlStateManager.color(color.getRed() / 255f, color.getGreen() / 255f,
                color.getBlue() / 255f, color.getAlpha() / 255f);
        wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        wr.pos(start.xCoord, start.yCoord, start.zCoord).endVertex();
        wr.pos(end.xCoord, end.yCoord, end.zCoord).endVertex();
        Tessellator.getInstance().draw();
    }

    public static void highlightBlock(BlockPos pos, Color color, boolean disableDepth, float partialTicks) {
        highlightBlock(pos, color, disableDepth, false, partialTicks);
    }

    public static void renderFilledBoundingBox(AxisAlignedBB bb, Color color, boolean disableDepth) {
        GlStateManager.pushMatrix();
        if (disableDepth) GlStateManager.disableDepth();
        GlStateManager.disableCull();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        drawFilledBoundingBox(bb, color, 1f);
        GL11.glEnable(GL11.GL_LIGHTING);
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.enableCull();
        if (disableDepth) GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    public static void highlightBlock(BlockPos pos, Color color, boolean disableDepth, boolean isButton, float partialTicks) {
        Entity viewer = mc.getRenderViewEntity();
        double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks;
        double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks;
        double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks;

        double x = pos.getX() - viewerX;
        double y = pos.getY() - viewerY;
        double z = pos.getZ() - viewerZ;

        if (disableDepth) GlStateManager.disableDepth();
        GlStateManager.disableCull();
        GlStateManager.disableLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        double initialToAddX = 0;
        if (!disableDepth) {
            initialToAddX = .05;
        }
        if (!isButton) {
            if (disableDepth) {
                RenderUtils.drawFilledBoundingBox(new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1), 1f, color);
            } else {
                RenderUtils.drawFilledBoundingBox(new AxisAlignedBB(x - initialToAddX, y, z, x + 1 + initialToAddX, y + 1, z + 1), 1f, color);
            }
        } else {
            RenderUtils.drawFilledBoundingBox(new AxisAlignedBB(x, y + 0.5 - 0.13, z + 0.5 - 0.191, x - .13, y + 0.5 + 0.13, z + 0.5 + 0.191), 1f, color);
        }

        GL11.glEnable(GL11.GL_LIGHTING);
        GlStateManager.enableLighting();
        if (disableDepth) GlStateManager.enableDepth();
        GlStateManager.enableCull();
    }

    public static void drawLeverBoundingBox(BlockPos pos, EnumFacing facing, Color color, float partialTicks) {
        Entity viewer = mc.getRenderViewEntity();
        double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks;
        double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks;
        double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks;

        double x = pos.getX() - viewerX;
        double y = pos.getY() - viewerY;
        double z = pos.getZ() - viewerZ;

        AxisAlignedBB boundingBox;
        switch (facing) {
            case NORTH:
                boundingBox = new AxisAlignedBB(x + 0.25, y + 0.1875, z + 0.75, x + 0.75, y + 0.8125, z + 1);
                break;
            case SOUTH:
                boundingBox = new AxisAlignedBB(x + 0.25, y + 0.1875, z, x + 0.75, y + 0.8125, z + 0.25);
                break;
            case WEST:
                boundingBox = new AxisAlignedBB(x + 0.75, y + 0.1875, z + 0.25, x + 1, y + 0.8125, z + 0.75);
                break;
            case EAST:
                boundingBox = new AxisAlignedBB(x, y + 0.1875, z + 0.25, x + 0.25, y + 0.8125, z + 0.75);
                break;
            default:
                boundingBox = new AxisAlignedBB(x + 0.25, y + 0.1875, z - 1.25, x + 0.75, y + 0.8125, z - 1);
                break;
        }

        GlStateManager.disableCull();
        GlStateManager.disableLighting();
        GL11.glDisable(GL11.GL_LIGHTING);

        RenderUtils.drawFilledBoundingBox(boundingBox, 1f, color);

        GL11.glEnable(GL11.GL_LIGHTING);
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
    }

    public static void drawFilledBoundingBox(AxisAlignedBB box, float alpha, Color color) {
        setupGlStateForBox();

        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;
        float a = MathHelper.clamp_float(color.getAlpha() / 255f * alpha, 0.0f, 1.0f);

        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();

        drawFace(wr, tess, box.minX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ, r, g, b, a);
        drawFace(wr, tess, box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.maxZ, r, g, b, a);
        drawFace(wr, tess, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.minZ, r, g, b, a);
        drawFace(wr, tess, box.minX, box.minY, box.maxZ, box.maxX, box.maxY, box.maxZ, r, g, b, a);
        drawFace(wr, tess, box.minX, box.minY, box.minZ, box.minX, box.maxY, box.maxZ, r, g, b, a);
        drawFace(wr, tess, box.maxX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, r, g, b, a);

        restoreGlState();
    }

    private static void setupGlStateForBox() {
        GlStateManager.pushAttrib();
        GlStateManager.disableTexture2D();
        GL11.glDisable(GL11.GL_LIGHTING);
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
    }

    private static void restoreGlState() {
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GL11.glEnable(GL11.GL_LIGHTING);
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.popAttrib();
    }

    private static void drawFace(WorldRenderer wr, Tessellator tess,
                                 double x1, double y1, double z1,
                                 double x2, double y2, double z2,
                                 float r, float g, float b, float a) {
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        double minX = Math.min(x1, x2);
        double maxX = Math.max(x1, x2);
        double minY = Math.min(y1, y2);
        double maxY = Math.max(y1, y2);
        double minZ = Math.min(z1, z2);
        double maxZ = Math.max(z1, z2);

        if (minX == maxX) {
            wr.pos(minX, minY, minZ).color(r, g, b, a).endVertex();
            wr.pos(minX, maxY, minZ).color(r, g, b, a).endVertex();
            wr.pos(minX, maxY, maxZ).color(r, g, b, a).endVertex();
            wr.pos(minX, minY, maxZ).color(r, g, b, a).endVertex();
        } else if (minY == maxY) {
            wr.pos(minX, minY, minZ).color(r, g, b, a).endVertex();
            wr.pos(maxX, minY, minZ).color(r, g, b, a).endVertex();
            wr.pos(maxX, minY, maxZ).color(r, g, b, a).endVertex();
            wr.pos(minX, minY, maxZ).color(r, g, b, a).endVertex();
        } else if (minZ == maxZ) {
            wr.pos(minX, minY, minZ).color(r, g, b, a).endVertex();
            wr.pos(maxX, minY, minZ).color(r, g, b, a).endVertex();
            wr.pos(maxX, maxY, minZ).color(r, g, b, a).endVertex();
            wr.pos(minX, maxY, minZ).color(r, g, b, a).endVertex();
        }

        tess.draw();
    }

    public static int colorToInt(Color color) {
        return (color.getRed() << 16) | (color.getGreen() << 8) | color.getBlue();
    }

    public static void drawFilledBoundingBoxEntity(AxisAlignedBB aabb, float alpha, Color color, float partialTicks) {
        Entity render = mc.getRenderViewEntity();

        double coordX = render.lastTickPosX + (render.posX - render.lastTickPosX) * partialTicks;
        double coordY = render.lastTickPosY + (render.posY - render.lastTickPosY) * partialTicks;
        double coordZ = render.lastTickPosZ + (render.posZ - render.lastTickPosZ) * partialTicks;

        GlStateManager.pushMatrix();
        GlStateManager.translate(-coordX, -coordY, -coordZ);

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GL11.glDisable(GL11.GL_LIGHTING);
        GlStateManager.disableLighting();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        GlStateManager.color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, (color.getAlpha() / 255f) * alpha);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GL11.glEnable(GL11.GL_LIGHTING);
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    public static void renderWaypointText(String str, BlockPos loc, float partialTicks) {
        renderWaypointText(str, loc, partialTicks, true);
    }

    public static void renderWaypointText(String str, BlockPos loc, float partialTicks, boolean showDistance) {
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GL11.glDisable(GL11.GL_LIGHTING);

        Entity viewer = mc.getRenderViewEntity();
        double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks;
        double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks;
        double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks;

        double x = loc.getX() + 0.5 - viewerX;
        double y = loc.getY() + 0.5 - viewerY - viewer.getEyeHeight();
        double z = loc.getZ() + 0.5 - viewerZ;

        double distSq = x * x + y * y + z * z;
        double dist = Math.sqrt(distSq);
        if (distSq > 144) {
            x *= 12 / dist;
            y *= 12 / dist;
            z *= 12 / dist;
        }
        GlStateManager.translate(x, y, z);
        GlStateManager.translate(0, viewer.getEyeHeight(), 0);

        float scale = 2.0F;
        GlStateManager.scale(scale, scale, scale);

        drawNametag(str);

        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(0, -0.25f, 0);
        GlStateManager.rotate(-mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);

        if (showDistance) drawNametag(EnumChatFormatting.YELLOW.toString() + Math.round(dist) + "m");

        GL11.glEnable(GL11.GL_LIGHTING);
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    public static void drawNametag(String str) {
        FontRenderer fontrenderer = mc.fontRendererObj;
        float f = 1.6F;
        float f1 = 0.016666668F * f;
        GlStateManager.pushMatrix();
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        RenderManager renderManager = mc.getRenderManager();
        GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-f1, -f1, f1);
        GlStateManager.disableLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        int j = fontrenderer.getStringWidth(str) / 2;
        GlStateManager.disableTexture2D();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(-j - 1, -1, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos(-j - 1, 8, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos(j + 1, 8, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos(j + 1, -1, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, 0, 553648127);
        GlStateManager.depthMask(true);
        fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, 0, -1);
        GlStateManager.enableDepth();
        GlStateManager.enableBlend();
        GL11.glEnable(GL11.GL_LIGHTING);
        GlStateManager.enableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB aabb, Color color, float width, float partialTicks) {
        Entity render = mc.getRenderViewEntity();
        double realX = render.lastTickPosX + (render.posX - render.lastTickPosX) * partialTicks;
        double realY = render.lastTickPosY + (render.posY - render.lastTickPosY) * partialTicks;
        double realZ = render.lastTickPosZ + (render.posZ - render.lastTickPosZ) * partialTicks;

        GlStateManager.pushMatrix();
        GlStateManager.translate(-realX, -realY, -realZ);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GL11.glDisable(GL11.GL_LIGHTING);
        GlStateManager.disableLighting();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glLineWidth(width);

        RenderGlobal.drawOutlinedBoundingBox(aabb, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        GlStateManager.translate(realX, realY, realZ);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GL11.glEnable(GL11.GL_LIGHTING);
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    public static Vector3f getInterpolatedPlayerPosition(float partialTicks) {
        Entity viewer = mc.getRenderViewEntity();
        Vector3f lastPos = new Vector3f(
                (float) viewer.lastTickPosX,
                (float) viewer.lastTickPosY,
                (float) viewer.lastTickPosZ
        );
        Vector3f currentPos = new Vector3f(
                (float) viewer.posX,
                (float) viewer.posY,
                (float) viewer.posZ
        );
        Vector3f movement = Vector3f.sub(currentPos, lastPos, currentPos);
        movement.scale(partialTicks);
        return Vector3f.add(lastPos, movement, lastPos);
    }

    public static void renderBlockBox(BlockPos pos, Color c, float partialTicks) {
        renderBlockBox(pos, c, partialTicks, false);
    }

    public static void renderBlockBox(BlockPos pos, Color c, float partialTicks, boolean disableDepth) {
        Vector3f interpolatedPlayerPosition = getInterpolatedPlayerPosition(partialTicks);
        renderBoundingBoxInViewSpace(
                pos.getX() - interpolatedPlayerPosition.x,
                pos.getY() - interpolatedPlayerPosition.y,
                pos.getZ() - interpolatedPlayerPosition.z,
                c,
                disableDepth
        );
    }

    private static void renderBoundingBoxInViewSpace(double x, double y, double z, Color c, boolean disableDepth) {
        AxisAlignedBB bb = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);

        if (disableDepth) GlStateManager.disableDepth();
        GlStateManager.disableCull();
        GlStateManager.disableTexture2D();
        drawFilledBoundingBox(bb, c, 1f);
        GlStateManager.enableTexture2D();
        GlStateManager.enableCull();
        if (disableDepth) GlStateManager.enableDepth();
    }

    public static void drawFilledBoundingBox(AxisAlignedBB p_181561_0_, Color c, float alpha) {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableTexture2D();

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        // Top + Bottom (full color)
        GlStateManager.color(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f * alpha);
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        tessellator.draw();

        // West + East (80% brightness)
        GlStateManager.color(c.getRed() / 255f * 0.8f, c.getGreen() / 255f * 0.8f, c.getBlue() / 255f * 0.8f, c.getAlpha() / 255f * alpha);
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        tessellator.draw();

        // North + South (90% brightness)
        GlStateManager.color(c.getRed() / 255f * 0.9f, c.getGreen() / 255f * 0.9f, c.getBlue() / 255f * 0.9f, c.getAlpha() / 255f * alpha);
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        tessellator.draw();
    }

    public static void renderBoundingBox(BlockPos pos, int rgb, float partialTicks) {
        renderBoundingBox(pos, rgb, partialTicks, true);
    }

    public static void renderBoundingBox(BlockPos pos, int rgb, float partialTicks, boolean ignoreDepth) {
        double playerX = mc.thePlayer.prevPosX + (mc.thePlayer.posX - mc.thePlayer.prevPosX) * partialTicks;
        double playerY = mc.thePlayer.prevPosY + (mc.thePlayer.posY - mc.thePlayer.prevPosY) * partialTicks;
        double playerZ = mc.thePlayer.prevPosZ + (mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * partialTicks;

        double x = pos.getX() - playerX;
        double y = pos.getY() - playerY;
        double z = pos.getZ() - playerZ;

        float r = ((rgb >> 16) & 0xFF) / 255.0f;
        float g = ((rgb >> 8) & 0xFF) / 255.0f;
        float b = (rgb & 0xFF) / 255.0f;

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        if (ignoreDepth) GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GL11.glDisable(GL11.GL_LIGHTING);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(2.0f);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

        double minX = x, maxX = x + 1;
        double minY = y, maxY = y + 1;
        double minZ = z, maxZ = z + 1;

        // Bottom face
        worldRenderer.pos(minX, minY, minZ).color(r, g, b, 1.0f).endVertex();
        worldRenderer.pos(maxX, minY, minZ).color(r, g, b, 1.0f).endVertex();
        worldRenderer.pos(maxX, minY, minZ).color(r, g, b, 1.0f).endVertex();
        worldRenderer.pos(maxX, minY, maxZ).color(r, g, b, 1.0f).endVertex();
        worldRenderer.pos(maxX, minY, maxZ).color(r, g, b, 1.0f).endVertex();
        worldRenderer.pos(minX, minY, maxZ).color(r, g, b, 1.0f).endVertex();
        worldRenderer.pos(minX, minY, maxZ).color(r, g, b, 1.0f).endVertex();
        worldRenderer.pos(minX, minY, minZ).color(r, g, b, 1.0f).endVertex();

        // Top face
        worldRenderer.pos(minX, maxY, minZ).color(r, g, b, 1.0f).endVertex();
        worldRenderer.pos(maxX, maxY, minZ).color(r, g, b, 1.0f).endVertex();
        worldRenderer.pos(maxX, maxY, minZ).color(r, g, b, 1.0f).endVertex();
        worldRenderer.pos(maxX, maxY, maxZ).color(r, g, b, 1.0f).endVertex();
        worldRenderer.pos(maxX, maxY, maxZ).color(r, g, b, 1.0f).endVertex();
        worldRenderer.pos(minX, maxY, maxZ).color(r, g, b, 1.0f).endVertex();
        worldRenderer.pos(minX, maxY, maxZ).color(r, g, b, 1.0f).endVertex();
        worldRenderer.pos(minX, maxY, minZ).color(r, g, b, 1.0f).endVertex();

        // Vertical edges
        worldRenderer.pos(minX, minY, minZ).color(r, g, b, 1.0f).endVertex();
        worldRenderer.pos(minX, maxY, minZ).color(r, g, b, 1.0f).endVertex();
        worldRenderer.pos(maxX, minY, minZ).color(r, g, b, 1.0f).endVertex();
        worldRenderer.pos(maxX, maxY, minZ).color(r, g, b, 1.0f).endVertex();
        worldRenderer.pos(maxX, minY, maxZ).color(r, g, b, 1.0f).endVertex();
        worldRenderer.pos(maxX, maxY, maxZ).color(r, g, b, 1.0f).endVertex();
        worldRenderer.pos(minX, minY, maxZ).color(r, g, b, 1.0f).endVertex();
        worldRenderer.pos(minX, maxY, maxZ).color(r, g, b, 1.0f).endVertex();

        tessellator.draw();

        if (ignoreDepth) GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glEnable(GL11.GL_LIGHTING);
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    public static void renderBlockHighlight(BlockPos pos, Color color, float partialTicks) {
        Entity viewer = mc.getRenderViewEntity();
        double px = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks;
        double py = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks;
        double pz = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks;

        AxisAlignedBB bb = new AxisAlignedBB(
                pos.getX() - px, pos.getY() - py, pos.getZ() - pz,
                pos.getX() + 1 - px, pos.getY() + 1 - py, pos.getZ() + 1 - pz
        ).expand(0.01, 0.01, 0.01);

        drawFilledBoundingBox(bb, color, 1f);

        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glLineWidth(3);
        GlStateManager.color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();

        wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        wr.pos(bb.minX, bb.minY, bb.minZ).endVertex();
        wr.pos(bb.maxX, bb.minY, bb.minZ).endVertex();
        wr.pos(bb.maxX, bb.minY, bb.maxZ).endVertex();
        wr.pos(bb.minX, bb.minY, bb.maxZ).endVertex();
        wr.pos(bb.minX, bb.minY, bb.minZ).endVertex();
        tessellator.draw();

        wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        wr.pos(bb.minX, bb.maxY, bb.minZ).endVertex();
        wr.pos(bb.maxX, bb.maxY, bb.minZ).endVertex();
        wr.pos(bb.maxX, bb.maxY, bb.maxZ).endVertex();
        wr.pos(bb.minX, bb.maxY, bb.maxZ).endVertex();
        wr.pos(bb.minX, bb.maxY, bb.minZ).endVertex();
        tessellator.draw();

        wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        wr.pos(bb.minX, bb.minY, bb.minZ).endVertex();
        wr.pos(bb.minX, bb.maxY, bb.minZ).endVertex();
        wr.pos(bb.maxX, bb.minY, bb.minZ).endVertex();
        wr.pos(bb.maxX, bb.maxY, bb.minZ).endVertex();
        wr.pos(bb.maxX, bb.minY, bb.maxZ).endVertex();
        wr.pos(bb.maxX, bb.maxY, bb.maxZ).endVertex();
        wr.pos(bb.minX, bb.minY, bb.maxZ).endVertex();
        wr.pos(bb.minX, bb.maxY, bb.maxZ).endVertex();
        tessellator.draw();

        GL11.glLineWidth(1);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawPetSidebar(net.minecraft.client.gui.inventory.GuiChest chest, int totalPetScore,
                                      List<String> missingPets, List<String> upgradeablePets,
                                      int scrollIndexRight, int scrollIndexLeft) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        FontRenderer fr = mc.fontRendererObj;

        int guiLeft = (chest.width - 176) / 2;
        int guiTop = (chest.height - 222) / 2;

        // --- RIGHT TABLE (Missing Pets & Score) ---
        int rightX = guiLeft + 182;
        int rightY = guiTop + 15;

        Gui.drawRect(rightX - 6, rightY - 6, rightX + 136, rightY + 186, 0xFFFFAA00); // Gold Border
        Gui.drawRect(rightX - 5, rightY - 5, rightX + 135, rightY + 185, 0xDD000000); // Dark Inner

        fr.drawStringWithShadow("\u00A76\u2605 Total Pet Score: \u00A7e" + totalPetScore, rightX, rightY, 0xFFFFFF);
        Gui.drawRect(rightX - 5, rightY + 12, rightX + 135, rightY + 13, 0x55FFAA00); // Separator Line
        rightY += 18;

        fr.drawStringWithShadow("\u00A7bMissing Pets:", rightX, rightY, 0xFFFFFF);
        rightY += 12;

        int itemsToShow = 15;
        for (int i = 0; i < itemsToShow; i++) {
            int listIndex = scrollIndexRight + i;
            if (listIndex >= missingPets.size()) break;
            fr.drawStringWithShadow(missingPets.get(listIndex), rightX, rightY, 0xFFFFFF);
            rightY += 10;
        }
        if (missingPets.size() > itemsToShow) {
            fr.drawStringWithShadow("\u00A78(Scroll for more)", rightX, rightY + 5, 0xFFFFFF);
        }

        // --- LEFT TABLE (Upgradeable Pets) ---
        int leftX = guiLeft - 150;
        int leftY = guiTop + 15;

        Gui.drawRect(leftX - 6, leftY - 6, leftX + 146, leftY + 186, 0xFFFFAA00); // Gold Border
        Gui.drawRect(leftX - 5, leftY - 5, leftX + 145, leftY + 185, 0xDD000000); // Dark Inner

        fr.drawStringWithShadow("\u00A7a\u2B06 Upgradeable Pets", leftX, leftY, 0xFFFFFF);
        Gui.drawRect(leftX - 5, leftY + 12, leftX + 145, leftY + 13, 0x5555FF55); // Separator Line
        leftY += 18;

        for (int i = 0; i < itemsToShow; i++) {
            int listIndex = scrollIndexLeft + i;
            if (listIndex >= upgradeablePets.size()) break;
            fr.drawStringWithShadow(upgradeablePets.get(listIndex), leftX, leftY, 0xFFFFFF);
            leftY += 10;
        }
        if (upgradeablePets.size() > itemsToShow) {
            fr.drawStringWithShadow("\u00A78(Scroll for more)", leftX, leftY + 5, 0xFFFFFF);
        }
    }

    public static void drawModalRectWithCustomSizedTexture(
            float x, float y,
            float u, float v,
            float width, float height,
            float textureWidth, float textureHeight,
            boolean linearTexture) {

        if (linearTexture) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        }

        float f  = 1.0f / textureWidth;
        float f1 = 1.0f / textureHeight;

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();
        wr.begin(7, DefaultVertexFormats.POSITION_TEX);
        wr.pos(x,         y + height, 0.0).tex( u          * f, (v + height) * f1).endVertex();
        wr.pos(x + width, y + height, 0.0).tex((u + width) * f, (v + height) * f1).endVertex();
        wr.pos(x + width, y,          0.0).tex((u + width) * f,  v            * f1).endVertex();
        wr.pos(x,         y,          0.0).tex( u          * f,  v            * f1).endVertex();
        tessellator.draw();

        if (linearTexture) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        }
    }

    public static void drawModalRectWithCustomSizedTexture(
            float x, float y,
            float u, float v,
            float width, float height,
            float textureWidth, float textureHeight) {
        drawModalRectWithCustomSizedTexture(x, y, u, v, width, height, textureWidth, textureHeight, false);
    }

    public static void drawScaledCustomSizeModalRect(
            float x, float y,
            float u, float v,
            float uWidth, float vHeight,
            float width, float height,
            float textureWidth, float textureHeight,
            boolean linearTexture) {

        if (linearTexture) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        }

        float f  = 1.0f / textureWidth;
        float f1 = 1.0f / textureHeight;

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();
        wr.begin(7, DefaultVertexFormats.POSITION_TEX);
        wr.pos(x,         y + height, 0.0).tex( u            * f, (v + vHeight) * f1).endVertex();
        wr.pos(x + width, y + height, 0.0).tex((u + uWidth) * f, (v + vHeight) * f1).endVertex();
        wr.pos(x + width, y,          0.0).tex((u + uWidth) * f,  v             * f1).endVertex();
        wr.pos(x,         y,          0.0).tex( u            * f,  v             * f1).endVertex();
        tessellator.draw();

        if (linearTexture) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        }
    }

    public static void drawScaledCustomSizeModalRect(
            float x, float y,
            float u, float v,
            float uWidth, float vHeight,
            float width, float height,
            float textureWidth, float textureHeight) {
        drawScaledCustomSizeModalRect(x, y, u, v, uWidth, vHeight, width, height, textureWidth, textureHeight, false);
    }
}