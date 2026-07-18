package com.vtx.vantix.features.dungeons.reward;

import com.vtx.vantix.Vantix;
import com.vtx.vantix.utils.ColorUtils;
import com.vtx.vantix.utils.Utils;
import com.vtx.vantix.utils.item.ItemUtils;
import net.minecraft.item.ItemStack;

import lombok.Getter;

@Getter
public final class DungeonReward {

    private final ItemStack item;
    private final double price;
    private final double singlePrice;

    public DungeonReward(ItemStack item, double price) {
        double price1;
        singlePrice = price;
        this.item = item;
        price1 = price;
        if(ItemUtils.getInternalName(item).startsWith("essence")){
            String displayName = ColorUtils.stripColor(item.getDisplayName());
            Vantix.logger.info("Checking Amount for " + displayName);
            if(displayName.contains("x")){
                int index = displayName.lastIndexOf("x");
                String amount = displayName.substring(index + 1);
                Vantix.logger.info("Checking Amount in " + amount);
                int iA = 0;
                try{
                    iA = Integer.parseInt(amount);
                } catch (NumberFormatException e) {
                    Vantix.logger.info("ERROR converting " + amount + " to numbers.");
                }
                if(iA > 0) price1 *= iA;
            }
        }
        this.price = price1;
    }
    public String getText() {
        return item == null ? "" : item.getDisplayName() + " §7: " +
                                   getPriceText();
    }

    private String getPriceText() {
        if(price <= 0) return "§cCould not determine price";
        if(singlePrice == price)  return "§6" + Utils.shortNumberFormat(price,0) + " Coins.";
        else return "§6" + Utils.shortNumberFormat(price,0) + " Coins." + "§7(§6" + Utils.shortNumberFormat(price,0) + " §7)";
    }
}
