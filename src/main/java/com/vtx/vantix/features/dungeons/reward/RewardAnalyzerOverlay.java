package com.vtx.vantix.features.dungeons.reward;

import com.vtx.vantix.core.VNTXConfig;
import com.vtx.vantix.core.moulconfig.editors.ChromaColour;
import com.vtx.vantix.init.RegisterEvents;
import com.vtx.vantix.utils.Position;
import com.vtx.vantix.utils.Utils;
import com.vtx.vantix.utils.overlay.Overlay;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RegisterEvents
public class RewardAnalyzerOverlay extends Overlay {

    @Getter
    private static RewardAnalyzerOverlay instance;

    public RewardAnalyzerOverlay() {
        super(25,25);
        instance = this;
    }

    @Override
    public List<String> getLines(boolean preview) {
        if(preview){
            return Collections.singletonList("§aPrice Analyzer");
        }
        if(DungeonRewardProfitEstimator.cache.isEmpty()) return Collections.emptyList();
        List<String> lines = new ArrayList<>();
        lines.add("§aChest Price Analyzer:");
        RewardEstimate highestProfitChest = null;
        for(RewardEstimate estimate : DungeonRewardProfitEstimator.cache.values()){
            String s = DungeonRewardProfitEstimator.getChestHeader(estimate.getChestID());
            double profit = estimate.getProfit();
            if(highestProfitChest == null) highestProfitChest = estimate;
            else if(highestProfitChest.getProfit() < profit)highestProfitChest = estimate;
            lines.add(s + " §7: " + (profit > 0 ? "§a" : "§c") + Utils.shortNumberFormat(profit,0));
        }
        if(highestProfitChest != null) {
            lines.add("§aRecommended Chest: " + DungeonRewardProfitEstimator.getChestHeader(highestProfitChest.getChestID()));
        }
        return lines;
    }

    @Override
    public Position getPosition() {
        return VNTXConfig.feature.dungeons.priceEstimator.analyzerPosition;
    }

    @Override
    public float getScale() {
        return VNTXConfig.feature.dungeons.priceEstimator.overlayScale;
    }

    @Override
    public int getBgColor() {
        return ChromaColour.specialToChromaRGB(VNTXConfig.feature.dungeons.priceEstimator.overlayBgColor);
    }

    @Override
    public int getCornerRadius() {
        return 0;
    }

    @Override
    protected boolean isEnabled() {
        return VNTXConfig.feature.dungeons.priceEstimator.enableAnalyzerOverlay;
    }
}
