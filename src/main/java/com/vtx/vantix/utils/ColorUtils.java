package com.vtx.vantix.utils;

import org.jetbrains.annotations.NotNull;
import java.awt.Color;

public class ColorUtils {

    // Color Fallback Constants
    private static final String DEFAULT_COLOR_STRING = "0:255:255:255:255"; // Opaque white as fallback
    private static final Color DEFAULT_COLOR = new Color(255, 255, 255, 255);

    // Chroma Math Constants & Fields
    private static final int RADIX = 10;
    private static final int MIN_CHROMA_SECS = 1;
    private static final int MAX_CHROMA_SECS = 60;
    public static long startTime = -1;

    // Private constructor to prevent instantiation
    private ColorUtils() {}

    /**
     * Strips Minecraft formatting codes using a strict character range.
     */
    public static String stripColor(String s) {
        return s == null ? "" : s.replaceAll("§[0-9a-fklmnorA-FKLMNOR]", "");
    }

    /**
     * Cleans color codes using a case-insensitive regex matching any character after section symbol.
     */
    public static String cleanColor(String in) {
        return in == null ? "" : in.replaceAll("(?i)\\u00A7.", "");
    }

    /**
     * Converts a chroma color string to a Color object.
     * Expects format "chromaSpeed:alpha:r:g:b" (e.g., "0:0:0:0:0").
     * Falls back to opaque white if the string is invalid.
     *
     * @param colorString The chroma color string
     * @return A Color object representing the parsed ARGB value
     */
    public static @NotNull Color getColor(String colorString) {
        // Handle null or malformed input safely without crashing
        if (colorString == null || colorString.split(":").length != 5) {
            System.err.println("Invalid color string: " + colorString + ". Expected 'chromaSpeed:alpha:r:g:b'. Using default: " + DEFAULT_COLOR_STRING);
            return DEFAULT_COLOR;
        }

        try {
            int argb = specialToChromaRGB(colorString);
            return new Color(argb, true); // ARGB format
        } catch (Exception e) {
            System.err.println("Error parsing color string: " + colorString + ". Error: " + e.getMessage() + ". Using default: " + DEFAULT_COLOR_STRING);
            return DEFAULT_COLOR;
        }
    }

    /**
     * Converts a Color object to a hexadecimal string.
     *
     * @param color        The Color object to convert
     * @param includeAlpha Whether to include the alpha channel in the hex string
     * @return A hex string (e.g., "#FF0000" or "#FF0000FF")
     */
    public static String colorToHex(Color color, boolean includeAlpha) {
        if (color == null) {
            return includeAlpha ? "#FFFFFFFF" : "#FFFFFF"; // Default to white
        }
        if (includeAlpha) {
            return String.format("#%08X", color.getRGB()); // ARGB format
        } else {
            return String.format("#%06X", color.getRGB() & 0xFFFFFF); // RGB only
        }
    }

    // =========================================================================
    // FIXED INTEGRATED CHROMACOLOUR LOGIC
    // =========================================================================

    public static String special(int chromaSpeed, int alpha, int rgb) {
        return special(chromaSpeed, alpha, (rgb & 0xFF0000) >> 16, (rgb & 0x00FF00) >> 8, (rgb & 0x0000FF));
    }

    public static String special(int chromaSpeed, int alpha, int r, int g, int b) {
        return Integer.toString(chromaSpeed, RADIX) + ":" +
                Integer.toString(alpha, RADIX) + ":" +
                Integer.toString(r, RADIX) + ":" +
                Integer.toString(g, RADIX) + ":" +
                Integer.toString(b, RADIX);
    }

    private static int[] decompose(String csv) {
        if (csv == null || csv.isEmpty()) {
            return new int[]{0, 255, 255, 255, 255}; // Safely handle empty strings
        }
        String[] split = csv.split(":");
        int[] arr = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            try {
                arr[i] = Integer.parseInt(split[split.length - 1 - i], RADIX);
            } catch (NumberFormatException e) {
                arr[i] = 0; // Fallback for invalid integers
            }
        }
        return arr;
    }

    public static int specialToSimpleRGB(String special) {
        int[] d = decompose(special);
        if (d.length < 5) return 0xFFFFFFFF; // Safe boundary fallback
        int r = d[2];
        int g = d[1];
        int b = d[0];
        int a = d[3];
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
    }

    public static int getSpeed(String special) {
        int[] d = decompose(special);
        return d.length >= 5 ? d[4] : 0;
    }

    public static float getSecondsForSpeed(int speed) {
        return (255 - speed) / 254f * (MAX_CHROMA_SECS - MIN_CHROMA_SECS) + MIN_CHROMA_SECS;
    }

    public static int specialToChromaRGB(String special) {
        if (startTime < 0) startTime = System.currentTimeMillis();

        int[] d = decompose(special);
        if (d.length < 5) return 0xFFFFFFFF; // Prevent crash if string formatting is broken

        int chr = d[4];
        int a = d[3];
        int r = d[2];
        int g = d[1];
        int b = d[0];

        float[] hsv = Color.RGBtoHSB(r, g, b, null);

        if (chr > 0) {
            float seconds = getSecondsForSpeed(chr);
            hsv[0] += (System.currentTimeMillis() - startTime) / 1000f / seconds;
            hsv[0] %= 1;
            if (hsv[0] < 0) hsv[0] += 1;
        }

        return (a & 0xFF) << 24 | (Color.HSBtoRGB(hsv[0], hsv[1], hsv[2]) & 0x00FFFFFF);
    }

    public static int rotateHue(int argb, int degrees) {
        int a = (argb >> 24) & 0xFF;
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = (argb) & 0xFF;

        float[] hsv = Color.RGBtoHSB(r, g, b, null);

        hsv[0] += degrees / 360f;
        hsv[0] %= 1;

        return (a & 0xFF) << 24 | (Color.HSBtoRGB(hsv[0], hsv[1], hsv[2]) & 0x00FFFFFF);
    }
}
