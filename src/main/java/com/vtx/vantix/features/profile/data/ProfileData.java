package com.vtx.vantix.features.profile.data;

import com.vtx.vantix.features.profile.data.bags.BagsData;
import com.vtx.vantix.features.profile.data.base.BaseData;
import com.vtx.vantix.features.profile.data.collection.CollectionsData;
import com.vtx.vantix.features.profile.data.dungeon.DungeonData;
import com.vtx.vantix.features.profile.data.inventory.InventoryData;
import com.vtx.vantix.features.profile.data.pets.PetsData;
import com.vtx.vantix.features.profile.data.skills.SkillsData;
import com.vtx.vantix.features.profile.data.slayer.SlayersData;
import com.vtx.vantix.features.profile.data.storage.StorageData;
import com.vtx.vantix.features.profile.data.wardrobe.WardrobeData;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class ProfileData {

    public BaseData baseData;
    public InventoryData inventoryData;
    public SkillsData skillData;
    public HOTMData hotmData;
    public DungeonData dungeonData;
    public SlayersData slayersData;
    public WardrobeData wardrobeData;
    public PetsData petsData;
    public StorageData storageData;
    public BagsData bagsData;
    public CollectionsData collectionData;

}
