package com.vtx.vantix.core.features.about;

import com.vtx.vantix.core.moulconfig.gui.config.ConfigAnnotations.*;

public class About {

    @ConfigOption(name = "Current Version", desc = "The VNTX version you are running")
    @ConfigEditorVersionDisplay
    public transient Void currentVersion = null;

    @ConfigOption(name = "Website", desc = "Visit the VNTX website")
    @ConfigEditorButton(runnableId = "openWebsite", buttonText = "Open")
    public boolean websiteButton = false;

    @ConfigOption(name = "Discord", desc = "Join the VNTX Discord server")
    @ConfigEditorButton(runnableId = "openDiscord", buttonText = "Open")
    public boolean discordButton = false;

    @ConfigOption(name = "GitHub", desc = "View the VNTX source code on GitHub")
    @ConfigEditorButton(runnableId = "openGithub", buttonText = "Open")
    public boolean githubButton = false;

    @ConfigOption(name = "Modrinth", desc = "View VNTX on Modrinth")
    @ConfigEditorButton(runnableId = "openModrinth", buttonText = "Open")
    public boolean modrinthButton = false;

    @ConfigOption(name = "SkyAtlas", desc = "Sync your gear and stats on SkyAtlas")
    @ConfigEditorButton(runnableId = "openSkyAtlas", buttonText = "Open")
    public boolean skyatlasButton = false;

    @ConfigOption(name = "Used Software / Libraries", desc = "Libraries and projects used by VNTX and their licenses")
    @ConfigEditorAccordion(id = 0)
    public boolean licensesAccordion = false;

    @ConfigOption(name = "Forge", desc = "Forge is available under the LGPL 3.0 license")
    @ConfigEditorButton(runnableId = "openLicenseForge", buttonText = "Source")
    @ConfigAccordionId(id = 0)
    public boolean forgeButton = false;

    @ConfigOption(name = "Mixin", desc = "Mixin is available under the MIT license")
    @ConfigEditorButton(runnableId = "openLicenseMixin", buttonText = "Source")
    @ConfigAccordionId(id = 0)
    public boolean mixinButton = false;

    @ConfigOption(name = "MoulConfig", desc = "Config GUI based on MoulConfig, available under the LGPL 3.0 license")
    @ConfigEditorButton(runnableId = "openLicenseMoulConfig", buttonText = "Source")
    @ConfigAccordionId(id = 0)
    public boolean moulConfigButton = false;

    @ConfigOption(name = "Lombok", desc = "Lombok is available under the MIT license")
    @ConfigEditorButton(runnableId = "openLicenseLombok", buttonText = "Website")
    @ConfigAccordionId(id = 0)
    public boolean lombokButton = false;

    @ConfigOption(name = "JB Annotations", desc = "JetBrains Annotations is available under the Apache 2 license")
    @ConfigEditorButton(runnableId = "openLicenseJbAnnotations", buttonText = "Source")
    @ConfigAccordionId(id = 0)
    public boolean jbAnnotationsButton = false;
}