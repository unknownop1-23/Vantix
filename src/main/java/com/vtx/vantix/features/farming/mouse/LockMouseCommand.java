package com.vtx.vantix.features.farming.mouse;

import com.vtx.vantix.command.ASMCommand;
import com.vtx.vantix.init.RegisterCommand;
import net.minecraft.command.ICommandSender;

@RegisterCommand
public class LockMouseCommand extends ASMCommand {

    @Override
    public String getName() {
        return "lockyp";
    }

    @Override
    public String getUsage() {
        return "/lockyp";
    }

    @Override
    public void execute(ICommandSender sender, String[] args) {
        LockMouse.setLocked(!LockMouse.isLocked());
    }
}