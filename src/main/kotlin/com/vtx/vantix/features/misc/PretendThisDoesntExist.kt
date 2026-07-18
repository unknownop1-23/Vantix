package com.vtx.vantix.features.misc

import com.vtx.vantix.command.ASMCommand
import com.vtx.vantix.init.RegisterCommand
import net.minecraft.command.ICommandSender

@RegisterCommand
class PretendThisDoesntExist : ASMCommand() {
    
    override fun getName() = "VNTXthisisatestdontusethispls"
    
    override fun getUsage() = "/VNTXthisisatestdontusethispls"
    
    override fun execute(sender: ICommandSender, args: Array<String>) {
        DVD.forceCornerHit()
    }
}
