package com.vtx.vantix.features.profile.viewer.ui.tabs;

import com.vtx.vantix.features.profile.data.ProfileData;
import lombok.AllArgsConstructor;
import net.minecraft.client.Minecraft;

@AllArgsConstructor
public abstract class Tab {

    public int tabIndex;
    public String name;
    public abstract void draw(float xPos, float yPos, int width, int height, ProfileData data, Minecraft mc);
    
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}

}