package com.vtx.vantix.utils;

import com.vtx.vantix.core.VNTXConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;

public final class SoundUtils {

    private SoundUtils() {}

    // ==========================================
    // VANTIX ORIGINAL METHODS
    // ==========================================

    public static void playSound(String sound, float volume, float pitch) {
        try {
            ISound s = new PositionedSound(new ResourceLocation(sound)) {{
                repeat = false;
                repeatDelay = 0;
                attenuationType = ISound.AttenuationType.NONE;
            }};
            Minecraft.getMinecraft().getSoundHandler().playSound(s);
        } catch (Exception ignored) {}
    }

    public static void playSound(String sound) {
        playSound(sound, 1.0f, 1.0f);
    }

    // ==========================================
    // NOTENOUGHFAKEPIXEL (NEF) PORTED METHODS
    // ==========================================

    public static void playSound(int[] cords, String sound, float volume, float pitch) {
        playGlobalSound(sound, volume, pitch);
    }

    public static void playSound(BlockPos pos, String sound, float volume, float pitch) {
        playGlobalSound(sound, volume, pitch);
    }

    public static void playSound(int x, int y, int z, String sound, float volume, float pitch) {
        playGlobalSound(sound, volume, pitch);
    }

    public static void playGlobalSound(String sound, float volume, float pitch) {
        // Uncomment this line if you add 'public boolean enableSounds = true;' to your MiscConfig!
        // if (VNTXConfig.feature != null && !VNTXConfig.feature.misc.enableSounds) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null || mc.thePlayer == null) return;

        mc.addScheduledTask(() -> {
            mc.theWorld.playSound(
                    mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ,
                    new ResourceLocation(sound).toString(),
                    volume,
                    pitch,
                    false
            );
        });
    }
}