package com.vtx.vantix.command;

import com.vtx.vantix.core.VNTXConfig;
import com.vtx.vantix.utils.chat.ChatUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class Command extends ASMCommand {

    @Override
    public String getName() { return "athr"; }

    @Override
    public String getUsage() { return "/athr | /athr config | /athr <category> | /athr reload"; }

    @Override
    public List<String> getAliases() { return Arrays.asList("vantix","jef","asm"); }

    @Override
    public void execute(ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "reload":
                    VNTXConfig.reloadRepo();
                    ChatUtils.sendMessage("§a[VNTX] §fRepo refresh triggered.");
                    break;
                case "config":
                    VNTXConfig.openGui();
                    break;
                default:
                    VNTXConfig.openCategory(StringUtils.join(args, " "));
                    break;
            }
        } else {
            VNTXConfig.openOptionsGui();
        }
    }
}