package com.vtx.vantix.features.chocolate;

import com.vtx.vantix.core.VNTXConfig;
import com.vtx.vantix.init.RegisterEvents;
import com.vtx.vantix.utils.data.SkyblockData;
import com.vtx.vantix.utils.ColorUtils;
import com.vtx.vantix.utils.render.RenderUtils;
import com.vtx.vantix.utils.item.ItemUtils;
import com.vtx.vantix.utils.SoundUtils;
import com.vtx.vantix.utils.StringUtils;
import com.vtx.vantix.variables.Skins;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RegisterEvents
public class ChocolateFactory {

    private final Pattern upgradeCostPattern = Pattern.compile("(§.)(?<cost>[0-9,]+) Chocolate");
    private final ArrayList<EggWaypoint> waypoints = new ArrayList<>();

    private static class EggWaypoint {
        int[] coords;
        boolean hidden = false;
        public EggWaypoint(int[] coords) { this.coords = coords; }
    }

    @SubscribeEvent
    public void onRenderLast(RenderWorldLastEvent event) {
        if (VNTXConfig.feature == null || VNTXConfig.feature.chocolateFactory == null) return;
        if (!SkyblockData.isSkyblock()) return;
        if (!VNTXConfig.feature.chocolateFactory.chocolateChocolateEggWaypoints) return;

        checkForEggs();
        drawWaypoints(event.partialTicks);
        drawTags(event.partialTicks);
    }

