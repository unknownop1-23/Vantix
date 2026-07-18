package com.vtx.vantix.features.profile.viewer.ui.modules;

import com.vtx.vantix.utils.render.PlayerRenderer;

public class PlayerModule {

    public static void draw(int scaledX,int scaledY,int playerScale,String username,int mouseX,int mouseY){
        PlayerRenderer.renderPlayer(username,scaledX, scaledY, playerScale, mouseX, mouseY,false);
    }



}
