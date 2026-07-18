package com.vtx.vantix.features.chat.chatfilters;

import com.vtx.vantix.command.ASMCommand;
import com.vtx.vantix.core.VNTXConfig;
import com.vtx.vantix.features.chat.chatfilters.ui.ChatFilterGUI;
import com.vtx.vantix.init.RegisterCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Arrays;
import java.util.List;

@RegisterCommand
public class ChatFilterCommand extends ASMCommand {
    @Override
    public String getName() {
        return "chatfilters";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("athrChatFilters",
                "athrchatfilters","acf","asmChatFilters","asmchatfilters",
                "vantixChatFilters","vantixchatfilters");
    }

    @Override
    public String getUsage() {
        return "/" + getName();
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws CommandException {
        if(!(sender instanceof EntityPlayer)) return;
        VNTXConfig.screenToOpen = new ChatFilterGUI();
    }
}
