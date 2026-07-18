package com.vtx.vantix.features.profile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vtx.vantix.Vantix;
import com.vtx.vantix.core.VNTXConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WaiterLogs {

    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static ArrayList<String> logs = new ArrayList<>();

    public static void addLog(String log){
        logs.add(log);
    }

    public static void saveLogs(){
        File file = new File(VNTXConfig.configDirectory,"guiWaiterLogs.log");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file,true);
            writer.write(gson.toJson(logs));
            writer.close();
        } catch (IOException e) {
            Vantix.logger.info("Error Saving GUI Waiter Logs.");
        }
    }

}
