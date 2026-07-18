package com.vtx.vantix;

import com.vtx.vantix.core.VNTXConfig;
import com.vtx.vantix.core.moulconfig.gui.GuiScreenElementWrapper;
import com.vtx.vantix.core.moulconfig.gui.config.ConfigEditor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.Set;

public class GuiFactory implements IModGuiFactory {

    @Override
    public void initialize(Minecraft minecraft) {
    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return WrappedVNTXConfig.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return null;
    }

    public static class WrappedVNTXConfig extends GuiScreenElementWrapper {

        private final GuiScreen parent;

        public WrappedVNTXConfig(GuiScreen parent) {
            super(new ConfigEditor(VNTXConfig.feature));
            this.parent = parent;
        }

        @Override
        public void handleKeyboardInput() throws IOException {
            if (Keyboard.getEventKeyState() && Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
                Minecraft.getMinecraft().displayGuiScreen(parent);
                return;
            }
            super.handleKeyboardInput();
        }
    }
}