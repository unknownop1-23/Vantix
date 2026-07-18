package com.vtx.vantix.features.profile.data.base;

import com.vtx.vantix.features.profile.vars.ProfileMode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BaseData {

    public String playerName,playerProfile;
    public int currentLevel;
    public long profileAge;
    public ProfileMode currentMode;
    public long currentPurse;
    public long bankBalance;
    public int bitCount;

    public NetworthData networth;
    public Statistics stats;

}
