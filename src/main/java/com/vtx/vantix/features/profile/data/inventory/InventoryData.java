package com.vtx.vantix.features.profile.data.inventory;

import com.vtx.vantix.features.profile.data.ItemData;
import com.vtx.vantix.features.profile.vars.EquipmentSlot;
import lombok.AllArgsConstructor;

import java.util.HashMap;

@AllArgsConstructor
public class InventoryData {

    public HashMap<EquipmentSlot, ItemData> armorData;
    public HashMap<Integer, ItemData> invData;

}
