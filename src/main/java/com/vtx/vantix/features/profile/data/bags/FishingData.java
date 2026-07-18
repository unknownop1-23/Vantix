package com.vtx.vantix.features.profile.data.bags;

import com.vtx.vantix.features.profile.data.bags.vars.Bait;
import lombok.AllArgsConstructor;

import java.util.EnumMap;

@AllArgsConstructor
public class FishingData {

    public EnumMap<Bait,Integer> baits;

}
