package com.vtx.vantix.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vtx.vantix.utils.Logger;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class AccessoriesData {

    public static AccessoriesData INSTANCE = new AccessoriesData();

    public static boolean finalPage = false;
    public static boolean show = false;

    public static int totalMp = 0;
    public static int maxMp = 0;
    public static int maxMpRec = 0;
    private static int bonuses = 0;

    @AllArgsConstructor @Data
    public static class Accessory {
        public String rarity;
        public String name;
    }

    private Set<Accessory> repoAccessories = new HashSet<>();
    private Set<Accessory> currentAccessories = new HashSet<>();
    private boolean dataLoaded = false;

    public AccessoriesData() {
        this.currentAccessories = new HashSet<>();
        loadData();
    }

    public void loadData() {
        if (dataLoaded) return;

        new Thread(() -> {
            try {
                // Manually fetches the data to bypass GSON strictness
                URL url = new URL("https://raw.githubusercontent.com/davidbelesp/NotEnoughFakepixel-REPO/refs/heads/main/data/accessories.json");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                if (conn.getResponseCode() == 200) {
                    InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                    JsonObject json = new JsonParser().parse(reader).getAsJsonObject();

                    if (json.has("bonuses")) {
                        bonuses = json.get("bonuses").getAsInt();
                    }

                    if (json.has("accessories")) {
                        JsonArray accArray = json.getAsJsonArray("accessories");
                        Set<Accessory> loadedAccessories = new HashSet<>();
                        for (JsonElement element : accArray) {
                            JsonObject accObj = element.getAsJsonObject();
                            String rarity = accObj.has("rarity") ? accObj.get("rarity").getAsString() : "COMMON";
                            String name = accObj.has("name") ? accObj.get("name").getAsString() : "";
                            loadedAccessories.add(new Accessory(rarity, name));
                        }
                        this.repoAccessories = loadedAccessories;
                    }

                    dataLoaded = true;
                    reader.close();
                } else {
                    Logger.logConsole("Failed to fetch accessories. HTTP Code: " + conn.getResponseCode());
                }
            } catch (Exception e) {
                Logger.logConsole("Exception loading accessories: " + e.getMessage());
            }
        }).start();
    }

    public List<Accessory> getMissingAccessories() {
        if (!dataLoaded || repoAccessories == null || repoAccessories.isEmpty()) {
            loadData();
        }

        return repoAccessories.stream()
                .filter(acc -> {
                    for (Accessory current : currentAccessories) {
                        if (handleSpecialCases(current, acc)) return false;
                        if (current.getName().equalsIgnoreCase(acc.getName())) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    public static void calculateMaxMp() {
        if (!INSTANCE.dataLoaded || INSTANCE.getRepoAccessories() == null || INSTANCE.getRepoAccessories().isEmpty()) {
            INSTANCE.loadData();
        }

        maxMp = INSTANCE.getRepoAccessories().stream()
                .mapToInt(acc -> getMpByRarity(acc.getRarity()))
                .sum();
        maxMp += bonuses;
    }

    private static void calculateMaxRecomb() {
        if (!INSTANCE.dataLoaded || INSTANCE.getRepoAccessories() == null || INSTANCE.getRepoAccessories().isEmpty()) {
            INSTANCE.loadData();
        }

        maxMpRec = INSTANCE.getRepoAccessories().stream()
                .mapToInt(acc -> getMpByRarity(acc.getRarity(), true))
                .sum();
        maxMpRec += bonuses;
    }

    private boolean handleSpecialCases(Accessory current, Accessory accessory) {
        String name = current.getName().toLowerCase();
        String accName = accessory.getName().toLowerCase();
        if (name.contains("abicase") && accName.contains("abicase")) {
            for (Accessory acc : currentAccessories) {
                if (acc.getName().toLowerCase().contains("abicase")) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addAccessory(Accessory accessory) {
        if (currentAccessories.stream().noneMatch(acc -> acc.getName().equalsIgnoreCase(accessory.getName()))) {
            currentAccessories.add(accessory);
        }
    }

    public void clearAccessories() {
        currentAccessories.clear();
    }

    public static void calculateMp(){
        calculateTotalMp();
        calculateMaxMp();
        calculateMaxRecomb();
    }

    private static int getMpByRarity(String rarity) {
        return getMpByRarity(rarity, false);
    }

    private static int getMpByRarity(String rarity, boolean withRecomb) {
        rarity = rarity.trim();
        if (withRecomb) {
            switch (rarity.toLowerCase()) {
                case "special":
                case "common": return 5;
                case "uncommon": return 8;
                case "rare": return 12;
                case "epic": return 16;
                case "legendary": return 22;
                case "mythic":
                case "divine": return 28;
                default: return 0;
            }
        }
        switch (rarity.toLowerCase()) {
            case "special":
            case "common": return 3;
            case "uncommon": return 5;
            case "rare": return 8;
            case "epic": return 12;
            case "legendary": return 16;
            case "mythic": return 22;
            case "divine": return 28;
            default: return 0;
        }
    }

    public static void calculateTotalMp() {
        totalMp = INSTANCE.getCurrentAccessories().stream()
                .mapToInt(acc -> getMpByRarity(acc.getRarity()))
                .sum();
    }

    public static String getColorLevel(int max) {
        int total = totalMp;
        if (total >= max) return "§b";
        if (total >= 2 * max / 3) return "§a";
        if (total >= max / 3) return "§e";
        return "§c";
    }
}