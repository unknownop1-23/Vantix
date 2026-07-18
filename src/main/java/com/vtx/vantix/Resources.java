package com.vtx.vantix;

import net.minecraft.util.ResourceLocation;


public final class Resources {

    public static final ResourceLocation DISCORD = new ResourceLocation("vantix:discord.png");
    public static final ResourceLocation GITHUB = new ResourceLocation("vantix:github.png");
    public static final ResourceLocation MODRINTH = new ResourceLocation("vantix:modrinth.png");
    public static final ResourceLocation SKYATLAS = new ResourceLocation("vantix:skyatlas.png");
    public static final ResourceLocation button_tex = new ResourceLocation("vantix:button.png");
    public static final ResourceLocation button_white = new ResourceLocation("vantix:button_white.png");
    public static final ResourceLocation BAR = new ResourceLocation("vantix:core/bar.png");
    public static final ResourceLocation OFF = new ResourceLocation("vantix:core/toggle_off.png");
    public static final ResourceLocation ONE = new ResourceLocation("vantix:core/toggle_1.png");
    public static final ResourceLocation TWO = new ResourceLocation("vantix:core/toggle_2.png");
    public static final ResourceLocation THREE = new ResourceLocation("vantix:core/toggle_3.png");
    public static final ResourceLocation ON = new ResourceLocation("vantix:core/toggle_on.png");
    public static final ResourceLocation DELETE = new ResourceLocation("vantix:core/delete.png");
    public static final ResourceLocation RESET = new ResourceLocation("vantix:core/reset.png");
    public static final ResourceLocation SEARCH_ICON = new ResourceLocation("vantix:search.png");
    public static final ResourceLocation slider_off_cap = new ResourceLocation("vantix:core/slider/slider_off_cap.png");
    public static final ResourceLocation slider_off_notch = new ResourceLocation("vantix:core/slider/slider_off_notch.png");
    public static final ResourceLocation slider_off_segment = new ResourceLocation("vantix:core/slider/slider_off_segment.png");
    public static final ResourceLocation slider_on_cap = new ResourceLocation("vantix:core/slider/slider_on_cap.png");
    public static final ResourceLocation slider_on_notch = new ResourceLocation("vantix:core/slider/slider_on_notch.png");
    public static final ResourceLocation slider_on_segment = new ResourceLocation("vantix:core/slider/slider_on_segment.png");
    public static final ResourceLocation slider_button_new = new ResourceLocation("vantix:core/slider/slider_button.png");
    public static final ResourceLocation colour_selector_dot = new ResourceLocation("vantix:core/colour_selector_dot.png");
    public static final ResourceLocation colour_selector_bar = new ResourceLocation("vantix:core/colour_selector_bar.png");
    public static final ResourceLocation colour_selector_bar_alpha = new ResourceLocation("vantix:core/colour_selector_bar_alpha.png");
    public static final ResourceLocation colour_selector_chroma = new ResourceLocation("vantix:core/colour_selector_chroma.png");
    public static final ResourceLocation colourPickerLocation = new ResourceLocation("mbcore:dynamic/colourpicker");
    public static final ResourceLocation colourPickerBarValueLocation = new ResourceLocation("mbcore:dynamic/colourpickervalue");
    public static final ResourceLocation colourPickerBarOpacityLocation = new ResourceLocation("mbcore:dynamic/colourpickeropacity");
    public static final ResourceLocation SEARCH_BAR_TEX = new ResourceLocation("vantix", "textures/gui/search_bar.png");
    public static final ResourceLocation SEARCH_BAR_TEX_GOLD = new ResourceLocation("vantix", "textures/gui/search_bar_gold.png");
    public static final ResourceLocation INVENTORY_TEX = new ResourceLocation("minecraft:textures/gui/container/inventory.png");
    public static final ResourceLocation INV_EDITOR_TEX = new ResourceLocation("vantix", "invbuttons/editor.png");
    public static final ResourceLocation INV_PRESETS_JSON = new ResourceLocation("vantix", "invbuttons/presets.json");
    public static final ResourceLocation INV_EXTRA_ICONS_JSON = new ResourceLocation("vantix", "invbuttons/extraicons.json");
    public static final ResourceLocation DUNGEON_ROOMS_JSON = new ResourceLocation("vantix", "dungeonrooms/dungeonrooms.json");
    public static final ResourceLocation SECRET_LOCATIONS_JSON = new ResourceLocation("vantix", "dungeonrooms/secretlocations.json");
    public static final ResourceLocation CASE_FADE_SIDE = new ResourceLocation("vantix", "textures/dungeons/caseopening/gui/fade_side.png");
    public static final ResourceLocation CASE_BLUR_SHADER = new ResourceLocation("vantix", "shaders/post/blur.json");
    public static final ResourceLocation PROTECT_ITEM_STAR = new ResourceLocation("vantix", "textures/gui/protect_star.png");
    public static final ResourceLocation CAPES_UI = new ResourceLocation("vantix", "textures/gui/capesUI.png");
    public static final ResourceLocation DVD_LOGO = new ResourceLocation("vantix", "textures/dvd.png");

