package com.vtx.vantix.features.price;

import com.vtx.vantix.command.ASMCommand;
import com.vtx.vantix.init.RegisterCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

@RegisterCommand
public class SendPriceCommand extends ASMCommand {
    @Override
    public String getName() {
        return "sendprice";
    }

    @Override
    public String getUsage() {
        return "/" + getName();
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws CommandException {
        PriceDetector.sendNow();
    }
}