    @SubscribeEvent
    public void onGuiOpen(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (VNTXConfig.feature == null || VNTXConfig.feature.chocolateFactory == null) return;
        if (!VNTXConfig.feature.chocolateFactory.chocolateChocolateShowBestUpgrade || !(event.gui instanceof GuiChest)) return;

        TreeMap<Float, Slot> upgradeCosts = new TreeMap<>();
        GuiChest chest = (GuiChest) event.gui;
        Container container = chest.inventorySlots;

        if (!(container instanceof ContainerChest)) return;
        ContainerChest containerChest = (ContainerChest) container;

        String rawName = containerChest.getLowerChestInventory().getDisplayName().getUnformattedText();
        String chestName = StringUtils.stripFormattingFast(rawName).toLowerCase();

        if (!chestName.startsWith("chocolate factory")) return;

        int index = 0;
        for (Slot slot : containerChest.inventorySlots) {
            if (slot.getSlotIndex() < 28 || slot.getSlotIndex() > 34) continue;
            index++;
            ItemStack item = slot.getStack();
            if (item != null && item.getItem() instanceof ItemSkull) {
                String upgradeCost = ItemUtils.getLoreLine(item, upgradeCostPattern);
                if (upgradeCost == null) continue;
                upgradeCost = ColorUtils.stripColor(upgradeCost).replaceAll(",", "").replaceAll(" Chocolate", "");

                float costRatio = Float.parseFloat(upgradeCost) / index;
                upgradeCosts.put(costRatio, slot);
            }
        }

        if (upgradeCosts.isEmpty()) return;
        float lowestValue = upgradeCosts.firstKey();
        Slot associatedSlot = upgradeCosts.get(lowestValue);

        RenderUtils.drawOnSlot(containerChest.inventorySlots.size(), associatedSlot.xDisplayPosition, associatedSlot.yDisplayPosition, new Color(0, 255, 0, 100).getRGB());
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent e) {
        if (VNTXConfig.feature == null || VNTXConfig.feature.chocolateFactory == null) return;
        if (!VNTXConfig.feature.chocolateFactory.chocolateChocolateEggWaypoints) return;
        if (!SkyblockData.isSkyblock()) return;

        String msg = StringUtils.stripFormattingFast(e.message.getUnformattedText());
        Matcher matcher = Pattern.compile("HOPPITY'S HUNT You found").matcher(msg);
        Matcher matcher2 = Pattern.compile("HOPPITY'S HUNT A Chocolate .* Egg has appeared").matcher(msg);
        Matcher matcher3 = Pattern.compile("You have already collected this Chocolate .* Egg! Try again when it respawns!").matcher(msg);

        int[] playerCoords = new int[]{
                Minecraft.getMinecraft().thePlayer.getPosition().getX(),
                Minecraft.getMinecraft().thePlayer.getPosition().getY(),
                Minecraft.getMinecraft().thePlayer.getPosition().getZ()
        };

        if (matcher.find()) {
            EggWaypoint w = getClosest(playerCoords);
            if (w != null) w.hidden = true;
        }
        if (matcher2.find()) {
            waypoints.removeIf(w -> w.hidden && getDistance(playerCoords, w.coords) > 64);
        }
        if (matcher3.find()) {
            EggWaypoint w = getClosest(playerCoords);
            if (w != null && getDistance(playerCoords, w.coords) < 6) w.hidden = true;
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (VNTXConfig.feature != null && VNTXConfig.feature.chocolateFactory != null && VNTXConfig.feature.chocolateFactory.chocolateChocolateEggWaypoints) {
            waypoints.clear();
        }
    }

    private void checkForEggs() {
        WorldClient world = Minecraft.getMinecraft().theWorld;
        for (int i = 0; i < world.loadedEntityList.size(); i++) {
            Entity entity = world.loadedEntityList.get(i);
            if (entity instanceof EntityArmorStand) {
                ItemStack it = ((EntityArmorStand) entity).getEquipmentInSlot(4);
                if (it != null && it.getItem() == Items.skull) {
                    String texture = ItemUtils.getSkullTexture(it);
                    if (texture.isEmpty()) continue;

                    if (isEgg(texture)) {
                        int[] entityCoords = new int[]{entity.getPosition().getX(), entity.getPosition().getY(), entity.getPosition().getZ()};
                        EggWaypoint waypoint = new EggWaypoint(entityCoords);
                        if (waypoints.stream().anyMatch(w -> Arrays.equals(w.coords, waypoint.coords))) continue;
                        waypoints.add(waypoint);
                        SoundUtils.playSound(entityCoords, "random.pop", 4.0f, 2.5f);
                    }
                }
            }
        }
    }

    private void drawWaypoints(float partialTicks) {
        Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
        double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks;
        double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks;
        double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks;

        Color color = ColorUtils.getColor(VNTXConfig.feature.chocolateFactory.chocolateChocolateEggWaypointsColor);
        color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 150);

        for (EggWaypoint waypoint : waypoints) {
            if (waypoint.hidden) continue;
            AxisAlignedBB bb = new AxisAlignedBB(
                    waypoint.coords[0] - viewerX,
                    waypoint.coords[1] - viewerY + 1,
                    waypoint.coords[2] - viewerZ,
                    waypoint.coords[0] + 1 - viewerX,
                    waypoint.coords[1] + 1 - viewerY + 150,
                    waypoint.coords[2] + 1 - viewerZ
            ).expand(0.01f, 0.01f, 0.01f);
            GlStateManager.disableCull();
            RenderUtils.drawFilledBoundingBox(bb, 1f, color);
            GlStateManager.enableCull();
            GlStateManager.enableTexture2D();
        }
    }

    private void drawTags(float partialTicks) {
        for (EggWaypoint waypoint : waypoints) {
            if (waypoint.hidden) continue;
            GlStateManager.disableCull();
            RenderUtils.drawTag("Egg", new double[]{waypoint.coords[0], waypoint.coords[1], waypoint.coords[2]}, Color.WHITE, partialTicks);
            GlStateManager.enableCull();
            GlStateManager.enableTexture2D();
        }
    }

    private EggWaypoint getClosest(int[] coords) {
        EggWaypoint closest = null;
        double minDst = Double.MAX_VALUE;
        for (EggWaypoint w : waypoints) {
            double dst = getDistance(coords, w.coords);
            if (dst < minDst) {
                minDst = dst;
                closest = w;
            }
        }
        return closest;
    }

    private double getDistance(int[] c1, int[] c2) {
        return Math.sqrt(Math.pow(c1[0]-c2[0], 2) + Math.pow(c1[1]-c2[1], 2) + Math.pow(c1[2]-c2[2], 2));
    }

    private boolean isEgg(String texture) {
        return Skins.equalsSkin(texture, Skins.EASTER_EGG_BREAKFAST) ||
                Skins.equalsSkin(texture, Skins.EASTER_EGG_LUNCH) ||
                Skins.equalsSkin(texture, Skins.EASTER_EGG_DINNER);
    }
}