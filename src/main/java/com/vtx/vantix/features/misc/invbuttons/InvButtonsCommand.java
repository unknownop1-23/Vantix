package com.vtx.vantix.features.misc.invbuttons;

import com.vtx.vantix.core.VNTXConfig;
import com.vtx.vantix.command.ASMCommand;
import com.vtx.vantix.init.RegisterCommand;
import net.minecraft.command.ICommandSender;
import java.util.Arrays;
import java.util.List;

@RegisterCommand
public class InvButtonsCommand extends ASMCommand {
    @Override
    public String getName() { return "athrbuttons"; }

    @Override
    public List<String> getAliases() { return Arrays.asList("vantixbuttons", "jefbuttons", "asmbuttons"); }

    @Override
    public String getUsage() {
        return "/athrbuttons";
    }

    @Override
    public void execute(ICommandSender sender, String[] args) {
        VNTXConfig.openInvButtonEditor();
    }
}