    public static final ResourceLocation GHOSTTRACKER_KILLS = new ResourceLocation("vantix", "ghosttracker/kills.png");
    public static final ResourceLocation GHOSTTRACKER_SORROW = new ResourceLocation("vantix", "ghosttracker/sorrow.png");
    public static final ResourceLocation GHOSTTRACKER_VOLTA = new ResourceLocation("vantix", "ghosttracker/volta.png");
    public static final ResourceLocation GHOSTTRACKER_PLASMA = new ResourceLocation("vantix", "ghosttracker/plasma.png");
    public static final ResourceLocation GHOSTTRACKER_BOOTS = new ResourceLocation("vantix", "ghosttracker/ghostly boots.png");
    public static final ResourceLocation GHOSTTRACKER_COINS = new ResourceLocation("vantix", "ghosttracker/coin-drop.png");
    public static final ResourceLocation GHOSTTRACKER_MAGIC_FIND = new ResourceLocation("vantix", "ghosttracker/magic find.png");
    public static final ResourceLocation GHOSTTRACKER_MONEY = new ResourceLocation("vantix", "ghosttracker/money.png");
    public static final ResourceLocation GHOSTTRACKER_TIME = new ResourceLocation("vantix", "ghosttracker/time.png");

    public static final ResourceLocation DEFAULT_MAP_ICONS = new ResourceLocation("textures/map/map_icons.png");

    public static final int BETTER_CONTAINERS_STYLE_COUNT = 7;
    public static final ResourceLocation BETTER_CONTAINERS_DYNAMIC = new ResourceLocation("vantix", "dynamic/better_containers");
    public static final int STORAGE_STYLE_COUNT = 5;
    private static final ResourceLocation[] BC_BG = new ResourceLocation[BETTER_CONTAINERS_STYLE_COUNT];
    private static final ResourceLocation[] BC_SLOT = new ResourceLocation[BETTER_CONTAINERS_STYLE_COUNT];
    private static final ResourceLocation[] BC_NINE_SLICE = new ResourceLocation[BETTER_CONTAINERS_STYLE_COUNT];
    private static final ResourceLocation[] STORAGE_BG_TEXTURES = new ResourceLocation[STORAGE_STYLE_COUNT];
    private static final ResourceLocation[] STORAGE_SLOT_TEXTURES = new ResourceLocation[STORAGE_STYLE_COUNT];

    static {
        for (int i = 0; i < BETTER_CONTAINERS_STYLE_COUNT; i++) {
            int s = i + 1;
            BC_BG[i] = new ResourceLocation("vantix", "dynamic_54/style" + s + "/dynamic_54.png");
            BC_SLOT[i] = new ResourceLocation("vantix", "dynamic_54/style" + s + "/dynamic_54_slot_ctm.png");
            BC_NINE_SLICE[i] = new ResourceLocation("vantix", "dynamic_54/style" + s + "/nine_slice.png");
        }
        for (int i = 0; i < STORAGE_STYLE_COUNT; i++) {
            STORAGE_BG_TEXTURES[i] = new ResourceLocation("vantix", "textures/gui/containers/style" + i + "_bg.png");
            STORAGE_SLOT_TEXTURES[i] = new ResourceLocation("vantix", "textures/gui/containers/style" + i + "_slot.png");
        }
    }
    private Resources() {
    }

    public static ResourceLocation betterContainersBg(int styleIndex) {
        return BC_BG[Math.max(0, Math.min(styleIndex, BETTER_CONTAINERS_STYLE_COUNT - 1))];
    }

    public static ResourceLocation betterContainersSlot(int styleIndex) {
        return BC_SLOT[Math.max(0, Math.min(styleIndex, BETTER_CONTAINERS_STYLE_COUNT - 1))];
    }

    public static ResourceLocation betterContainerNineSlice(int styleIndex) {
        return BC_NINE_SLICE[Math.max(0, Math.min(styleIndex, BETTER_CONTAINERS_STYLE_COUNT - 1))];
    }

    /**
     * Returns the background texture for the given storage overlay style index.
     */
    public static ResourceLocation storageBackground(int style) {
        return STORAGE_BG_TEXTURES[Math.max(0, Math.min(style, STORAGE_STYLE_COUNT - 1))];
    }

    /**
     * Returns the slot texture for the given storage overlay style index.
     */
    public static ResourceLocation storageSlot(int style) {
        return STORAGE_SLOT_TEXTURES[Math.max(0, Math.min(style, STORAGE_STYLE_COUNT - 1))];
    }
}