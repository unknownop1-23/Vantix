package com.vtx.vantix.features.profile.viewer.cmd;

import com.vtx.vantix.core.VNTXConfig;
import com.vtx.vantix.command.ASMCommand;
import com.vtx.vantix.features.profile.viewer.ui.ProfileViewerGUI;
import com.vtx.vantix.init.RegisterCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

@RegisterCommand
public class ProfileViewCommand extends ASMCommand {
    @Override
    public String getName() {
        return "pv";
    }

    @Override
    public String getUsage() {
        return "/" + getName();
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws CommandException {
        if(!(sender instanceof EntityPlayer)) return;
        if(args.length < 1){
            VNTXConfig.screenToOpen = new ProfileViewerGUI(Minecraft.getMinecraft().getSession().getUsername());
            return;
        }
        String user = args[0];
        VNTXConfig.screenToOpen = new ProfileViewerGUI(user);
    }
}
