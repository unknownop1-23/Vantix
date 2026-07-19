package com.vtx.vantix.variables;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;

@Getter
@AllArgsConstructor
public enum Resources {

    STORAGE_SMALL(new ResourceLocation("vantix:skyblock/storage/storage_size_1.png")),
    STORAGE_MEDIUM(new ResourceLocation("vantix:skyblock/storage/storage_size_2.png")),
    STORAGE_LARGE(new ResourceLocation("vantix:skyblock/storage/storage_size_3.png")),
    STORAGE_GREATER(new ResourceLocation("vantix:skyblock/storage/storage_size_4.png")),
    STORAGE_JUMBO(new ResourceLocation("vantix:skyblock/storage/storage_size_5.png")),
    STORAGE_INVENTORY(new ResourceLocation("vantix:skyblock/storage/inventory.png")),

    BEACON(new ResourceLocation("textures/entity/beacon_beam.png")),
    MAP_ICONS(new ResourceLocation("textures/map/map_icons.png")),

    ITEM_LOCK(new ResourceLocation("vantix", "invbuttons/editor.png")),

    LOCK(new ResourceLocation("vantix:slotlocking/lock.png")),
    BOUND(new ResourceLocation("vantix:slotlocking/bound.png")),
    RARITY_TEXTURE(new ResourceLocation("vantix:skyblock/textures/gui/rarity.png")),

    DARK_AH(new ResourceLocation("vantix:skyblock/dark_ah.png")),
    EGG_HUNT(new ResourceLocation("vantix:skyblock/egg_hunt.png")),
    SCATHA(new ResourceLocation("vantix:crystalhollows/pets_scatha.png")),

    CRYSTAL_MAP_POINT(new ResourceLocation("vantix:crystalhollows/map_point.png")),
    CRYSTAL_MAP_ARROW(new ResourceLocation("vantix:crystalhollows/map_arrow.png")),

    CRYSTAL_MAP_ZONES(new ResourceLocation("vantix:crystalhollows/map.png")),
    CRYSTAL_MAP_GEMS( new ResourceLocation("vantix:crystalhollows/map_gems.png")),

    EQUIPMENT(new ResourceLocation("vantix", "equipment.png")),

    RESET(new ResourceLocation("vantix:core/reset.png")),

    STATBAR(new ResourceLocation("vantix:skyblock/stats/bars/bar.png")),

    ;

    private final ResourceLocation resource;

}
