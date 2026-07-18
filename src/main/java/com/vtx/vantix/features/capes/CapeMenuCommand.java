package com.vtx.vantix.features.capes;

import com.vtx.vantix.core.VNTXConfig;
import com.vtx.vantix.command.ASMCommand;
import com.vtx.vantix.features.capes.ui.CapeSelectorGUI;
import com.vtx.vantix.init.RegisterCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

@RegisterCommand
public class CapeMenuCommand extends ASMCommand {
    @Override
    public String getName() {
        return "capes";
    }

    @Override
    public String getUsage() {
        return "/" + getName();
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws CommandException {
        if(!(sender instanceof EntityPlayer)) return;
        VNTXConfig.screenToOpen = new CapeSelectorGUI();
    }
}
